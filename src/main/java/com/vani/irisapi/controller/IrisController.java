package com.vani.irisapi.controller;

import com.vani.irisapi.service.IrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/iris")
public class IrisController {
    @Autowired
    private IrisService irisService;
    @PostMapping(value = "/verify", consumes = "multipart/form-data")
    public Map<String, Object> verify(@RequestParam("file") MultipartFile file) throws Exception {

        File tempFile = new File(System.getProperty("user.dir") + "/temp.jpeg");
        file.transferTo(tempFile);
        String userId = irisService.matchIris("temp.jpeg");

        Map<String, Object> response = new HashMap<>();

        if (userId != null) {
            response.put("match", true);
            response.put("userId", userId);
        } else {
            response.put("match", false);
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        return response;
    }
}
