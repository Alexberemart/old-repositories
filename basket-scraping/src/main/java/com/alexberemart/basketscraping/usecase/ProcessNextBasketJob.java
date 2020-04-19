package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.services.BasketAPI;
import com.alexberemart.basketscraping.services.BasketJobServices;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.services.FibaWorldCupAPI;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class ProcessNextBasketJob {

    protected DownloadSeason downloadSeason;
    protected BasketJobServices basketJobServices;
    protected ScrapeAllFileFromBasketJob scrapeAllFileFromBasketJob;
    protected CreateEvents createEvents;
    protected BasketReferenceAPI basketReferenceAPI;
    protected FibaWorldCupAPI fibaWorldCupAPI;

    public BasketJob execute() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {

        BasketAPI basketAPI;
        List<BasketJob> basketJobList = basketJobServices.findNonFinished();

        if (basketJobList == null) {
            return null;
        }

        if (basketJobList.size() == 0) {
            return null;
        }

        BasketJob basketJob = basketJobList.get(0);

        if (basketJob.getId() == null) {
            return null;
        }

        switch (basketJob.getOriginType()) {
            case FIBA_WORLD_CUP:
                basketAPI = fibaWorldCupAPI;
                break;
            case BASKET_REFERENCE:
            default:
                basketAPI = basketReferenceAPI;
                break;
        }

        switch (basketJob.getState()) {
            case INIT:
                downloadSeason.execute(basketJob.getWebKey(), basketAPI);
                basketJobServices.markAsDownloaded(basketJob.getWebKey());
                break;
            case DOWNLOADED:
                scrapeAllFileFromBasketJob.execute(basketAPI, basketJob.getWebKey());
                break;
            case PROCESSED:
                createEvents.execute(basketJob.getWebKey());
                break;
        }

        basketJob.setState(basketJob.getState().getNextState());
        return basketJobServices.save(basketJob);
    }

}