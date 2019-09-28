package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.GameReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameReportRepository extends JpaRepository<GameReport, Long> {

}
