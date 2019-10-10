package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FakeService {
    Set<Game> generateGames(Competition competition);

    Set<Game> generateOdds(Set<Game> games);

    Set<ResultFootball> generateResult(Set<Game> games);

    GameReportFootball generateReportFootball(Game game);
}
