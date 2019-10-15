package com.pulawskk.sportseventapi;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.impl.CompetitionServiceImpl;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class TeamRepositoryIT {

    public static PostgreSQLContainer container = MainPostgresqlContainer.getInstance();

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private CompetitionServiceImpl competitionServiceImpl;

    @Autowired
    private GameServiceImpl gameService;

    private Competition competition;
    private Set<Competition> competitions = new HashSet<>();

    @BeforeEach
    void setUp() {
        competition = Competition.builder().name("FA Cup").build();

        insertData();
    }

    private void insertData() {

        Team teamChelsea = Team.builder().name("Chelsea").competitions(competitions).build();
        Team teamArsenal = Team.builder().name("Arsenal").competitions(competitions).build();
        Team teamEverton = Team.builder().name("Everton").competitions(competitions).build();
        teamService.save(teamChelsea);
        teamService.save(teamArsenal);
        teamService.save(teamEverton);
        Set<Team> teams = new HashSet<>();
        teams.add(teamChelsea);
        teams.add(teamArsenal);
        teams.add(teamEverton);

        competition.setTeams(teams);
        Competition savedCompetition = competitionServiceImpl.save(competition);
        competition.setId(savedCompetition.getId());
        competitions.add(competition);

        Game gameFirst = Game.builder().teamAway(teamArsenal).teamHome(teamChelsea)
                .competition(competition).status(GameStatus.PREMATCH).build();
        gameService.save(gameFirst);

        Game gameSecond = Game.builder().teamHome(teamEverton).teamAway(teamChelsea)
                .competition(competition).status(GameStatus.PREMATCH).build();
        gameService.save(gameSecond);

        Game gameThird = Game.builder().teamHome(teamChelsea).teamAway(teamEverton)
                .competition(competition).status(GameStatus.PREMATCH).build();
        gameService.save(gameThird);

        teamService.flush();
        competitionServiceImpl.flush();
        gameService.flush();
    }

//    @AfterEach
//    void tearDown() {
//        dropTables();
//    }

    private void dropTables() {
        deleteTeams();
        deleteCompetitions();
    }

    private void deleteTeams() {
        teamService.deleteAll();
    }

    private void deleteCompetitions(){
        competitionServiceImpl.deleteAll();
    }

    @Test
    void testSave() {
        Team newTeam = Team.builder().name("Newcastle").competitions(competitions).build();
        Team savedTeam = teamService.save(newTeam);
        Long savedId = savedTeam.getId();

        assertAll(() -> {
            assertThat(savedId, is(greaterThan(0L)));
        });
    }

    @Test
    void testUpdate() {
        Team newTeam = Team.builder().name("Newcastle").competitions(competitions).build();
        Team savedTeam = teamService.save(newTeam);
        Long savedId = savedTeam.getId();

        Team teamFromDb = teamService.findById(savedId);

        teamFromDb.setName("Newcastle United");

        Team savedTeamAgain = teamService.save(teamFromDb);
        Long savedIdAgain = savedTeamAgain.getId();

        assertAll(() -> {
            assertThat(savedIdAgain, is(savedId));
            assertThat(savedTeamAgain.getName(), is("Newcastle United"));
        });
    }

    @Test
    void testFindTeamByName() {
        Team teamFromDb = teamService.findByName("Chelsea");
        assertAll(() -> {
            assertThat(teamFromDb.getName(), is("Chelsea"));
        });
    }

    @Test
    void testFindAll() {
        Set<Team> teamsFromDb = teamService.findAll();

        assertAll(()->{
            assertThat(teamsFromDb, hasSize(3));
            assertThat(teamsFromDb.iterator().next().getId(), greaterThan(0L));
            assertThat(teamsFromDb.iterator().next().getName(), is(notNullValue()));
        });
    }

    @Test
    void testFindById() {
        Team newTeam = Team.builder().name("Newcastle").competitions(competitions).build();
        Team savedTeam = teamService.save(newTeam);
        Long savedId = savedTeam.getId();

        Team teamFromDb = teamService.findById(savedId);

        assertAll(() -> {
            assertThat(teamFromDb.getId(), is(savedId));
            assertThat(teamFromDb.getName(), is("Newcastle"));
        });
    }

    @Test
    void testDeleteAll() {
        Set<Team> teams2 = teamService.findAll();
        teamService.deleteAll();

        Set<Team> teams = teamService.findAll();

        assertAll(() -> {
            assertThat(teams, hasSize(0));
        });
    }

    @Test
    void testDeleteById() {
        Team newTeam = Team.builder().name("Newcastle").competitions(competitions).build();
        Team savedTeam = teamService.save(newTeam);
        Long savedId = savedTeam.getId();

        teamService.deleteById(savedId);

        Team teamFromDb = teamService.findById(savedId);

        assertAll(() -> {
            assertThat(teamFromDb, is(nullValue()));
        });
    }

    @Test
    void testDeleteByName() {
        Team newTeam = Team.builder().name("Newcastle").competitions(competitions).build();
        Team savedTeam = teamService.save(newTeam);
        Long savedId = savedTeam.getId();

        teamService.delete(savedTeam);

        Team teamFromDb = teamService.findById(savedId);

        assertAll(() -> {
            assertThat(teamFromDb, is(nullValue()));
        });
    }
}
