package com.gradproject.commentservice.repository;

import com.gradproject.commentservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> ,
                                            QuerydslPredicateExecutor<Comment>,
                                            CommentRepositoryCustom {


    @Modifying
    @Query("delete from Comment c where c.id = :commentId")
    void deleteById(@Param("commentId") Long commentId);

    Comment findByUserEmailAndGameId(String email ,Integer gameId);
}
