package com.pulawskk.sportseventapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class Result {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_generator")
//    @SequenceGenerator(name="new_generator", sequenceName = "result_seq", initialValue = 5)
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    public Result(Long id, Game game) {
        this.id = id;
        this.game = game;
    }
}
