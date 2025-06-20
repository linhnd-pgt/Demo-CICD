package com.thanhhuong.rookbooks.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private final String CLOUD_NAME = "duylinhmedia";
    private final String API_KEY = "944914366153752";
    private final String API_SECRET = "Qv6TlyPXA0rqWzsWzPlDkHzxMcs";

    @Bean
    public Cloudinary configCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }

}
