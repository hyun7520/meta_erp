package com.meta.stock.user.employees.mapper;

import com.meta.stock.user.employees.dto.employeesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface employeesMapper {
    employeesDto findById(int employeeId);

    void insertEmployee(employeesDto eDto);

    int selectCountById(String id);

    List<employeesDto> selectAll(Map<String, Object> params);

    employeesDto selectEmployeeById(int employeeId);

    void updateEmployee(employeesDto employee);

    void deleteEmployee(int employeeId);

    int countTotalEmployees(Map<String, Object> params);
}
