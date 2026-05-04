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

        System.out.println("Saved at: " + tempFile.getAbsolutePath());
        System.out.println("File exists: " + tempFile.exists());

        String userId = irisService.matchIris(path);

        Map<String, Object> response = new HashMap<>();
        if (userId != null) {
            String result = attendanceService.markAttendance(userId);
            String cleanId = userId.split("_")[0]; // remove _1, _2

            Student student = studentRepository.findById(cleanId).orElse(null);

            if (student != null) {
                response.put("userId", cleanId);
                response.put("name", student.getName());
            } else {
                response.put("userId", cleanId);
            }

            if (result.equals("Marked")) {
                response.put("message", "Attendance Marked");
            } else {
                response.put("message", "Already Marked Today");
            }
        } else {
            response.put("message", "User not recognized");
        }
        tempFile.delete();
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