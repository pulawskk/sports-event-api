package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FakeFootballServiceTest {

    @BeforeEach
    void setUp() {
        Competition competition = Competition.builder().id(1L).name("Premier League").build();

    }

    @Test
    void shouldReturnSetOfGamesTwiceLess_whenCompetitionHasEvenNumberOfTeams() {
        //given
        //competition with 20 teams

        //helper method to generate pair values from 1 to number of teams

        //return set of games, game has only start date, end date, teamHome, teamAway, competition

        //game should have status: BEFORE_ODDS / ODDS / RESULTED
    }

    @Test
    void generateOdds() {
        //pass games with teams -> check if their status is BEFORE_ODDS

        //randomly or based on history/form of each team create odds and generate value for them

        //game has teamHome, teamAway, starDate, endDate, competition, odds

        //game status is ODDS
    }

    @Test
    void generateResult() {
        //pass games with odds -> check if their status is ODDS

        //based on form/history or randomly generate results for each game -> create game report

        //create Result with game and created game report

        //game can change status for RESULTED

        //return set of results
    }
}