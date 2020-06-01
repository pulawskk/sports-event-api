package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FakeService {
    Set<Game> generateGames(Competition competition);

    Game generateOdds(Game game);

    Set<ResultFootball> generateResults(Set<Game> games);

    GameReportFootball generateReportFootball(Game game);

    void deleteOldGames();
}
