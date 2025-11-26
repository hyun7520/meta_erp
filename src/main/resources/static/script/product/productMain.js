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

// 해당 productionModal이 load 했을때 호출한 메서드
document.addEventListener('DOMContentLoaded', function() {
    loadOngoingProductList();
    loadProgressBar();
    loadRequestReadyMaterials();
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

// productionMain.html 화면에 로드할 함수들
// 왼쪽: 진행 중인 주문 (50%)
const productsParam = {
    page: 1,
    keyword: '',
    sortBy: 'requestDate',
    sortDir: 'ASC',
}

const searchOngoingList = () => {
    const main = document.getElementById("product_ongoing_list");
    const keyword = main.querySelector("input[name='prKeyword']").value;

    productsParam.search = keyword;
    loadOngoingProductList(productsParam);
}

const sortOngoingList = (element) => {
    const sortKey = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "ASC" : "DESC";

    const main = document.getElementById("product_ongoing_list");
    const sortableClasses = main.querySelectorAll(".sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === sortKey ? sortDir.toLowerCase() : "");
    }

    productsParam.sortBy = sortKey;
    productsParam.sortDir = sortDir;
    loadOngoingProductList(productsParam);
}

const resetOngoingSort = () => {
    const main = document.getElementById("product_ongoing_list");
    const sortableClasses = main.querySelectorAll(".sortable");
    sortableClasses.forEach(el => el.className = "sortable");

    productsParam.sortBy = 'requestDate';
    productsParam.sortDir = 'ASC';
    loadOngoingProductList(productsParam);
}

const renderOngoingList = (list) => {
    const main = document.getElementById("product_ongoing_list");
    const title = main.querySelector("h2");
    title.querySelector("span").innerText = list?.length || 0;

    const tbody = main.querySelector("tbody");
    tbody.innerHTML = "";
    if (list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; color: #9CA3AF; padding: 40px;">
                    진행 중인 주문이 없습니다.
                </td>
            </tr>
        `;
    } else {
        list.forEach(data => {
            const tr = document.createElement("tr");
            const stockBadge = `<span class="status-badge status-${data.isStockSufficient ? 'approved' : 'pending'}">${data.isStockSufficient ? '충분' : '부족'}</span>`;

            tr.innerHTML = `
                <td>${data.prId || 1}</td>
                <td>
                    <a th:onclick="|showDetailModal(${data.prId})|"
                    style="color: #2A9D8F; text-decoration: none; font-weight: 600; cursor: pointer;">
                    <span>${data.productName || ""}</span>
                </a>
                </td>
                <td>${data.toCompany || "거래처A"}</td>
                <td><span class="qty-unit">${data.targetQty || 0}</span></td>
                <td>${data.deadline}</td>
                <td><span class="qty-unit">${data.inStockQty || 0}</span></td>
                <td>${stockBadge}</td>
            `;

            tbody.appendChild(tr);
        });
    }
}

const moveOngoingPage = (pageNum) => {
    productsParam.page = pageNum;
    loadOngoingProductList(productsParam);
}

const loadOngoingProductList = ({page, keyword, sortBy, sortDir} = productsParam) => {
    const parseParams = `page=${page - 1}&prKeyword=${keyword}&prSortBy=${sortBy || 'requestDate'}&prSortDir=${sortDir || 'ASC'}`;
    fetch("/product/ongoing?" + parseParams, {method: "GET"})
        .then(response => response.json())
        .then(json => {
            productsParam.page = json.products.number + 1;
            productsParam.keyword = json.prKeyword || "";
            productsParam.sortBy = json.prSortBy;
            productsParam.sortDir = json.prSortDir;

            renderOngoingList(json.products.content);
            renderPagination(productsParam.page, json.products.totalPages, moveOngoingPage, "pagination_ongoing");
        })
        .catch(error => {
            console.log("오류 발생 : " + error);
            renderOngoingList();
        });
}

// 오른쪽 위: 완제품 재고 현황
const drawProgressBar = (list) => {
    const progressBoard = document.getElementById("products_progress_bar");
    progressBoard.innerHTML = "";

    if (list.length === 0) {
        const emptyBox = document.createElement("div");
        emptyBox.style.textAlign = "center";
        emptyBox.style.color = "#9CA3AF";
        emptyBox.style.padding = "40px";
        emptyBox.innerHTML = "재고가 없습니다.";
        progressBoard.appendChild(emptyBox);
    } else {
        list.forEach(stock => {
            const div = document.createElement("div");
            div.style.padding = "15px 10px";
            div.style.borderBottom = "1px solid #E5E7EB";

            const progressSize = stock.qty > 1000 ? 100 : (stock.qty * 100 / 1000);
            div.innerHTML = `
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                    <span style="font-weight: 600; color: #374151; font-size: 14px;">${stock.productName || '부대찌개'}</span>
                    <span class="qty-unit" style="font-size: 18px; font-weight: 700; color: #2A9D8F;">${stock.qty || '480'}</span>
                </div>

                <div style="background: #E5E7EB; border-radius: 8px; height: 10px; overflow: hidden;">
                    <div style="background: linear-gradient(90deg, #2A9D8F, #10B981); height: 100%; width: ${progressSize}%; border-radius: 8px; transition: width 0.3s;">
                    </div>
                </div>
            `
            progressBoard.appendChild(div);
        });
    }
}

const loadProgressBar = () => {
    fetch("/product/progress", {method: "GET"})
        .then(response =>  response.json())
        .then(data => drawProgressBar(data))
        .catch(error => {
            console.log("오류 발생 : " + error);
            drawProgressBar([]);
        });
}


// 오른쪽 아래: 승인 대기 중인 재료 발주 요청
const materialsParam = {
    page: 1,
    keyword: '',
    sortBy: 'requestDate',
    sortDir: 'DESC',
}

const searchReadyTable = () => {
    const main = document.getElementById("product_material_ready");
    const keyword = main.querySelector("input[name='mrKeyword']").value;

    materialsParam.search = keyword;
    loadRequestReadyMaterials(materialsParam);
}

const readySort = (element) => {
    const sortKey = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "ASC" : "DESC";

    const main = document.getElementById("product_material_ready");
    const sortableClasses = main.querySelectorAll(".sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === sortKey ? sortDir.toLowerCase() : "");
    }

    materialsParam.sortBy = sortKey;
    materialsParam.sortDir = sortDir;
    loadRequestReadyMaterials(materialsParam);
}

const renderReadyTable = (list) => {
    const main = document.getElementById("product_material_ready");
    const title = main.querySelector("h2");
    title.querySelector("span").innerText = list?.length || 0;

    const tbody = main.querySelector("tbody");
    tbody.innerHTML = "";
    if (list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align: center; color: #9CA3AF; padding: 20px;">
                    발주 요청이 없습니다.
                </td>
            </tr>
        `;
    } else {
        list.forEach(data => {
            const tr = document.createElement("tr");

            let approveBadge = '';
            if (data.approved === 0) {
                approveBadge = '<span class="status-badge status-pending">대기</span>';
            } else if (data.approved === 1) {
                approveBadge = '<span class="status-badge status-approved">승인</span>';
            } else if (data.approved === 2) {
                approveBadge = '<span class="status-badge status-rejected">완료</span>';
            }

            tr.innerHTML = `
                <td>${data.mrId || "1"}</td>
                <td>${data.materialName || "밀가루"}</td>
                <td><span class="qty-unit">${data.qty || "500"}</span></td>
                <td>${data.requestDate || "2025-11-15"}</td>
                <td>${data.note || "추가 발주"}</td>
                <td>${approveBadge}</td>
            `
            tbody.appendChild(tr);
        });
    }
}

const moveReadyPage = (pageNum) => {
    materialsParam.page = pageNum;
    loadRequestReadyMaterials(materialsParam);
}

const loadRequestReadyMaterials = ({page, keyword, sortBy, sortDir} = materialsParam) => {
    const parseParams = `page=${page - 1}&mrKeyword=${keyword}&mrSortBy=${sortBy || 'requestDate'}&mrSortDir=${sortDir || 'DESC'}`;
    fetch("/product/readyMaterials?" + parseParams, {method: "GET"})
        .then(response => response.json())
        .then(json => {
            materialsParam.page = json.materials.number + 1;
            materialsParam.keyword = json.mrKeyword || "";
            materialsParam.sortBy = json.mrSortBy;
            materialsParam.sortDir = json.mrSortDir;

            renderReadyTable(json.materials.content);
            renderPagination(materialsParam.page, json.materials.totalPages, moveReadyPage, "pagination_ready");
        })
        .catch(error => {
            console.log("오류 발생 : " + error);
            renderReadyTable();
        });
}
