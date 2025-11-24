const onLoginSubmit = () => {
    const loginForm = document.getElementById("login_form");

    const email = loginForm.querySelector("#employee_login_email").value;
    const password = loginForm.querySelector("#employee_login_pw").value;

    if (email.trim() && password.trim()) {
        login({email: email, password: password});
    } else {
        alert("이메일과 비밀번호를 입력하세요.");
    }
}

const resetInput = (status) => {
    if (status === "no email") {
        document.getElementById("employee_login_email").value = "";
        document.getElementById("employee_login_pw").value = "";
    } else {
        document.getElementById("employee_login_pw").value = "";
    }
}

const login = (data) => {
    fetch("/login.mb",
        {method: "POST", headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)})
        .then(response => response.json())
        .then(({status, value}) => {
            alert(value);
            if (status === "login") {
                location.href = "/dash";
            } else {
                resetInput();
            }
        });
}
