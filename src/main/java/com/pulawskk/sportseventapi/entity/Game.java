package com.pulawskk.sportseventapi.entity;

import com.pulawskk.sportseventapi.enums.GameStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "team_home_id", referencedColumnName = "id")
    private Team teamHome;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "team_away_id", referencedColumnName = "id")
    private Team teamAway;
//
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "competition_id", referencedColumnName = "id")
    private Competition competition;

    @Column(name = "start_date")
    private Calendar startDate;

    @Column(name = "end_date")
    private Calendar endDate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Odd> odds = new HashSet<>();

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private ResultFootball resultFootball;

    @Column(name = "status")
    private GameStatus status;

    @Builder
    public Game(Long id, Team teamHome, Team teamAway, Competition competition, Calendar startDate, Calendar endDate, Set<Odd> odds, GameStatus status) {
        this.id = id;
        this.teamAway = teamAway;
        this.teamHome = teamHome;
        this.competition = competition;
        this.startDate = startDate;
        this.endDate = endDate;
        if(odds != null) {
            this.odds = odds;
        }
        this.status = status;
    }

    public Game(Team teamHome, Team teamAway) {
        this.teamHome = teamHome;
        this.teamAway = teamAway;
    }
}
