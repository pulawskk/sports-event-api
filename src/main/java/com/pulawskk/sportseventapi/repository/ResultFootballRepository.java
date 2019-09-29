package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.ResultFootball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultFootballRepository extends JpaRepository<ResultFootball, Long> {

}
