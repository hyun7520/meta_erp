package com.meta.stock.user;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "SEQ_EMPLOYEES", allocationSize = 1)
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "HIRE_DATE")
    private String hireDate;  // 👈 LocalDate에서 String으로

    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "ROLE_ID")
    private Long roleId;

    // Getter
    public Long getEmployeeId() {
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

    public String getHireDate() {  // 👈 String
        return hireDate;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    // Setter
    public void setEmployeeId(Long employeeId) {
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

    public void setHireDate(String hireDate) {  // 👈 String
        this.hireDate = hireDate;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}