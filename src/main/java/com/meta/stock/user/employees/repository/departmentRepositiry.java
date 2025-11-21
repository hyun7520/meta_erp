package com.meta.stock.user.employees.repository;

import com.meta.stock.user.employees.entity.Department;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class departmentRepositiry {
    @Autowired
    departmentEntityRepository departmentEntityRepository;

    @Test
    public void insertDepartment(){
        Department department = new Department();
        department.setDepartment_id(100);
        department.setDepartment_name("경영");
        department.setDescription("회사 경영 관리");

        department.setDepartment_id(200);
        department.setDepartment_name("생산");
        department.setDescription("회사 제품 생산 관리");

        departmentEntityRepository.save(department);
    }

}
