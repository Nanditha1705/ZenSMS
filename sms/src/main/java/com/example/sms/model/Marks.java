package com.example.sms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marks")
@Data @NoArgsConstructor @AllArgsConstructor
public class Marks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subjectName;
    private int marksObtained;
    private int maxMarks;
    private int semester;
    private String examType;   // e.g. MID, FINAL

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}