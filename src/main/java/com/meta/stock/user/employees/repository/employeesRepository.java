package com.meta.stock.user.employees.repository;

import com.meta.stock.user.employees.entity.Employees;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class employeesRepository {
    @Autowired
    employeesEntityRepositiry employeesEntityRepositiry;

    @Test
    public void insertEmployees(){
        Employees employees = new Employees();

        employeesEntityRepositiry.save(employees);
    }
}
