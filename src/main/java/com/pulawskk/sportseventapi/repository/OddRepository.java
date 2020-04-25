package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Odd;
import com.pulawskk.sportseventapi.enums.GameOddType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OddRepository extends JpaRepository<Odd, Long> {
    List<Odd> findAllByGame(Game game);

    List<Odd> findAllByGameAndType(Game game, GameOddType type);

    void deleteAllByGame(Game game);

    @Query(value = "select * from odds where start_date < (select start_date from games order by start_date desc limit 1 OFFSET ?1)", nativeQuery = true)
    List<Game> findAllOddsToBeDeleted(int offset);
}
