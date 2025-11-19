let currentTab = 'pending';
let currentAction = null;
let currentMrId = null;
let currentPage = 1;
let itemsPerPage = 5;
let allData = [];

window.onload = function() {
    updateTime();
    setInterval(updateTime, 1000);
    loadUserInfo();
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

function loadData(type) {
    document.getElementById('loading').style.display = 'block';
    document.getElementById('content').style.display = 'none';
    document.getElementById('empty').style.display = 'none';

    const url = type === 'pending' ? '/api/material-requests/pending' : '/api/material-requests';

    fetch(url)
        .then(response => response.json())
        .then(data => {
            document.getElementById('loading').style.display = 'none';
            allData = data;

            if (data.length === 0) {
                document.getElementById('empty').style.display = 'block';
            } else {
                document.getElementById('content').style.display = 'block';
                renderPage();
            }

            updateTabCounts(type, data.length);
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

    renderTable(pageData);
    renderPagination();
}

function renderTable(data) {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '';

    data.forEach(item => {
        const row = document.createElement('tr');

        let statusBadge = '';
        if (item.approved === null || item.approved === 0) {
            statusBadge = '<span class="status-badge status-pending">미승인</span>';
        } else if (item.approved === 1) {
            statusBadge = '<span class="status-badge status-approved">승인</span>';
        } else {
            statusBadge = '<span class="status-badge status-rejected">반려</span>';
        }

        let actionButtons = '';
        if (item.approved === null || item.approved === 0) {
            actionButtons = 
                '<button class="btn btn-approve" onclick="openModal(' + item.mrId + ', 1)">✓ 승인</button>' +
                '<button class="btn btn-reject" onclick="openModal(' + item.mrId + ', 2)">✕ 반려</button>';
        } else {
            actionButtons = '<span style="color: #ff0000;">처리완료</span>';
        }

        row.innerHTML = 
            '<td><strong>' + item.mrId + '</strong></td>' +
            '<td>' + (item.materialName || '-') + '</td>' +
            '<td>' + (item.requestByName || '-') + '</td>' +
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

    if (currentType === 'pending') {
        tabs[0].querySelector('.tab-count').textContent = currentCount;
        fetch('/api/material-requests')
            .then(response => response.json())
            .then(data => {
                tabs[1].querySelector('.tab-count').textContent = data.length;
            });
    } else {
        tabs[1].querySelector('.tab-count').textContent = currentCount;
        fetch('/api/material-requests/pending')
            .then(response => response.json())
            .then(data => {
                tabs[0].querySelector('.tab-count').textContent = data.length;
            });
    }
}

function openModal(mrId, approved) {
    currentMrId = mrId;
    currentAction = approved;

    const modal = document.getElementById('modal');
    const modalTitle = document.getElementById('modalTitle');
    const confirmBtn = document.getElementById('confirmBtn');

    if (approved === 1) {
        modalTitle.textContent = '✓ 발주 승인';
        confirmBtn.textContent = '승인';
        confirmBtn.className = 'btn-modal btn-confirm';
    } else {
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

    const data = {
        mrId: currentMrId,
        approved: currentAction,
        note: note || null
    };

    fetch('/api/material-requests/approve', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
        closeModal();
        alert(currentAction === 1 ? '승인되었습니다.' : '반려되었습니다.');
        loadData(currentTab);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('처리 중 오류가 발생했습니다.');
    });
}

window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        closeModal();
    }
}