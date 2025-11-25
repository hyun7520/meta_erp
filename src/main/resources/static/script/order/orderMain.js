let currentTab = 'pending';
let currentPage = 1;
let itemsPerPage = 10;
let allData = [];

window.onload = function() {
    updateTime();
    setInterval(updateTime, 1000);
    loadProducts();
    loadData('pending');
};

function showTab(tab) {
    currentTab = tab;
    currentPage = 1;
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    event.target.classList.add('active');
    loadData(tab);
}

function loadData(type) {
    document.getElementById('loading').style.display = 'block';
    document.getElementById('content').style.display = 'none';
    document.getElementById('empty').style.display = 'none';

    fetch('/pro')  // 수정
        .then(response => response.json())
        .then(data => {
            document.getElementById('loading').style.display = 'none';

            let filteredData = [];
            if (type === 'pending') {
                filteredData = data.filter(item => item.complete === 0);
            } else if (type === 'in-progress') {
                filteredData = data.filter(item => item.complete === 1);
            } else if (type === 'completed') {
                filteredData = data.filter(item => item.complete === 2);
            } else {
                filteredData = data;
            }

            filteredData.sort((a, b) => b.orderId - a.orderId);
            allData = filteredData;

            if (filteredData.length === 0) {
                document.getElementById('empty').style.display = 'block';
            } else {
                document.getElementById('content').style.display = 'block';
                renderPage();
            }

            updateTabCounts(data);
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('loading').style.display = 'none';
            alert('데이터를 불러오는데 실패했습니다.');
        });
}
function renderPage() {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = allData.slice(startIndex, endIndex);
    const totalPages = Math.ceil(allData.length / itemsPerPage);

    renderTable(pageData);
    renderPagination(currentPage, totalPages, goToPage);
}

function renderTable(data) {
    const tbody = document.getElementById('tableBody');
    const thead = document.querySelector('thead tr');

    // 전체 탭이고 완료된 항목이 있을 때만 체크박스 컬럼 표시
    const showCheckbox = currentTab === 'all' && data.some(item => item.complete === 2);

    // 헤더 업데이트
    if (showCheckbox) {
        if (!thead.querySelector('.checkbox-cell')) {
            const checkboxTh = document.createElement('th');
            checkboxTh.className = 'checkbox-cell';
            checkboxTh.innerHTML = '<input type="checkbox" id="selectAll" class="delete-checkbox" onchange="toggleSelectAll()">';
            thead.insertBefore(checkboxTh, thead.firstChild);
        }
        document.getElementById('btnDeleteSelected').classList.add('show');
    } else {
        const checkboxTh = thead.querySelector('.checkbox-cell');
        if (checkboxTh) {
            checkboxTh.remove();
        }
        document.getElementById('btnDeleteSelected').classList.remove('show');
    }

    tbody.innerHTML = '';

    data.forEach(item => {
        const row = document.createElement('tr');

        let statusBadge = '';
        if (item.complete === 0) {
            statusBadge = '<span class="status-badge status-rejected">대기중</span>';
        } else if (item.complete === 1) {
            statusBadge = '<span class="status-badge status-pending">생산시작</span>';
        } else if (item.complete === 2) {
            statusBadge = '<span class="status-badge status-approved">생산완료</span>';
        }

        let actionButtons = '';
        if (item.complete === 0) {
            actionButtons = `
                <button class="btn btn-sm btn-edit" onclick="openOrderModal(${item.orderId})" title="수정">
                    ✏️
                </button>
                <button class="btn btn-sm btn-delete" onclick="deleteOrder(${item.orderId})" title="삭제">
                    🗑️
                </button>
            `;
        } else {
            actionButtons = '<span style="color: #9CA3AF;">-</span>';
        }

        // 체크박스 셀 추가 (전체 탭이고 완료 상태일 때만)
        let checkboxCell = '';
        if (showCheckbox) {
            if (item.complete === 2) {
                checkboxCell = `<td class="checkbox-cell">
                    <input type="checkbox" class="delete-checkbox row-checkbox" value="${item.orderId}" onchange="updateDeleteButton()">
                </td>`;
            } else {
                checkboxCell = '<td class="checkbox-cell"></td>';
            }
        }

        row.innerHTML = `
            ${checkboxCell}
            <td><strong>${item.orderId}</strong></td>
            <td>${item.productName || '-'}</td>
            <td>${item.requestBy || '-'}</td>
            <td><span class="qty-unit">${item.qty} ${item.unit || '개'}</span></td>
            <td>${item.requestDate || '-'}</td>
            <td>${item.deadline || '-'}</td>
            <td>${statusBadge}</td>
            <td>${actionButtons}</td>
        `;

        tbody.appendChild(row);
    });
}

