package com.gradproject.historyservice.repository;

import com.gradproject.historyservice.entity.CardGameHistory;

public interface HistoryRepositoryCustom {

    CardGameHistory getLastSavedHistory(Integer gameId, String email);

}
