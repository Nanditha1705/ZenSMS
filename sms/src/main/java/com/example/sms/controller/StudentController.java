package com.example.sms.controller;

import com.example.sms.model.Student;
import com.example.sms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final MarksService marksService;
    private final AttendanceService attendanceService;
    private final com.example.sms.repository.UserRepository userRepository;

    private Student getCurrentStudent(Authentication auth) {
        UserDetails ud = (UserDetails) auth.getPrincipal();
        Long userId = userRepository.findByUsername(ud.getUsername())
            .orElseThrow().getId();
        return studentService.getStudentByUserId(userId);
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        model.addAttribute("student", student);
        return "student/dashboard";
    }

    @GetMapping("/marks")
    public String marks(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        model.addAttribute("marks", marksService.getMarksByStudent(student.getId()));
        model.addAttribute("student", student);
        return "student/marksheet";
    }

    @GetMapping("/attendance")
    public String attendance(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        model.addAttribute("attendanceList",
            attendanceService.getAttendanceByStudent(student.getId()));
        model.addAttribute("student", student);
        return "student/attendance";
    }

    @PostMapping("/attendance/mark")
    public String markAttendance(Authentication auth,
                                 @RequestParam String status) {
        Student student = getCurrentStudent(auth);
        attendanceService.markAttendance(student, status);
        return "redirect:/student/attendance?success";
    }
}