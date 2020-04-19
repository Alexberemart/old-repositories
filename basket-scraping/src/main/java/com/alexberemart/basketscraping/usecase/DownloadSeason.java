package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketscraping.services.BasketAPI;
import com.alexberemart.basketscraping.services.DocumentServices;
import com.alexberemart.basketscraping.services.WebScrapingServices;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.alexberemart.common_utils.MathUtils.round;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class DownloadSeason {

    protected WebScrapingServices webScrapingServices;
    protected DocumentServices documentServices;

    public List<String> execute(
            String basketJobKey,
            BasketAPI basketAPI) throws IOException {
        List<String> filePathList = new ArrayList<>();
        Document doc = webScrapingServices.getDownloadedFile(basketAPI.getSeasonUrl(basketJobKey));
        Elements elements = basketAPI.getGameList(doc);
        Integer totalElements = elements.size();
        log.info("there are " + totalElements + " elements to download");
        Integer counter = 0;
        for (Element element : elements) {
            String gameKey = basketAPI.getGameKeyFromGameList(element);
            if (!documentServices.existFile(basketJobKey, gameKey)) {
                try {
                    doc = webScrapingServices.getDownloadedFile(basketAPI.getMatchUrl(gameKey));
                    documentServices.saveFile(doc, gameKey, basketJobKey);
                    filePathList.add(gameKey);
                    log.info(basketJobKey + " element " + counter.toString() + " downloaded (" + round(((double) counter / totalElements) * 100, 1) + "%)");
                } catch (HttpStatusException e) {
                    log.info(basketJobKey + " element " + counter.toString() + " fails (" + round(((double) counter / totalElements) * 100, 1) + "%)");
                }
            } else {
                log.info(basketJobKey + " element " + counter.toString() + " already downloaded before (" + round(((double) counter / totalElements) * 100, 1) + "%)");
            }
            counter++;
        }

        return filePathList;
    }

}