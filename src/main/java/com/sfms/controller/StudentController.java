package com.sfms.controller;

import com.sfms.dto.PaymentForm;
import com.sfms.dto.StudentForm;
import com.sfms.model.Student;
import com.sfms.service.BalanceService;
import com.sfms.service.ClassService;
import com.sfms.service.PaymentService;
import com.sfms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final ClassService classService;
    private final PaymentService paymentService;
    private final BalanceService balanceService;

    public StudentController(StudentService studentService, ClassService classService,
                             PaymentService paymentService, BalanceService balanceService) {
        this.studentService = studentService;
        this.classService = classService;
        this.paymentService = paymentService;
        this.balanceService = balanceService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        StudentForm form = new StudentForm();
        form.setAcademicYear(LocalDate.now().getYear());
        model.addAttribute("studentForm", form);
        model.addAttribute("classes", classService.findAll());
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute StudentForm studentForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("classes", classService.findAll());
            return "students/form";
        }
        studentService.create(studentForm);
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        model.addAttribute("rest", balanceService.totalRest(student));
        model.addAttribute("totalFees", balanceService.totalFees(student));
        model.addAttribute("totalPayments", balanceService.totalPayments(student));
        model.addAttribute("payments", paymentService.findByStudent(student));
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setPaymentDate(LocalDate.now());
        model.addAttribute("paymentForm", paymentForm);
        return "students/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { studentService.delete(id); }
        catch (IllegalStateException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/students";
    }
}
