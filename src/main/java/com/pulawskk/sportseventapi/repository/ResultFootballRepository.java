package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Result;
import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultFootballRepository extends JpaRepository<Result, Long> {

}
