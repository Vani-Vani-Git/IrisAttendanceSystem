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
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private IrisService irisService;
    @Autowired
    private AttendanceService attendanceService;
    @PostMapping("/mark")
    public Map<String, Object> verify(
            @RequestParam("file") MultipartFile file) throws Exception {

        String path = System.getProperty("java.io.tmpdir")
                + "/temp_" + System.currentTimeMillis() + ".jpeg";

        File tempFile = new File(path);

        Map<String, Object> response = new HashMap<>();

        try {

            // 1. Save captured iris image temporarily
            file.transferTo(tempFile);

            // 2. Find the closest iris
            String userId = irisService.matchIris(path);

            // 3. No match
            if (userId == null) {
                response.put("recognized", false);
                response.put("message", "User not recognized");
                return response;
            }

            // 4. Convert 104_4 → 104
            String cleanId = userId.split("_")[0];

            // 5. Find student 104 in database
            Student student = studentRepository
                    .findById(cleanId)
                    .orElse(null);

            // 6. Student doesn't exist in database
            if (student == null) {
                response.put("recognized", false);
                response.put("message", "Student not found");
                return response;
            }

            // 7. Mark attendance
            String attendanceResult =
                    attendanceService.markAttendance(cleanId);

            // 8. Send response expected by index.html
            response.put("recognized", true);
            response.put("studentId", cleanId);
            response.put("studentName", student.getName());

            response.put(
                    "time",
                    java.time.LocalTime.now()
                            .format(
                                    java.time.format.DateTimeFormatter.ofPattern("hh:mm a")
                            )
            );

            response.put("message", attendanceResult);

            return response;

        } finally {

            // 9. Delete temporary iris image
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
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