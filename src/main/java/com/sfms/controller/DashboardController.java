package com.sfms.controller;

import com.sfms.model.Student;
import com.sfms.service.BalanceService;
import com.sfms.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final StudentService studentService;
    private final BalanceService balanceService;

    public DashboardController(StudentService studentService, BalanceService balanceService) {
        this.studentService = studentService;
        this.balanceService = balanceService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Student> students = studentService.findAll();
        Map<Long, BigDecimal> rests = students.stream().collect(Collectors.toMap(Student::getId, balanceService::totalRest));
        Map<Long, Boolean> arrears = students.stream().collect(Collectors.toMap(Student::getId, balanceService::hasArrearsFromPreviousYears));
        model.addAttribute("students", students);
        model.addAttribute("rests", rests);
        model.addAttribute("arrears", arrears);
        return "dashboard/index";
    }
}
