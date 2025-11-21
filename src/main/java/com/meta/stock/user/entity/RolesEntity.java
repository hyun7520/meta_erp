package com.meta.stock.user.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROLES")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "role_name", length = 20)
    private String roleName;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "role")
    private List<EmployeeEntity> employees = new ArrayList<>();
}
