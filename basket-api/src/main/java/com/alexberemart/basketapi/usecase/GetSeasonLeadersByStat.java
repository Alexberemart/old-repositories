package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import com.alexberemart.basketapi.services.SeasonServices;
import com.alexberemart.basketapi.usecase.model.SeasonLeaders;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GetSeasonLeadersByStat {

    public SeasonServices seasonServices;
    public BasketPlayerEntryServices basketPlayerEntryServices;

    public List<SeasonLeaders> execute() {
        List<SeasonLeaders> seasonLeadersList = new ArrayList<>();
        List<Season> seasonList = seasonServices.findAll();
        for (Season season : seasonList) {
            BasketPlayerEntry basketPlayerEntryLeaderByPoints = basketPlayerEntryServices.getSeasonLeaderByPoints(season);
            BasketPlayerEntry basketPlayerEntryLeaderByRebounds = basketPlayerEntryServices.getSeasonLeaderByRebounds(season);
            seasonLeadersList.add(SeasonLeaders.builder()
                    .seasonId(season.getId())
                    .seasonName(season.getWebKey())
                    .basketPlayerIdLeaderByPoints(basketPlayerEntryLeaderByPoints.getBasketPlayer().getId())
                    .basketPlayerNameLeaderByPoints(basketPlayerEntryLeaderByPoints.getBasketPlayer().getName())
                    .points(basketPlayerEntryLeaderByPoints.getPoints())
                    .basketPlayerIdLeaderByRebounds(basketPlayerEntryLeaderByRebounds.getBasketPlayer().getId())
                    .basketPlayerNameLeaderByRebounds(basketPlayerEntryLeaderByRebounds.getBasketPlayer().getName())
                    .rebounds(basketPlayerEntryLeaderByRebounds.getRebounds())
                    .build());
        }
        return seasonLeadersList;
    }
}
