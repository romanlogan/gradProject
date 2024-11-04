package com.gradproject.historyservice.repository;


import com.gradproject.historyservice.entity.CardGameHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.gradproject.historyservice.entity.QCardGameHistory.cardGameHistory;

public class HistoryRepositoryCustomImpl implements HistoryRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public HistoryRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

////    @Override
//    public List<History> getHistoriesByEmail(String email) {
//
////        QHistory history = QHistory.history;
//
//
//        return queryFactory
//                .selectFrom(history)
//                .where(history.userEmail.eq(email))
//                .fetch();
//    }


    @Override
    public CardGameHistory getLastSavedHistory(Integer gameId, String email) {

        return queryFactory
//                .select(
//                        new QLastSavedHistory(
//                                cardGameHistory.id,
//                                cardGameHistory.startTime,
//                                cardGameHistory.endTime,
//                                cardGameHistory.exitType,
//                                cardGameHistory.gameId,
//                                cardGameHistory.userEmail,
//                                cardGameHistory.hp,
//                                cardGameHistory.cards,
//                                cardGameHistory.route,
//                                cardGameHistory.enemy
//                        )
//              )
                .selectFrom(cardGameHistory)
                .where(
                        cardGameHistory.gameId.eq(gameId),
                        cardGameHistory.userEmail.eq(email)
                )
                .orderBy(cardGameHistory.endTime.desc())
                .limit(1)
                .fetchOne();
    }
}
