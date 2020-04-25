package com.pulawskk.sportseventapi;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.OddRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableScheduling
public class AppConfig {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private OddRepository oddRepository;

    @PostConstruct
    private void postConstruct() {
        List<Game> gamesToBeDeleted = gameRepository.findAllGamesToBeDeleted(100);
        gameRepository.deleteAll(gamesToBeDeleted);
    }


}
