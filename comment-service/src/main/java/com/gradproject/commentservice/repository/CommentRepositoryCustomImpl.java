package com.gradproject.commentservice.repository;

import com.gradproject.commentservice.dto.CommentDto;
import com.gradproject.commentservice.dto.QCommentDto;
import com.gradproject.commentservice.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.gradproject.commentservice.entity.QComment.comment;

public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public CommentRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<CommentDto> getCommentDtoListBy(Long gameId) {

        return queryFactory
                .select(
                        new QCommentDto(
                                comment.id,
                                comment.content,
                                comment.userEmail,
                                comment.gameId,
                                comment.createdAt
                        )
                )
                .from(comment)
                .where(comment.gameId.eq(gameId))
                .orderBy(comment.createdAt.desc())
                .fetch();
    }

}
