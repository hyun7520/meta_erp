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

document.addEventListener('DOMContentLoaded', function() {
    loadProductRequestList();

    // 모달 외부 클릭 시 닫기
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

// product request list render method
let prParam = {
    page: 1,
    keyword: '',
    sortBy: "prId",
    sortDir: 'ASC',
}

const searchPR = () => {
    const main = document.getElementById("product_request_list");
    const keyword = main.querySelector("input[name='keyword']").value;

    prParam.keyword = keyword;
    loadProductRequestList(prParam);
}

const sortPR = (element) => {
    const sortKey = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "ASC" : "DESC";

    const main = document.getElementById("product_request_list");
    const sortableClasses = main.querySelectorAll(".sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === sortKey ? sortDir.toLowerCase() : "");
    }

    prParam.sortBy = sortKey;
    prParam.sortDir = sortDir;
    loadProductRequestList(prParam);
}

const renderPRList = (list) => {
    const main = document.getElementById("product_request_list");
    const title = main.querySelector("h2");
    title.querySelector("span").innerText = list?.length || 0;

    const tbody = main.querySelector("tbody");
    tbody.innerHTML = "";
    if (list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align: center; color: #9CA3AF; padding: 40px;">
                    주문이 없습니다.
                </td>
            </tr>
        `;
    } else {
        list.forEach(data => {
            const tr = document.createElement("tr");

            let completeBadge = '';
            if (data.complete === 0) {
                completeBadge = `<span class="status-badge status-pending">미수주</span>`;
            } else if (data.complete === 1) {
                completeBadge = `<span class="status-badge status-approved">수주완료</span>`;
            } else if (data.complete === 2) {
                completeBadge = `<span class="status-badge status-rejected">완료</span>`;
            }

            tr.innerHTML = `
                <td>${data.prId}1</td>
                <td>${data.managementEmployee || "김경영"}</td>
                <td>${data.productName || "부대찌개"}</td>
                <td>${data.toCompany || "거래처A"}</td>
                <td><span class="qty-unit">${data.targetQty || 0}</span></td>
                <td>${data.deadline || "2025-12-01"}</td>
                <td>${completeBadge}</td>
                <td>
                    <button type="button" class="btn btn-approve" style="padding: 6px 12px;"
                        onclick="showDetailModal(${data.prId})">
                        <i class="fas fa-eye"></i> 상세
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }
}

const movePrPage = (pageNum) => {
    prParam.page = pageNum;
    loadProductRequestList(prParam);
}

const loadProductRequestList = ({page, keyword, sortBy, sortDir} = prParam) => {
    const parseParams = `page=${page - 1}&keyword=${keyword}&sortBy=${sortBy || 'prId'}&sortDir=${sortDir || 'ASC'}`;
    fetch("/pr/list?" + parseParams, {method: "GET"})
        .then(response => response.json())
        .then(json => {
            prParam.page = json.pRequests.number + 1;
            prParam.keyword = json.keyword || "";
            prParam.sortBy = json.sortBy;
            prParam.sortDir = json.sortDir;

            renderPRList(json.pRequests.content);
            renderPagination(prParam.page, json.pRequests.totalPages, movePrPage);
        })
        .catch(error => {
            console.log("오류 발생 : " + error);
            renderPRList();
        });
}
