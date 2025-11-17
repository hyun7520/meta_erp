package com.meta.stock.user.employees.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Employees")
public class employees {
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
}
