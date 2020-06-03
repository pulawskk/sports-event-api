package com.pulawskk.sportseventapi.service.impl;

import com.google.common.collect.Lists;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.repository.ResultFootballRepository;
import com.pulawskk.sportseventapi.service.JsonUtil;
import com.pulawskk.sportseventapi.service.ResultService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResultFootballService implements ResultService, JsonUtil {

    private final ResultFootballRepository resultFootballRepository;

    public ResultFootballService(ResultFootballRepository resultFootballRepository) {
        this.resultFootballRepository = resultFootballRepository;
    }

    @Override
    public ResultFootball findResultByGameUniqueId(String uniqueId) {
        ResultFootball resultFootball = resultFootballRepository.findByGameUniqueId(uniqueId);
        if (resultFootball != null) {
            return resultFootball;
        }
        return ResultFootball.builder().build();
    }

    public ResultFootball findById(Long id) {
        return resultFootballRepository.findById(id).orElse(null);
    }

    public ResultFootball save(ResultFootball resultFootball) {
        return resultFootballRepository.save(resultFootball);
    }

    public void delete(ResultFootball resultFootball) {
        resultFootballRepository.delete(resultFootball);
    }

    public void deleteById(Long id) {
        resultFootballRepository.deleteById(id);
    }

    @Override
    public Set<ResultFootball> findAllResultsForCompetition(Long competitionId) {
        return resultFootballRepository.findAllResultsForCompetition(competitionId);
    }

    @Override
    public Set<ResultFootball> saveAll(Set<ResultFootball> results) {
        return new HashSet<>(resultFootballRepository.saveAll(results));
    }

    @Transactional
    public List<ResultFootball> generateAllResults() {
        return resultFootballRepository.findAll();
    }

    @Transactional
    public List<ResultFootball> generateResultsForCompetition(Long competitionId) {
        return Lists.newArrayList(findAllResultsForCompetition(competitionId));
    }
}
