package com.example.sms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
@Data @NoArgsConstructor @AllArgsConstructor
public class Attendance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String status;   // PRESENT or ABSENT

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}