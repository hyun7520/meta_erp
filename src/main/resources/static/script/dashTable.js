const GET_TABLE_API = "/dash/table";

const params = {
    column: '',
    search: '',
    date: '',
    sort: 'product_id'
};

window.addEventListener("load", () => {
    renderDashTable();
});

const renderTable = ({list, page, totalPage, ...data}) => {
    const tbody = document.getElementById('dash_table_body');
    tbody.innerHTML = '';

    list.forEach(item => {
        const row = document.createElement('tr');

        // 사용가능 / 판매완료 / 폐기예정
        const today = new Date();
        const lifeEndDay = new Date(item.shelfLifeDays);
        const isToday =
            (today.getFullYear() === lifeEndDay.getFullYear())
            && (today.getMonth() === lifeEndDay.getMonth())
            &&  (today.getDate() - 3 === lifeEndDay.getDate());

        let statusBadge = '';
        if (item.qty === 0) {
            statusBadge = '<span class="status-badge status-pending">제품 없음</span>';
        } else if (lifeEndDay - today < 0 || isToday) {
            statusBadge = '<span class="status-badge status-rejected">폐기 예정</span>';
        } else {
            statusBadge = '<span class="status-badge status-approved">출하 가능</span>';
        }

        row.innerHTML = `
            <td>${item.serialCode}</td>
            <td>${item.productName}</td>
            <td>${item.qty}(${item.unit})</td>
            <td>${statusBadge}</td>
            <td>${item.storageDate}</td>
            <td>${item.shelfLifeDays}</td>
        `;

        tbody.appendChild(row);
    });
}

const movePage = (pageNum) => {
    renderDashTable(pageNum);
}

const parseParams = (page, {column, search, date, sort}) => {
    return `page=${page}&column=${column}&search=${search}&start_date=${date}&sort=${sort}`
}

const renderDashTable = (page = 1) => {
    const getLink = GET_TABLE_API + "?" + parseParams(page, params);
    fetch(getLink, {method: 'GET'})
        .then(response => response.json())
        .then(data => {
            renderTable(data);
            renderPagination(data.page, data.totalPage, movePage)

            const now = new Date();
            const drawTime = document.getElementById("products_table_refresh_time")
            drawTime.innerText = calcDrawDate(now);
        });
}

const dashSearch = () => {
    const column = document.getElementsByName("column").item(0).selectedOptions.item(0)
    const search = document.getElementsByName("search").item(0)
    const date = document.getElementsByName("start_date").item(0)

    params.column = column.value;
    params.search = search.value;
    params.date = date.value;
    renderDashTable();
}

const dashSort = () => {
    const sortKey = document.getElementsByName("sort").item(0).selectedOptions.item(0).value;
    params.sort = sortKey;
    renderDashTable();
}
