package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.GameReportFootball;
import com.pulawskk.sportseventapi.repository.GameReportFootballRepository;
import com.pulawskk.sportseventapi.service.GameReportService;
import org.springframework.stereotype.Service;

@Service
public class GameReportFootballService implements GameReportService {

    private final GameReportFootballRepository gameReportFootballRepository;

    public GameReportFootballService(GameReportFootballRepository gameReportFootballRepository) {
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
