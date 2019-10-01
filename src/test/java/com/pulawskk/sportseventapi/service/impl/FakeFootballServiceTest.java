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

        //return set of games, game has only start date, end date, teamHome, teamAway

        //game should have status: BEFORE_ODDS / ODDS / READY_FOR_RESULT
    }

    @Test
    void generateOdds() {

    }

    @Test
    void generateResult() {

    }
}