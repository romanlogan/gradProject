package com.gradproject.historyservice.repository;

import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.entity.CardGameHistory;
import com.gradproject.historyservice.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

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
