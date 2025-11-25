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
    data.hire_date = calcDrawDate(new Date(data.hire_date)).split(" ")[0]

    form.querySelectorAll("input").forEach(el => {
        const id = el.id;
        const key = id.substring(id.indexOf("_") + 1);
        el.value = id.startsWith("email") ? prefix : data[key];
    });

    form.querySelectorAll("select").forEach(el => {
        const key = el.id.substring(el.id.indexOf("_") + 1);
        const selectedValue = el.id.startsWith("email") ? domain : data[key + "_id"].toString();
        Array.from(el.options).forEach(opt => {
            if (opt.value === selectedValue) {
                el.selectedIndex = opt.index;
            }
        });
    });
}

const renderUpdateEmployeeModal = (employeeId) => {
    fetch(ROAD_EMPLOYEE + employeeId, {method: 'GET'})
        .then(response => response.json())
        .then(insertBaseValue);
}

const renderInsertEmployeeModal = () => {
    const form = document.getElementById('employee_insert_form');

    // 각 input type들 초기화 작업
    form.querySelectorAll("input").forEach(el => {
        el.value = el.type  === "data" ? calcDrawDate(new Date()).split(" ")[0] : "";
    });

    form.querySelectorAll("select").forEach(el => el.selectedIndex = 0);
}

const submitEmployeeInsert = () => {
    const isEdit = updateEmployeeId > 0;
    const postAPI = isEdit ? UPDATE_EMPLOYEE : INSERT_EMPLOYEE;

    if (checkDataIn() === false) {
        return false;
    }
    const data = inputData();
    if (isEdit) {
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
       alert(`사원 정보가 ${isEdit ? '수정' : '등록'} 되었습니다.`);
       renderEmployeeList();
    }).catch((error) => {
        console.log(error);
        alert(`사원 정보 ${isEdit ? '수정' : '등록'}에 실패하였습니다.`);
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

    form.querySelectorAll("input").forEach(el => {
        const id = el.id;
        if (id.startsWith("email")) {
            body["email"] = el.value + "@";
        } else {
            const key = id.substring(id.indexOf("_") + 1);
            body[key] = el.value;
        }
    });

    form.querySelectorAll("select").forEach(el => {
        if (el.id.startsWith("email")) {
            body["email"] += el.selectedOptions.item(0).value;
        } else {
            const key = el.id.substring(el.id.indexOf("_") + 1) + "_id";
            body[key] = el.selectedOptions.item(0).value;
        }
    });

    return body;
}

