package com.meta.stock.user.employees.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class EmployeesDto {
    private int employee_id;

    @NotBlank(message = "이름은 필수입력사항 입니다.")
    private String name;

    private String email;

    @NotBlank(message = "비밀번호는 필수입력사항 입니다.")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "입사일은 필수입력사항 입니다.")
    @PastOrPresent(message = "입사일은 오늘 혹은 오늘 이전이여야 합니다.")
    private Date hire_date;

    @Min(value = 1, message = "부서를 선택해주세요.")
    private int department_id;

    @Min(value = 1, message = "직책을 선택해주세요.")
    private int role_id;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getHire_date() {
        return hire_date;
    }

    public void setHire_date(Date hire_date) {
        this.hire_date = hire_date;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
