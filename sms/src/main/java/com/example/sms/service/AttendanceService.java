package com.example.sms.service;

import com.example.sms.model.Attendance;
import com.example.sms.model.Student;
import com.example.sms.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public void markAttendance(Student student, String status) {
        LocalDate today = LocalDate.now();
        Optional<Attendance> existing =
            attendanceRepository.findByStudentIdAndDate(student.getId(), today);

        if (existing.isPresent()) {
            existing.get().setStatus(status);
            attendanceRepository.save(existing.get());
        } else {
            Attendance a = new Attendance();
            a.setStudent(student);
            a.setDate(today);
            a.setStatus(status);
            attendanceRepository.save(a);
        }
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}