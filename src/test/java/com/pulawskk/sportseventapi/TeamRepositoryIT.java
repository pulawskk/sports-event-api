package com.pulawskk.sportseventapi;

import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class TeamRepositoryIT {

    public static PostgreSQLContainer container = MainPostgresqlContainer.getInstance();

    @Autowired
    private TeamRepository teamRepository;

    private void insertTeams() {
        teamRepository.save(Team.builder().id(1L).name("Chelsea").competitions(null).build());
        teamRepository.flush();
    }

    @Test
    void firstTest() {
        insertTeams();
        Team teamFromDb = teamRepository.findByName("Chelsea").get();
        assertAll(() -> {
            assertThat(teamFromDb.getId(), is(1L));
            assertThat(teamFromDb.getName(), is("Chelsea"));
        });
    }
}
