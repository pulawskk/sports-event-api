package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.enums.GameOddType;
import org.json.JSONObject;

import java.util.Optional;

public interface JsonUtil {

    default JSONObject generateJsonFromResult(ResultFootball result) {
        JSONObject json = new JSONObject();
        json.put("teamHome", result.getGame().getTeamHome().getName());
        json.put("teamAway", result.getGame().getTeamAway().getName());
        json.put("competition", result.getGame().getCompetition().getName());
        json.put("homeScores", result.getGameReport().getGoalHome());
        json.put("homeCorners", result.getGameReport().getCornerHome());
        json.put("homeOffsides", result.getGameReport().getOffsideHome());
        json.put("homeYellowCards", result.getGameReport().getYCardHome());
        json.put("homeRedCards", result.getGameReport().getRCardHome());
        json.put("awayScores", result.getGameReport().getGoalAway());
        json.put("awayCorners", result.getGameReport().getCornerAway());
        json.put("awayOffsides", result.getGameReport().getOffsideAway());
        json.put("awayYellowCards", result.getGameReport().getYCardAway());
        json.put("awayRedCards", result.getGameReport().getRCardAway());
        return json;
    }

    default JSONObject generateJsonFromGame(Game game) {
        JSONObject jsonFromGame = new JSONObject();
        jsonFromGame.put("teamHome", game.getTeamHome().getName());
        jsonFromGame.put("teamAway", game.getTeamAway().getName());
        jsonFromGame.put("oddsH", game.getOddByType(GameOddType.HOME_WIN).getValue());
        jsonFromGame.put("oddsX", game.getOddByType(GameOddType.DRAW).getValue());
        jsonFromGame.put("oddsA", game.getOddByType(GameOddType.AWAY_WIN).getValue());
        jsonFromGame.put("gameStatus", game.getStatus().name());
        jsonFromGame.put("competition", game.getCompetition().getName());
        Optional.ofNullable(game.getStartDate()).ifPresent(date -> jsonFromGame.put("startGame", date));
        Optional.ofNullable(game.getEndDate()).ifPresent(date -> jsonFromGame.put("endGame", date));
        return jsonFromGame;
    }
}
