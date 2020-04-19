package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketEventLevel;
import com.alexberemart.basketapi.model.BasketEventType;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.services.BasketEventServices;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "test")
public class CreateEventsFromMatchIdTest {

    @Mock
    protected BasketEventServices basketEventServices;
    @Mock
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    protected CreateEventsFromMatchId createEventsFromMatchId;

    @Before
    public void setUp() {
        createEventsFromMatchId = new CreateEventsFromMatchId(
                basketEventServices,
                basketPlayerEntryServices);
    }

    @Test
    public void execute() {
        String gameKey = "gameKey";
        BasketEventType basketEventType = BasketEventType.DOUBLE_DOUBLE;

        BasketPlayerEntry basketPlayerEntry = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .build();

        BasketEvent basketEvent = BasketEvent.builder()
                .type(basketEventType)
                .webKey(gameKey)
                .build();

        when(basketPlayerEntryServices.findByBasketMatch(gameKey))
                .thenReturn(Arrays.asList(basketPlayerEntry));

        doReturn(basketEvent).when(basketEventServices).createAndSaveEvent(1, basketPlayerEntry, basketEventType, BasketEventLevel.MATCH, gameKey);

        List<BasketEvent> basketEvents = createEventsFromMatchId.execute(gameKey);
        Assert.assertEquals(1, basketEvents.size());
        Assert.assertEquals(basketEventType, basketEvents.get(0).getType());
        Assert.assertEquals(gameKey, basketEvents.get(0).getWebKey());
    }

}
