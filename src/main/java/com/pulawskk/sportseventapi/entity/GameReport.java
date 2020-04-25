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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_generator")
    @SequenceGenerator(name="new_generator", sequenceName = "game_report_seq", allocationSize = 1)
    private Long id;

    public GameReport(Long id) {
        this.id = id;
    }
}
