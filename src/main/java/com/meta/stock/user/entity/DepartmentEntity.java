package com.meta.stock.user.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEPARTMENTS")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dept_seq")
    @SequenceGenerator(name = "dept_seq", sequenceName = "dept_seq", allocationSize = 1)
    @Column(name = "department_id")
    private long departmentId;

    @Column(name = "department_name", length = 20)
    private String departmentName;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "department")
    private List<EmployeeEntity> employees = new ArrayList<>();
}
