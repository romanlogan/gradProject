package com.gardproject.replyservice.repository;

import com.gardproject.replyservice.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>,
                                        QuerydslPredicateExecutor<Reply>,
                                            ReplyRepositoryCustom{

    @Modifying
    @Query("delete from Reply r where r.id = :replyId")
    void deleteById(@Param("replyId") Long replyId);

    List<Reply> findByCommentId(long commentId);

}
