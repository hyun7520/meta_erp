package com.meta.stock.user.employees.dto;

public class EmployeeGetDto {
    private int employeeId;
    private String name;
    private String email;
    private String password;
    private String hireDate;
    private String role;
    private String department;

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
