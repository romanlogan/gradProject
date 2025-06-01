package com.gradproject.gameservice.repository;

import com.gradproject.gameservice.entity.CardGameHistory;

public interface HistoryRepositoryCustom {

    CardGameHistory getLastSavedHistory(Integer gameId, String email);

}
