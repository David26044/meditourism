package com.meditourism.meditourism.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:10090", // Frontend local development
                        "http://127.0.0.1:5501", // Alternate local frontend
                        "https://e74d-2803-1800-133a-e914-a34b-1e51-ff20-8ff4.ngrok-free.app", // Public ngrok URL
                        "https://meditourism-zszy.vercel.app" // New Vercel frontend URL
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Allow all necessary HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies and credentials
    }
}