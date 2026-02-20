package com.sfms.controller;

import com.sfms.dto.ClassForm;
import com.sfms.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/classes")
public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", classService.findAll());
        return "classes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("classForm", new ClassForm());
        return "classes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute ClassForm classForm, BindingResult result) {
        if (result.hasErrors()) return "classes/form";
        classService.create(classForm);
        return "redirect:/classes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { classService.delete(id); }
        catch (IllegalStateException ex) { ra.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/classes";
    }
}
