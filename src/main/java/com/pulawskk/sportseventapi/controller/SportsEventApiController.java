package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/games", produces = "application/json")
    public ResponseEntity<List<Game>> apiEventsGeneratedGames() {
        List<Game> inplayGames = gameService.findAll();
        inplayGames.sort((g1, g2) -> g2.getStartDate().compareTo(g1.getStartDate()));
        return new ResponseEntity<>(inplayGames, HttpStatus.OK);
    }

    @GetMapping(value = "/games/competition/{competitionId}", produces = "application/json")
    public ResponseEntity<List<Game>> apiEventsGeneratedGamesForCompetition(@PathVariable("competitionId") Long competitionId) {
        List<Game> inplayGames = gameService.generateInplayGamesForCompetition(competitionId);
        return new ResponseEntity<>(inplayGames, HttpStatus.OK);
    }

    @GetMapping(value = "/results", produces = "application/json")
    public ResponseEntity<List<ResultFootball>> apiEventsResultedGames() {
        List<ResultFootball> allResultsList = resultFootballService.generateAllResults();
        return new ResponseEntity<>(allResultsList, HttpStatus.OK);
    }

    @GetMapping(value = "/results/competition/{competitionId}", produces = "application/json")
    public ResponseEntity<List<ResultFootball>> apiEventsResultedGamesForCompetition(@PathVariable("competitionId") Long competitionId) {
        List<ResultFootball> allResultsForCompetitionList = resultFootballService.generateResultsForCompetition(competitionId);
        return new ResponseEntity<>(allResultsForCompetitionList, HttpStatus.OK);
    }

    @GetMapping(value = "/games/{uniqueId}")
    public ResponseEntity<Game> apiGameWithUniqueId(@PathVariable("uniqueId") String uniqueId) {
        Game game = gameService.findGameByUniqueId(uniqueId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping(value = "/results/{uniqueId}")
    public ResponseEntity<ResultFootball> apiResultWithUniqueId(@PathVariable("uniqueId") String uniqueId) {
        ResultFootball result = resultFootballService.findResultByGameUniqueId(uniqueId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
