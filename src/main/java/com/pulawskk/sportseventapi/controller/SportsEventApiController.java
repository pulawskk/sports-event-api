package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.service.GameReportFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/events")
public class SportsEventApiController {

    private final GameServiceImpl gameService;

    private final GameReportFootballService gameReportFootballService;

    private final ResultFootball resultFootball;

    public SportsEventApiController(GameServiceImpl gameService, GameReportFootballService gameReportFootballService, ResultFootball resultFootball) {
        this.gameService = gameService;
        this.gameReportFootballService = gameReportFootballService;
        this.resultFootball = resultFootball;
    }


}
