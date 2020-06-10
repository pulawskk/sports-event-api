package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.CompetitionType;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FakeFootballService implements FakeService, JsonUtil {

    private final Integer MAX_HOME_EXPECTATION = 57/5;
    private final Integer MAX_AWAY_EXPECTATION = 141/10;

    private final Double BETTING_SITE_PERCENTAGE = 1.1;

    void setBettingServerIp(String bettingServerIp) {
        this.bettingServerIp = bettingServerIp;
    }

    void setBettingServerPort(String bettingServerPort) {
        this.bettingServerPort = bettingServerPort;
    }

    @Value("${betting.serverip}")
    private String bettingServerIp;

    @Value("${betting.serverport}")
    private String bettingServerPort;

    private final TeamService teamService;
    private final GameService gameService;
    private final OddService oddService;
    private final GameReportFootballService gameReportFootballService;
    private final ResultFootballService resultFootballService;
    private final CompetitionService competitionService;
    private final JmsService jmsService;
    private final HttpPostService httpPostService;

    private final String URL_SERVER_SCHEDULED = "/games/game";
    private final String URL_SERVER_RESULT = "/games/result";

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
    public Game generateOdds(Game game) {
        if (game == null) {
            return null;
        }

        Set<Game> lastHomeGamesForHomeTeamSet = gameService.findAllByTeamHome(game.getTeamHome());
        Set<Game> lastAwayGamesForAwayTeamSet = gameService.findAllByTeamAway(game.getTeamAway());
        Set<Game> lastGamesForHomeTeamSet = gameService.findAllByTeamAwayOrTeamHome(game.getTeamHome().getId());
        Set<Game> lastGamesForAwayTeamSet = gameService.findAllByTeamAwayOrTeamHome(game.getTeamAway().getId());

        List<Game> lastHomeGamesForHomeTeam = new ArrayList<>();
        if (lastHomeGamesForHomeTeamSet != null) {
            lastHomeGamesForHomeTeam = new ArrayList<>(lastHomeGamesForHomeTeamSet);
        }

        List<Game> lastAwayGamesForAwayTeam = new ArrayList<>();
        if (lastAwayGamesForAwayTeamSet != null) {
            lastAwayGamesForAwayTeam = new ArrayList<>(lastAwayGamesForAwayTeamSet);
        }

        List<Game> lastGamesForHomeTeam = new ArrayList<>();
        if (lastGamesForHomeTeamSet != null) {
            lastGamesForHomeTeam = new ArrayList<>(lastGamesForHomeTeamSet);
        }

        List<Game> lastGamesForAwayTeam = new ArrayList<>();
        if (lastGamesForAwayTeamSet != null) {
            lastGamesForAwayTeam = new ArrayList<>(lastGamesForAwayTeamSet);
        }

//        System.out.println("GAMES NUMBERS: " + lastHomeGamesForHomeTeam.size() + " | " + lastGamesForHomeTeam.size() + " | " +  lastAwayGamesForAwayTeam.size() + " | " + lastGamesForAwayTeam.size());

        final String homeTeamName = game.getTeamHome().getName();
        final String awayTeamName = game.getTeamAway().getName();

        int lastHomeGamesForHomeTeamPoints = lastHomeGamesForHomeTeam.stream()
                .filter(g -> g.getStatus() == GameStatus.RESULTED)
                .sorted((g1, g2) -> g2.getEndDate().compareTo(g1.getEndDate()))
                .limit(3)
                .map(g -> resultFootballService.findResultByGameUniqueId(g.getUniqueId()))
                .mapToInt(r -> {
                    int goalHome = r.getGameReport().getGoalHome();
                    int goalAway = r.getGameReport().getGoalAway();
                    if (goalHome > goalAway) {
                        return 3;
                    } else if (goalHome == goalAway) {
                        return 1;
                    } else return 0;
                })
                .sum();

        int lastAwayGamesForAwayTeamPoints = lastAwayGamesForAwayTeam.stream()
                .filter(g -> g.getStatus() == GameStatus.RESULTED)
                .sorted((g1, g2) -> g2.getEndDate().compareTo(g1.getEndDate()))
                .limit(3)
                .map(g -> resultFootballService.findResultByGameUniqueId(g.getUniqueId()))
                .mapToInt(r -> {
                    int goalHome = r.getGameReport().getGoalHome();
                    int goalAway = r.getGameReport().getGoalAway();
                    if (goalHome < goalAway) {
                        return 3;
                    } else if (goalHome == goalAway) {
                        return 1;
                    } else return 0;
                })
                .sum();

        int lastGamesForHomeTeamPoints = lastGamesForHomeTeam.stream()
                .filter(g -> g.getStatus() == GameStatus.RESULTED)
                .sorted((g1, g2) -> g2.getEndDate().compareTo(g1.getEndDate()))
                .limit(5)
                .map(g -> resultFootballService.findResultByGameUniqueId(g.getUniqueId()))
                .mapToInt(r -> {
                    int goalHome = r.getGameReport().getGoalHome();
                    int goalAway = r.getGameReport().getGoalAway();
                    if (goalHome == goalAway) {
                        return 1;
                    }
                    if (r.getGame().getTeamHome().getName().equals(homeTeamName)) {
                        if (goalHome > goalAway) {
                            return 3;
                        }
                    } else {
                        if (goalAway > goalHome) {
                            return 3;
                        }
                    }
                    return 0;
                })
                .sum();

        int lastGamesForAwayTeamPoints = lastGamesForAwayTeam.stream()
                .filter(g -> g.getStatus() == GameStatus.RESULTED)
                .sorted((g1, g2) -> g2.getEndDate().compareTo(g1.getEndDate()))
                .limit(5)
                .map(g -> resultFootballService.findResultByGameUniqueId(g.getUniqueId()))
                .mapToInt(r -> {
                    int goalHome = r.getGameReport().getGoalHome();
                    int goalAway = r.getGameReport().getGoalAway();
                    if (goalHome == goalAway) {
                        return 1;
                    }
                    if (r.getGame().getTeamAway().getName().equals(awayTeamName)) {
                        if (goalAway > goalHome) {
                            return 3;
                        }
                    } else {
                        if (goalHome > goalAway) {
                            return 3;
                        }
                    }
                    return 0;
                })
                .sum();

        double homeTeamLastThree = lastHomeGamesForHomeTeamPoints;
        double homeTeamLastFive = lastGamesForHomeTeamPoints;
        double awayTeamLastThree = lastAwayGamesForAwayTeamPoints;
        double awayTeamLastFive = lastGamesForAwayTeamPoints;

//        System.out.println("POINTS: homeTeamLastThree-" + homeTeamLastThree + " homeTeamLastFive-" + homeTeamLastFive + " awayTeamLastThree-" + awayTeamLastThree + " awayTeamLastFive:" + awayTeamLastFive);

        double homeExpectationPoints = homeTeamLastFive*0.4 + homeTeamLastThree*0.6;
        double awayExpectationPoints = awayTeamLastFive*0.4 + awayTeamLastThree*0.6*1.5;

        double homeExpectationPercentage = homeExpectationPoints / MAX_HOME_EXPECTATION;
        double awayExpectationPercentage = awayExpectationPoints / MAX_AWAY_EXPECTATION;

        calculateOdd(homeExpectationPercentage, awayExpectationPercentage, game);

        game.setStatus(GameStatus.PREMATCH);
        Game savedGame = gameService.save(game);
        return savedGame;
    }

    private void calculateOdd(double homeExpactationPercentage, double awayExapctationPercentage, Game game) {
        double difference = homeExpactationPercentage - awayExapctationPercentage;
        double homeOdd = 0.0;
        double awayOdd = 0.0;
        double drawOdd = 0.0;

        if(difference == 0) {
            homeOdd = 2.5;
            awayOdd = 2.5;
        } else if (difference > 0) {
            homeOdd = Math.exp(1-difference);
            awayOdd = Math.pow(3,1+difference);
        } else if (difference < 0) {
            homeOdd = Math.pow(3,1+difference);
            awayOdd = Math.exp(1-difference);
        }

        drawOdd = calculateDrawOdd(homeOdd, awayOdd);

        Set<Odd> calculatedOdds = new HashSet<>();

        Odd oddH = Odd.builder().type(GameOddType.HOME_WIN).value((new BigDecimal(homeOdd)).setScale(2, RoundingMode.CEILING)).game(game).build();
        Odd oddA = Odd.builder().type(GameOddType.AWAY_WIN).value((new BigDecimal(awayOdd)).setScale(2, RoundingMode.CEILING)).game(game).build();
        Odd oddD = Odd.builder().type(GameOddType.DRAW).value((new BigDecimal(drawOdd)).setScale(2, RoundingMode.CEILING)).game(game).build();
        calculatedOdds.add(oddH);
        calculatedOdds.add(oddA);
        calculatedOdds.add(oddD);
//        System.out.println("CALCULATED ODDS: \thome: " + oddH.getValue() + "\tdraw: " + oddD.getValue() + "\taway: " + oddA.getValue());
        game.setOdds(calculatedOdds);
    }

    private double calculateDrawOdd(double hOdd, double aOdd) {
        return (hOdd * aOdd) / (BETTING_SITE_PERCENTAGE*aOdd*hOdd - aOdd - hOdd);
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
                gameService.save(game);
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

        gamesWithOutOdds.forEach(g -> {
            Game game = generateOdds(g);
            jmsService.sendJsonMessage(queueName, generateJsonFromGame(game));
        });
    }

    @Scheduled(cron = "0 15/20 8-20 * * ?")
    void generateResultsForInplayGamesForFaCup() {
        String queueName = "FA CUP result";

        Competition competition = competitionService.findByName("FA Cup");
        Set<Game> inplayGames = gameService.findAllGeneratedGamesForCompetition(competition.getId());

        inplayGames.forEach(g -> {
            Set<ResultFootball> resultsFootball = generateResults(inplayGames);
            resultsFootball.forEach(result -> {
                jmsService.sendJsonMessage(queueName, generateJsonFromResult(result));
            });
        });
    }

    @Scheduled(cron = "0 0/4 8-20 * * ?")
    void generateGamesForPremierLeague() {
        Competition competition = competitionService.findByName("Premier League");
        Set<Game> gamesWithOutOdds = generateGames(competition);
        if (gamesWithOutOdds.size() == 0) {
            return;
        }
        gamesWithOutOdds.forEach(g -> {
            Game game = generateOdds(g);
            postGameMsg(game);
        });
    }

    private void postGameMsg(Game game) {
        if (bettingServerIp == null || bettingServerPort == null || bettingServerIp.isBlank() || bettingServerPort.isBlank()) {
            //todo throw exception
            //todo make method generic
            System.out.println("Could not send a message. Betting ip or server is not valid.");
            return;
        }
        try {
            final String urlServerScheduled = "http://" + bettingServerIp + ":" + bettingServerPort + URL_SERVER_SCHEDULED;
            httpPostService.postJsonMessage(generateJsonFromGame(game), urlServerScheduled);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 2/4 8-20 * * ?")
    void generateResultsForInplayGamesForPremierLeague() {

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
        if (bettingServerIp.isBlank() || bettingServerPort.isBlank()) {
            //todo throw exception
            //todo make method generic
            System.out.println("Could not send a message. Betting ip or server is not valid.");
            return;
        }
        try {
            final String urlServerResult = "http://" + bettingServerIp + ":" + bettingServerPort + URL_SERVER_RESULT;
            httpPostService.postJsonMessage(generateJsonFromResult(result), urlServerResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Scheduled(cron = "0 10 7,10,14,17,19 * * ?")
    public void deleteOldGames() {
        final int amountToBeLeft = 100;
        gameService.deleteOldGames(amountToBeLeft);
    }
}
