package com.sfms.controller;

import com.sfms.dto.PaymentForm;
import com.sfms.model.Payment;
import com.sfms.model.Student;
import com.sfms.service.BalanceService;
import com.sfms.service.PaymentService;
import com.sfms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students/{studentId}/payments")
public class PaymentController {
    private final StudentService studentService;
    private final PaymentService paymentService;
    private final BalanceService balanceService;

    public PaymentController(StudentService studentService, PaymentService paymentService, BalanceService balanceService) {
        this.studentService = studentService;
        this.paymentService = paymentService;
        this.balanceService = balanceService;
    }

    @PostMapping
    public String create(@PathVariable Long studentId, @Valid @ModelAttribute PaymentForm paymentForm,
                         BindingResult result, Model model) {
        Student student = studentService.findById(studentId);
        if (result.hasErrors()) {
            model.addAttribute("student", student);
            model.addAttribute("payments", paymentService.findByStudent(student));
            model.addAttribute("rest", balanceService.totalRest(student));
            return "students/detail";
        }
        Payment payment = paymentService.create(student, paymentForm, "admin");
        return "redirect:/students/" + studentId + "/payments/" + payment.getId() + "/receipt";
    }

    @GetMapping("/{paymentId}/edit")
    public String editForm(@PathVariable Long studentId, @PathVariable Long paymentId, Model model) {
        Payment payment = paymentService.findById(paymentId);
        PaymentForm form = new PaymentForm();
        form.setAmount(payment.getAmount());
        form.setPaymentDate(payment.getPaymentDate());
        model.addAttribute("paymentForm", form);
        model.addAttribute("payment", payment);
        model.addAttribute("studentId", studentId);
        return "payments/edit";
    }

    @PostMapping("/{paymentId}/edit")
    public String edit(@PathVariable Long studentId, @PathVariable Long paymentId,
                       @Valid @ModelAttribute PaymentForm paymentForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("payment", paymentService.findById(paymentId));
            model.addAttribute("studentId", studentId);
            return "payments/edit";
        }
        paymentService.edit(paymentId, paymentForm, "admin");
        return "redirect:/students/" + studentId;
    }

    @GetMapping("/{paymentId}/receipt")
    public String receipt(@PathVariable Long studentId, @PathVariable Long paymentId, Model model) {
        Student student = studentService.findById(studentId);
        Payment payment = paymentService.findById(paymentId);
        model.addAttribute("student", student);
        model.addAttribute("payment", payment);
        model.addAttribute("rest", balanceService.totalRest(student));
        return "payments/receipt";
    }
}
