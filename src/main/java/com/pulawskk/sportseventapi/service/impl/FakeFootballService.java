package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.CompetitionType;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FakeFootballService implements FakeService, JsonUtil {

    private final TeamService teamService;
    private final GameService gameService;
    private final OddService oddService;
    private final GameReportFootballService gameReportFootballService;
    private final ResultFootballService resultFootballService;
    private final CompetitionService competitionService;
    private final JmsService jmsService;
    private final HttpPostService httpPostService;

    private final String URL_SERVER_SCHEDULED = "http://localhost:8081/games/game";
    private final String URL_SERVER_RESULT = "http://localhost:8081/games/result";

    public FakeFootballService(TeamService teamService, GameService gameService, OddService oddService, GameReportFootballService gameReportFootballService, ResultFootballService resultFootballService, CompetitionService competitionService, JmsService jmsService, HttpPostService httpPostService) {
        this.teamService = teamService;
        this.gameService = gameService;
        this.oddService = oddService;
        this.gameReportFootballService = gameReportFootballService;
        this.resultFootballService = resultFootballService;
        this.competitionService = competitionService;
        this.jmsService = jmsService;
        this.httpPostService = httpPostService;
    }

    @Override
    @Transactional
    public Set<Game> generateGames(Competition competition) {
        List<Integer> orderPairs = generatePairs(competition);

        Set<Game> generatedGames = new HashSet<>();

        int gamesNumber = competition.getTeams().size()/2;

        if(gamesNumber == 0) {
            return generatedGames;
        }

        Set<Team> teams = teamService.findAllByCompetitions(competition).stream().collect(Collectors.toSet());

        while (generatedGames.size() < gamesNumber) {
            Team teamH = teams.iterator().next();
            generatedGames.add(Game.builder().teamHome(teamH).build());
            teams.remove(teamH);
        }

        generatedGames.forEach(game -> {
            Team teamA = teams.iterator().next();
            game.setTeamAway(teamA);
            game.setUniqueId(UUID.randomUUID().toString().substring(0, 7));
            game.setStartDate(LocalDateTime.now());
            game.setEndDate(LocalDateTime.now().plusMinutes(5));
            game.setCompetition(competition);
            teams.remove(teamA);
            return;
        });

        generatedGames.forEach(game -> {
            Game savedGame = gameService.save(game);
            game.setId(savedGame.getId());
            game.setStatus(GameStatus.CREATED);
        });

        gameService.saveAll(generatedGames);
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
    @Transactional
    public Set<Game> generateOdds(Set<Game> games) {
        if (games != null && games.size() > 0) {
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

            games.forEach(game -> {
                game.setStatus(GameStatus.PREMATCH);
            });
            gameService.saveAll(games);
        }

        return games;
    }

    @Override
    public Set<ResultFootball> generateResults(Set<Game> games) {
        Set<ResultFootball> results = new HashSet<>();

        if(games != null && games.size() > 0) {
            for (Game game : games) {
                ResultFootball resultFootball = ResultFootball.builder()
                        .id(game.getId())
                        .game(game)
                        .gameReportFootball(generateReportFootball(game))
                        .build();
                if(game.getCompetition().getType() == CompetitionType.TOURNAMENT_ROUNDS) {
                    int goalHome = resultFootball.getGameReport().getGoalHome();
                    int goalAway = resultFootball.getGameReport().getGoalAway();
                    Team lostTeam = null;
                    if (goalHome > goalAway) {
                        lostTeam = teamService.findByName(game.getTeamAway().getName());
                    } else if(goalAway > goalHome) {
                        lostTeam = teamService.findByName(game.getTeamHome().getName());
                    } else if(goalHome == goalAway) {
                        resultFootball.getGameReport().setGoalHome(goalHome + 1);
                        lostTeam = teamService.findByName(game.getTeamAway().getName());
                    }

                    Competition competition = game.getCompetition();
                    lostTeam.removeCompetitionByName(competition.getName());
                    teamService.save(lostTeam);
                    competition.removeTeamByName(lostTeam.getName());
                    competitionService.save(competition);
                }

                game.setResultFootball(resultFootball);
                game.setStatus(GameStatus.RESULTED);

                results.add(resultFootball);
            }
        }

        resultFootballService.saveAll(results);
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
        int number = (int) (Math.random() * 600);
        double doubleNumber = number/100.00 + 1.20;
        String numberString = String.valueOf(doubleNumber);
        if (numberString.length() > 3) {
            numberString = numberString.substring(0, 4);
        }

        return new BigDecimal(numberString);
    }

    @Scheduled(cron = "0 5/20 8-20 * * ?")
    void generateGamesForFaCup() {
        Competition competition = competitionService.findByName("FA Cup");
        String queueName = "FA CUP prematch";

        Set<Game> gamesWithOutOdds = generateGames(competition);
        if (gamesWithOutOdds.size() == 0) {
            return;
        }
        Optional.ofNullable(gamesWithOutOdds.size()).ifPresent(s -> {
            if(s > 0) {
               Set<Game> games = generateOdds(gamesWithOutOdds);
               games.forEach(game -> {
                   jmsService.sendJsonMessage(queueName, generateJsonFromGame(game));
               });
            }
        });
    }

    @Scheduled(cron = "0 15/20 8-20 * * ?")
    void generateResultsForInplayGamesForFaCup() throws IOException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        String queueName = "FA CUP result";

        Competition competition = competitionService.findByName("FA Cup");
        Set<Game> inplayGames = gameService.findAllGeneratedGamesForCompetition(competition.getId());

        Optional.ofNullable(inplayGames.size()).ifPresent(s -> {
            if(s > 0) {
                Set<ResultFootball> resultsFootball = generateResults(inplayGames);
                resultsFootball.forEach(result -> {
                    jmsService.sendJsonMessage(queueName, generateJsonFromResult(result));
                });
            }
        });
    }

    @Scheduled(cron = "0 0/2 8-20 * * ?")
    void generateGamesForPremierLeague() {
        Competition competition = competitionService.findByName("Premier League");

        Set<Game> gamesWithOutOdds = generateGames(competition);
        if (gamesWithOutOdds.size() == 0) {
            return;
        }
        Optional.ofNullable(gamesWithOutOdds.size()).ifPresent(s -> {
            if(s > 0) {
                Set<Game> games = generateOdds(gamesWithOutOdds);
                games.forEach(game -> {
                    postGameMsg(game);
                });
            }
        });
    }

    private void postGameMsg(Game game) {
        try {
            httpPostService.postJsonMessage(generateJsonFromGame(game), URL_SERVER_SCHEDULED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 1/2 8-20 * * ?")
    void generateResultsForInplayGamesForPremierLeague() throws IOException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException { ;

        Competition competition = competitionService.findByName("Premier League");
        Set<Game> inplayGames = gameService.findAllGeneratedGamesForCompetition(competition.getId());

        Optional.ofNullable(inplayGames.size()).ifPresent(s -> {
            if(s > 0) {
                Set<ResultFootball> resultsFootball = generateResults(inplayGames);
                resultsFootball.forEach(result -> {
                    postResultMsg(result);
                });
            }
        });
    }

    private void postResultMsg(ResultFootball result) {
        try {
            httpPostService.postJsonMessage(generateJsonFromResult(result), URL_SERVER_RESULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Scheduled(cron = "0 10 7,14,21 * * ?")
    public void deleteOldGames() {
        final int amountToBeLeft = 100;
        gameService.deleteOldGames(amountToBeLeft);
    }
}
