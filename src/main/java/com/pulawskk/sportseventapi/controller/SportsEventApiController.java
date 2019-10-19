package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.service.GameReportFootballService;
import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

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
        return gameService.generateGames(competitionId).toString();
    }
}
