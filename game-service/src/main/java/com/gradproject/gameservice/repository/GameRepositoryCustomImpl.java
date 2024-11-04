package com.gradproject.gameservice.repository;

import com.gradproject.gameservice.entity.Game;
import com.gradproject.gameservice.entity.QGame;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.gradproject.gameservice.entity.QGame.*;

public class GameRepositoryCustomImpl implements GameRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public GameRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

//    @Override
//    public Game findRecentlyPlayedGame(String email) {
//
//        return queryFactory
//                .selectFrom(game)
//                .where(game.endTime.isNotNull())
//                .orderBy(game.endTime.desc())
//                .limit(1)
//                .fetchOne();
//    }
}
