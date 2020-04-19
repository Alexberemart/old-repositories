package usecaseit;

import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.CompetitionTest;
import com.alexberemart.basketscraping.BasketScrapingIntegrationTest;
import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.services.DocumentServices;
import com.alexberemart.basketscraping.services.FibaWorldCupAPI;
import com.alexberemart.basketscraping.usecase.DownloadSeason;
import com.alexberemart.basketscraping.usecase.ProcessNextBasketJob;
import com.alexberemart.basketscraping.usecase.ScrapeFile;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.alexberemart.basketscraping.IntegrationTestUtils.getCloudDocument;
import static com.alexberemart.basketscraping.IntegrationTestUtils.getDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FibaWorldCupAPITestCase extends BasketScrapingIntegrationTest implements CompetitionTest {

    @Autowired
    protected DownloadSeason downloadSeason;
    @Autowired
    protected ScrapeFile scrapeFile;
    @Autowired
    protected ProcessNextBasketJob processNextBasketJob;
    @Autowired
    protected FibaWorldCupAPI fibaWorldCupAPI;
    @Autowired
    protected DocumentServices documentServices;

    private final String championshipPath = "fiba_world_cup/";

    @Before
    public void setUp() {
        downloadSeason = spy(downloadSeason);
        scrapeFile = spy(scrapeFile);
    }

    @Test
    @Override
    public void downloadSeason() throws IOException {
        File input = new File("src/test/resources/" + championshipPath + "schedule.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        String basketJobKey = "6241_2014";

        doReturn(doc).when(webScrapingServices).getDownloadedFile(any(String.class));

        List<String> stringList = downloadSeason.execute(basketJobKey, fibaWorldCupAPI);
        Assert.assertEquals(stringList.size(), 76);
        Assert.assertEquals("1_B_9719_6241_2014", stringList.get(0));
    }

    @Test
    @Override
    public void scrape() throws IOException, ParseException {
        String gameKey = "archive.fiba.com_ 2014 FIBA Basketball World Cup";
        CloudDocument cloudDocument = getCloudDocument(championshipPath, gameKey);

        String playerKey = "Ante_Tomic";
        String filePlayerPath = "src/test/resources/" + championshipPath + playerKey + ".html";
        Document docPlayer = getDocument(filePlayerPath);
        documentServices.saveFile(docPlayer, "fiba_world_cup/43731");
        documentServices.saveFile(docPlayer, "fiba_world_cup/76416");
        documentServices.saveFile(docPlayer, "fiba_world_cup/63548");
        documentServices.saveFile(docPlayer, "fiba_world_cup/51445");

        Season season = new Season();

        when(webScrapingServices.getDownloadedFile(contains("player")))
                .thenReturn(docPlayer);

        when(basketPlayerClient.getByReferenceId(any(String.class)))
                .thenReturn(BasketPlayer.builder().build());

        Answer<BasketMatch> answer = invocation -> invocation.getArgument(0);

        doAnswer(answer).when(basketMatchClient).save(any(BasketMatch.class));

        DateFormat format = new SimpleDateFormat("dd MMM.yyyy");

        BasketScrapedMatch basketScrapedMatch = scrapeFile.execute(cloudDocument, season, fibaWorldCupAPI);
        Assert.assertNotNull(basketScrapedMatch.getBasketScrapedMatchStatList());
        Assert.assertTrue(basketScrapedMatch.getBasketScrapedMatchStatList().size() > 0);
        Assert.assertNotNull(basketScrapedMatch.getBasketScrapedMatchTeamScoreList());
        Assert.assertTrue(basketScrapedMatch.getBasketScrapedMatchTeamScoreList().size() > 0);
        Assert.assertEquals("Ante Tomic", basketScrapedMatch.getBasketScrapedPlayers().get(0).getPlayerName());
        Assert.assertEquals(format.parse("17 Feb.1987"), basketScrapedMatch.getBasketScrapedPlayers().get(0).getBirthDate());
        Assert.assertEquals("CRO", basketScrapedMatch.getBasketScrapedPlayers().get(0).getCountry());
        Assert.assertEquals("43731", basketScrapedMatch.getBasketScrapedPlayers().get(0).getBasketReferenceKey());
        Assert.assertEquals("217", basketScrapedMatch.getBasketScrapedPlayers().get(0).getHeight());
    }

    @Test
    @Override
    public void scrapeWhenBasketMatchHasBeenScrapedBefore() throws IOException, ParseException {
        Season season = new Season();

        List<BasketMatch> basketMatches = new ArrayList<>();
        basketMatches.add(new BasketMatch());

        when(basketMatchClient.findByGameKey(any(String.class)))
                .thenReturn(basketMatches);

        String gameKey = "201712300POR";
        CloudDocument cloudDocument = CloudDocument.builder()
                .key(gameKey + ".html")
                .build();
        scrapeFile.execute(cloudDocument, season, fibaWorldCupAPI);
    }

    @Test
    @Override
    @DatabaseSetup(value = "classpath:datasets/fiba_world_cup/basket-jobs.xml")
    public void processNextBasketJob() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        processNextBasketJobcommon("schedule.html", "1_B_9719_6241_2014");

    }

    @Test
    @DatabaseSetup(value = "classpath:datasets/fiba_world_cup/basket-jobs_2010.xml")
    public void processNextBasketJob_2010() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        processNextBasketJobcommon("2010.html", "A_80_6948_4728_2010");

    }

    protected void processNextBasketJobcommon(String fileName, String BasketMatchIdExcepted) throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        String filePath = "src/test/resources/" + championshipPath + fileName;
        Document docSchedule = getDocument(filePath);

        filePath = "src/test/resources/" + championshipPath + "archive.fiba.com_ 2014 FIBA Basketball World Cup.html";
        Document docMatch = getDocument(filePath);

        when(webScrapingServices.getDownloadedFile(contains("schedule")))
                .thenReturn(docSchedule);

        when(webScrapingServices.getDownloadedFile(contains("statistic")))
                .thenReturn(docMatch);

        when(basketPlayerClient.getByReferenceId(any(String.class)))
                .thenReturn(BasketPlayer.builder().build());

        Answer<Season> answer = invocation -> invocation.getArgument(0);
        doAnswer(answer).when(seasonClient).saveIfNotExist(any(Season.class));

        Answer<BasketMatch> matchAnswer = invocation -> invocation.getArgument(0);
        doAnswer(matchAnswer).when(basketMatchClient).save(any(BasketMatch.class));

        BasketJob basketJob = processNextBasketJob.execute();
        Assert.assertNotNull(basketJob);
        Assert.assertEquals(BasketJobState.DOWNLOADED, basketJob.getState());
        basketJob = processNextBasketJob.execute();
        Assert.assertEquals(BasketJobState.PROCESSED, basketJob.getState());
        verify(basketMatchClient).findByGameKey(BasketMatchIdExcepted);
        basketJob = processNextBasketJob.execute();
    }

}