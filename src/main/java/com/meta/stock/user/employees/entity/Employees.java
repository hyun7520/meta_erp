package com.meta.stock.user.employees.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Employees")
public class Employees {
    @Id
    @Column(name = "employee_id")
    private Integer employee_id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "hire_date")
    private Date hire_date;

    // Department와의 N:1 관계 (department_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id") // Employees 테이블의 FK 컬럼
    private Department department;

    // Role과의 N:1 관계 (role_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id") // Employees 테이블의 FK 컬럼
    private Role role;

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
