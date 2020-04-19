package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketapi.*;
import com.alexberemart.basketapi.dto.BasketPlayerEntrySaveDto;
import com.alexberemart.basketapi.dto.BasketPlayerSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.UnitTest;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.BasketScrapedMatchStat;
import com.alexberemart.basketscraping.model.BasketScrapedPlayer;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.usecase.ScrapeFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScrapeFileTest extends UnitTest {

    @Mock
    protected BasketMatchClient basketMatchClient;
    @Mock
    protected BasketOriginClient basketOriginClient;
    @Mock
    protected BasketPlayerClient basketPlayerClient;
    @Mock
    protected BasketPlayerEntryClient basketPlayerEntryClient;
    @Mock
    protected BasketTeamEntryClient basketTeamEntryClient;
    @Mock
    protected BasketTeamClient basketTeamClient;
    @Mock
    protected BasketReferenceAPI basketReferenceAPI;
    protected ScrapeFile scrapeFile;

    @Before
    public void setUp() {
        scrapeFile = new ScrapeFile(
                basketMatchClient,
                basketOriginClient,
                basketPlayerClient,
                basketPlayerEntryClient,
                basketTeamClient,
                basketTeamEntryClient);
        scrapeFile = spy(scrapeFile);
    }

    @Test
    public void savePlayers() throws IOException, ParseException {

        String playerKey1 = "key_1";
        String playerKey2 = "key_2";

        BasketScrapedPlayer basketScrapedPlayer1 = BasketScrapedPlayer.builder()
                .basketReferenceKey(playerKey1)
                .build();

        BasketScrapedPlayer basketScrapedPlayer2 = BasketScrapedPlayer.builder()
                .basketReferenceKey(playerKey2)
                .build();

        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder()
                .basketScrapedPlayers(Arrays.asList(basketScrapedPlayer1, basketScrapedPlayer2))
                .build();

        when(basketPlayerClient.saveIfNotExist(any(BasketPlayerSaveIfNotExistDto.class)))
                .thenReturn("");

        List<String> basketPlayersSaved = scrapeFile.savePlayers(basketReferenceAPI, basketScrapedMatch.getBasketScrapedPlayers());
        Assert.assertEquals(2, basketPlayersSaved.size());
    }

    @Test
    public void savePlayerEntries() {

        BasketScrapedMatchStat firstBasketScrapedMatchStat = BasketScrapedMatchStat.builder()
                .basketReferenceKey("key_1")
                .build();

        BasketScrapedMatchStat secondBasketScrapedMatchStat = BasketScrapedMatchStat.builder()
                .basketReferenceKey("key_2")
                .build();

        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder()
                .basketScrapedMatchStatList(Arrays.asList(firstBasketScrapedMatchStat,
                        secondBasketScrapedMatchStat))
                .build();

        when(basketPlayerClient.getByReferenceId(any(String.class)))
                .thenReturn(new BasketPlayer());

        when(basketPlayerEntryClient.save(any(BasketPlayerEntrySaveDto.class)))
                .thenReturn("");

        List<String> basketPlayerEntryList = scrapeFile.savePlayerEntries(basketScrapedMatch, new BasketMatch());
        Assert.assertEquals(2, basketPlayerEntryList.size());
        Assert.assertNotNull(basketPlayerEntryList.get(0));
    }

    @Test
    public void saveMatch() {

        Season season = new Season();

        String gameKey = "gameKey";
        BasketScrapedMatch firstBasketScrapedMatch = BasketScrapedMatch.builder()
                .gameKey(gameKey)
                .build();

        when(basketMatchClient.save(any(BasketMatch.class)))
                .thenReturn(BasketMatch.builder()
                        .gameKey(gameKey)
                        .build());

        BasketMatch basketMatch = scrapeFile.saveMatch(firstBasketScrapedMatch, season);
        Assert.assertEquals(gameKey, basketMatch.getGameKey());
    }

    @Test
    public void scrape() throws IOException, ParseException {
        Season season = new Season();
        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder().build();

        when(basketReferenceAPI.createMatch(any(String.class), any(Season.class), any()))
                .thenReturn(basketScrapedMatch);

        doNothing().when(scrapeFile).saveEntities(any(), any(), any());

        String gameKey = "201711300POR";
        CloudDocument cloudDocument = CloudDocument.builder()
                .key(gameKey + ".html")
                .build();
        scrapeFile.execute(cloudDocument, season, basketReferenceAPI);
    }

    @Test
    public void scrapeWithEmptyCells() throws IOException, ParseException {
        Season season = new Season();
        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder().build();

        when(basketReferenceAPI.createMatch(any(String.class), any(Season.class), any()))
                .thenReturn(basketScrapedMatch);

        doNothing().when(scrapeFile).saveEntities(any(), any(), any());

        String gameKey = "201712120NYK";
        CloudDocument cloudDocument = CloudDocument.builder()
                .key(gameKey + ".html")
                .build();
        scrapeFile.execute(cloudDocument, season, basketReferenceAPI);
        verify(scrapeFile).saveEntities(basketReferenceAPI,season, basketScrapedMatch);
    }

    @Test
    public void scrapeWhenBasketMatchHasBeenScrapedBefore() throws IOException, ParseException {
        Season season = new Season();
        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder().build();

        List<BasketMatch> basketMatches = new ArrayList<>();
        basketMatches.add(new BasketMatch());

        when(basketMatchClient.findByGameKey(any(String.class)))
                .thenReturn(basketMatches);

        String gameKey = "201712300POR";
        CloudDocument cloudDocument = CloudDocument.builder()
                .key(gameKey + ".html")
                .build();
        scrapeFile.execute(cloudDocument, season, basketReferenceAPI);
        verify(scrapeFile, times(0)).saveEntities(basketReferenceAPI,season, basketScrapedMatch);
    }

}