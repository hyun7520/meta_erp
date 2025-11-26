const materialParam = {
    page: 1,
    keyword: '',
    sortBy: "requestDate",
    sortDir: 'DESC',
}

window.addEventListener("DOMContentLoaded", () => {
    loadMaterialRequestList();
    loadMaterialProgress();
});

// 재료 발주 요청 목록
const searchMR = () => {
    const main = document.getElementById("material_request_list");
    const keyword = main.querySelector("input[name='keyword']").value;

    materialParam.keyword = keyword;
    loadMaterialRequestList(materialParam);
}

const sortMr = (element) => {
    const sortKey = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "ASC" : "DESC";

    const main = document.getElementById("material_request_list");
    const sortableClasses = main.querySelectorAll(".sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === sortKey ? sortDir.toLowerCase() : "");
    }

    materialParam.sortBy = sortKey;
    materialParam.sortDir = sortDir;
    loadMaterialRequestList(materialParam);
}

const renderMaterialRequestList = (pageData) => {
    const main = document.getElementById("material_request_list");
    const title = main.querySelector("h2");
    title.querySelector("span").innerText = pageData.totalElements;
    main.querySelector("input[name='keyword']").value = materialParam.keyword;

    const tbody = main.querySelector("tbody");
    tbody.innerHTML = "";

    if (pageData.totalElements.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align: center; color: #9CA3AF; padding: 40px;">
                    발주 요청이 없습니다.
                </td>
            </tr>
        `;
    } else {
        pageData.content.forEach(data => {
            const tr = document.createElement("tr");
            let trClass = "status-2";
            let approveBadge = "";
            if (data.approved === 0) {
                trClass = "status-0"
                approveBadge = `<span class="status-badge status-pending">확인중</span>`;
            } else if (data.approved === 1) {
                trClass = "status-1"
                approveBadge = `<span class="status-badge status-approved">승인</span>`;
            } else if (data.approved === 2) {
                trClass = "status-2"
                approveBadge = `<span class="status-badge status-rejected">승인</span>`;
            }

            tr.className = trClass;
            tr.innerHTML = `
                <td>${data.mrId || 1}</td>
                <td>${data.materialName || '햄'}</td>
                <td><span class="qty-unit">${data.qty || 0}</span></td>
                <td>${data.requestDate || '2025-11-05'}</td>
                <td>${approveBadge}</td>
            `;

            if (data.approved === 0) {
                tr.innerHTML += `
                    <td>
                        <button type="button" class="btn btn-approve" onclick="location.href='/material/update/${data.mrId}'">
                            수정
                        </button>
                    </td>
                `;
            } else  {
                tr.innerHTML += `<td></td>`;
            }

            tbody.appendChild(tr);
        });
    }
}

const moveMRPage = (pageNum) => {
    materialParam.page = pageNum;
    loadMaterialRequestList(materialParam);
}

const loadMaterialRequestList = ({page, keyword, sortBy, sortDir} = materialParam) => {
    const parseParams = `page=${page - 1}&keyword=${keyword}&sortBy=${sortBy || 'requestDate'}&sortDir=${sortDir || 'DESC'}`;
    fetch("/material/list?" + parseParams, {method: "GET"})
        .then(response => response.json())
        .then(json => {
            materialParam.page = json.materials.number + 1;
            materialParam.keyword = json.keyword || "";
            materialParam.sortBy = json.sortBy;
            materialParam.sortDir = json.sortDir;

            renderMaterialRequestList(json.materials);
            renderPagination(materialParam.page, json.materials.totalPages, moveMRPage);
        })
        .catch(error => {
            console.log("오류 발생 : " + error);
            renderMaterialRequestList([]);
        });
}

// 현재 재료 재고 progress bar
const renderMaterialProgress = (list) => {
    const main = document.getElementById("material_progress");
    const listBoard = main.querySelector(".material-list");
    listBoard.innerHTML = "";

    if (list.length === 0) {
        listBoard.innerHTML = `
            <div style="text-align: center; color: #9CA3AF; padding: 40px;">
                <i class="fas fa-box-open" style="font-size: 32px; opacity: 0.3; margin-bottom: 10px;"></i>
                <div>재고 정보가 없습니다.</div>
            </div>
        `;
    } else {
        list.forEach(data => {
            const card = document.createElement("div");
            card.className = "material-card";

            const progressSize = data.qty > 10000 ? 100 : (data.qty * 100 / 10000);
            card.innerHTML = `
                <div class="material-header">
                    <span class="material-name">${data.materialName || '햄'}</span>
                    <span class="material-qty">${data.qty || '0 g'}</span>
                </div>
                <div class="material-progress">
                    <div class="material-progress-bar" style="width: ${progressSize}%"></div>
                </div>
            `;
            listBoard.appendChild(card);
        });
    }
}

const loadMaterialProgress = () => {
    fetch("/material/progress", {method: "GET"})
        .then(response => response.json())
        .then(data => {
            renderMaterialProgress(data);
        }).catch(error => {
            console.log("오류 발생 :" + error);
            renderMaterialProgress([]);
        });
}
