package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Result;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FakeService {
    Set<Game> generateGames(Competition competition);

    Set<Game> generateOdds(Game game);

    Set<Result> generateResult(Game game);
}
