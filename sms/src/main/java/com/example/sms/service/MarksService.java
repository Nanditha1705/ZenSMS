package com.example.sms.service;

import com.example.sms.model.Marks;
import com.example.sms.model.Student;
import com.example.sms.repository.MarksRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarksService {

    private final MarksRepository marksRepository;
    private final StudentService studentService;

    public List<Marks> getMarksByStudent(Long studentId) {
        return marksRepository.findByStudentId(studentId);
    }

    public void uploadMarksFromCsv(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                String[] cols = line.split(",");
                // CSV: rollNumber,subjectName,marksObtained,maxMarks,semester,examType
                if (cols.length < 6) continue;
                Student student = studentService.getById(
                    studentService.getAllStudents().stream()
                        .filter(s -> s.getRollNumber().equals(cols[0].trim()))
                        .findFirst()
                        .orElseThrow().getId()
                );
                Marks marks = new Marks();
                marks.setStudent(student);
                marks.setSubjectName(cols[1].trim());
                marks.setMarksObtained(Integer.parseInt(cols[2].trim()));
                marks.setMaxMarks(Integer.parseInt(cols[3].trim()));
                marks.setSemester(Integer.parseInt(cols[4].trim()));
                marks.setExamType(cols[5].trim());
                marksRepository.save(marks);
            }
        }
    }

    public void addMark(Marks marks) {
        marksRepository.save(marks);
    }
}