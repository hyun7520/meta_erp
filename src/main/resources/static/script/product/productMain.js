function showDetailModal(prId) {
    // 모달 표시
    document.getElementById('detailModal').style.display = 'block';
    document.body.style.overflow = 'hidden';

    // AJAX로 기존 HTML fragment 로드
    fetch('/pr/' + prId)
        .then(response => response.text())
        .then(html => {
            document.getElementById('modalContent').innerHTML = html;
        })
        .catch(error => {
            document.getElementById('modalContent').innerHTML =
                '<div style="text-align: center; padding: 40px; color: #EF4444;">오류가 발생했습니다.</div>';
        });
}

function closeModal() {
    document.getElementById('detailModal').style.display = 'none';
    document.body.style.overflow = 'auto';
}

// ESC 키로 모달 닫기
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        closeModal();
    }
});

// 모달 외부 클릭 시 닫기
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('detailModal');
    if (modal) {
        modal.addEventListener('click', function(event) {
            if (event.target === this) {
                closeModal();
            }
        });
    }
});

function toggleSelectAll(checkbox) {
    const checkboxes = document.querySelectorAll('.material-checkbox');
    checkboxes.forEach(cb => {
        cb.checked = checkbox.checked;
    });
}

function requestMaterials() {
    const checkboxes = document.querySelectorAll('.material-checkbox:checked');

    if (checkboxes.length === 0) {
        alert('발주할 재료를 선택해주세요.');
        return;
    }

    const materials = [];
    checkboxes.forEach(cb => {
        materials.push({
            fmid: cb.dataset.fmid,
            name: cb.value,
            qty: cb.dataset.qty,
            unit: cb.dataset.unit
        });
    });

    const params = new URLSearchParams();
    materials.forEach((material) => {
        params.append('fmIds', material.fmid);
        params.append('materialNames', material.name);
        params.append('quantities', material.qty);
        params.append('units', material.unit);
    });

    window.location.href = '/material/request?' + params.toString();
}

function acceptOrder(prId) {
    if (confirm('이 주문을 수주하시겠습니까?')) {
        fetch('/pr/accept/' + prId, { method: 'POST' })
            .then(response => {
                if (response.ok) {
                    alert('주문이 수주되었습니다.');
                    closeModal();
                    location.reload();
                } else {
                    alert('오류가 발생했습니다.');
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다.');
            });
    }
}

function shipOrder(prId) {
    if (confirm('제품을 출하하시겠습니까?')) {
        fetch('/pr/ship/' + prId, { method: 'POST' })
            .then(response => {
                if (response.ok) {
                    alert('제품이 출하되었습니다.');
                    closeModal();
                    location.reload();
                } else {
                    alert('오류가 발생했습니다.');
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다.');
            });
    }
}