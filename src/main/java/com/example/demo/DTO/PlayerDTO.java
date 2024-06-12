package com.example.demo.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private Long playerId;
    private String firstName;
    private String lastName;
    private String emailPlayer;
    private Date birthdate;
    private String position;
    private String gender;
    private double weight;
    private double high;
    private double imc;
    private double fat;
}

