package com.meta.stock.user.employees.repository;

import com.meta.stock.user.employees.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface employeesEntityRepositiry extends JpaRepository<Employees, Integer> {
}
