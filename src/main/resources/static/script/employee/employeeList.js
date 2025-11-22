const GET_EMPLOYEES_API = "/employees";

let params = {
    page: 1,
    whatColumn: '',
    keyword: '',
};

window.addEventListener('DOMContentLoaded', (event) => {
    renderEmployeeList();
});


const renderEmployeeTable = (list) => {
    const tbody = document.getElementById("employees_tbody");
    tbody.innerHTML = '';
    list.forEach(element => {
        const row = document.createElement('tr');

        const dates = calcDrawDate(new Date(element.hire_date)).split(" ")[0];
        row.innerHTML = `
            <td>${element.employee_id}</td>
            <td>${element.name}</td>
            <td>${element.email}</td>
            <td>${dates}</td>
            <td>${element.department_id}</td>
            <td>${element.role_id}</td>
            <td><button type="button" class="btn btn-cancel">수정</button></td>
            <td><button type="button" class="btn btn-reject">삭제</button></td>
        `;
        tbody.appendChild(row);
    });
}

const movePage = (pageNum) => {
    params.page = pageNum;
    renderEmployeeList(params);
}

const renderEmployeeList = ({page, whatColumn, keyword} = params) => {
    const apiWithParms = GET_EMPLOYEES_API + `?page=${page}&whatColumn=${whatColumn}&keyword=${keyword}`;
    fetch(apiWithParms, {method: "GET"})
        .then(response => response.json())
        .then(({list, totalPage, ...data}) => {
            params = {...data};
            renderEmployeeTable(list);
            renderPagination(data.page, totalPage, movePage);
        });
}

const onSearchEmployees = () => {
    const form = document.getElementById("employee_search");
    const column = form.querySelector("select[name='whatColumn']").selectedOptions.item(0)
    const search = form.querySelector("input[name='keyword']")

    params.whatColumn = column.value;
    params.keyword = search.value;
    renderEmployeeList(params);
}
