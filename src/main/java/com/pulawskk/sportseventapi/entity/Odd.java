package com.pulawskk.sportseventapi.entity;

import com.pulawskk.sportseventapi.enums.GameOddType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name = "odds")
public class Odd {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private GameOddType type;

    @Column(name = "value")
    private BigDecimal value;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "games_id")
    private Game game;

    @Builder
    public Odd(Long id, GameOddType type, BigDecimal value, Game game) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.game = game;
    }
}
