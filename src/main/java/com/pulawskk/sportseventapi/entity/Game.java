package com.pulawskk.sportseventapi.entity;

import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_generator")
    @SequenceGenerator(name="new_generator", sequenceName = "game_seq", allocationSize = 1)
    private Long id;

    @Column(name = "unique_id")
    private String uniqueId;

    @ManyToOne
    @JoinColumn(name = "team_home_id", referencedColumnName = "id")
    private Team teamHome;

    @ManyToOne
    @JoinColumn(name = "team_away_id", referencedColumnName = "id")
    private Team teamAway;

    @ManyToOne
    @JoinColumn(name = "competition_id", referencedColumnName = "id")
    private Competition competition;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Odd> odds = new HashSet<>();

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private ResultFootball resultFootball;

    @Column(name = "status")
    private GameStatus status;

    @Builder
    public Game(Long id, Team teamHome, Team teamAway, Competition competition, LocalDateTime startDate, LocalDateTime endDate, Set<Odd> odds, GameStatus status) {
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

    public Odd getOddByType(GameOddType gameOddType) {
        for (Odd odd : odds) {
            if(odd.getType().name() == gameOddType.name()) {
                return odd;
            }
        }
        return null;
    }
}
