package com.example.demo.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class TeamDTO {
    private Long teamId;
    private String teamName;
    private String teamEmail;
    private Date since;
    private String city;
}
