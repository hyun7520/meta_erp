const GET_TABLE_API = "/dash/table";

let params = {
    page: 1,
    column: '',
    search: '',
    date: '',
    sort: 'product_id',
    order: 'desc',
};

window.addEventListener("load", () => {
    renderDashTable();
});

const renderTable = (list) => {
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
            &&  (today.getDate() === lifeEndDay.getDate());

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
    params.page = pageNum;
    renderDashTable(params);
}

const renderDashTable = ({page, column, search, date, sort, order} = params) => {
    const parseParams = `page=${page || 1}&column=${column || ''}&search=${search || ''}&start_date=${date || ''}&sort=${sort || 'product_id'}&order=${order || 'asc'}`;
    const getLink = GET_TABLE_API + "?" + parseParams;
    fetch(getLink, {method: 'GET'})
        .then(response => response.json())
        .then(({list, page, totalPage, ...data}) => {
            params = {...data}

            renderTable(list);
            renderPagination(page, totalPage, movePage)

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
    renderDashTable(params);
}

const dashSort = (element) => {
    const sortKey = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "asc" : "desc";

    const sortableClasses = document.getElementsByClassName("sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === sortKey ? sortDir : "");
    }

    params.sort = sortKey;
    params.order = sortDir;
    renderDashTable(params);
}

const resetDash = () => {
    const sortableClasses = document.getElementsByClassName("sortable");
    for (let el of sortableClasses) {
        el.className = "sortable";
    }

    params = {
        page: 1,
        column: '',
        search: '',
        date: '',
        sort: 'product_id',
        order: 'desc',
    }

    renderDashTable(params);
}
