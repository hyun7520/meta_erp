let currentTab = 'pending';
let currentAction = null; // 1: 승인, 2: 반려
let currentMrId = null;
let currentPage = 1;
let itemsPerPage = 5;
let allData = [];

window.onload = function() {
    updateTime();
    setInterval(updateTime, 1000);
    //loadUserInfo();
    loadData('pending');
};

function updateTime() {
    const now = new Date();
    const formatted = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0');
    document.getElementById('currentTime').innerHTML = '🕐 ' + formatted;
}

function loadUserInfo() {
    const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
    if (userInfo.name) {
        document.getElementById('userName').textContent = userInfo.name;
        document.getElementById('userAvatar').textContent = userInfo.name.charAt(0);
        const department = userInfo.department || '경영팀';
        const role = userInfo.role || '매니저';
        document.getElementById('userRole').textContent = department + ' · ' + role;
    }
}

function showTab(tab) {
    currentTab = tab;
    currentPage = 1;
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    event.target.classList.add('active');
    loadData(tab);
}

async function loadData(tabName) {
    const apiUrl = tabName === 'pending' ? '/api/material-requests/pending' : '/api/material-requests';

    try {
        document.getElementById('loading').style.display = 'block';
        document.getElementById('content').style.display = 'none';
        document.getElementById('empty').style.display = 'none';

        const response = await fetch(apiUrl);

        if (!response.ok) {
            // 서버 500 에러 등의 경우, 오류 처리
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // **[수정 1]** 'let'을 제거하고 전역 변수 allData에 할당
        allData = Array.isArray(data) ? data : [];

        if (allData.length === 0) {
            document.getElementById('empty').style.display = 'block';
            document.getElementById('content').style.display = 'none'; // 데이터가 없으면 테이블 숨김
        } else {
            document.getElementById('content').style.display = 'block';
            // renderPage는 전역 allData를 사용하므로 인자 제거
            renderPage();
        }

        // 탭 카운트 업데이트
        updateTabCounts(tabName, allData.length);

    } catch (error) {
        console.error("API 로드 중 오류 발생:", error);
        document.getElementById('loading').innerHTML = `데이터 로드 실패: ${error.message}`;
        document.getElementById('loading').style.display = 'block';
    } finally {
        document.getElementById('loading').style.display = 'none';
    }
}

function renderPage() {
    // allData가 배열임을 loadData에서 보장했지만, 만약을 대비해 다시 확인
    if (!Array.isArray(allData)) {
        console.error("renderPage: allData is not an array.");
        return;
    }

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = allData.slice(startIndex, endIndex);

    renderTable(pageData);
    renderPagination();
}

function renderTable(data) {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '';

    data.forEach(item => {
        const row = document.createElement('tr');

        let statusBadge = '';
        // **[수정 2]** 상태 코드 논리 변경: approved > 0 (승인자 ID)이면 승인
        if (item.approved === 0) {
            statusBadge = '<span class="status-badge status-pending">미승인</span>';
        } else if (item.approved > 0) {
            statusBadge = '<span class="status-badge status-approved">승인</span>';
        } else { // -1 이거나 그 외의 값
            statusBadge = '<span class="status-badge status-rejected">반려</span>';
        }

        let actionButtons = '';
        if (item.approved === 0) { // 미승인 상태일 때만 버튼 표시
            actionButtons =
                '<button class="btn btn-approve" onclick="openModal(' + item.mrId + ', 1)">✓ 승인</button>' + // 1: approve
                '<button class="btn btn-reject" onclick="openModal(' + item.mrId + ', 2)">✕ 반려</button>';  // 2: reject
        } else {
            // 승인자 이름 표시 (item.approverName은 DTO에 추가되어 있다고 가정)
            const approverText = item.approved > 0 ? (item.approverName || '처리완료') : '처리완료';
            actionButtons = `<span style="color: #666;">${approverText}</span>`;
        }

        // **[수정 3]** item.requestByName 대신 item.requesterName 사용
        row.innerHTML =
            '<td><strong>' + item.mrId + '</strong></td>' +
            '<td>' + (item.materialName || '-') + '</td>' +
            '<td>' + (item.requesterName || '-') + '</td>' +
            '<td><span class="qty-unit">' + item.qty + ' ' + item.unit + '</span></td>' +
            '<td>' + (item.requestDate || '-') + '</td>' +
            '<td>' + statusBadge + '</td>' +
            '<td>' + (item.approvedDate || '-') + '</td>' +
            '<td>' + (item.note || '-') + '</td>' +
            '<td>' + actionButtons + '</td>';

        tbody.appendChild(row);
    });
}

function renderPagination() {
    const totalPages = Math.ceil(allData.length / itemsPerPage);
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    if (totalPages <= 1) return;

    const prevBtn = document.createElement('button');
    prevBtn.className = 'pagination-btn';
    prevBtn.innerHTML = '‹';
    prevBtn.disabled = currentPage === 1;
    prevBtn.onclick = () => goToPage(currentPage - 1);
    pagination.appendChild(prevBtn);

    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 2);

    if (startPage > 1) {
        const firstBtn = document.createElement('button');
        firstBtn.className = 'pagination-btn';
        firstBtn.textContent = '1';
        firstBtn.onclick = () => goToPage(1);
        pagination.appendChild(firstBtn);

        if (startPage > 2) {
            const dots = document.createElement('span');
            dots.className = 'pagination-info';
            dots.textContent = '...';
            pagination.appendChild(dots);
        }
    }

    for (let i = startPage; i <= endPage; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = 'pagination-btn' + (i === currentPage ? ' active' : '');
        pageBtn.textContent = i;
        pageBtn.onclick = () => goToPage(i);
        pagination.appendChild(pageBtn);
    }

    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
            const dots = document.createElement('span');
            dots.className = 'pagination-info';
            dots.textContent = '...';
            pagination.appendChild(dots);
        }

        const lastBtn = document.createElement('button');
        lastBtn.className = 'pagination-btn';
        lastBtn.textContent = totalPages;
        lastBtn.onclick = () => goToPage(totalPages);
        pagination.appendChild(lastBtn);
    }

    const nextBtn = document.createElement('button');
    nextBtn.className = 'pagination-btn';
    nextBtn.innerHTML = '›';
    nextBtn.disabled = currentPage === totalPages;
    nextBtn.onclick = () => goToPage(currentPage + 1);
    pagination.appendChild(nextBtn);

    const info = document.createElement('span');
    info.className = 'pagination-info';
    info.textContent = allData.length + '개 중 ' + (((currentPage - 1) * itemsPerPage) + 1) + '-' + Math.min(currentPage * itemsPerPage, allData.length);
    pagination.appendChild(info);
}

