package com.meta.stock.user.employees.service;

import com.meta.stock.user.employees.dto.EmployeeGetDto;
import com.meta.stock.user.employees.dto.EmployeeInsertDto;
import com.meta.stock.user.employees.mapper.EmployeesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeesMapper employeesMapper;

    public Map<String, Object> getEmployeeList(String whatColumn, String keyword, int page, String sortBy, String sortDir) {
        int limit = 4;
        int offset = (page - 1) * limit;

        Map<String, Object> map = new HashMap<>();
        map.put("whatColumn", whatColumn);
        map.put("keyword", keyword);
        map.put("offset", offset);
        map.put("limit", limit);
        map.put("sortBy", sortBy);
        map.put("sortDir", sortDir);

        List<EmployeeGetDto> elist = employeesMapper.selectAll(map);

        int totalCount = employeesMapper.countTotalEmployees(map);
        int totalPage = (int)Math.ceil((double)totalCount/limit);

        Map<String, Object> resultMap = new HashMap<>();
        map.put("list", elist);
        map.put("page", page);
        map.put("totalPage", totalPage);

        map.remove("offset");
        map.remove("limit");

        return map;
    }

    public EmployeeInsertDto findById(int id) {
        return employeesMapper.selectEmployeeById(id);
    }

    public Map<String, Object> loginLogic(String email, String password) {
        EmployeeGetDto employee = employeesMapper.findByEmail(email);
        Map<String, Object> status = new HashMap<>();
        if (employee == null) {
            status.put("status", "no email");
            status.put("value", "존재하지 않는 이메일입니다. 다시 입력하세요.");
        } else if (employee.getPassword().equals(password)) {
            status.put("status", "login");
            status.put("value", "로그인 되었습니다.");
            status.put("employee", employee);
        } else {
            status.put("status", "no password");
            status.put("value", "비밀번호가 일치하지않습니다. 다시 입력하세요.");
        }

        return status;
    }

    public void insertEmployee(EmployeeInsertDto employee) {
        employee.setEmployee_id(employeesMapper.getLastEmployeeId() + 1);
        employeesMapper.insertEmployee(employee);
    }

    public void updateEmployee(EmployeeInsertDto employee) {
        employeesMapper.updateEmployee(employee);
    }

    public void deleteEmployee(int id) {
        employeesMapper.deleteEmployee(id);
    }
}
