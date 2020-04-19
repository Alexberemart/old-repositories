package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketapi.BasketEventClient;
import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketEventLevel;
import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.repositories.CloudRepository;
import com.alexberemart.basketscraping.services.BasketJobServices;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CreateEvents {

    protected BasketJobServices basketJobServices;
    protected CloudRepository cloudRepository;
    protected BasketEventClient basketEventClient;

    @Transactional
    public List<BasketEvent> execute(String key) throws IOException, IllegalBasketJobException {

        BasketJob basketJob = basketJobServices.findOneByWebKey(key);
        if (basketJob == null) {
            throw new IllegalBasketJobException();
        }

        if (!BasketJobState.PROCESSED.equals(basketJob.getState())) {
            throw new IllegalBasketJobException();
        }

        //TODO: We Should work over matches created, not still over files!!!
        List<BasketEvent> result = new ArrayList<>();
        List<CloudDocument> files = cloudRepository.getFilesFromFolder(key);

        Integer totalElements = files.size();
        log.info("there are " + totalElements + " elements to create events");
        Integer counter = 0;

        for (CloudDocument cloudDocument : files) {
            List<BasketEvent> basketEvents = createEvents(cloudDocument);
            if (basketEvents == null) {
                log.info(key + " element " + counter.toString() + " NOT create events, because it already has been create events before (" + round(((double) counter / totalElements) * 100, 1) + "%)");
            } else {
                result.addAll(basketEvents);
                log.info(key + " element " + counter.toString() + " create events (" + round(((double) counter / totalElements) * 100, 1) + "%)");
            }
            counter++;
        }

        basketJob.setState(basketJob.getState().getNextState());
        basketJobServices.save(basketJob);

        return result;
    }

    protected List<BasketEvent> createEvents(CloudDocument cloudDocument) {
        if (cloudDocument.getKey() == null) {
            throw new IllegalArgumentException("cloud document must have a webKey");
        }

        String gameKey = cloudDocument.getKey().split("\\.")[0];
        List<BasketEvent> basketEvents = basketEventClient.findByWebKeyAndLevel(gameKey, BasketEventLevel.MATCH.getCode());
        if (basketEvents.size() != 0) {
            return null;
        }
        return basketEventClient.createDoubleDoubleEventFromMatch(gameKey);
    }

}