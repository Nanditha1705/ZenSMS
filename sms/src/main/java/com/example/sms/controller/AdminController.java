package com.example.sms.controller;

import com.example.sms.model.*;
import com.example.sms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StudentService studentService;
    private final MarksService marksService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("departments", studentService.getAllDepartments());
        return "admin/dashboard";
    }

    // --- Students ---
    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("departments", studentService.getAllDepartments());
        model.addAttribute("student", new Student());
        return "admin/students";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student,
                             @RequestParam String username,
                             @RequestParam String password) {
        studentService.addStudent(student, username, password);
        return "redirect:/admin/students";
    }

    // --- Departments ---
    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", studentService.getAllDepartments());
        return "admin/departments";
    }

    @PostMapping("/departments/add")
    public String addDepartment(@RequestParam String name) {
        studentService.addDepartment(name);
        return "redirect:/admin/departments";
    }

    // --- Assign Department + Semester ---
    @GetMapping("/assign")
    public String assignPage(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("departments", studentService.getAllDepartments());
        return "admin/assign";
    }

    @PostMapping("/assign")
    public String assign(@RequestParam Long studentId,
                         @RequestParam Long departmentId,
                         @RequestParam int semester) {
        studentService.assignDepartmentAndSemester(studentId, departmentId, semester);
        return "redirect:/admin/assign?success";
    }

    // --- Marks Upload ---
    @GetMapping("/marks")
    public String marksPage(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("mark", new Marks());
        return "admin/marks";
    }

    @PostMapping("/marks/upload")
    public String uploadMarks(@RequestParam MultipartFile file) throws Exception {
        marksService.uploadMarksFromCsv(file);
        return "redirect:/admin/marks?success";
    }

    @PostMapping("/marks/add")
    public String addMark(@ModelAttribute Marks marks,
                          @RequestParam Long studentId) {
        marks.setStudent(studentService.getById(studentId));
        marksService.addMark(marks);
        return "redirect:/admin/marks?success";
    }
}