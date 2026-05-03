package com.vani.irisapi.service;

import com.vani.irisapi.entity.Teacher;
import com.vani.irisapi.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private TeacherRepository repo;

    public boolean login(String username, String password) {

        Optional<Teacher> teacher = repo.findById(username);

        if (teacher.isPresent()) {
            return teacher.get().getPassword().equals(password);
        }

        return false;
    }
}