function deleteOrder(prId) {
    if (!confirm('정말로 이 주문을 삭제하시겠습니까?')) {
        return;
    }

    fetch(`/pro/${prId}`, {  // 수정
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('삭제 실패');
            }
            alert('주문이 삭제되었습니다.');
            loadData(currentTab);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('주문 삭제에 실패했습니다.');
        });
}

function goToPage(page) {
    currentPage = page;
    renderPage();
    document.getElementById('content').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function updateTabCounts(allOrders) {
    const pendingCount = allOrders.filter(item => item.complete === 0).length;
    const inProgressCount = allOrders.filter(item => item.complete === 1).length;
    const completedCount = allOrders.filter(item => item.complete === 2).length;

    document.getElementById('pendingCount').textContent = pendingCount;
    document.getElementById('inProgressCount').textContent = inProgressCount;
    document.getElementById('completedCount').textContent = completedCount;
    document.getElementById('allCount').textContent = allOrders.length;
}

function toggleSelectAll() {
    const selectAll = document.getElementById('selectAll');
    const checkboxes = document.querySelectorAll('.row-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = selectAll.checked;
    });
    updateDeleteButton();
}
function updateDeleteButton() {
    const checkedCount = document.querySelectorAll('.row-checkbox:checked').length;
    const btnDelete = document.getElementById('btnDeleteSelected');
    const selectAll = document.getElementById('selectAll');

    if (checkedCount > 0) {
        btnDelete.innerHTML = `🗑️ 선택 삭제 (${checkedCount})`;
    } else {
        btnDelete.innerHTML = '🗑️ 선택 삭제';
    }

    // 전체 선택 체크박스 상태 업데이트
    if (selectAll) {
        const allCheckboxes = document.querySelectorAll('.row-checkbox');
        const checkedCheckboxes = document.querySelectorAll('.row-checkbox:checked');
        selectAll.checked = allCheckboxes.length > 0 && allCheckboxes.length === checkedCheckboxes.length;
    }
}
function deleteSelectedOrders() {
    const checkedBoxes = document.querySelectorAll('.row-checkbox:checked');

    if (checkedBoxes.length === 0) {
        alert('삭제할 항목을 선택해주세요.');
        return;
    }

    if (!confirm(`선택한 ${checkedBoxes.length}개의 완료된 주문을 삭제하시겠습니까?`)) {
        return;
    }

    const orderIds = Array.from(checkedBoxes).map(cb => cb.value);

    // 순차적으로 삭제
    let deleteCount = 0;
    let failCount = 0;

    Promise.all(
        orderIds.map(orderId =>
            fetch(`/pro/${orderId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        deleteCount++;
                    } else {
                        failCount++;
                    }
                    return response;
                })
                .catch(error => {
                    console.error(`주문 ${orderId} 삭제 실패:`, error);
                    failCount++;
                })
        )
    )
        .then(() => {
            if (deleteCount > 0) {
                alert(`${deleteCount}개의 주문이 삭제되었습니다.` +
                    (failCount > 0 ? `\n${failCount}개의 주문 삭제에 실패했습니다.` : ''));
            } else {
                alert('선택한 주문을 삭제하는데 실패했습니다.');
            }
            loadData(currentTab);
        });
}
