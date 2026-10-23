package com.capstone.dashboard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS setup. Browsers block a page on one address (localhost:3000) from
// calling an API on another (localhost:8080) unless the API says it's allowed.
// This tells Spring to allow our frontend's address.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // @Value reads app.cors.allowed-origins from application.properties.
    private final String[] allowedOrigins;

    public WebConfig(@Value("${app.cors.allowed-origins}") String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT");
    }
}
