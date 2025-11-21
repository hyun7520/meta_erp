package com.meta.stock.user.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "EMPLOYEES")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @SequenceGenerator(name = "emp_seq", sequenceName = "emp_seq", allocationSize = 1)
    @Column(name = "employee_id")
    private long employeeId;

    @Column(length = 20)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(name = "hire_date")
    private String hireDate = LocalDate.now().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RolesEntity role;
}