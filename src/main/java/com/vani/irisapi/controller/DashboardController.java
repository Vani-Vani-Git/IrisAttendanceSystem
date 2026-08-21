package com.vani.irisapi.controller;

import com.vani.irisapi.entity.Attendance;
import com.vani.irisapi.entity.Student;
import com.vani.irisapi.repository.AttendanceRepository;
import com.vani.irisapi.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardController(
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository) {

        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // 1. Total enrolled students
    @GetMapping("/students/count")
    public long getStudentCount() {
        return studentRepository.count();
    }

    // 2. List all enrolled students
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 3. Number of students present today
    @GetMapping("/attendance/today/count")
    public long getPresentTodayCount() {
        return attendanceRepository.countByDateAndStatus(
                LocalDate.now(),
                "Present"
        );
    }

    // 4. Today's attendance records
    @GetMapping("/attendance/today")
    public List<Map<String, Object>> getTodayAttendance() {

        List<Attendance> records =
                attendanceRepository.findByDate(LocalDate.now());

        return records.stream().map(attendance -> {

            Map<String, Object> result = new HashMap<>();

            result.put("studentId", attendance.getStudentId());

            Student student = studentRepository
                    .findById(attendance.getStudentId())
                    .orElse(null);

            result.put(
                    "studentName",
                    student != null ? student.getName() : "Unknown"
            );

            result.put("time", attendance.getStatus());

            return result;

        }).toList();
    }

}