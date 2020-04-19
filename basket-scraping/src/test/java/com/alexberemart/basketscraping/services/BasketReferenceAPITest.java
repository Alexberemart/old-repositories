package com.alexberemart.basketscraping.services;

import com.alexberemart.basketapi.dto.BasketTeamSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.IntegrationTestUtils;
import com.alexberemart.basketscraping.UnitTest;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.BasketScrapedMatchStat;
import com.alexberemart.basketscraping.model.BasketScrapedMatchTeamScore;
import com.alexberemart.basketscraping.model.BasketScrapedPlayer;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.services.DocumentServices;
import com.alexberemart.basketscraping.services.WebScrapingServices;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BasketReferenceAPITest extends UnitTest {

    @Mock
    protected WebScrapingServices webScrapingServices;
    @Mock
    protected DocumentServices documentServices;
    protected BasketReferenceAPI basketReferenceAPI;

    final String championshipPath = "basket_reference/";

    @Before
    public void setUp() {
        basketReferenceAPI = new BasketReferenceAPI(
                webScrapingServices,
                documentServices);
        basketReferenceAPI = spy(basketReferenceAPI);
    }

    @Test
    public void getSeasonUrl() {
        String seasonUrl = basketReferenceAPI.getSeasonUrl("key");
        Assert.assertNotNull(seasonUrl);
    }

    @Test
    public void getMatchUrl() {
        String matchUrl = basketReferenceAPI.getMatchUrl("key");
        Assert.assertNotNull(matchUrl);
    }

    @Test
    public void createBasketReferenceMatchStats() throws IOException {
        List<BasketScrapedMatchStat> basketScrapedMatchStatList = basketReferenceAPI.createBasketReferenceMatchStats(Jsoup.parse(getDoc("src/test/resources/basket_reference/201711290DAL.html")));
        Assert.assertNotNull(basketScrapedMatchStatList.get(0).getBasketReferenceKey());
        Assert.assertNotNull(basketScrapedMatchStatList);
        Assert.assertEquals(26, basketScrapedMatchStatList.size());
        Assert.assertEquals("carrode01", basketScrapedMatchStatList.get(0).getBasketReferenceKey());
        Assert.assertEquals("33:54", basketScrapedMatchStatList.get(0).getMinutes());
        Assert.assertEquals(BasketMatchTeamType.AWAY.getCode(), basketScrapedMatchStatList.get(0).getType());
        Assert.assertEquals(BasketMatchTeamType.HOME.getCode(), basketScrapedMatchStatList.get(basketScrapedMatchStatList.size() - 1).getType());
    }

    @Test
    public void createBasketReferenceMatchStatsWithEmptyCells() throws IOException {
        List<BasketScrapedMatchStat> basketScrapedMatchStatList = basketReferenceAPI.createBasketReferenceMatchStats(Jsoup.parse(getDocWithEmptyCells()));
        Assert.assertNotNull(basketScrapedMatchStatList.get(10).getBasketReferenceKey());
        Assert.assertNotNull(basketScrapedMatchStatList);
        Assert.assertEquals(26, basketScrapedMatchStatList.size());
        Assert.assertEquals("bogutan01", basketScrapedMatchStatList.get(10).getBasketReferenceKey());
        Assert.assertEquals(0, basketScrapedMatchStatList.get(10).getFieldGoal3Points(), 0);
        Assert.assertEquals(BasketMatchTeamType.AWAY.getCode(), basketScrapedMatchStatList.get(0).getType());
        Assert.assertEquals(BasketMatchTeamType.HOME.getCode(), basketScrapedMatchStatList.get(basketScrapedMatchStatList.size() - 1).getType());
    }

    protected String getDoc(String filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    protected String getDocWithEmptyCells() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/basket_reference/201712120NYK.html"));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    @Test
    public void getPlayer() throws IOException, ParseException {

        String playerKey = "playerKey";
        String playerUrl = "/a/b/" + playerKey + ".html";
        CloudDocument cloudDocument = IntegrationTestUtils.getCloudDocument(championshipPath, "brookmi01");
        Document doc = IntegrationTestUtils.getDocument("src/test/resources/basket_reference/brookmi01.html");

        when(documentServices.getFile(playerUrl))
                .thenReturn(cloudDocument);

        when(documentServices.parseFile(cloudDocument.getDocument()))
                .thenReturn(doc);

        BasketScrapedPlayer basketScrapedPlayer = basketReferenceAPI.getPlayer(playerUrl);
        Assert.assertEquals(playerKey, basketScrapedPlayer.getBasketReferenceKey());
        Assert.assertEquals("Small Forward", basketScrapedPlayer.getPosition());
        Assert.assertEquals("201", basketScrapedPlayer.getHeight());
        Assert.assertEquals("99", basketScrapedPlayer.getWeight());
        Assert.assertEquals("US", basketScrapedPlayer.getCountry());

    }

    @Test
    public void getPlayerFileDoesntExist() throws IOException, ParseException {

        String playerKey = "playerKeyDoesntExist";
        String playerUrl = "/a/b/" + playerKey + ".html";

        when(documentServices.getFile(playerUrl))
                .thenThrow(new NoSuchFileException("a"));

        when(webScrapingServices.getDownloadedFile("http://www.basketball-reference.com" + playerUrl))
                .thenReturn(Jsoup.parse(getDoc("src/test/resources/basket_reference/brookmi01.html")));

        BasketScrapedPlayer basketScrapedPlayer = basketReferenceAPI.getPlayer(playerUrl);
        Assert.assertEquals(playerKey, basketScrapedPlayer.getBasketReferenceKey());
        Assert.assertEquals("Small Forward", basketScrapedPlayer.getPosition());
        Assert.assertEquals("201", basketScrapedPlayer.getHeight());
        Assert.assertEquals("99", basketScrapedPlayer.getWeight());
        Assert.assertEquals("US", basketScrapedPlayer.getCountry());
    }

    @Test
    public void getPlayerDoesntHaveCountry() throws IOException, ParseException {

        String playerKey = "playerKey";
        String playerUrl = "/a/b/" + playerKey + ".html";
        CloudDocument cloudDocument = IntegrationTestUtils.getCloudDocument(championshipPath, "brookmi01");
        Document doc = IntegrationTestUtils.getDocument("src/test/resources/basket_reference/brookmi01.html");

        when(documentServices.getFile(playerUrl))
                .thenReturn(cloudDocument);

        when(documentServices.parseFile(cloudDocument.getDocument()))
                .thenReturn(doc);

        doThrow(new ArrayIndexOutOfBoundsException("a"))
                .when(basketReferenceAPI).getCountry(any(Document.class));

        BasketScrapedPlayer basketScrapedPlayer = basketReferenceAPI.getPlayer(playerUrl);
        Assert.assertEquals(playerKey, basketScrapedPlayer.getBasketReferenceKey());
        Assert.assertEquals("Small Forward", basketScrapedPlayer.getPosition());
        Assert.assertEquals("201", basketScrapedPlayer.getHeight());
        Assert.assertEquals("99", basketScrapedPlayer.getWeight());

    }

    @Test
    public void getCountry() throws IOException {
        String country = basketReferenceAPI.getCountry(Jsoup.parse(getDoc("src/test/resources/basket_reference/brookmi01.html")));
        Assert.assertEquals("US", country);
    }

    @Test
    public void createBasketReferenceMatchTeamScoreList() throws IOException {
        String homeTeamReferenceKey = "DEN";
        String homeTeamName = "Denver Nuggets";

        String awayTeamReferenceKey = "DAL";
        String awayTeamName = "Dallas Mavericks";

        List<BasketScrapedMatchTeamScore> basketScrapedMatchTeamScoreList = basketReferenceAPI.createScrapedMatchTeamScoreList(getDoc(), "gameKey");
        Assert.assertEquals(2, basketScrapedMatchTeamScoreList.size());
        Assert.assertEquals(awayTeamReferenceKey, basketScrapedMatchTeamScoreList.get(0).getBasketTeamWebKey());
        Assert.assertEquals(awayTeamName, basketScrapedMatchTeamScoreList.get(0).getBasketTeamName());
        Assert.assertEquals(92, basketScrapedMatchTeamScoreList.get(0).getScore(), 0);
        Assert.assertEquals(BasketMatchTeamType.AWAY, basketScrapedMatchTeamScoreList.get(0).getBasketMatchTeamType());
        Assert.assertEquals(homeTeamReferenceKey, basketScrapedMatchTeamScoreList.get(1).getBasketTeamWebKey());
        Assert.assertEquals(homeTeamName, basketScrapedMatchTeamScoreList.get(1).getBasketTeamName());
        Assert.assertEquals(91, basketScrapedMatchTeamScoreList.get(1).getScore(), 0);
        Assert.assertEquals(BasketMatchTeamType.HOME, basketScrapedMatchTeamScoreList.get(1).getBasketMatchTeamType());
    }

    protected Document getDoc() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/basket_reference/199611010DEN.html"));
        return Jsoup.parse(new String(encoded, StandardCharsets.UTF_8));
    }

    @Test
    public void createBasketReferenceMatch() throws IOException, ParseException {
        String gameKey = "199611010DEN";
        int numberOfPlayers = 1;

        Season season = Season.builder()
                .webKey("1996")
                .build();

        CloudDocument cloudDocument = CloudDocument.builder()
                .key("documentKey")
                .document(getDoc("src/test/resources/basket_reference/199611010DEN.html"))
                .build();

        Document doc = Jsoup.parse(cloudDocument.getDocument());

        BasketScrapedMatchStat basketScrapedMatchStat = BasketScrapedMatchStat.builder()
                .build();

        BasketScrapedMatchTeamScore homeBasketScrapedMatchTeamScore = BasketScrapedMatchTeamScore.builder()
                .build();

        BasketScrapedMatchTeamScore awayBasketScrapedMatchTeamScore = BasketScrapedMatchTeamScore.builder()
                .build();

        doReturn(Arrays.asList(basketScrapedMatchStat))
                .when(basketReferenceAPI).createBasketReferenceMatchStats(any());

        doReturn(Arrays.asList(homeBasketScrapedMatchTeamScore, awayBasketScrapedMatchTeamScore))
                .when(basketReferenceAPI).createScrapedMatchTeamScoreList(any(), any());

        when(documentServices.parseFile(any(String.class)))
                .thenReturn(doc);

        BasketScrapedMatch basketScrapedMatch = basketReferenceAPI.createMatch(gameKey, season, cloudDocument);

        verify(basketReferenceAPI).createScrapedMatchTeamScoreList(any(), any());
        verify(basketReferenceAPI, times(1)).createBasketReferenceMatchStats(any(Document.class));

        Assert.assertEquals(2, basketScrapedMatch.getBasketScrapedMatchTeamScoreList().size());
        Assert.assertEquals(numberOfPlayers, basketScrapedMatch.getBasketScrapedMatchStatList().size());
    }

}