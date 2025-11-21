package com.meta.stock.user.employees.repository;

import com.meta.stock.user.employees.entity.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class roleRepository {
    @Autowired
    roleEntityRepository roleEntityRepository;

    @Test
    public void insertRole(){
        Role role = new Role();
        role.setRole_id(1);
        role.setRole_name("사원");
        role.setDescription("작업 담당");

        role.setRole_id(2);
        role.setRole_name("감독관");
        role.setDescription("사원 감독");

        roleEntityRepository.save(role);
    }
}
