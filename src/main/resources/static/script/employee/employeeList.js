const GET_EMPLOYEES_API = "/employees";

let params = {
    page: 1,
    whatColumn: '',
    keyword: '',
};

window.addEventListener('DOMContentLoaded', (event) => {
    renderEmployeeList();
});

const onClickEdit = (employeeId) => {
    openEmployeeModal(employeeId);
}

const onClickDelete = (employeeId) => {}

const renderEmployeeTable = (list) => {
    const tbody = document.getElementById("employees_tbody");
    tbody.innerHTML = '';
    list.forEach(element => {
        const row = document.createElement('tr');

        const dates = calcDrawDate(new Date(element.hireDate)).split(" ")[0];
        row.innerHTML = `
            <td>${element.employeeId}</td>
            <td>${element.name}</td>
            <td>${element.email}</td>
            <td>${dates}</td>
            <td>${element.department}</td>
            <td>${element.role}</td>
            <td><button type="button" class="btn btn-cancel" onclick="onClickEdit(${element.employeeId})">수정</button></td>
            <td><button type="button" class="btn btn-reject" onclick="onClickDelete(${element.employeeId})">삭제</button></td>
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
