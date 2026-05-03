package com.vani.irisapi.controller;

import com.vani.irisapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");

        boolean success = authService.login(username, password);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);

        return response;
    }
}