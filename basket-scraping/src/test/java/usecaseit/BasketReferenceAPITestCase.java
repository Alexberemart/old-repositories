package usecaseit;

import com.alexberemart.basketapi.dto.BasketTeamSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.CompetitionTest;
import com.alexberemart.basketscraping.BasketScrapingIntegrationTest;
import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.services.DocumentServices;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.usecase.DownloadSeason;
import com.alexberemart.basketscraping.usecase.ProcessNextBasketJob;
import com.alexberemart.basketscraping.usecase.ScrapeFile;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;

import static com.alexberemart.basketscraping.IntegrationTestUtils.getCloudDocument;
import static com.alexberemart.basketscraping.IntegrationTestUtils.getDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BasketReferenceAPITestCase extends BasketScrapingIntegrationTest implements CompetitionTest {

    @Autowired
    protected DownloadSeason downloadSeason;
    @Autowired
    protected ScrapeFile scrapeFile;
    @Autowired
    protected BasketReferenceAPI basketReferenceAPI;
    @Autowired
    protected DocumentServices documentServices;
    @Autowired
    protected ProcessNextBasketJob processNextBasketJob;

    final String championshipPath = "basket_reference/";

    @Before
    public void setUp() {
        downloadSeason = spy(downloadSeason);
        scrapeFile = spy(scrapeFile);
    }

    @Test
    public void downloadSeason() throws IOException {
        Document doc = getDocument("src/test/resources/" + championshipPath + "schedule.html");
        String seasonKey = "2014";

        doReturn(doc).when(webScrapingServices).getDownloadedFile(any(String.class));

        Assert.assertEquals(downloadSeason.execute(seasonKey, basketReferenceAPI).size(), 2);
    }

    @Test
    public void scrape() throws IOException, ParseException {
        String gameKey = "199611010DEN";
        CloudDocument cloudDocument = getCloudDocument(championshipPath, gameKey);

        String playerKey = "brookmi01";
        String filePlayerPath = "src/test/resources/" + championshipPath + playerKey + ".html";
        Document docPlayer = getDocument(filePlayerPath);
        documentServices.saveFile(docPlayer, "brookmi01");

        Season season = new Season();

        when(webScrapingServices.getDownloadedFile(contains("player")))
                .thenReturn(docPlayer);

        when(basketPlayerClient.getByReferenceId(any(String.class)))
                .thenReturn(BasketPlayer.builder().build());

        when(basketTeamClient.saveIfNotExist(any(BasketTeamSaveIfNotExistDto.class)))
                .thenAnswer(
                        (Answer<BasketTeam>) invocation -> {
                            BasketTeamSaveIfNotExistDto basketTeamSaveIfNotExistDto = invocation.getArgument(0);
                            return BasketTeam.builder()
                                    .referenceId(basketTeamSaveIfNotExistDto.getTeamKey())
                                    .name(basketTeamSaveIfNotExistDto.getTeamName())
                                    .build();
                        });

        Answer<BasketMatch> answer = invocation -> invocation.getArgument(0);
        doAnswer(answer).when(basketMatchClient).save(any(BasketMatch.class));

        scrapeFile.execute(cloudDocument, season, basketReferenceAPI);
    }

    @Override
    @Test
    public void scrapeWhenBasketMatchHasBeenScrapedBefore() throws IOException, ParseException {

    }

    @Override
    @Test
    @DatabaseSetup(value = "classpath:datasets/basket_reference/basket-jobs.xml")
    public void processNextBasketJob() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        String filePath = "src/test/resources/" + championshipPath + "schedule.html";
        Document docSchedule = getDocument(filePath);

        filePath = "src/test/resources/" + championshipPath + "199611010DEN.html";
        Document docMatch = getDocument(filePath);

        when(webScrapingServices.getDownloadedFile("http://www.basketball-reference.com/leagues/NBA_2019_games-october.html"))
                .thenReturn(docSchedule);

        when(webScrapingServices.getDownloadedFile(contains("boxscores")))
                .thenReturn(docMatch);

        when(basketPlayerClient.getByReferenceId(any(String.class)))
                .thenReturn(BasketPlayer.builder().build());

        when(basketTeamClient.saveIfNotExist(any(BasketTeamSaveIfNotExistDto.class)))
                .thenAnswer(
                        (Answer<BasketTeam>) invocation -> {
                            BasketTeamSaveIfNotExistDto basketTeamSaveIfNotExistDto = invocation.getArgument(0);
                            return BasketTeam.builder()
                                    .referenceId(basketTeamSaveIfNotExistDto.getTeamKey())
                                    .name(basketTeamSaveIfNotExistDto.getTeamName())
                                    .build();
                        });

        Answer<Season> answer = invocation -> invocation.getArgument(0);
        doAnswer(answer).when(seasonClient).saveIfNotExist(any(Season.class));

        Answer<BasketMatch> matchAnswer = invocation -> invocation.getArgument(0);
        doAnswer(matchAnswer).when(basketMatchClient).save(any(BasketMatch.class));

        BasketJob basketJob = processNextBasketJob.execute();
        Assert.assertNotNull(basketJob);
        Assert.assertEquals(BasketJobState.DOWNLOADED, basketJob.getState());
        basketJob = processNextBasketJob.execute();
        Assert.assertEquals(BasketJobState.PROCESSED, basketJob.getState());
        verify(basketMatchClient, atLeast(1)).findByGameKey("201610250GSW");
        basketJob = processNextBasketJob.execute();
    }

    @Test
    public void scrapeWithEmptyCells() throws IOException, ParseException {
        String gameKey = "199611010DEN";
        CloudDocument cloudDocument = getCloudDocument(championshipPath, gameKey);

        String playerKey = "brookmi01";
        String filePlayerPath = "src/test/resources/" + championshipPath + playerKey + ".html";
        Document docPlayer = getDocument(filePlayerPath);
        documentServices.saveFile(docPlayer, "brookmi01");

        Season season = new Season();

        when(webScrapingServices.getDownloadedFile(contains("player")))
                .thenReturn(docPlayer);

        when(basketPlayerClient.getByReferenceId(any(String.class)))
                .thenReturn(BasketPlayer.builder().build());

        Answer<BasketMatch> answer = invocation -> invocation.getArgument(0);

        doAnswer(answer).when(basketMatchClient).save(any(BasketMatch.class));

        scrapeFile.execute(cloudDocument, season, basketReferenceAPI);
    }

}