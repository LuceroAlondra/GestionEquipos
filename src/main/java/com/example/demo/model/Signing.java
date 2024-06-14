package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "signings")
public class Signing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long signingsId;

    @Column(name = "since", columnDefinition = "Date not null")
    private Date since;

    @Column(name = "until", columnDefinition = "Date not null")
    private Date until;

    @ManyToOne
    @JoinColumn(name = "player_id",
            referencedColumnName = "player_id",
            foreignKey = @ForeignKey(name = "fk_player_id"))
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id",
            referencedColumnName = "team_id",
            foreignKey = @ForeignKey(name = "fk_team_id"))
    private Team team;

    public Signing(Player player) {
        this.player = player;
    }

}