package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.repository.ResultFootballRepository;
import com.pulawskk.sportseventapi.service.ResultService;
import org.springframework.stereotype.Service;

@Service
public class ResultFootballService implements ResultService {

    private final ResultFootballRepository resultFootballRepository;

    public ResultFootballService(ResultFootballRepository resultFootballRepository) {
        this.resultFootballRepository = resultFootballRepository;
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
}
