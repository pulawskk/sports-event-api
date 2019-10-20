package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.springframework.web.bind.annotation.*;

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
    public String apiEventsGeneratedGamesForCompetition(@PathVariable("competitionId") Long competitionId) {
        return gameService.generateJsonForInplayGamesForCompetition(competitionId).toString();
    }

    @GetMapping(value = "/results")
    public String apiEventsResultedGames() {
        return resultFootballService.generateJsonForAllResults().toString();
    }

    @GetMapping(value = "/{competitionId}/results")
    public String apiEventsResultedGamesForCompetition(@PathVariable("competitionId") Long competitionId) {
        return resultFootballService.generateJsonForResultsForCompetition(competitionId).toString();
    }
}
