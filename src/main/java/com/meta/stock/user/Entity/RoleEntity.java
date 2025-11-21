package com.meta.stock.user.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @Column(name = "ROLE_ID")
    private long roleId;

    @Column(name = "ROLE_NAME", length = 20)
    private String roleName;

    @Column(name = "DESCRIPTION")
    private String description;

    // 해당 역할을 가진 직원들
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employees;
}