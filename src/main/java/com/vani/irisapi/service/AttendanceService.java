package com.vani.irisapi.service;

import com.vani.irisapi.entity.Attendance;
import com.vani.irisapi.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    public String markAttendance(String studentId) {

        LocalDate today = LocalDate.now();

        Optional<Attendance> existing =
                attendanceRepository.findByStudentIdAndDate(studentId, today);

        if (existing.isPresent()) {
            return "Already Marked";
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setDate(today);
        attendance.setStatus("Present");

        attendanceRepository.save(attendance);

        return "Marked";
    }
    public List<Attendance> getAttendanceByStudent(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}
