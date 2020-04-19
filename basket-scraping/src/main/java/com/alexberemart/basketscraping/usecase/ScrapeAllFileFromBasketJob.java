package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketapi.NotificationClient;
import com.alexberemart.basketapi.SeasonClient;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.repositories.CloudRepository;
import com.alexberemart.basketscraping.services.BasketAPI;
import com.alexberemart.basketscraping.services.BasketJobServices;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.alexberemart.common_utils.MathUtils.round;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class ScrapeAllFileFromBasketJob {

    private BasketJobServices basketJobServices;
    protected CloudRepository cloudRepository;
    private ScrapeFile scrapeFile;
    protected NotificationClient notificationClient;
    protected SeasonClient seasonClient;

    @Transactional
    public List<BasketScrapedMatch> execute(BasketAPI basketAPI, String basketJobWebKey) throws IllegalSeasonException, IOException, ParseException {

        BasketJob basketJob = basketJobServices.findOneByWebKey(basketJobWebKey);
        if (basketJob == null) {
            throw new IllegalSeasonException();
        }

        if (!BasketJobState.DOWNLOADED.equals(basketJob.getState())) {
            throw new IllegalSeasonException();
        }

        List<BasketScrapedMatch> result = new ArrayList<>();
        List<CloudDocument> files = cloudRepository.getFilesFromFolder(basketJobWebKey);

        Season season = createAndSaveSeason(basketAPI, basketJobWebKey);

        Integer totalElements = files.size();
        log.info("there are " + totalElements + " elements to scrape");
        Integer counter = 0;
        Integer numberOfNewElements = 0;

        for (CloudDocument cloudDocument : files) {
            BasketScrapedMatch basketScrapedMatch = scrapeFile.execute(cloudDocument, season, basketAPI);
            if (basketScrapedMatch == null) {
                log.info(basketJobWebKey + " element " + counter.toString() + " NOT scrapped, because it already has been scraped before (" + round(((double) counter / totalElements) * 100, 1) + "%)");
            } else {
                result.add(basketScrapedMatch);
                log.info(basketJobWebKey + " element " + counter.toString() + " scrapped (" + round(((double) counter / totalElements) * 100, 1) + "%)");
                numberOfNewElements++;
            }
            counter++;
        }

        basketJob.setState(basketJob.getState().getNextState());
        basketJobServices.save(basketJob);

        if (numberOfNewElements > 0 && (basketJob.getRestartJob() == Boolean.TRUE)) {
            notificationClient.SendRecapNotificationOfLastDay();
        }

        return result;
    }

    protected Season createAndSaveSeason(BasketAPI basketAPI, String basketJobWebKey) throws ParseException {
        Season season = new Season();
        season.setStartDate(basketAPI.getSeasonStartDate(basketJobWebKey));
        season.setWebKey(basketAPI.getSeasonWebKey(basketJobWebKey));
        return seasonClient.saveIfNotExist(season);
    }

}