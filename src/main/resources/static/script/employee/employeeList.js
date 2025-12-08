const GET_EMPLOYEES_API = "/employees";

let params = {
    page: 1,
    whatColumn: '',
    keyword: '',
    sortBy: 'employee_id',
    sortDir: 'desc'
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
            closeDeleteModal();
            renderEmployeeList(params);
        }).catch(error => {
        console.error('Error:', error);
        alert("사원 정보를 삭제하지 못했습니다. 잠시후 다시 시도해주세요.");
    });
}

const renderEmployeeTable = (list) => {
    const tbody = document.getElementById("employees_tbody");
    tbody.innerHTML = '';
    const keyList = ['employeeId', 'name', 'email', 'hireDate', 'department', 'role'];
    list.forEach(element => {
        const row = document.createElement('tr');

        for (let key of keyList) {
            const td = document.createElement("td");
            if (key === 'hireDate') {
                td.innerHTML = calcDrawDate(new Date(element[key])).split(" ")[0];
            } else {
                td.innerHTML = element[key];
            }
            row.appendChild(td);
        }

        row.innerHTML += `<td><button type="button" class="btn btn-cancel" onclick="onClickEdit(${element.employeeId})">수정</button></td>`;
        row.innerHTML += `<td><button type="button" class="btn btn-reject" onclick="onClickDelete(${element.employeeId})">삭제</button></td>`;

        tbody.appendChild(row);
    });
}

const movePage = (pageNum) => {
    params.page = pageNum;
    renderEmployeeList(params);
}

const renderEmployeeList = (parameters = params) => {
    const {page, whatColumn, keyword, sortBy, sortDir} = parameters;
    const parseParam = `page=${page}&sortBy=${sortBy}&sortDir=${sortDir}&whatColumn=${whatColumn}&keyword=${keyword}`;

    fetch(GET_EMPLOYEES_API + '?' + parseParam, {method: "GET"})
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

const sortEmployees = (element) => {
    const key = element.getAttribute("name");
    const sortDir = element.className.includes("desc") ? "asc" : "desc";

    const sortableClasses = document.getElementsByClassName("sortable");
    for (let el of sortableClasses) {
        el.className = "sortable " + (el.getAttribute("name") === key ? sortDir : "");
    }

    params.sortBy = key;
    params.sortDir = sortDir;
    renderEmployeeList(params);
}

const resetSearchEmployees = () => {
    const form = document.getElementById("employee_search");
    form.querySelector("select[name='whatColumn']").selectedIndex = 0;
    form.querySelector("input[name='keyword']").value = "";

    const sortableClasses = document.getElementsByClassName("sortable");
    for (let el of sortableClasses) {
        el.className = "sortable";
    }

    params = {
        page: 1,
        whatColumn: '',
        keyword: '',
        sortBy: 'employee_id',
        sortDir: 'desc'
    }

    renderEmployeeList(params);
}
