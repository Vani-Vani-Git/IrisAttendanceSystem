package com.vani.irisapi.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dlqad43qq",
                "api_key", "515511727242444",
                "api_secret", "Ry349b38jjcyO506ku5vcHNN_Mo"
        ));
    }
}