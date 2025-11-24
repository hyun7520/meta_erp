const ROAD_EMPLOYEE = "/employee/";
const INSERT_EMPLOYEE = "/employee/register";
const UPDATE_EMPLOYEE = "/employee/update";
let updateEmployeeId = 0;

window.onclick = (event) => {
    const modal = document.getElementById("employee_modal");
    if (event.target === modal) {
        closeEmployeeModal();
    }
}


const openEmployeeModal = (employeeId = 0) => {
    const defaultTitle = "➕ 사원 정보 ";
    const modal =  document.getElementById('employee_modal');
    modal.style.display = 'block';
    const title = document.getElementById("employee_modal_h3");
    if (employeeId > 0) {
        title.innerText = defaultTitle + "수정";
        modal.querySelector(".btn-submit").innerHTML = "정보 수정";
        updateEmployeeId = employeeId;
        renderUpdateEmployeeModal(employeeId);
    } else {
        title.innerText = defaultTitle + "추가";
        modal.querySelector(".btn-submit").innerHTML = "정보 추가";
        renderInsertEmployeeModal();
    }
}

const closeEmployeeModal = () => {
    document.getElementById('employee_modal').style.display = 'none';
}

const insertBaseValue = (data) => {
    const form = document.getElementById('employee_insert_form');
    const [prefix, domain] = data.email.split('@');

    form.querySelector("input[id='employee_name']").value = data.name;
    form.querySelector("input[id='employee_password']").value = data.password;
    form.querySelector("input[id='email_prefix']").value = prefix;
    form.querySelector("input[id='employee_hire_date']").value = calcDrawDate(new Date(data.hire_date)).split(" ")[0];

    const domainSelect = document.getElementById('email_domain');
    const domainOptions = Array.from(domainSelect.options);
    const domainIdx = domainOptions.findIndex(option => option.value === domain);
    if (domainIdx >= 0) domainSelect.selectedIndex = domainIdx;

    const departmentSelect = document.getElementById('employee_department');
    const departmentOptions = Array.from(departmentSelect.options);
    const departmentIdx = departmentOptions.findIndex(option => option.value === String(data.department_id));
    if (departmentIdx >= 0) departmentSelect.selectedIndex = departmentIdx;

    const roleSelect = document.getElementById('employee_role');
    const roleOptions = Array.from(roleSelect.options);
    const roleIdx = roleOptions.findIndex(option => option.value === String(data.role_id));
    if (roleIdx >= 0) roleSelect.selectedIndex = roleIdx;
}

const renderUpdateEmployeeModal = (employeeId) => {
    fetch(ROAD_EMPLOYEE + employeeId, {method: 'GET'})
        .then(response => response.json())
        .then(insertBaseValue);
}

const renderInsertEmployeeModal = () => {
    const form = document.getElementById('employee_insert_form');

    form.querySelector("input[id='employee_name']").value = "";
    form.querySelector("input[id='employee_password']").value = "";
    form.querySelector("input[id='email_prefix']").value = "";
    form.querySelector("input[id='employee_hire_date']").value = calcDrawDate(new Date()).split(" ")[0];

    document.getElementById('email_domain').selectedIndex = 0;
    document.getElementById('employee_department').selectedIndex = 0;
    document.getElementById('employee_role').selectedIndex = 0;
}

const submitEmployeeInsert = () => {
    const postAPI = updateEmployeeId > 0 ? UPDATE_EMPLOYEE : INSERT_EMPLOYEE;
    console.log(postAPI);
    const isAvail = checkDataIn();
    if (isAvail === false) {
        return false;
    }
    const data = inputData();
    if (updateEmployeeId > 0) {
        data["employee_id"] = updateEmployeeId;
    }

    fetch(postAPI, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    }).then(response => {
        if (!response.ok) {
            return response.json().then(err => Promise.reject(err));
        }
        return response.json();
    })
    .then(() => {
       closeEmployeeModal();
       alert(`사원 정보가 ${updateEmployeeId > 0 ? '수정' : '등록'} 되었습니다.`);
       renderEmployeeList();
    }).catch((error) => {
        console.log(error);
        alert(`사원 정보 ${updateEmployeeId > 0 ? '수정' : '등록'}에 실패하였습니다.`);
    });
}

const checkDataIn = () => {
    const form = document.getElementById('employee_insert_form');
    if (form.querySelector("input[id='employee_name']").value === '') {
        alert("사원명을 입력하세요.")
        return false;
    }

    if (form.querySelector("input[id='employee_password']").value === '') {
        alert("사원의 비밀번호를 입력하세요.")
        return false;
    }

    if (form.querySelector("input[id='email_prefix']").value === '') {
        alert("사원의 이메일을 입력하세요.")
        return false;
    }

    if (form.querySelector("select[id='email_domain']").selectedOptions.item(0).value === "") {
        alert("사원의 이메일 도메인을 선택하세요.")
        return false;
    }

    const hireDate = form.querySelector("input[id='employee_hire_date']").value;
    if (hireDate === '') {
        alert("사원의 입사일을 입력하세요.")
        return false;
    } else if (hireDate > new Date()) {
        alert("사원의 입사일은 오늘 혹은 오늘 이전이여야 합니다.")
        return false;
    }

    if (form.querySelector("select[id='employee_department']").selectedOptions.item(0).value === "") {
        alert("사원의 부서를 선택하세요.")
        return false;
    }

    if (form.querySelector("select[id='employee_role']").selectedOptions.item(0).value === "") {
        alert("사원의 직책을 선택하세요.")
        return false;
    }
}

const inputData = () => {
    const form = document.getElementById('employee_insert_form');
    const body = {};

    body["name"] = form.querySelector("input[id='employee_name']").value;
    body["password"] = form.querySelector("input[id='employee_password']").value;

    const prefix = form.querySelector("input[id='email_prefix']").value
    const domain = form.querySelector("select[id='email_domain']").selectedOptions.item(0).value;
    body["email"] = prefix + "@" + domain;

    body["hire_date"] = form.querySelector("input[id='employee_hire_date']").value;

    const departmentSelect = form.querySelector("select[id='employee_department']");
    const roleSelect = form.querySelector("select[id='employee_role']");

    body["department_id"] = departmentSelect.selectedOptions.item(0).value;
    body["role_id"] = roleSelect.selectedOptions.item(0).value;

    return body;
}

