package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.enums.CompetitionType;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {"classpath:application-test.properties"})
@ActiveProfiles("test")
class CompetitionRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CompetitionRepository competitionRepository;

    Competition premierLeagueCompetition;
    Competition championsLeagueCompetition;
    Set<Competition> competitions;
    Team chelseaTeam;
    Team arsenalTeam;
    Set<Team> teams;

    @BeforeEach
    void setUp() {
        premierLeagueCompetition = Competition.builder().id(4L).name("Premier League").type(CompetitionType.LEAGUE).build();
        championsLeagueCompetition = Competition.builder().id(5L).name("Champions League").type(CompetitionType.TOURNAMENT_ROUNDS_GROUPS).build();
        competitions = new HashSet<>();

        chelseaTeam = Team.builder().id(11L).name("Chelsea").build();
        arsenalTeam = Team.builder().id(12L).name("Arsenal").build();
        teams = new HashSet<>();
    }

    @Test
    void testEntityManagerInjectedProper() {
        assertAll(() -> {
            assertThat(testEntityManager).isNotNull();
            assertThat(competitionRepository).isNotNull();
        });
    }

    @Test
    void shouldFoundCompetitionByName_whenExistsInDb() {
        //given
        competitions.add(premierLeagueCompetition);
        chelseaTeam.setCompetitions(competitions);
        arsenalTeam.setCompetitions(competitions);
        teams.add(chelseaTeam);
        teams.add(arsenalTeam);
        premierLeagueCompetition.setTeams(teams);

        competitionRepository.save(premierLeagueCompetition);

        //when
        Optional<Competition> competitionFromDb = competitionRepository.findByName("Premier League");

        //then
        competitionFromDb.ifPresent(competition -> assertAll( () -> {
            assertThat(competition).isNotNull();
            assertThat(competition.getTeams()).hasSize(2);
            assertThat(competition.getId()).isEqualTo(4L);
            assertThat(competition.getName()).isEqualTo("Premier League");
        }));
    }

    @Test
    void shouldFoundCompetitionsByTeam_whenExistInDb() {
        //given
        competitions.add(championsLeagueCompetition);
        competitions.add(premierLeagueCompetition);

        chelseaTeam.setCompetitions(competitions);
        teams.add(chelseaTeam);
        championsLeagueCompetition.setTeams(teams);
        premierLeagueCompetition.setTeams(teams);

        competitionRepository.save(premierLeagueCompetition);
        competitionRepository.save(championsLeagueCompetition);

        //when
        List<Competition> competitionsFromDb = competitionRepository.findCompetitionsByTeams(chelseaTeam);

        //then
        assertAll(() -> {
            assertThat(competitionsFromDb.size()).isEqualTo(2);
        });
    }
}