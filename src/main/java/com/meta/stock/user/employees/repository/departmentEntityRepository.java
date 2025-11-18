package com.meta.stock.user.employees.repository;

import com.meta.stock.user.employees.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface departmentEntityRepository extends JpaRepository<Department, Integer> {
}
