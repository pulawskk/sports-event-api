package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Odd;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.repository.OddRepository;
import com.pulawskk.sportseventapi.service.OddService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OddServiceImpl implements OddService {
    private final OddRepository oddRepository;

    public OddServiceImpl(OddRepository oddRepository) {
        this.oddRepository = oddRepository;
    }

    public Odd findById(Long id) {
        return oddRepository.findById(id).orElse(null);
    }

    public Set<Odd> findAllByGame(Game game) {
        return new HashSet<>(oddRepository.findAllByGame(game));
    }


    public Set<Odd> findAllByGameAndType(Game game, GameOddType gameOddType) {
        return new HashSet<>(oddRepository.findAllByGameAndType(game, gameOddType));
    }

    public Odd save(Odd odd) {
        return oddRepository.save(odd);
    }


    public void deleteById(Long id) {
        oddRepository.deleteById(id);
    }


    public void delete(Odd odd) {
        oddRepository.delete(odd);
    }


    public void deleteAllByGame(Game game) {
        oddRepository.deleteAllByGame(game);
    }
}
