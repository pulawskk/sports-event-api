package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.GameReportFootball;
import org.springframework.stereotype.Service;

@Service
public interface GameReportFootballService {

    GameReportFootball findById(Long id);

    GameReportFootball save(GameReportFootball gameReportFootball);

    void delete(GameReportFootball gameReportFootball);

    void deleteById(Long id);
}
