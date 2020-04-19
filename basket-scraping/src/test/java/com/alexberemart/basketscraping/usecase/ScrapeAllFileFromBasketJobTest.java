package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketapi.NotificationClient;
import com.alexberemart.basketapi.SeasonClient;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.UnitTest;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.repositories.CloudRepository;
import com.alexberemart.basketscraping.services.BasketJobServices;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.usecase.ScrapeAllFileFromBasketJob;
import com.alexberemart.basketscraping.usecase.ScrapeFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.*;

public class ScrapeAllFileFromBasketJobTest extends UnitTest {

    @Mock
    private BasketJobServices basketJobServices;
    @Mock
    protected CloudRepository cloudRepository;
    @Mock
    private ScrapeFile scrapeFile;
    @Mock
    protected NotificationClient notificationClient;
    @Mock
    protected SeasonClient seasonClient;
    @Mock
    BasketReferenceAPI basketReferenceAPI;
    protected ScrapeAllFileFromBasketJob scrapeAllFileFromBasketJob;

    @Before
    public void setUp() {
        scrapeAllFileFromBasketJob = new ScrapeAllFileFromBasketJob(
                basketJobServices,
                cloudRepository,
                scrapeFile,
                notificationClient,
                seasonClient);
        scrapeAllFileFromBasketJob = spy(scrapeAllFileFromBasketJob);
    }

    @Test(expected = IllegalSeasonException.class)
    public void processSeasonShouldThrowAErrorBecauseSeasonDoesntHaveABasketJob() throws IOException, ParseException, IllegalSeasonException {
        scrapeAllFileFromBasketJob.execute(basketReferenceAPI, "NBA");
    }

    @Test(expected = IllegalSeasonException.class)
    public void processSeasonShouldThrowAErrorBecauseSeasonDoesntHasBeenDownloaded() throws IOException, ParseException, IllegalSeasonException {

        BasketJob basketJob = BasketJob.builder()
                .id("NBA")
                .build();

        scrapeAllFileFromBasketJob.execute(basketReferenceAPI, "NBA");
    }

    @Test
    public void createAndSaveSeason() throws ParseException {

        String seasonWebKey = "NBA_2017";
        String basketJobWebKey = seasonWebKey + "_games-october";

        String startDate = "20171001";
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date SeasonStartDate = format.parse(startDate);

        Answer<Season> answer = invocation -> invocation.getArgument(0);

        doAnswer(answer).when(seasonClient).saveIfNotExist(any(Season.class));

        when(basketReferenceAPI.getSeasonStartDate(basketJobWebKey))
                .thenReturn(SeasonStartDate);

        when(basketReferenceAPI.getSeasonWebKey(basketJobWebKey))
                .thenReturn(seasonWebKey);

        Season season = scrapeAllFileFromBasketJob.createAndSaveSeason(basketReferenceAPI, basketJobWebKey);
        Assert.assertEquals("NBA_2017", season.getWebKey());
        verify(seasonClient).saveIfNotExist(season);
    }

}