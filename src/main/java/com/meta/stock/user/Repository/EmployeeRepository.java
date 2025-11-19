package com.meta.stock.user.Repository;

import com.meta.stock.user.Entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    // 이메일로 직원 조회
    Optional<EmployeeEntity> findByEmail(String email);

    // 이름으로 직원 조회
    Optional<EmployeeEntity> findByName(String name);
}