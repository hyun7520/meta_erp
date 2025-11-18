package com.meta.stock.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // 이메일로 직원 찾기 (로그인 등에 사용 가능)
    Employee findByEmail(String email);
}