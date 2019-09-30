package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.GameReportFootball;
import com.pulawskk.sportseventapi.repository.GameReportFootballRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GameReportFootballServiceTest {

    @Mock
    private GameReportFootballRepository gameReportFootballRepository;

    private GameReportFootballFootballService gameReportFootballService;

    private GameReportFootball gameReportFootball;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        gameReportFootballService = new GameReportFootballFootballService(gameReportFootballRepository);

        gameReportFootball = GameReportFootball.builder()
                .id(1L)
                .cornerAway(1)
                .cornerHome(2)
                .goalHome(3)
                .goalAway(4)
                .rCardAway(5)
                .rCardHome(6)
                .yCardAway(7)
                .yCardHome(8)
                .offsideAway(9)
                .offsideHome(10).build();

    }

    @Test
    void shouldReturnGameReportFootball_whenGameReportFootballExistsWithSpecificId() {
        when(gameReportFootballRepository.findById(anyLong())).thenReturn(Optional.of(gameReportFootball));

        GameReportFootball gameReportFootballFromDb = gameReportFootballService.findById(1L);

        assertAll(() -> {
            assertThat(gameReportFootballFromDb.getId(), is(1L));
            assertThat(gameReportFootballFromDb.getCornerAway(), is(1));
            assertThat(gameReportFootballFromDb.getYCardAway(), is(7));
        });
    }

    @Test
    void shouldReturnSavedGameReportFootball_whenGameReportFootballIsSaved() {
        when(gameReportFootballRepository.save(any())).thenReturn(gameReportFootball);

        GameReportFootball savedReportFootball = gameReportFootballService.save(gameReportFootball);
        assertAll(() -> {
            assertThat(savedReportFootball.getId(), is(1L));
            assertThat(savedReportFootball.getCornerAway(), is(1));
            assertThat(savedReportFootball.getYCardAway(), is(7));
        });
    }

    @Test
    void shouldDeleteGameReportFootbal_whenGameReportFootballExists() {
        gameReportFootballService.delete(gameReportFootball);
        verify(gameReportFootballRepository, times(1)).delete(any());
    }

    @Test
    void shouldDeleteGameReportFootbal_whenGameReportFootballExistsWithSpecificId() {
        gameReportFootballService.deleteById(1L);
        verify(gameReportFootballRepository, times(1)).deleteById(anyLong());
    }
}