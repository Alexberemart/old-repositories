package com.alexberemart.basketscraping.services;

import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.IntegrationTestUtils;
import com.alexberemart.basketscraping.UnitTest;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.BasketScrapedMatchStat;
import com.alexberemart.basketscraping.model.BasketScrapedMatchTeamScore;
import com.alexberemart.basketscraping.model.BasketScrapedPlayer;
import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.services.DocumentServices;
import com.alexberemart.basketscraping.services.FibaWorldCupAPI;
import com.alexberemart.basketscraping.services.WebScrapingServices;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.alexberemart.basketscraping.IntegrationTestUtils.getCloudDocument;
import static com.alexberemart.basketscraping.IntegrationTestUtils.getDocument;
import static org.mockito.Mockito.*;

public class FibaWorldCupAPITest extends UnitTest {

    @Mock
    protected WebScrapingServices webScrapingServices;
    @Mock
    protected DocumentServices documentServices;
    protected FibaWorldCupAPI fibaWorldCupAPI;

    private final String championshipPath = "fiba_world_cup/";

    private final String basketJobKey = "6241_2014";

    @Before
    public void setUp() {
        fibaWorldCupAPI = new FibaWorldCupAPI(
                webScrapingServices,
                documentServices
        );
        fibaWorldCupAPI = spy(fibaWorldCupAPI);
    }

    @Test
    public void getMatchUrl() {
        String MatchKey = "1_B_9719_6241_2014";
        String matchUrl = fibaWorldCupAPI.getMatchUrl(MatchKey);
        Assert.assertNotNull(matchUrl);
        String expected = "https://archive.fiba.com/pages/eng/fa/game/p/gid/1/grid/B/rid/9719/sid/6241/_/2014_FIBA_Basketball_World_Cup/statistic.html";
        Assert.assertEquals(expected, matchUrl);
    }

    @Test
    public void getMatchKeyFromMatchUrl() {
        String matchUrl = "https://archive.fiba.com/pages/eng/fa/game/p/gid/1/grid/B/rid/9719/sid/6241/_/2014_FIBA_Basketball_World_Cup/statistic.html";
        String matchKey = fibaWorldCupAPI.getMatchKeyFromMatchUrl(matchUrl);
        Assert.assertNotNull(matchKey);
        String expected = "1_B_9719_6241_2014";
        Assert.assertEquals(expected, matchKey);
    }

    @Test
    public void getPlayerUrl() {
        String playerRelativePath = "/pages/eng/fa/player/p/pid/43731/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/index.html";
        String playerUrl = fibaWorldCupAPI.getPlayerUrl(playerRelativePath);
        Assert.assertNotNull(playerUrl);
        String expected = "https://archive.fiba.com/pages/eng/fa/player/p/pid/43731/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/index.html";
        Assert.assertEquals(expected, playerUrl);
    }

    @Test
    public void getPlayer() throws IOException, ParseException {
        String playerKey = "Ante_Tomic";
        String playerRelativePath = "/pages/eng/fa/player/p/pid/43731/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/index.html";

        CloudDocument cloudDocument = IntegrationTestUtils.getCloudDocument(championshipPath, playerKey);
        Document doc = IntegrationTestUtils.getDocument("src/test/resources/" + championshipPath + playerKey + ".html");

        when(documentServices.getFile(playerRelativePath))
                .thenReturn(cloudDocument);

        when(documentServices.parseFile(cloudDocument.getDocument()))
                .thenReturn(doc);

        BasketScrapedPlayer basketScrapedPlayer = fibaWorldCupAPI.getPlayer(playerRelativePath);
        Assert.assertNotNull(basketScrapedPlayer);
        String expected = "43731";
        Assert.assertEquals(expected, basketScrapedPlayer.getBasketReferenceKey());
    }

    @Test
    public void getSeasonUrl() {
        String seasonUrl = fibaWorldCupAPI.getSeasonUrl(basketJobKey);
        String expected = "https://archive.fiba.com/pages/eng/fa/event/p/cid/WMM/sid/6241/_/2014_FIBA_Basketball_World_Cup/schedule.html";
        Assert.assertEquals(expected, seasonUrl);
    }

    @Test
    public void getSeasonStartDate() throws ParseException {
        Date seasonStartDate = fibaWorldCupAPI.getSeasonStartDate(basketJobKey);
        String startDate = "20141001";
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date expected = format.parse(startDate);
        Assert.assertEquals(expected, seasonStartDate);
    }

    @Test
    public void getSeasonWebKey() {
        String seasonWebKey = fibaWorldCupAPI.getSeasonWebKey(basketJobKey);
        String expected = "2014";
        Assert.assertEquals(expected, seasonWebKey);
    }

