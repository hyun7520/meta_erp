package com.meta.stock.user;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEES")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "SEQ_EMPLOYEES", allocationSize = 1)
    @Column(name = "EMPLOYEE_ID")
    private long employeeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "HIRE_DATE")
    private String hireDate;

    @Column(name = "DEPARTMENT_ID")
    private long departmentId;

    @Column(name = "ROLE_ID")
    private long roleId;

    // Getter
    public long getEmployeeId() {
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

    public long getDepartmentId() {
        return departmentId;
    }

    public long getRoleId() {
        return roleId;
    }

    // Setter
    public void setEmployeeId(long employeeId) {
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

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}