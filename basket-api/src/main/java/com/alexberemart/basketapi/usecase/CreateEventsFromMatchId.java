package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.services.BasketEventServices;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateEventsFromMatchId {

    protected BasketEventServices basketEventServices;
    protected BasketPlayerEntryServices basketPlayerEntryServices;

    public List<BasketEvent> execute(String gameKey) {
        return createDoubleDoubleEventFromMatch(gameKey);
    }

    protected List<BasketEvent> createDoubleDoubleEventFromMatch(String gameKey) {
        List<BasketEvent> result = new ArrayList<>();
        List<BasketPlayerEntry> basketPlayerEntries = basketPlayerEntryServices.findByBasketMatch(gameKey);
        for (BasketPlayerEntry basketPlayerEntry : basketPlayerEntries) {
            if (basketPlayerEntry.isDoubleDouble()) {
                result.add(basketEventServices.createAndSaveEvent(1, basketPlayerEntry, BasketEventType.DOUBLE_DOUBLE, BasketEventLevel.MATCH, gameKey));
            }
        }
        return result;
    }
}
