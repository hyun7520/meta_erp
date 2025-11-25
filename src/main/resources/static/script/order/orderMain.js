let currentTab = 'pending';
let currentPage = 1;
let itemsPerPage = 10;
let allData = [];
let products = [];

window.onload = function() {
    updateTime();
    setInterval(updateTime, 1000);
    loadProducts();
    loadData('pending');
};

function loadProducts() {
    fetch('/products')  // 수정
        .then(response => response.json())
        .then(data => {
            products = data;
            updateProductSelect();
        })
        .catch(error => console.error('제품 목록 로딩 실패:', error));
}

function updateProductSelect() {
    const select = document.getElementById('productSelect');
    if (!select) return;
    select.innerHTML = '<option value="">선택하세요</option>';
    products.forEach(product => {
        const option = document.createElement('option');
        option.value = product.productId;
        option.textContent = product.productName;
        select.appendChild(option);
    });
}

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
                <button class="btn btn-sm btn-edit" onclick="openEditModal(${item.orderId})" title="수정">
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


function openEditModal(prId) {
    fetch(`/pro/${prId}`)  //  수정
        .then(response => response.json())
        .then(data => {
            const editProductSelect = document.getElementById('editProductSelect');
            editProductSelect.innerHTML = '<option value="">선택하세요</option>';
            products.forEach(product => {
                const option = document.createElement('option');
                option.value = product.productId;
                option.textContent = product.productName;
                if (product.productName === data.productName) {
                    option.selected = true;
                }
                editProductSelect.appendChild(option);
            });

            document.getElementById('editPrId').value = data.orderId;
            document.getElementById('editClientName').value = data.requestBy || '';
            document.getElementById('editQuantity').value = data.qty || '';
            document.getElementById('editUnit').value = data.unit || '';
            document.getElementById('editDueDate').value = data.deadline || '';

            document.getElementById('editModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('주문 정보를 불러오는데 실패했습니다.');
        });
}

function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

function submitEditRequest() {
    const prId = document.getElementById('editPrId').value;
    const productId = document.getElementById('editProductSelect').value;
    const clientName = document.getElementById('editClientName').value;
    const quantity = document.getElementById('editQuantity').value;
    const unit = document.getElementById('editUnit').value;
    const dueDate = document.getElementById('editDueDate').value;

    if (!productId || !clientName || !quantity || !unit || !dueDate) {
        alert('필수 항목을 모두 입력해주세요.');
        return;
    }

    if (isNaN(quantity) || parseInt(quantity) <= 0) {
        alert('수량은 1 이상의 숫자여야 합니다.');
        return;
    }

    const dueDateObj = new Date(dueDate);
    if (isNaN(dueDateObj.getTime())) {
        alert('올바른 날짜 형식을 입력해주세요.');
        return;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (dueDateObj < today) {
        alert('납기일은 오늘 이후여야 합니다.');
        return;
    }

    const orderData = {
        productId: parseInt(productId),
        requestBy: clientName.trim(),
        qty: parseInt(quantity),
        unit: unit.trim(),
        deadline: dueDate
    };

    fetch(`/pro/${prId}`, {  // 수정
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    console.error('서버 응답:', text);
                    throw new Error(text || '수정 실패');
                });
            }
            return response.json();
        })
        .then(result => {
            closeEditModal();
            alert('발주 요청이 수정되었습니다.');
            loadData(currentTab);
        })
        .catch(error => {
            console.error('Error:', error);
            if (error.message && error.message.includes('이미 생산이 시작')) {
                alert('이미 생산이 시작된 주문은 수정할 수 없습니다.');
            } else if (error.message && error.message.includes('ORA-01861')) {
                alert('날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식이어야 합니다)');
            } else {
                alert('발주 요청 수정에 실패했습니다.\n' + error.message);
            }
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

function openRequestModal() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('dueDate').value = today;
    document.getElementById('requestModal').style.display = 'block';
}

function closeRequestModal() {
    document.getElementById('requestModal').style.display = 'none';
}

function submitProductionRequest() {
    const productId = document.getElementById('productSelect').value;
    const clientName = document.getElementById('clientName').value;
    const quantity = document.getElementById('quantity').value;
    const unit = document.getElementById('unit').value;
    const dueDate = document.getElementById('dueDate').value;

    if (!productId || !clientName || !quantity || !unit || !dueDate) {
        alert('필수 항목을 모두 입력해주세요.');
        return;
    }

    const orderData = {
        productId: parseInt(productId),
        requestBy: clientName,
        qty: parseInt(quantity),
        unit: unit,
        deadline: dueDate
    };

    fetch('/pro', {  //  수정
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => Promise.reject(err));
            }
            return response.json();
        })
        .then(result => {
            closeRequestModal();
            alert('생산 요청이 성공적으로 접수되었습니다.');
            loadData(currentTab);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('생산 요청 중 오류가 발생했습니다: ' + (error.message || '알 수 없는 오류'));
        });
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

window.onclick = function(event) {
    const requestModal = document.getElementById('requestModal');
    const editModal = document.getElementById('editModal');

    if (event.target === requestModal) {
        closeRequestModal();
    }
    if (event.target === editModal) {
        closeEditModal();
    }
}