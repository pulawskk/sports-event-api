package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.ResultFootball;
import com.pulawskk.sportseventapi.repository.ResultFootballRepository;
import com.pulawskk.sportseventapi.service.JsonUtil;
import com.pulawskk.sportseventapi.service.ResultService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResultFootballService implements ResultService, JsonUtil {

    private final ResultFootballRepository resultFootballRepository;

    public ResultFootballService(ResultFootballRepository resultFootballRepository) {
        this.resultFootballRepository = resultFootballRepository;
    }

    public ResultFootball findById(Long id) {
        return resultFootballRepository.findById(id).orElse(null);
    }

    public ResultFootball save(ResultFootball resultFootball) {
        return resultFootballRepository.save(resultFootball);
    }

    public void delete(ResultFootball resultFootball) {
        resultFootballRepository.delete(resultFootball);
    }

    public void deleteById(Long id) {
        resultFootballRepository.deleteById(id);
    }

    @Override
    public Set<ResultFootball> findAllResultsForCompetition(Long competitionId) {
        return resultFootballRepository.findAllResultsForCompetition(competitionId);
    }

    @Override
    public Set<ResultFootball> saveAll(Set<ResultFootball> results) {
        return resultFootballRepository.saveAll(results).stream().collect(Collectors.toSet());
    }

    @Transactional
    public List<JSONObject> generateJsonForAllResults() {
        Set<ResultFootball> resultedGamesFromDb = resultFootballRepository.findAll().stream().collect(Collectors.toSet());

        List<JSONObject> jsonList = new ArrayList<>();

        resultedGamesFromDb.forEach(result -> {
            JSONObject json = generateJsonFromResult(result);
            jsonList.add(json);
        });

        JSONObject jsonInfo = new JSONObject();
        jsonInfo.put("resultsNumber", resultedGamesFromDb.size());
        jsonList.add(jsonInfo);

        return jsonList;
    }

//    private JSONObject generateJsonFromResult(ResultFootball result) {
//        JSONObject json = new JSONObject();
//        json.put("teamHome", result.getGame().getTeamHome().getName());
//        json.put("teamAway", result.getGame().getTeamAway().getName());
//        json.put("competition", result.getGame().getCompetition().getName());
//        json.put("homeScores", result.getGameReport().getGoalHome());
//        json.put("homeCorners", result.getGameReport().getCornerHome());
//        json.put("homeOffsides", result.getGameReport().getOffsideHome());
//        json.put("homeYellowCards", result.getGameReport().getYCardHome());
//        json.put("homeRedCards", result.getGameReport().getRCardHome());
//        json.put("awayScores", result.getGameReport().getGoalAway());
//        json.put("awayCorners", result.getGameReport().getCornerAway());
//        json.put("awayOffsides", result.getGameReport().getOffsideAway());
//        json.put("awayYellowCards", result.getGameReport().getYCardAway());
//        json.put("awayRedCards", result.getGameReport().getRCardAway());
//        return json;
//    }

    public List<JSONObject> generateJsonForResultsForCompetition(Long competitionId) {
        Set<ResultFootball> resultsFootballsForCompetition = findAllResultsForCompetition(competitionId);

        List<JSONObject> jsonList = new ArrayList<>();


        if (resultsFootballsForCompetition != null) {
            resultsFootballsForCompetition.forEach(result -> {
                jsonList.add(generateJsonFromResult(result));
            });
        }
        JSONObject jsonInfo = new JSONObject();
        jsonInfo.put("gameNumbers", resultsFootballsForCompetition.size());
        jsonList.add(jsonInfo);
        return jsonList;
    }
}
