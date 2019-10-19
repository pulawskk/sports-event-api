package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FakeFootballService implements FakeService {

    private final TeamService teamService;
    private final GameService gameService;
    private final OddService oddService;
    private final GameReportFootballService gameReportFootballService;
    private final ResultFootballService resultFootballService;
    private final CompetitionService competitionService;

    public FakeFootballService(TeamService teamService, GameService gameService, OddService oddService, GameReportFootballService gameReportFootballService, ResultFootballService resultFootballService, CompetitionService competitionService) {
        this.teamService = teamService;
        this.gameService = gameService;
        this.oddService = oddService;
        this.gameReportFootballService = gameReportFootballService;
        this.resultFootballService = resultFootballService;
        this.competitionService = competitionService;
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
        
        games.forEach(game -> {
            Odd oddH = Odd.builder().type(GameOddType.HOME_WIN).build();
            oddH.setValue(generateRandomValueForOdds());
            Odd oddA = Odd.builder().type(GameOddType.AWAY_WIN).build();
            oddA.setValue(generateRandomValueForOdds());
            Odd oddX = Odd.builder().type(GameOddType.DRAW).build();
            oddX.setValue(generateRandomValueForOdds());
            Set<Odd> odds = new HashSet<>();
            oddH.setGame(game);
            oddX.setGame(game);
            oddA.setGame(game);
            odds.add(oddA);
            odds.add(oddX);
            odds.add(oddH);
            Odd savedOddA = oddService.save(oddA);
            Odd savedOddX = oddService.save(oddX);
            Odd savedOddH = oddService.save(oddH);
            oddA.setId(savedOddA.getId());
            oddH.setId(savedOddH.getId());
            oddX.setId(savedOddX.getId());
            game.setOdds(odds);
        });

        for(Game game : games) {
            if(game.getId() == null) {
                Game savedGame = gameService.save(game);
                game.setId(savedGame.getId());
            }
        }
        games.forEach(game -> gameService.save(game));

        return games;
    }

    @Override
    public Set<ResultFootball> generateResult(Set<Game> games) {
        Set<ResultFootball> results = new HashSet<>();

        if(games != null) {
            for (Game game : games) {
                game.setStatus(GameStatus.RESULTED);
                gameService.save(game);
                ResultFootball resultFootball = ResultFootball.builder()
                        .game(game)
                        .gameReportFootball(generateReportFootball(game))
                        .build();
                ResultFootball resultFootballSaved = resultFootballService.save(resultFootball);
                resultFootball.setId(resultFootballSaved.getId());
                results.add(resultFootball);
            }
        }
        return results;
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
        GameReportFootball gameReportFootball1Saved = gameReportFootballService.save(gameReportFootball);
        gameReportFootball.setId(gameReportFootball1Saved.getId());
        return gameReportFootball;
    }

    int generateRandomValueRangeOneToNine() {
        return (int) (Math.random() * 9 + 1);
    }

    BigDecimal generateRandomValueForOdds() {
        double number = (Math.random() * 600 )/100 + 1.2;
        String numberString = String.valueOf(number);
        return new BigDecimal(numberString);
    }

    @Scheduled(fixedDelay = 10000)
    void generateGamesForCompetition() {
        Competition competition = competitionService.findByName("Premier League");

        Set<Game> gamesWithOutOdds = generateGames(competition);
        generateOdds(gamesWithOutOdds);
    }
}
