package com.vani.irisapi.controller;
import com.vani.irisapi.repository.StudentRepository;
import com.vani.irisapi.entity.Student;
import com.vani.irisapi.service.AttendanceService;
import com.vani.irisapi.entity.Attendance;
import com.vani.irisapi.service.IrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private IrisService irisService;
    @Autowired
    private AttendanceService attendanceService;
    @PostMapping("/mark")
    public Map<String, Object> verify(@RequestParam("file") MultipartFile file) throws Exception {

        String path = System.getProperty("java.io.tmpdir")
                + "/temp_" + System.currentTimeMillis() + ".jpeg";

        File tempFile = new File(path);
        file.transferTo(tempFile);

        String userId = irisService.matchIris(path);

        Map<String, Object> response = new HashMap<>();

        if (userId != null) {

            String cleanId = userId.split("_")[0];

            Student student = studentRepository.findById(cleanId).orElse(null);

            if (student != null) {
                response.put("userId", cleanId);
                response.put("name", student.getName());
                response.put("message", "MATCHED");
            } else {
                response.put("message", "User not found");
            }

        } else {
            response.put("message", "User not recognized");
        }

        tempFile.delete();
        return response;
    }
    @PostMapping("/confirm")
    public Map<String, String> confirmAttendance(@RequestParam String userId) {

        String result = attendanceService.markAttendance(userId);

        Map<String, String> response = new HashMap<>();

        if ("Marked".equals(result)) {
            response.put("message", "Attendance Marked");
        } else {
            response.put("message", "Already Marked Today");
        }

        return response;
    }
    @GetMapping("/student/{id}")
    public List<Attendance> getAttendance(@PathVariable String id) {
        return attendanceService.getAttendanceByStudent(id);
    }
    @PostMapping("/student/add")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }
}