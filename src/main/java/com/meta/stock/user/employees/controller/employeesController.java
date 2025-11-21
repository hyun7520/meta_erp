package com.meta.stock.user.employees.controller;

import com.meta.stock.user.employees.dto.employeesDto;
import com.meta.stock.user.employees.mapper.employeesMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class employeesController {
    @Autowired
    employeesMapper employeesMapper;

    @RequestMapping(value = "/login")
    public String login(){
        return "employees/login";
    }

    @PostMapping(value = "login.mb")
    public String login(employeesDto eDto, HttpServletResponse response,
                        HttpSession session) throws IOException {
        employeesDto employee = employeesMapper.findById(eDto.getEmployee_id());

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = response.getWriter();

        if(employee == null){
            pw.println("<script type='text/javascript'>");
            pw.println("alert('해당 아이디가 존재하지 않습니다.')");
            pw.println("history.back();"); // 이전 페이지로 돌아가기
            pw.println("</script>");
            pw.flush();
            return null;
        }

        else{
            if(employee.getPassword().equals(eDto.getPassword())){
                session.setAttribute("loginId", employee.getEmployee_id()); // ID를 세션에 저장
                return "dashboard/dashboard";
            }
            else{
                pw.println("<script type='text/javascript'>");
                pw.println("alert('비밀번호가 일치하지 않습니다.')");
                pw.println("history.back();"); // 이전 페이지로 돌아가기
                pw.println("</script>");
                pw.flush();
                return null;
            }
        }
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("eDto", new employeesDto());
        return "employees/register";
    }

    @PostMapping(value = "registerProc")
    public String registerProc(@ModelAttribute("eDto") @Valid employeesDto eDto, BindingResult bResult, Model model){
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

    @GetMapping(value = "updateEmployee")
    public String updateEmployee(@RequestParam(value = "employee_id") int employee_id,
                                 @RequestParam(value = "whatColumn",required = false) String whatColumn,
                                 @RequestParam(value = "keyword",required = false) String keyword,
                                 @RequestParam(value = "page",defaultValue = "1") int page,
                              Model model, HttpSession session){
        employeesDto employee = employeesMapper.selectEmployeeById(employee_id);
        model.addAttribute("employee",employee);

        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        return "employees/updateEmployee";
    }

    @PostMapping(value = "/updateEmployeeProc")
    public String updateSongProc(@ModelAttribute("employee") @Valid employeesDto employee,
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

    @GetMapping(value = "/showEmployees")
    public String showEmployees(Model model,
                                @RequestParam(value = "whatColumn",required = false)String whatColumn,
                                @RequestParam(value = "keyword",required = false)String keyword,
                                @RequestParam(value = "page", defaultValue = "1")int page){
        int limit = 7;
        int offset = (page-1)*limit;

        Map<String, Object> params = new HashMap<>();
        params.put("whatColumn", whatColumn);
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("limit", limit);

        List<employeesDto> elist = employeesMapper.selectAll(params);

        int totalCount = employeesMapper.countTotalEmployees(params);

        int totalPage = (int)Math.ceil((double)totalCount/limit);

        Map<Integer, String> departmentMap = new HashMap<>();
        departmentMap.put(100,"경영");
        departmentMap.put(200,"생산");

        Map<Integer, String> roleMap = new HashMap<>();
        roleMap.put(1,"사원");
        roleMap.put(2,"감독관");

        model.addAttribute("elist",elist);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("page",page);
        model.addAttribute("whatColumn",whatColumn);
        model.addAttribute("keyword",keyword);
        model.addAttribute("departmentMap",departmentMap);
        model.addAttribute("roleMap",roleMap);

        return "employees/employeesList";
    }
}
