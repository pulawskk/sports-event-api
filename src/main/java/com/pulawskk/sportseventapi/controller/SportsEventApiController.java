package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.service.GameReportFootballService;
import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/events")
public class SportsEventApiController {

    private final GameServiceImpl gameService;

    private final GameReportFootballFootballService gameReportFootballFootballService;

    private final ResultFootballService resultFootballService;

    public SportsEventApiController(GameServiceImpl gameService, GameReportFootballFootballService gameReportFootballFootballService, ResultFootballService resultFootballService) {
        this.gameService = gameService;
        this.gameReportFootballFootballService = gameReportFootballFootballService;
        this.resultFootballService = resultFootballService;
    }

}
