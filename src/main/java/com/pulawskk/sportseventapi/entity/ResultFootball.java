package com.pulawskk.sportseventapi.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name="results_football")
public class ResultFootball extends Result {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_report_id", referencedColumnName = "id")
    private GameReportFootball gameReport;

    @Builder
    public ResultFootball(Long id, Game game, GameReportFootball gameReportFootball) {
        super(id, game);
        this.gameReport = gameReportFootball;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("ResultFootball game id<")
                .append(getGame().getUniqueId())
                .append("> event: ")
                .append(getGame().getTeamHome().getName())
                .append(" ")
                .append(getGameReport().getGoalHome())
                .append("-")
                .append(getGameReport().getGoalAway())
                .append(" ")
                .append(getGame().getTeamAway().getName());
        return stringBuffer.toString();
    }
}
