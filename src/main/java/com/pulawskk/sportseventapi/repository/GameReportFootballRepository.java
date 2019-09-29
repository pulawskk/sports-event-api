package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.GameReport;
import com.pulawskk.sportseventapi.entity.GameReportFootball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameReportFootballRepository extends JpaRepository<GameReportFootball, Long> {

}
