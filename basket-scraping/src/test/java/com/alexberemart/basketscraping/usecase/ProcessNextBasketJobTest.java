package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketscraping.UnitTest;
import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.OriginType;
import com.alexberemart.basketscraping.services.BasketJobServices;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.services.FibaWorldCupAPI;
import com.alexberemart.basketscraping.usecase.CreateEvents;
import com.alexberemart.basketscraping.usecase.DownloadSeason;
import com.alexberemart.basketscraping.usecase.ProcessNextBasketJob;
import com.alexberemart.basketscraping.usecase.ScrapeAllFileFromBasketJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProcessNextBasketJobTest extends UnitTest {

    @Mock
    protected DownloadSeason downloadSeason;
    @Mock
    protected BasketJobServices basketJobServices;
    @Mock
    protected ScrapeAllFileFromBasketJob scrapeAllFileFromBasketJob;
    @Mock
    protected CreateEvents createEvents;
    @Mock
    protected BasketReferenceAPI basketReferenceAPI;
    @Mock
    protected FibaWorldCupAPI fibaWorldCupAPI;
    protected ProcessNextBasketJob processNextBasketJob;

    @Before
    public void setUp() {
        processNextBasketJob = new ProcessNextBasketJob(
                downloadSeason,
                basketJobServices,
                scrapeAllFileFromBasketJob,
                createEvents,
                basketReferenceAPI,
                fibaWorldCupAPI);
        processNextBasketJob = spy(processNextBasketJob);
    }

    @Test
    public void downloadNext() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {

        String webKey = "NBA";
        String id = "id";
        String webKey1 = "NBA1";
        String id1 = "id1";

        BasketJob basketJob = BasketJob.builder()
                .id(id)
                .webKey(webKey)
                .state(BasketJobState.INIT)
                .originType(OriginType.BASKET_REFERENCE)
                .build();

        BasketJob basketJob1 = BasketJob.builder()
                .id(id1)
                .webKey(webKey1)
                .state(BasketJobState.DOWNLOADED)
                .originType(OriginType.BASKET_REFERENCE)
                .build();

        doReturn(null).when(downloadSeason).execute(any(String.class), any());
        doReturn(null).when(scrapeAllFileFromBasketJob).execute(any(), any(String.class));
        doReturn(null).when(createEvents).execute(any(String.class));

        Answer<BasketJob> answer = new Answer<BasketJob>() {
            public BasketJob answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        };

        doAnswer(answer).when(basketJobServices).save(any(BasketJob.class));

        Answer<List<BasketJob>> otherAnswer = new Answer<List<BasketJob>>() {
            public List<BasketJob> answer(InvocationOnMock invocation) throws Throwable {
                List<BasketJob> result = new ArrayList<>();
                if (basketJob.getState() != BasketJobState.EVENTS_FINISHED) {
                    result.add(basketJob);
                }
                if (basketJob1.getState() != BasketJobState.EVENTS_FINISHED) {
                    result.add(basketJob1);
                }
                return result;
            }
        };

        doAnswer(otherAnswer).when(basketJobServices).findNonFinished();

        BasketJob job = processNextBasketJob.execute();
        Assert.assertEquals(id, job.getId());
        Assert.assertEquals(webKey, job.getWebKey());
        Assert.assertEquals(BasketJobState.DOWNLOADED, job.getState());
        verify(downloadSeason).execute(webKey, basketReferenceAPI);

        job = processNextBasketJob.execute();
        Assert.assertEquals(id, job.getId());
        Assert.assertEquals(webKey, job.getWebKey());
        Assert.assertEquals(BasketJobState.PROCESSED, job.getState());
        verify(scrapeAllFileFromBasketJob).execute(basketReferenceAPI, webKey);

        job = processNextBasketJob.execute();
        Assert.assertEquals(id, job.getId());
        Assert.assertEquals(webKey, job.getWebKey());
        Assert.assertEquals(BasketJobState.EVENTS_FINISHED, job.getState());
        verify(createEvents).execute(webKey);

        job = processNextBasketJob.execute();
        Assert.assertEquals(id1, job.getId());
        Assert.assertEquals(webKey1, job.getWebKey());
        Assert.assertEquals(BasketJobState.PROCESSED, job.getState());
        verify(scrapeAllFileFromBasketJob).execute(basketReferenceAPI, webKey1);
    }

    @Test
    public void downloadNextReturnNullBecausefindNonFinishedReturnNull() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        when(basketJobServices.findNonFinished())
                .thenReturn(null);
        processNextBasketJob.execute();
    }

    @Test
    public void downloadNextReturnNullBecauseThereIsntJobsToProcess() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        when(basketJobServices.findNonFinished())
                .thenReturn(new ArrayList<>());
        processNextBasketJob.execute();
    }

    @Test
    public void downloadNextReturnNullBecauseJobReturnedHasNullId() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        when(basketJobServices.findNonFinished())
                .thenReturn(Arrays.asList(BasketJob.builder().build()));
        processNextBasketJob.execute();
    }

}