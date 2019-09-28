package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.repository.CompetitionRepository;
import com.pulawskk.sportseventapi.service.CompetitionService;
import org.springframework.stereotype.Service;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }
}
