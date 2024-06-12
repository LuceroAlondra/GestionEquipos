package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"modelMapper"})
public class SigningDTO {
    @JsonProperty("signingsId")
    private Long signingsId;

    @JsonProperty("idPlayer")
    private PlayerDTO player;

    @JsonProperty("idTeam")
    private TeamDTO team;
    private Date since;
    private Date until;

}