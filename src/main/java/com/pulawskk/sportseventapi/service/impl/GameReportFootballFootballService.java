package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.GameReportFootball;
import com.pulawskk.sportseventapi.repository.GameReportFootballRepository;
import com.pulawskk.sportseventapi.service.GameReportFootballService;
import org.springframework.stereotype.Service;

@Service
public class GameReportFootballFootballService implements GameReportFootballService {

    private final GameReportFootballRepository gameReportFootballRepository;

    public GameReportFootballFootballService(GameReportFootballRepository gameReportFootballRepository) {
        this.gameReportFootballRepository = gameReportFootballRepository;
    }

    public GameReportFootball findById(Long id) {
        return gameReportFootballRepository.findById(id).orElse(null);
    }

    public GameReportFootball save(GameReportFootball gameReportFootball) {
        return gameReportFootballRepository.save(gameReportFootball);
    }

    public void delete(GameReportFootball gameReportFootball) {
        gameReportFootballRepository.delete(gameReportFootball);
    }

    public void deleteById(Long id) {
        gameReportFootballRepository.deleteById(id);
    }
}
