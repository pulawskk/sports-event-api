package com.pulawskk.sportseventapi.entity;

import com.pulawskk.sportseventapi.enums.GameOddType;
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
    private GameOddType name;

    @Column(name = "value")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "games_id")
    private Game game;
}
