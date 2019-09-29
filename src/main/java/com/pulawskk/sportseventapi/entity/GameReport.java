package com.pulawskk.sportseventapi.entity;

import com.pulawskk.sportseventapi.repository.GameReportFootballRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class GameReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public GameReport(Long id) {
        this.id = id;
    }
}
