package com.meta.stock.user.employees.mapper;

import com.meta.stock.user.employees.dto.EmployeesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeesMapper {
    EmployeesDto findById(int employeeId);

    void insertEmployee(EmployeesDto eDto);

    int selectCountById(String id);

    List<EmployeesDto> selectAll(Map<String, Object> params);

    EmployeesDto selectEmployeeById(int employeeId);

    void updateEmployee(EmployeesDto employee);

    void deleteEmployee(int employeeId);

    int countTotalEmployees(Map<String, Object> params);
}
