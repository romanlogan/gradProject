package com.gardproject.replyservice.repository;

import com.gardproject.replyservice.dto.QReplyDto;
import com.gardproject.replyservice.dto.ReplyDto;
import com.gardproject.replyservice.entity.QReply;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.gardproject.replyservice.entity.QReply.reply;

public class ReplyRepositoryCustomImpl implements ReplyRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ReplyRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<ReplyDto> getReplyDtosByCommentId(Long commentId) {


        return queryFactory
                .select(
                        new QReplyDto(
                                reply.id,
                                reply.content,
                                reply.userEmail,
                                reply.createdAt
                        )
                )
                .from(reply)
                .where(reply.commentId.eq(commentId))
                .orderBy(reply.createdAt.desc())
                .fetch();
    }
}
