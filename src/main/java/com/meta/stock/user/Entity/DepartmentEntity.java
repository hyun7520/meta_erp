package com.meta.stock.user.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "DEPARTMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentEntity {

    @Id
    @Column(name = "DEPARTMENT_ID")
    private long departmentId;

    @Column(name = "DEPARTMENT_NAME", length = 20)
    private String departmentName;

    @Column(name = "DESCRIPTION")
    private String description;

    // 부서 소속 직원들
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employees;
}