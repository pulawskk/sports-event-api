package com.pulawskk.sportseventapi.aspect;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.entity.Team;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Aspect
@Order(1)
public class AspectUtil {

    private final Logger logger = LoggerFactory.getLogger(AspectUtil.class);

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.FakeFootballService.generateGames*(..))")
    private void generateGames() {}

    @Before("generateGames()")
    public void beforeGenerateGames(JoinPoint joinPoint) {
        logger.info("class: FakeFootballService, method: " + joinPoint.getSignature().getName() + "start generating games");
    }

    @AfterReturning("generateGames()")
    public void afterReturningGenerateGames(JoinPoint joinPoint) {
        logger.info("["+joinPoint.getTarget().getClass().getSimpleName()+"] method: " + joinPoint.getSignature().getName() + "games were generated");
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.HttpPostServiceImpl.postJsonMessage(..))")
    private void postJsonMessage() {}

    @Before("postJsonMessage()")
    public void beforePostJsonMessage(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        String uniqueId = "";
        String url = "";

        for (Object o : args) {
            if (o instanceof JSONObject) {
                uniqueId = ((JSONObject) o).getString("uniqueId");
            }
            if (o instanceof String) {
                url = (String) o;
            }
        }
        logger.info("["+joinPoint.getTarget().getClass().getSimpleName()+"] method: " + joinPoint.getSignature().getName() + " try to send object with uniqueId: " + uniqueId + " to: " + url);
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.GameServiceImpl.findAllByTeam*(..))")
    private void findAllByTeam() {}

    @Around("findAllByTeam()")
    public Object aroundFindByTeam(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String teamName = "";
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object o : args) {
            if (o instanceof Team) {
                teamName = ((Team) o).getName();
            }
            if (o instanceof Long) {
                teamName = "<team id: " + o + ">";
            }
        }

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        Set<Game> games = (Set<Game>) result;
        int numberOfGames = 0;
        if (games != null) {
            numberOfGames = games.size();
        }
        logger.info("["+proceedingJoinPoint.getTarget().getClass().getSimpleName()+"] method: " + proceedingJoinPoint.getSignature().getName() + " found for team: "
                + teamName + " games number: " + numberOfGames + " in total time: " + (end-start) + " ms");
        return result;
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.*.save*(..))")
    private void saveGame() {}

    @Around("saveGame()")
    public Object aroundSaveGame(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        String objectClassDetail = "";
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object o : args) {
            if (o instanceof Game) {
                final String uniqueId = ((Game) o).getUniqueId();
                objectClassDetail = "Game with uniqueId = " + uniqueId;
            }
            if (o instanceof ResultFootball) {
                final String uniqueId = ((ResultFootball) o).getGame().getUniqueId();
                objectClassDetail = "ResultFootball with uniqueId = " + uniqueId;
            }
        }

        logger.info("["+proceedingJoinPoint.getTarget().getClass().getSimpleName()+"] method: " + proceedingJoinPoint.getSignature().getName()
                + " object: " + objectClassDetail + " in total time: " + (end-start) + " ms");
        return result;
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.ResultFootballService.generateAllResults(..))")
    private void generateAllResults() {}

    @Around("generateAllResults()")
    public Object aroundGenerateAllResults(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        List<ResultFootball> resultFootballList = new ArrayList<>();
        if (result != null) {
            resultFootballList = (List<ResultFootball>) result;
        }

        logger.info("["+proceedingJoinPoint.getTarget().getClass().getSimpleName()+"] method: " + proceedingJoinPoint.getSignature().getName()
                + " found: " + resultFootballList.size() + " of results in total time = " + (end - start) + "ms");
        return result;
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.GameServiceImpl.findAll())")
    private void findAllGames() {}

    @Around("findAllGames()")
    public Object aroundFindAllGames(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        List<Game> gameList = new ArrayList<>();
        if (result != null) {
            gameList = (List<Game>) result;
        }
        logger.info("["+proceedingJoinPoint.getTarget().getClass().getSimpleName()+"] method: " + proceedingJoinPoint.getSignature().getName()
                + " found: " + gameList.size() + " of inplay games in total time = " + (end - start) + "ms");

        return result;
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.GameServiceImpl.findGameByUniqueId(..))")
    private void findUniqueGame() {}

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.ResultFootballService.findResultByGameUniqueId(..))")
    private void findUniqueResult() {}

    @Around("findUniqueGame() || findUniqueResult()")
    public Object aroundFindUniqueGame(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        String objectDetail = "null";

        if (result != null) {
            if (result instanceof Game) {
                final String gameUniqueId = ((Game) result).getUniqueId();
                objectDetail = "Game with unique id = " + gameUniqueId;
            }
            if (result instanceof ResultFootball) {
                final String[] resultUniqueId = new String[1];
                Optional.ofNullable(((ResultFootball) result).getGame()).ifPresent( r -> resultUniqueId[0] = r.getUniqueId());
                objectDetail = "FootballResult with unique id = " + resultUniqueId[0];
            }
        }
        logger.info("["+proceedingJoinPoint.getTarget().getClass().getSimpleName()+"] method: " + proceedingJoinPoint.getSignature().getName()
                + " found "+ objectDetail + " in total time = " + (end - start) + "ms");

        return result;
    }

    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.GameServiceImpl.generateInplayGamesForCompetition(..))")
    private void findInplayGamesForCompetition() {}

    @Around("findInplayGamesForCompetition()")
    public Object aroundFindInplayGamesForCompetition(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        Object[] args = proceedingJoinPoint.getArgs();
        Long competitionId = (Long) args[0];

        List<Game> gameList = new ArrayList<>();

        if (result != null) {
            gameList = (List<Game>) result;
        }
        logger.info("["+proceedingJoinPoint.getTarget().getClass().getSimpleName()+"] method: " + proceedingJoinPoint.getSignature().getName()
                + " found number of inplay games: " + gameList.size()
                + " for competition id = "+ competitionId+ " in total time = " + (end - start) + "ms");

        return result;
    }


////        ===============CHECKING ALL INVOKATIONS FROM SERVICE.IMPL
//    @Pointcut("execution(* com.pulawskk.sportseventapi.service.impl.*.*(..))")
//    private void checking() {}
//
//    @Before("checking()")
//    public void beforeResultByGameUniqueId(JoinPoint joinPoint) {
//
//        logger.info("["+joinPoint.getTarget().getClass().getName()+"] method: " + joinPoint.getSignature().getName());
//    }
}
