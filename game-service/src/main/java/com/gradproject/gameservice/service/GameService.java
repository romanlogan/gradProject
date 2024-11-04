package com.gradproject.gameservice.service;

import com.gradproject.gameservice.dto.ResponseGame;
import com.gradproject.gameservice.dto.ResponseMain;
import com.gradproject.gameservice.dto.SaveHistoryRequest;

public interface GameService {

    Long save(SaveHistoryRequest request);

    ResponseGame getRecentlyPlayedGameBy(String email);

    ResponseMain getCommentList(Long gameId);
}
