package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.ResultFootball;
import org.springframework.stereotype.Service;

@Service
public interface ResultService {

    ResultFootball findById(Long id);

    ResultFootball save(ResultFootball resultFootball);

    void delete(ResultFootball resultFootball);

    void deleteById(Long id);
}