function goToPage(page) {
    currentPage = page;
    renderPage();
    document.getElementById('content').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function updateTabCounts(currentType, currentCount) {
    const tabs = document.querySelectorAll('.tab');

    // API 호출이 중복되므로, 필요한 데이터만 요청하거나, 백엔드에서 카운트 API를 제공하는 것이 효율적입니다.
    // 현재는 기존 로직을 따라 fetch를 수행합니다.
    if (currentType === 'pending') {
        tabs[0].querySelector('.tab-count').textContent = currentCount;
        // 전체 카운트 업데이트
        fetch('/api/material-requests')
            .then(response => response.json())
            .then(data => {
                const count = Array.isArray(data) ? data.length : 0;
                tabs[1].querySelector('.tab-count').textContent = count;
            })
            .catch(error => console.error("전체 카운트 로드 실패:", error));
    } else { // currentType === 'all'
        tabs[1].querySelector('.tab-count').textContent = currentCount;
        // 미승인 카운트 업데이트
        fetch('/api/material-requests/pending')
            .then(response => response.json())
            .then(data => {
                const count = Array.isArray(data) ? data.length : 0;
                tabs[0].querySelector('.tab-count').textContent = count;
            })
            .catch(error => console.error("미승인 카운트 로드 실패:", error));
    }
}

function openModal(mrId, action) {
    currentMrId = mrId;
    currentAction = action; // 1: 승인, 2: 반려

    const modal = document.getElementById('modal');
    const modalTitle = document.getElementById('modalTitle');
    const confirmBtn = document.getElementById('confirmBtn');

    if (action === 1) {
        modalTitle.textContent = '✓ 발주 승인';
        confirmBtn.textContent = '승인';
        confirmBtn.className = 'btn-modal btn-confirm';
    } else { // action === 2 (반려)
        modalTitle.textContent = '✕ 발주 반려';
        confirmBtn.textContent = '반려';
        confirmBtn.className = 'btn-modal btn-confirm reject';
    }

    document.getElementById('modalNote').value = '';
    modal.style.display = 'block';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

function confirmAction() {
    const note = document.getElementById('modalNote').value.trim();
    const isApprove = currentAction === 1;

    // **[수정 4]** 승인자와 API 엔드포인트 설정
    //const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
    //const approvedBy = userInfo.employeeId; // userInfo 객체에 ID가 있다고 가정
    const approvedBy = 200;

    if (!approvedBy) {
        alert("사용자 정보를 찾을 수 없습니다. 로그인이 필요합니다.");
        return;
    }

    const apiUrl = isApprove ? '/api/material-requests/approve' : '/api/material-requests/reject';

    const data = {
        mrId: currentMrId,
        approvedBy: approvedBy, // **[수정 3]** 승인자 ID 추가
        note: note || null
    };

    // 반려 요청의 경우 approved 필드는 서비스 레이어에서 -1로 설정되므로 필요 없음.
    // 승인 요청의 경우 approvedBy가 승인자 ID로 사용됨.

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
        loadData(currentTab);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('처리 중 오류가 발생했습니다: ' + error.message);
    });
}

window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        closeModal();
    }
}

// 사용하지 않는 getStatusText 함수는 제거합니다.