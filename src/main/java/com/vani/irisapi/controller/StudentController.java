package com.vani.irisapi.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    private Cloudinary cloudinary;
    @Autowired
    private StudentRepository studentRepository;
    @PostMapping("/save")
    public Map<String, String> saveStudent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("studentName") String studentName,
            @RequestParam("studentId") String studentId) throws Exception {
        String folder = "dataset/";

        File dir = new File(folder);
        if (!dir.exists()) dir.mkdirs();

        // 🔥 Count existing files for this student
        int count = 1;
        while (true) {
            File f = new File(folder + studentId + "_" + count + ".jpeg");
            if (!f.exists()) break;
            count++;
        }

        String path = folder + studentId + "_" + count + ".jpeg";

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = uploadResult.get("url").toString();
        // 🔥 Save student in DB
        Student student = new Student();
        student.setId(studentId);
        student.setName(studentName);

        if (!studentRepository.existsById(studentId)) {
            studentRepository.save(student);
        }

        Map<String, String> res = new HashMap<>();
        res.put("message", "Saved as " + studentId + "_" + count);

        return res;
    }
}