package com.meta.stock.user.employees.mapper;

import com.meta.stock.user.employees.dto.employeesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface employeesMapper {
    employeesDto findById(int employeeId);

    void insertEmployee(employeesDto eDto);

    int selectCountById(String id);

    List<employeesDto> selectAll();

    employeesDto selectEmployeeById(int employeeId);

    void updateEmployee(employeesDto employee);

    void deleteEmployee(int employeeId);
}
