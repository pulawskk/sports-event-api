package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/events")
public class SportsEventApiController {

    private final GameServiceImpl gameService;

    private final GameReportFootballFootballService gameReportFootballFootballService;

    private final ResultFootballService resultFootballService;

    public SportsEventApiController(GameServiceImpl gameService, GameReportFootballFootballService gameReportFootballFootballService, ResultFootballService resultFootballService) {
        this.gameService = gameService;
        this.gameReportFootballFootballService = gameReportFootballFootballService;
        this.resultFootballService = resultFootballService;
    }

    @GetMapping(value = "/{competitionId}/games")
    public ResponseEntity<List<Game>> apiEventsGeneratedGamesForCompetition(@PathVariable("competitionId") Long competitionId) {
        List<Game> inplayGames = gameService.generateJsonForInplayGamesForCompetition(competitionId);
        return new ResponseEntity<>(inplayGames, HttpStatus.OK);
    }

    @GetMapping(value = "/results", produces = "application/json")
    public String apiEventsResultedGames() {
        return resultFootballService.generateJsonForAllResults().toString();
    }

    @GetMapping(value = "/{competitionId}/results")
    public String apiEventsResultedGamesForCompetition(@PathVariable("competitionId") Long competitionId) {
        return resultFootballService.generateJsonForResultsForCompetition(competitionId).toString();
    }
}
