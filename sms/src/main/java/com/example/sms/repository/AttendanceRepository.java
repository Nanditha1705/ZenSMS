// AttendanceRepository.java
package com.example.sms.repository;
import com.example.sms.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    Optional<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
}