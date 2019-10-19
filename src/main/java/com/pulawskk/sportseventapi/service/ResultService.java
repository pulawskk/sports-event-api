package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface ResultService {

    ResultFootball findById(Long id);

    ResultFootball save(ResultFootball resultFootball);

    void delete(ResultFootball resultFootball);

    void deleteById(Long id);

    Set<ResultFootball> findAllResultsForCompetition(Long competitionId);

    Set<ResultFootball> saveAll(Set<ResultFootball> results);
}
