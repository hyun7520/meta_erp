package com.meta.stock.user.employees.repository;

import com.meta.stock.user.employees.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
}
