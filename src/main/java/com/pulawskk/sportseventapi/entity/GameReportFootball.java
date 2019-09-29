package com.pulawskk.sportseventapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="game_reports_football")
public class GameReportFootball extends GameReport{

    @Column(name = "goal_home")
    private int goalHome;

    @Column(name = "goal_away")
    private int goalAway;

    @Column(name = "offside_home")
    private int offsideHome;

    @Column(name = "offside_away")
    private int offsideAway;

    @Column(name = "corner_home")
    private int cornerHome;

    @Column(name = "corner_away")
    private int cornerAway;

    @Column(name = "y_card_home")
    private int yCardHome;

    @Column(name = "y_card_away")
    private int yCardAway;

    @Column(name = "r_card_home")
    private int rCardHome;

    @Column(name = "r_card_away")
    private int rCardAway;
}
