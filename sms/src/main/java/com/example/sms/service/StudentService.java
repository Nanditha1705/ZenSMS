package com.example.sms.service;

import com.example.sms.model.*;
import com.example.sms.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserService userService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student, String username, String password) {
        User user = userService.createUser(username, password, "ROLE_STUDENT");
        student.setUser(user);
        return studentRepository.save(student);
    }

    public Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public void assignDepartmentAndSemester(Long studentId, Long deptId, int semester) {
        Student student = getById(studentId);
        Department dept = departmentRepository.findById(deptId)
            .orElseThrow(() -> new RuntimeException("Department not found"));
        student.setDepartment(dept);
        student.setSemester(semester);
        studentRepository.save(student);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void addDepartment(String name) {
        Department dept = new Department();
        dept.setName(name);
        departmentRepository.save(dept);
    }
}