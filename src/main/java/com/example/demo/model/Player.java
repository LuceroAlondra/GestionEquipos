package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
@Table(name = "player")
public class Player {
    @JsonIgnore
    @OneToMany(mappedBy = "player",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    List<Signing> signings;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;
    @Column(name = "firstName", columnDefinition = "varchar(50) not null")
    private String firstName;
    @Column(name = "lastName", columnDefinition = "varchar(50) not null")
    private String lastName;
    @Column(name = "email", columnDefinition = "varchar(50) not null")
    private String emailPlayer;
    @Column(name = "birthdate", columnDefinition = "Date not null")
    private Date birthdate;
    @Column(name = "position", columnDefinition = "varchar(50) not null")
    private String position;
    @Column(name = "gender", columnDefinition = "varchar(100) not null")
    private String gender;
    @Column(name = "weight", columnDefinition = "double not null")
    private double weight;
    @Column(name = "high", columnDefinition = "double not null")
    private double high;
    @Column(name = "imc", columnDefinition = "double not null")
    private double imc;
    @Column(name = "fat", columnDefinition = "double not null")
    private double fat;
}
