package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GestionConfig {
    @Bean
    ModelMapper modelMapper() {

        return new ModelMapper();
    }
}
