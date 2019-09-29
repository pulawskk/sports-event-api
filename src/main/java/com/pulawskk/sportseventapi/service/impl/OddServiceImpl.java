package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.repository.OddRepository;
import com.pulawskk.sportseventapi.service.OddService;
import org.springframework.stereotype.Service;

@Service
public class OddServiceImpl implements OddService {
    private final OddRepository oddRepository;

    public OddServiceImpl(OddRepository oddRepository) {
        this.oddRepository = oddRepository;
    }
}
