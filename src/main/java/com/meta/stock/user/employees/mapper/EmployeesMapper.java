package com.meta.stock.user.employees.mapper;

import com.meta.stock.user.employees.dto.EmployeeGetDto;
import com.meta.stock.user.employees.dto.EmployeeInsertDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeesMapper {
    EmployeeGetDto findByEmail(String email);

    void insertEmployee(EmployeeInsertDto eDto);

    List<EmployeeGetDto> selectAll(Map<String, Object> params);

    EmployeeInsertDto selectEmployeeById(int employeeId);

    void updateEmployee(EmployeeInsertDto employee);

    void deleteEmployee(int employeeId);

    int countTotalEmployees(Map<String, Object> params);

    int getLastEmployeeId();
}
