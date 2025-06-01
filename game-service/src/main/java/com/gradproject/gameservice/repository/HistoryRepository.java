package com.gradproject.gameservice.repository;

import com.gradproject.gameservice.entity.CardGameHistory;
import com.gradproject.gameservice.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface HistoryRepository extends JpaRepository<CardGameHistory, Long> ,
                                            QuerydslPredicateExecutor<CardGameHistory>,
                                            HistoryRepositoryCustom {


    List<History> findByUserEmail(String email);

//    @Query(value = "select game_id, sum(TIMESTAMPDIFF(MINUTE, start_time, end_time)), max(end_time) " +
//            "from history " +
//            "where user_email = :email " +
//            "group by game_id" ,nativeQuery = true)
//    void getMyInfoHistory(@Param("email") String email);
}
