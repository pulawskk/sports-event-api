package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Result;
import com.pulawskk.sportseventapi.service.FakeService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FakeFootballService implements FakeService {
    @Override
    public Set<Game> generateGames(Competition competition) {
        return null;
    }

    @Override
    public Set<Game> generateOdds(Game game) {
        return null;
    }

    @Override
    public Set<Result> generateResult(Game game) {
        return null;
    }
}