    @Test
    public void createBasketReferenceMatchStats() {
        Element tableRowElement = new Element(Tag.valueOf("tr"), "");

        Element tableRowDataElement = new Element(Tag.valueOf("td"), "");

        Element tableRowDataLinkElement = new Element(Tag.valueOf("a"), "");
        tableRowDataElement.appendChild(tableRowDataLinkElement);

        tableRowElement.appendChild(tableRowDataElement);

        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "pts").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "a").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "min").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "ma").html("3/4"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "ma").html("3/4"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "ma").html("0/0"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "ma").html("2/2"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "per").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "o").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "st").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "pf").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "t").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "tot").html("8"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "bs").html("8"));

        tableRowDataLinkElement.attr("href", "/pages/eng/fa/player/p/pid/43731/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/index.html");
        BasketScrapedMatchStat basketReferenceMatchStats = fibaWorldCupAPI.createBasketReferenceMatchStats(tableRowElement);
        Assert.assertEquals(8, basketReferenceMatchStats.getPoints(), 0);
        Assert.assertNull(basketReferenceMatchStats.getIssue());

        /*<tr class="odd" onmouseout="this.className='odd';" onmouseover="this.className='highlight';">
            <td class="g">4</td>
            <td class="name"><a href="/pages/eng/fa/player/p/pid/43731/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/index.html">A.&nbsp;TOMIC*</a></td>
            <td class="min">25</td><td class="ma">3/4</td><td class="per">75</td><td class="ma">3/4</td><td class="per">75</td><td class="ma">0/0</td><td class="per">0</td>
            <td class="ma">2/2</td>
            <td class="per">100</td>
            <td class="o">3</td><td class="d">8</td><td class="tot">11</td><td class="a">4</td>
            <td class="pf">0</td>
            <td class="t">5</td><td class="st">1</td><td class="bs">0</td>
            <td class="pts">8</td>
		</tr>*/
    }

    @Test
    public void createBasketReferenceMatchStatsFromDoc() throws IOException {
        String filePath = "src/test/resources/" + championshipPath + "archive.fiba.com_ 2014 FIBA Basketball World Cup.html";
        Document docMatch = getDocument(filePath);

        doReturn(BasketScrapedMatchStat.builder().build())
                .when(fibaWorldCupAPI).createBasketReferenceMatchStats(any(Element.class));

        List<BasketScrapedMatchStat> basketReferenceMatchStats = fibaWorldCupAPI.createBasketReferenceMatchStats(docMatch);
        Assert.assertEquals(4, basketReferenceMatchStats.size());
    }

    @Test
    public void getMatchDate() throws IOException, ParseException {
        String filePath = "src/test/resources/" + championshipPath + "archive.fiba.com_ 2014 FIBA Basketball World Cup.html";
        Document docMatch = getDocument(filePath);

        Date matchDate = fibaWorldCupAPI.getMatchDate(docMatch);

        String startDate = "20140830";
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date expected = format.parse(startDate);

        Assert.assertEquals(expected, matchDate);
    }

    @Test
    public void getTeamInformation() {
        String searchKey = "odd";
        String teamName = "Croatia";
        Integer type = BasketMatchTeamType.fromCode(0).getCode();

        Element tableRowElement = new Element(Tag.valueOf("tr"), "");
        tableRowElement.attr("class", "odd");

        Element tableRowDataElement = new Element(Tag.valueOf("td"), "");
        tableRowDataElement.attr("class", "country");

        Element tableRowDataLinkElement = new Element(Tag.valueOf("a"), "");
        tableRowDataLinkElement.text(teamName);
        tableRowDataLinkElement.attr("href", "/pages/eng/fa/team/p/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/accumulated-statistics.html");

        tableRowDataElement.appendChild(tableRowDataLinkElement);
        tableRowElement.appendChild(tableRowDataElement);
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).html("23"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).html("14"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).html("20"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).html("14"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).html("10"));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).html("81"));
        tableRowElement.appendChild(Jsoup.parse("", "", Parser.htmlParser()));
        tableRowElement.appendChild((new Element(Tag.valueOf("td"), "")).attr("class", "tp"));
        tableRowElement.appendChild(Jsoup.parse("", "", Parser.htmlParser()));

        BasketScrapedMatchTeamScore teamInformation = fibaWorldCupAPI.getTeamInformation(searchKey, tableRowElement, type);
        Assert.assertEquals(81, teamInformation.getScore(), 0);
        Assert.assertEquals(teamName, teamInformation.getBasketTeamName());
        Assert.assertEquals("2168", teamInformation.getBasketTeamWebKey());

        /*<tr class="odd" onmouseout="this.className='odd';" onmouseover="this.className='highlight';">
            <td><img src="/img/flag/_34/CRO.jpg" title="Flag of Croatia" alt="Croatia"></td>
            <td class="country"><a href="/pages/eng/fa/team/p/sid/6241/tid/2168/_/2014_FIBA_Basketball_World_Cup/accumulated-statistics.html">Croatia</a></td>

                <td>23</td>
                <td>14</td>
                <td>20</td><td>14</td><td>10</td>
            <td>81</td>
            <td class="tp" colspan="2">Bogdanovic (26pts), Tomic (11rbs), Tomic (4ast)&nbsp;</td>
        </tr>*/
    }

    @Test
    public void createMatch() throws IOException, ParseException {
        String gameKey = "archive.fiba.com_ 2014 FIBA Basketball World Cup";
        Season season = Season.builder().build();
        CloudDocument cloudDocument = getCloudDocument(championshipPath, gameKey);

        BasketScrapedMatch basketScrapedMatch = fibaWorldCupAPI.createMatch(gameKey, season, cloudDocument);
        Assert.assertEquals(gameKey, basketScrapedMatch.getGameKey());
    }
}