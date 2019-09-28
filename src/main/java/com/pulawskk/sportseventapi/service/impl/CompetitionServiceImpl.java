package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.repository.CompetitionRepository;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import com.pulawskk.sportseventapi.service.CompetitionService;
import org.springframework.stereotype.Service;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, TeamRepository teamRepository) {
        this.competitionRepository = competitionRepository;
        this.teamRepository = teamRepository;
    }
}
