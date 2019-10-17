package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Odd;
import com.pulawskk.sportseventapi.enums.GameOddType;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface OddService {

    Odd findById(Long id);

    Set<Odd> findAllByGame(Game game);

    Set<Odd> findAllByGameAndType(Game game, GameOddType gameOddType);

    Odd save(Odd odd);

    void deleteById(Long id);

    void delete(Odd odd);

    void deleteAllByGame(Game game);

    void flush();
}
