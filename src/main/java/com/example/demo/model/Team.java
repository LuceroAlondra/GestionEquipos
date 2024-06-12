package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "team")
public class Team {
    @JsonIgnore
    @OneToMany(mappedBy = "team",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    List<Signing> signings;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;
    @Column(name = "name", columnDefinition = "varchar(50) not null")
    private String teamName;
    @Column(name = "email", columnDefinition = "varchar(100) not null")
    private String teamEmail;
    @Column(name = "since", columnDefinition = "date not null")
    private Date since;
    @Column(name = "city", columnDefinition = "varchar(50) not null")
    private String city;
}
