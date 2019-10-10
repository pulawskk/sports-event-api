package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.service.FakeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FakeFootballService implements FakeService {
    @Override
    public Set<Game> generateGames(Competition competition) {
        return null;
    }

    @Override
    public Set<Game> generateOdds(Set<Game> game) {
        return null;
    }

    @Override
    public Set<ResultFootball> generateResult(Set<Game> game) {
        return null;
    }

    @Override
    public GameReportFootball generateReportFootball(Game game) {
        return null;
    }

    public List<Integer> generatePairs(Competition competition) {
        return null;
    }
}
