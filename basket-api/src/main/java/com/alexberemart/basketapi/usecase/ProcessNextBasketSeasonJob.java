package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketSeasonJob;
import com.alexberemart.basketapi.services.BasketEventServices;
import com.alexberemart.basketapi.services.BasketSeasonJobServices;
import com.alexberemart.basketapi.services.Top20EntryServices;
import com.alexberemart.basketscraping.BasketJobClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProcessNextBasketSeasonJob {

    protected BasketSeasonJobServices basketSeasonJobServices;
    protected BasketJobClient basketJobClient;
    protected BasketEventServices basketEventServices;
    protected Top20EntryServices top20EntryServices;
    protected GetDoubleDoubleEventFromSeason getDoubleDoubleEventFromSeason;

    public BasketSeasonJob processNextBasketSeasonJob() {

        List<BasketSeasonJob> basketSeasonJobList = basketSeasonJobServices.findNonFinished();

        if (basketSeasonJobList == null) {
            return null;
        }

        if (basketSeasonJobList.size() == 0) {
            return null;
        }

        BasketSeasonJob basketSeasonJob = basketSeasonJobList.get(0);

        if (basketSeasonJob.getId() == null) {
            return null;
        }

        Boolean allOk = Boolean.FALSE;

        switch (basketSeasonJob.getState()) {
            case INIT:
                List<BasketEvent> basketEventList = createEventsFromSeason(basketSeasonJob);
                allOk = basketEventList != null;
                break;
            case EVENTS_GENERATED:
                top20EntryServices.createAll(basketSeasonJob.getWebKey());
                allOk = Boolean.TRUE;
                break;
        }

        if (allOk) {
            basketSeasonJob.setState(basketSeasonJob.getState().getNextState());
            return basketSeasonJobServices.save(basketSeasonJob);
        } else {
            return basketSeasonJob;
        }
    }

    protected List<BasketEvent> createEventsFromSeason(BasketSeasonJob basketSeasonJob) {
        Boolean existBasketJobByWebKeyLikeNonFinished = basketJobClient.existBasketJobByWebKeyLikeNonFinished(basketSeasonJob.getWebKey() + "%");

        if (existBasketJobByWebKeyLikeNonFinished) {
            return null;
        }

        return getDoubleDoubleEventFromSeason.execute(basketSeasonJob.getWebKey());
    }
}
