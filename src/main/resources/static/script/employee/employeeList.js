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

const onClickDelete = (employeeId) => {
    const modal = document.getElementById("employee_delete_modal");
    modal.style.display = 'block';
    modal.querySelector(".btn-submit").setAttribute("name", employeeId);
}

const closeDeleteModal = () => {
    const modal = document.getElementById("employee_delete_modal");
    modal.style.display = 'none';
    modal.querySelector(".btn-submit").removeAttribute("name");
}

const deleteEmployee = (element) => {
    const employeeId = element.name;
    fetch("/employee/delete/" + employeeId, {method: "GET"})
        .then(() => {
            alert("사원 정보가 삭제되었습니다.")
            renderEmployeeList(params);
        }).catch(error => {
        console.error('Error:', error);
        alert("사원 정보를 삭제하지 못했습니다. 잠시후 다시 시도해주세요.");
    });
}

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
            if (list.length === 0 && params.page > 1) {
                params.page -= 1;
                renderEmployeeList(params);
            }

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
