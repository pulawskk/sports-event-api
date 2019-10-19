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
    public String apiEventsGeneratedGames(@PathVariable("competitionId") Long competitionId) {
        return gameService.generateJsonForInplayGames(competitionId).toString();
    }

    @GetMapping(value = "/results")
    public String apiEventsResultedGames() {
        return resultFootballService.generateJsonForAllResultedGames().toString();
    }
}
