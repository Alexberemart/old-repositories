package com.alexberemart.basketscraping.model;

import com.alexberemart.basketscraping.model.BasketJobState;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
public class BasketJobStateTest {

    @Test
    public void downloadBasketJob() {
        BasketJobState basketJobState = BasketJobState.INIT;
        BasketJobState basketJobStateNextState = basketJobState.getNextState();
        Assert.assertEquals(BasketJobState.DOWNLOADED, basketJobStateNextState);
    }

    @Test
    public void processBasketJob() {
        BasketJobState basketJobState = BasketJobState.DOWNLOADED;
        BasketJobState basketJobStateNextState = basketJobState.getNextState();
        Assert.assertEquals(BasketJobState.PROCESSED, basketJobStateNextState);
    }

    @Test
    public void getEventsBasketJob() {
        BasketJobState basketJobState = BasketJobState.PROCESSED;
        BasketJobState basketJobStateNextState = basketJobState.getNextState();
        Assert.assertEquals(BasketJobState.EVENTS_FINISHED, basketJobStateNextState);
    }

}
