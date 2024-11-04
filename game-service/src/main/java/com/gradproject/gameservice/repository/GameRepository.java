package com.gradproject.gameservice.repository;

import com.gradproject.gameservice.entity.CardGame;
import com.gradproject.gameservice.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface GameRepository extends JpaRepository<CardGame, Long>{
//                                        QuerydslPredicateExecutor<Game>,
//                                        GameRepositoryCustom {



}
