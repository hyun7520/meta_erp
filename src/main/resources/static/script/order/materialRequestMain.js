const MaterialModule = (function() {
    const API = {
        ALL_REQUESTS: '/pro/material-requests',
        PENDING_REQUESTS: '/pro/material-requests/pending',
        APPROVE: '/pro/material-requests/approve',
        REJECT: '/pro/material-requests/reject'
    };

    let state = {
        currentTab: 'pending',
        currentAction: null, // 1: 승인, 2: 반려
        currentMrId: null,
        currentPage: 1,
        itemsPerPage: 5,
        allData: []
    };

    function init() {
        updateTime();
        setInterval(updateTime, 1000);
        loadData('pending');
    }

    function showTab(tab) {
        state.currentTab = tab;
        state.currentPage = 1;
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        event.target.classList.add('active');
        loadData(tab);
    }

    async function loadData(tabName) {
        const apiUrl = tabName === 'pending' ? API.PENDING_REQUESTS : API.ALL_REQUESTS;

        try {
            document.getElementById('materialLoading').style.display = 'block';
            document.getElementById('materialContent').style.display = 'none';
            document.getElementById('materialEmpty').style.display = 'none';

            const response = await fetch(apiUrl);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            state.allData = Array.isArray(data) ? data : [];

            if (state.allData.length === 0) {
                document.getElementById('materialEmpty').style.display = 'block';
                document.getElementById('materialContent').style.display = 'none';
            } else {
                document.getElementById('materialContent').style.display = 'block';
                renderPage();
            }

            updateTabCounts(tabName, state.allData.length);

        } catch (error) {
            console.error("API 로드 중 오류 발생:", error);
            document.getElementById('materialLoading').innerHTML = `데이터 로드 실패: ${error.message}`;
            document.getElementById('materialLoading').style.display = 'block';
        } finally {
            document.getElementById('materialLoading').style.display = 'none';
        }
    }

    function renderPage() {
        if (!Array.isArray(state.allData)) {
            console.error("renderPage: allData is not an array.");
            return;
        }

        const startIndex = (state.currentPage - 1) * state.itemsPerPage;
        const endIndex = startIndex + state.itemsPerPage;
        const pageData = state.allData.slice(startIndex, endIndex);
        const totalPages = Math.ceil(state.allData.length / state.itemsPerPage);

        renderTable(pageData);
        renderPagination(currentPage, totalPages, goToPage);
    }

    function renderTable(data) {
        const tbody = document.getElementById('materialTableBody');
        tbody.innerHTML = '';

        data.forEach(item => {
            const row = document.createElement('tr');

            let statusBadge = '';
            if (item.approved === 0) {
                statusBadge = '<span class="status-badge status-pending">미승인</span>';
            } else if (item.approved > 0) {
                statusBadge = '<span class="status-badge status-approved">승인</span>';
            } else {
                statusBadge = '<span class="status-badge status-rejected">반려</span>';
            }

            let actionButtons = '';
            if (item.approved === 0) {
                actionButtons = `
                        <button class="btn btn-approve" onclick="MaterialModule.openModal(${item.mrId}, 1)">✓ 승인</button>
                        <button class="btn btn-reject" onclick="MaterialModule.openModal(${item.mrId}, 2)">✕ 반려</button>
                    `;
            } else {
                const approverText = item.approved > 0 ? (item.approverName || '처리완료') : '처리완료';
                actionButtons = `<span style="color: #666;">${approverText}</span>`;
            }

            row.innerHTML = `
                    <td><strong>${item.mrId}</strong></td>
                    <td>${item.materialName || '-'}</td>
                    <td>${item.requesterName || '-'}</td>
                    <td><span class="qty-unit">${item.qty} ${item.unit}</span></td>
                    <td>${item.requestDate || '-'}</td>
                    <td>${statusBadge}</td>
                    <td>${item.approvedDate || '-'}</td>
                    <td>${item.note || '-'}</td>
                    <td>${actionButtons}</td>
                `;

            tbody.appendChild(row);
        });
    }

    function goToPage(page) {
        state.currentPage = page;
        renderPage();
        document.getElementById('materialContent').scrollIntoView({ behavior: 'smooth', block: 'start' });
    }

    function updateTabCounts(currentType, currentCount) {
        const tabs = document.querySelectorAll('.tab');

        if (currentType === 'pending') {
            document.getElementById('materialPendingCount').textContent = currentCount;
            fetch(API.ALL_REQUESTS)
                .then(response => response.json())
                .then(data => {
                    const count = Array.isArray(data) ? data.length : 0;
                    document.getElementById('materialAllCount').textContent = count;
                })
                .catch(error => console.error("전체 카운트 로드 실패:", error));
        } else {
            document.getElementById('materialAllCount').textContent = currentCount;
            fetch(API.PENDING_REQUESTS)
                .then(response => response.json())
                .then(data => {
                    const count = Array.isArray(data) ? data.length : 0;
                    document.getElementById('materialPendingCount').textContent = count;
                })
                .catch(error => console.error("미승인 카운트 로드 실패:", error));
        }
    }

    function openModal(mrId, action) {
        state.currentMrId = mrId;
        state.currentAction = action;

        const modal = document.getElementById('materialModal');
        const modalTitle = document.getElementById('materialModalTitle');
        const confirmBtn = document.getElementById('materialConfirmBtn');

        if (action === 1) {
            modalTitle.textContent = '✓ 발주 승인';
            confirmBtn.textContent = '승인';
            confirmBtn.className = 'btn-modal btn-confirm';
        } else {
            modalTitle.textContent = '✕ 발주 반려';
            confirmBtn.textContent = '반려';
            confirmBtn.className = 'btn-modal btn-confirm reject';
        }

        document.getElementById('materialModalNote').value = '';
        modal.style.display = 'block';
    }

    function closeModal() {
        document.getElementById('materialModal').style.display = 'none';
    }

    function confirmAction() {
        const note = document.getElementById('materialModalNote').value.trim();
        const isApprove = state.currentAction === 1;

        // 다른 팀원의 employees 세션에서 승인자 ID 가져오기
        const employeeInfo = JSON.parse(sessionStorage.getItem('employees') || '{}');
        const approvedBy = employeeInfo.employeeId || 200; // 기본값 200

        if (!approvedBy) {
            alert("사용자 정보를 찾을 수 없습니다. 로그인이 필요합니다.");
            return;
        }

        const apiUrl = isApprove ? API.APPROVE : API.REJECT;

        const data = {
            mrId: state.currentMrId,
            approvedBy: approvedBy,
            note: note || null
        };

        fetch(apiUrl, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('처리 API 서버 오류');
                }
                return response.json();
            })
            .then(result => {
                closeModal();
                alert(isApprove ? '승인되었습니다.' : '반려되었습니다.');
                loadData(state.currentTab);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('처리 중 오류가 발생했습니다: ' + error.message);
            });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('materialModal');
        if (event.target === modal) {
            closeModal();
        }
    }

    return {
        init,
        showTab,
        openModal,
        closeModal,
        confirmAction
    };
})();

window.onload = MaterialModule.init;