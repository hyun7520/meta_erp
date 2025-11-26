const pStockParam = {
    page: 1,
    keyword: '',
    sortBy: "storageDate",
    sortDir: 'DESC',
}

window.addEventListener("DOMContentLoaded", () => {
    updateTime();
    setInterval(updateTime, 1000);
    loadProductStockList();
});

const searchPStockk = () => {
    const main = document.getElementById("product_stock_list");
    const keyword = main.querySelector("input[name='keyword']").value;

    pStockParam.keyword = keyword;
    loadProductStockList(pStockParam);
}

const sortPStock = (element) => {
    const sortKey = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "ASC" : "DESC";

    const main = document.getElementById("product_stock_list");
    const sortableClasses = main.querySelectorAll(".sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === sortKey ? sortDir.toLowerCase() : "");
    }

    pStockParam.sortBy = sortKey;
    pStockParam.sortDir = sortDir;
    loadProductStockList(pStockParam);
}

const renderPStockList = (pageData) => {
    const main = document.getElementById("product_stock_list");
    const title = main.querySelector("h2");
    title.querySelector("span").innerText = pageData.totalElements;

    const tbody = main.querySelector("tbody");
    tbody.innerHTML = "";
    if (pageData.totalElements === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align: center; color: #9CA3AF; padding: 40px;">
                    재고가 없습니다.
                </td>
            </tr>
        `;
    } else {
        pageData.content.forEach(data => {
            const tr = document.createElement("tr");

            let leftBadge = '';
            if (data.expired) {
                leftBadge = `
                    <span style="color: #EF4444; font-weight: 600;">
                        <span>${data.daysLeft * -1}</span>일 지남
                    </span>
                `;
            } else {
                leftBadge = `
                    <span style="color: #6B7280;">
                        <span>${data.daysLeft || 7}</span>일
                    </span>
                `;
            }

            tr.innerHTML = `
                <td>${data.productName || '부대찌개'}</td>
                <td>${data.lotId || 'LOT-001'}</td>
                <td><span class="qty-unit">${data.qty || 0}</span></td>
                <td>${data.unit || '개'}</td>
                <td>${data.storageDate || '2025-11-01'}</td>
                <td>${data.shelfLifeDays || '2025-11-25'}</td>
                <td>${leftBadge}</td>
                <td>
                    <span class="${data.statusBadgeClass}">${data.statusText || '정상'}</span>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }
}

const movePStockPage = (pageNum) => {
    pStockParam.page = pageNum;
    loadProductStockList(pStockParam);
}

const loadProductStockList = ({page, keyword, sortBy, sortDir} = pStockParam) => {
    const parseParams = `page=${page - 1}&keyword=${keyword}&sortBy=${sortBy || 'storageDate'}&sortDir=${sortDir || 'DESC'}`;
    fetch("/product/stock/list?" + parseParams, {method: "GET"})
        .then(response => response.json())
        .then(json => {
            pStockParam.page = json.stocks.number + 1;
            pStockParam.keyword = json.keyword || "";
            pStockParam.sortBy = json.sortBy;
            pStockParam.sortDir = json.sortDir;

            renderPStockList(json.stocks);
            renderPagination(pStockParam.page, json.stocks.totalPages, movePStockPage);
        })
        .catch(error => {
            console.log("오류 발생 : " + error);
            renderPStockList([]);
        });
}
