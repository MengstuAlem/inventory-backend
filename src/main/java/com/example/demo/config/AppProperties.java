package com.example.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppProperties {

    private String clientUrl;
    private String jwtSecret;

}