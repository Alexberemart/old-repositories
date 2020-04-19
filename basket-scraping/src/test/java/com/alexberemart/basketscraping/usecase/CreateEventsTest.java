package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketapi.BasketEventClient;
import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketEventLevel;
import com.alexberemart.basketscraping.UnitTest;
import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.repositories.CloudRepository;
import com.alexberemart.basketscraping.services.BasketJobServices;
import com.alexberemart.basketscraping.usecase.CreateEvents;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class CreateEventsTest extends UnitTest {

    @Mock
    protected BasketJobServices basketJobServices;
    @Mock
    protected CloudRepository cloudRepository;
    @Mock
    protected BasketEventClient basketEventClient;
    protected CreateEvents createEvents;

    @Before
    public void setUp() {
        createEvents = new CreateEvents(
                basketJobServices,
                cloudRepository,
                basketEventClient);
        createEvents = spy(createEvents);
    }

    @Test
    public void createEvents() {
        CloudDocument cloudDocument = CloudDocument.builder()
                .key("key")
                .build();
        List<BasketEvent> basketEvents = createEvents.createEvents(cloudDocument);
        Assert.assertNotNull(basketEvents);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEventsThrowAErrorBecauseCloudDocumentDoesntHaveAWebKey() {
        CloudDocument cloudDocument = new CloudDocument();
        createEvents.createEvents(cloudDocument);
    }

    @Test
    public void createEventsMustReturnNullBecauseThereAreEventsForActualWebKey() {
        String webKey = "key";
        CloudDocument cloudDocument = CloudDocument.builder()
                .key(webKey)
                .build();

        when(basketEventClient.findByWebKeyAndLevel(webKey, BasketEventLevel.MATCH.getCode()))
                .thenReturn(Arrays.asList(BasketEvent.builder().build()));

        List<BasketEvent> basketEvents = createEvents.createEvents(cloudDocument);
        Assert.assertNull(basketEvents);
    }

    @Test
    public void createEventsFromWebKey() throws IOException, IllegalBasketJobException {
        String webKey = "webKey";
        BasketJob basketJob = BasketJob.builder()
                .state(BasketJobState.PROCESSED)
                .build();
        CloudDocument cloudDocument = new CloudDocument();
        CloudDocument otherCloudDocument = new CloudDocument();
        BasketEvent basketEvent = BasketEvent.builder().build();

        when(cloudRepository.getFilesFromFolder(webKey))
                .thenReturn(Arrays.asList(
                        cloudDocument,
                        otherCloudDocument));

        when(basketJobServices.findOneByWebKey(webKey))
                .thenReturn(basketJob);

        doReturn(Arrays.asList(basketEvent)).when(createEvents).createEvents(cloudDocument);
        doReturn(null).when(createEvents).createEvents(otherCloudDocument);

        List<BasketEvent> basketEvents = createEvents.execute("webKey");
        Assert.assertEquals(1, basketEvents.size());
    }

    @Test(expected = IllegalBasketJobException.class)
    public void createEventsFromWebKeyThrowAErrorBecauseDownNtExistsBasketJob() throws IOException, IllegalBasketJobException {
        String webKey = "webKey";

        when(basketJobServices.findOneByWebKey(webKey))
                .thenReturn(null);

        createEvents.execute(webKey);
    }

    @Test(expected = IllegalBasketJobException.class)
    public void createEventsFromWebKeyThrowAErrorBecauseBasketJobReturnedIsNotAtProcessedState() throws IOException, IllegalBasketJobException {
        String webKey = "webKey";

        when(basketJobServices.findOneByWebKey(webKey))
                .thenReturn(BasketJob.builder().build());

        createEvents.execute("webKey");
    }

}