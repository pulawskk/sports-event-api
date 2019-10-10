package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.FakeService;
import com.pulawskk.sportseventapi.service.GameService;
import com.pulawskk.sportseventapi.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FakeFootballService implements FakeService {

    private final TeamService teamService;
    private final GameService gameService;

    public FakeFootballService(TeamService teamService, GameService gameService) {
        this.teamService = teamService;
        this.gameService = gameService;
    }

    @Override
    public Set<Game> generateGames(Competition competition) {
        List<Integer> orderPairs = generatePairs(competition);

        Set<Game> generatedGames = new HashSet<>();

        Set<Team> teams = teamService.findAllByCompetitions(competition).stream().collect(Collectors.toSet());

        int teamNumber = competition.getTeams().size()/2;

        while (generatedGames.size() < teamNumber) {
            Team teamH = teams.iterator().next();
            generatedGames.add(Game.builder().teamHome(teamH).build());
            teams.remove(teamH);
        }

        generatedGames.forEach(game -> {
            Team teamA = teams.iterator().next();
            game.setTeamAway(teamA);
            game.setStatus(GameStatus.PREMATCH);
            game.setStartDate(null);
            game.setEndDate(null);
            game.setCompetition(competition);
            teams.remove(teamA);
            return;
        });

        generatedGames.forEach(game -> {
            Game savedGame = gameService.save(game);
            game.setId(savedGame.getId());
            return;
        });

        return generatedGames;
    }

    List<Integer> generatePairs(Competition competition) {
        List<Integer> orderList = new ArrayList<>();
        if(competition != null) {
            orderList = IntStream.iterate(0, n -> n + 1).limit(competition.getTeams().size()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
        Collections.shuffle(orderList);
        return orderList;
    }

    @Override
    public Set<Game> generateOdds(Set<Game> games) {
        
        Set<Game> calculatedOdds = games.stream().map(game -> {
            Odd oddH = Odd.builder().type(GameOddType.HOME_WIN).build();
            Odd oddA = Odd.builder().type(GameOddType.AWAY_WIN).build();
            Odd oddX = Odd.builder().type(GameOddType.DRAW).build();
            Set<Odd> odds = new HashSet<>();
            odds.add(oddA);
            odds.add(oddX);
            odds.add(oddH);
            game.setOdds(odds);
            return game;
        }).collect(Collectors.toSet());

        return calculatedOdds;
    }

    @Override
    public Set<ResultFootball> generateResult(Set<Game> games) {
        Set<ResultFootball> resultFootball = new HashSet<>();

        if(games != null) {
            for (Game game : games) {
                game.setStatus(GameStatus.RESULTED);
                ResultFootball resultFootball1 = ResultFootball.builder()
                        .game(game)
                        .gameReportFootball(generateReportFootball(game))
                        .build();
                resultFootball.add(resultFootball1);
            }
        }
        return resultFootball;
    }

    @Override
    public GameReportFootball generateReportFootball(Game game) {
        GameReportFootball gameReportFootball = null;
        if (game != null) {
            gameReportFootball = GameReportFootball.builder()
                    .goalHome(generateRandomValueRangeOneToNine())
                    .goalAway(generateRandomValueRangeOneToNine())
                    .cornerHome(generateRandomValueRangeOneToNine())
                    .cornerAway(generateRandomValueRangeOneToNine())
                    .offsideHome(generateRandomValueRangeOneToNine())
                    .offsideAway(generateRandomValueRangeOneToNine())
                    .yCardHome(generateRandomValueRangeOneToNine())
                    .yCardAway(generateRandomValueRangeOneToNine())
                    .rCardHome(generateRandomValueRangeOneToNine())
                    .rCardAway(generateRandomValueRangeOneToNine())
                    .build();
        }
        return gameReportFootball;
    }

    int generateRandomValueRangeOneToNine() {
        return (int) (Math.random() * 9 + 1);
    }
}
