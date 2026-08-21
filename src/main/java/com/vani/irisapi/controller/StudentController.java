package com.vani.irisapi.controller;

import com.vani.irisapi.entity.Student;
import com.vani.irisapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Absolute path — always resolves here regardless of where the JVM is launched from
    private static final String DATASET_FOLDER = "C:\\Users\\VANITHA\\Desktop\\irisapi\\dataset\\";

    @PostMapping("/save")
    public Map<String, String> saveStudent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("studentName") String studentName,
            @RequestParam("studentId") String studentId) throws Exception {

        File dir = new File(DATASET_FOLDER);
        if (!dir.exists()) dir.mkdirs();

        // Count existing files for this student to get the next number
        int count = 1;
        while (true) {
            File f = new File(DATASET_FOLDER + studentId + "_" + count + ".jpeg");
            if (!f.exists()) break;
            count++;
        }

        File destination = new File(DATASET_FOLDER + studentId + "_" + count + ".jpeg");
        file.transferTo(destination); // <-- this line was missing; it's what actually writes the file

        // Save student in DB (only once, first capture)
        if (!studentRepository.existsById(studentId)) {
            Student student = new Student();
            student.setId(studentId);
            student.setName(studentName);
            studentRepository.save(student);
        }

        Map<String, String> res = new HashMap<>();
        res.put("message", "Saved as " + studentId + "_" + count);

        return res;
    }
}