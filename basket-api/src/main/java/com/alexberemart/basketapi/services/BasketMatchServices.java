package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.model.BasketMatch;

import java.util.Date;
import java.util.List;

public interface BasketMatchServices {
    BasketMatch save(BasketMatch basketMatch);

    List<BasketMatch> findByGameKey(String gameKey);

    BasketMatch findById(String id);

    List<BasketMatch> findAll();

    BasketMatch getLastGameBetweenTeams(Date date, String basketMatchId, String basketTeamId);

    List<BasketMatch> findBySeason_WebKey(String seasonWebKey);

    List<BasketMatch> findBySeason_IdIn(List<String> seasonIdList);

    BasketMatch findFirstByOrderByDateDesc();
}