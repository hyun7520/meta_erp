package com.meta.stock.user.employees.controller;

import com.meta.stock.user.employees.dto.EmployeeGetDto;
import com.meta.stock.user.employees.dto.EmployeeInsertDto;
import com.meta.stock.user.employees.mapper.EmployeesMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeesController {
    @Autowired
    EmployeesMapper employeesMapper;

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
    public String showEmployees(){
        return "employees/employeesList";
    }

    @PostMapping(value = "login.mb")
    public ResponseEntity<Map<String, String>> login(@RequestBody EmployeeInsertDto eDto, HttpSession session) {
        EmployeeGetDto employee = employeesMapper.findByEmail(eDto.getEmail());
        Map<String, String> status = new HashMap<>();
        if (employee == null) {
            status.put("status", "no email");
            status.put("value", "존재하지 않는 이메일입니다. 다시 입력하세요.");
        } else if (employee.getPassword().equals(eDto.getPassword())) {
            status.put("status", "login");
            status.put("value", "로그인 되었습니다.");
            session.setAttribute("employee", employee);
        } else {
            status.put("status", "no password");
            status.put("value", "비밀번호가 일치하지않습니다. 다시 입력하세요.");
        }

        return ResponseEntity.ok(status);
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("eDto", new EmployeeInsertDto());
        return "employees/register";
    }

    @PostMapping(value = "registerProc")
    public String registerProc(@ModelAttribute("eDto") @Valid EmployeeInsertDto eDto, BindingResult bResult, Model model){
        if(bResult.hasErrors())
            return "employees/register";

        employeesMapper.insertEmployee(eDto);
        return "employees/login";
    }

    @RequestMapping(value = "/checkId.mb")
    @ResponseBody//=>문자열 자체를 return 하고자 할때 사용하는 것
    public String checkDuplicate(@RequestParam("id") String id){
        int count = employeesMapper.selectCountById(id);

        String result = "";
        if(count > 0){
            result = "duplicate";
        } else {
            result = "available";
        }

        return result;
    }

    @GetMapping(value = "/updateEmployee/{employeeId}")
    public String updateEmployee(@PathVariable int employeeId,
                                 @RequestParam(value = "whatColumn",required = false) String whatColumn,
                                 @RequestParam(value = "keyword",required = false) String keyword,
                                 @RequestParam(value = "page",defaultValue = "1") int page,
                              Model model, HttpSession session){
        EmployeeInsertDto employee = employeesMapper.selectEmployeeById(employeeId);
        model.addAttribute("employee",employee);

        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        return "employees/updateEmployee";
    }

    @PostMapping(value = "/updateEmployeeProc")
    public String updateSongProc(@ModelAttribute("employee") @Valid EmployeeInsertDto employee,
                                 @RequestParam(value = "whatColumn",required = false) String whatColumn,
                                 @RequestParam(value = "keyword",required = false) String keyword,
                                 @RequestParam(value = "page",defaultValue = "1") int page,
                                 BindingResult bResult, Model model){
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);

        if(bResult.hasErrors())
            return "employees/updateEmployee";

        employeesMapper.updateEmployee(employee);
        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/showEmployees?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
    }

    @PostMapping("/employee/update")
    public ResponseEntity<Boolean> updateEmployee(@RequestBody EmployeeInsertDto employee) {
        employeesMapper.updateEmployee(employee);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/employee/register")
    public ResponseEntity<Boolean> registerEmployee(@RequestBody EmployeeInsertDto employee) {
        employee.setEmployee_id(employeesMapper.getLastEmployeeId() + 1);
        employeesMapper.insertEmployee(employee);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/employee/delete/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable int employeeId) {
        employeesMapper.deleteEmployee(employeeId);
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/deleteEmployee")
    public String deleteEmployee(@RequestParam(value = "employee_id") int employee_id,
                                 @RequestParam(value = "whatColumn",required = false) String whatColumn,
                                 @RequestParam(value = "keyword",required = false) String keyword,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 Model model, HttpSession session){

        employeesMapper.deleteEmployee(employee_id);

        int limit = 7;
        int offset = (page - 1) * limit;
        Map<String, Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        int totalCount = employeesMapper.countTotalEmployees(params);
        if(totalCount % limit == 0) {
            page = page - 1;
        }

        String encodeKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";
        return "redirect:/showEmployees?page="+page+"&whatColumn="+whatColumn+"&keyword="+encodeKeyword;
    }

    @GetMapping(value = "/employees")
    public ResponseEntity<Map<String, Object>> getEmployeeList(
            @RequestParam(value = "whatColumn",required = false)String whatColumn,
            @RequestParam(value = "keyword",required = false)String keyword,
            @RequestParam(value = "page", defaultValue = "1")int page
    ){
        int limit = 4;
        int offset = (page-1)*limit;

        Map<String, Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        List<EmployeeGetDto> elist = employeesMapper.selectAll(params);

        int totalCount = employeesMapper.countTotalEmployees(params);
        int totalPage = (int)Math.ceil((double)totalCount/limit);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", elist);
        resultMap.put("whatColumn",whatColumn);
        resultMap.put("keyword",keyword);
        resultMap.put("page", page);
        resultMap.put("totalPage", totalPage);

        return ResponseEntity.ok(resultMap);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeInsertDto> getEmployeeById(@PathVariable int employeeId) {
        return ResponseEntity.ok(employeesMapper.selectEmployeeById(employeeId));
    }
}
