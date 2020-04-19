package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.services.BasketEventServices;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log
public class GetDoubleDoubleEventFromSeason {

    protected BasketEventServices basketEventServices;
    protected BasketPlayerEntryServices basketPlayerEntryServices;

    @Transactional
    public List<BasketEvent> execute(String seasonWebKey) {
        basketEventServices.deleteBySeason(seasonWebKey);
        List<BasketEvent> result = new ArrayList<>();
        List<BasketPlayer> basketPlayers = basketPlayerEntryServices.findGroupedByPlayerBySeason(seasonWebKey);
        for (BasketPlayer basketPlayer : basketPlayers) {
            List<BasketPlayerEntry> basketPlayerEntries = basketPlayerEntryServices.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(basketPlayer, seasonWebKey);
            Integer streakCount = 0;
            Integer seasonCount = 0;
            for (BasketPlayerEntry basketPlayerEntry : basketPlayerEntries) {
                if (basketPlayerEntry.isDoubleDouble()) {
                    log.info("doble doble");
                    streakCount += 1;
                    seasonCount += 1;
                    result.add(basketEventServices.createAndSaveEvent(seasonCount, basketPlayerEntry, BasketEventType.DOUBLE_DOUBLE, BasketEventLevel.SEASON, seasonWebKey));
                    if (streakCount > 1) {
                        log.info("streakCount " + streakCount);
                        result.add(basketEventServices.createAndSaveEvent(streakCount, basketPlayerEntry, BasketEventType.DOUBLE_DOUBLE_STREAK, BasketEventLevel.SEASON, seasonWebKey));
                    }
                } else {
                    streakCount = 0;
                }
            }
        }
        return result;
    }
}
