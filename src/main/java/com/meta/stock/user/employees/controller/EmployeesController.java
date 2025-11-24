package com.meta.stock.user.employees.controller;

import com.meta.stock.user.employees.dto.EmployeeGetDto;
import com.meta.stock.user.employees.dto.EmployeeInsertDto;
import com.meta.stock.user.employees.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class EmployeesController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/login")
    public String login(){
        return "employees/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping(value = "/showEmployees")
    public String showEmployees(HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        } else {
            EmployeeGetDto employee = (EmployeeGetDto) session.getAttribute("employee");
            if (employee.getDepartment().equals("생산") || employee.getRole().equals("사원")) {
                return "redirect:/dash";
            }
        }
        return "employees/employeesList";
    }

    @PostMapping(value = "login.mb")
    public ResponseEntity<Map<String, Object>> login(@RequestBody EmployeeInsertDto eDto, HttpSession session) {
        Map<String, Object> status = employeeService.loginLogic(eDto.getEmail(), eDto.getPassword());
        if (status.get("status").equals("login") && status.containsKey("employee")) {
            session.setAttribute("employee", status.get("employee"));
        }
        return ResponseEntity.ok(status);
    }

    @PostMapping("/employee/update")
    public ResponseEntity<Boolean> updateEmployee(@RequestBody EmployeeInsertDto employee) {
        employeeService.updateEmployee(employee);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/employee/register")
    public ResponseEntity<Boolean> registerEmployee(@RequestBody EmployeeInsertDto employee) {
        employeeService.insertEmployee(employee);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/employee/delete/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/employees")
    public ResponseEntity<Map<String, Object>> getEmployeeList(
            @RequestParam(value = "whatColumn",required = false)String whatColumn,
            @RequestParam(value = "keyword",required = false)String keyword,
            @RequestParam(value = "page", defaultValue = "1")int page
    ){
        return ResponseEntity.ok(employeeService.getEmployeeList(whatColumn, keyword, page));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeInsertDto> getEmployeeById(@PathVariable int employeeId) {
        return ResponseEntity.ok(employeeService.findById(employeeId));
    }
}
