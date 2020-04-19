package com.alexberemart.basketscraping.services;

import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.BasketScrapedMatchStat;
import com.alexberemart.basketscraping.model.BasketScrapedMatchTeamScore;
import com.alexberemart.basketscraping.model.BasketScrapedPlayer;
import com.alexberemart.basketscraping.model.CloudDocument;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.WordUtils.capitalizeFully;

@Service
@AllArgsConstructor
@Log
public class FibaWorldCupAPI implements BasketAPI {

    protected WebScrapingServices webScrapingServices;
    protected DocumentServices documentServices;

    @Override
    public Date getSeasonStartDate(String basketJobKey) throws ParseException {
        boolean matches = isValidFileNameMatch(basketJobKey);
        if (!matches) {
            throw new IllegalArgumentException("wrong web key");
        }

        String[] basketJobSplitted = basketJobKey.split("_");
        String startDate = basketJobSplitted[1] + "1001";
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.parse(startDate);
    }

    @Override
    public String getSeasonWebKey(String basketJobKey) {
        String[] splitted = basketJobKey.split("_");
        return splitted[1];
    }

    public String getGameKeyFromGameList(Element element) {
        String gameUrl = element.select("a").attr("href");
        return getMatchKeyFromMatchUrl(gameUrl);
    }

    protected String getMatchKeyFromMatchUrl(String gameUrl) {
        String[] gameUrlSplitted = gameUrl.split("/grid/|/rid/|/gid/|/sid/|/_/|_FIBA_");
        return String.join("_", gameUrlSplitted[1], gameUrlSplitted[2], gameUrlSplitted[3], gameUrlSplitted[4], gameUrlSplitted[5]);
    }

    public Elements getGameList(Document doc) {
        return doc.select("div.moduleSpace")
                .select("div.border")
                .select("div.module-content")
                .select("div.inner")
                .select("table")
                .select("tbody")
                .select("tr")
                .select("td.links");
    }

    @Override
    public String getSeasonUrl(String basketJobKey) {
        //Example 6241_2014
        String[] splitted = basketJobKey.split("_");
        return "https://archive.fiba.com/pages/eng/fa/event/p/cid/WMM/sid/" + splitted[0] + "/_/" + splitted[1] + "_FIBA_Basketball_World_Cup/schedule.html";
        //return "https://archive.fiba.com/pages/eng/fa/event/p/cid/WMM/sid/6241/_/" + basketJobKey + "_FIBA_Basketball_World_Cup/schedule.html";
    }

    @Override
    public String getMatchUrl(String matchKey) {
        String[] matchKeySplitted = matchKey.split("_");
        return "https://archive.fiba.com/pages/eng/fa/game/p/gid/" +
                matchKeySplitted[0] +
                "/grid/" +
                matchKeySplitted[1] +
                "/rid/" +
                matchKeySplitted[2] +
                "/sid/" +
                matchKeySplitted[3] +
                "/_/" +
                matchKeySplitted[4] +
                "_FIBA_Basketball_World_Cup/statistic.html";
    }

    @Override
    public BasketScrapedMatch createMatch(String gameKey, Season season, CloudDocument cloudDocument) throws ParseException {
        Document doc = Jsoup.parse(cloudDocument.getDocument());
        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder().build();
        basketScrapedMatch.setGameKey(gameKey);
        basketScrapedMatch.setDate(getMatchDate(doc));
        basketScrapedMatch.setSeason(season);
        basketScrapedMatch.setBasketScrapedMatchTeamScoreList(createScrapedMatchTeamScoreList(doc, gameKey));
        basketScrapedMatch.setBasketScrapedMatchStatList(createBasketReferenceMatchStats(doc));
        return basketScrapedMatch;
    }

    @Override
    public List<BasketScrapedMatchTeamScore> createScrapedMatchTeamScoreList(Document doc, String gameKey) {
        Element scoreBoxElements = getScoreBoxElements(doc);

        if (scoreBoxElements.childNodes().size() == 0) {
            throw new RuntimeException("El elemento " + gameKey + " no tiene scorebox ¿Puede que haya que descargarlo de nuevo?");
        }

        List<BasketScrapedMatchTeamScore> referenceTeamScoreList = new ArrayList<>();

        referenceTeamScoreList.add(getTeamInformation("odd", scoreBoxElements, 0));
        referenceTeamScoreList.add(getTeamInformation("even", scoreBoxElements, 1));

        if (referenceTeamScoreList.size() > 2) {
            throw new IllegalArgumentException();
        }

        return referenceTeamScoreList;
    }


    @Override
    public Element getScoreBoxElements(Document matchDoc) {
        return matchDoc.select("th.country").parents().get(0).parent();
    }

    @Override
    public BasketScrapedPlayer getPlayer(String playerRelativePath) throws IOException, ParseException {
        CloudDocument cloudDocument;
        Document doc;
        try {
            cloudDocument = documentServices.getFile(playerRelativePath);
            doc = documentServices.parseFile(cloudDocument.getDocument());
        } catch (NoSuchFileException e) {
            doc = webScrapingServices.getDownloadedFile(getPlayerUrl(playerRelativePath));
            documentServices.saveFile(doc, playerRelativePath);
        }

        BasketScrapedPlayer basketScrapedPlayer = new BasketScrapedPlayer();
        basketScrapedPlayer.setBasketReferenceKey(getPlayerKey(playerRelativePath));

        Elements playerInfoElement = doc.select("div.player").select("div.bio").select("div.row");
        String heightAndWeight = playerInfoElement.select("div:contains(Height)").text().replace("\u00A0", " ");
        String[] heightAndWeightSplitted = heightAndWeight.split(": |cm");
        try {
            basketScrapedPlayer.setHeight(heightAndWeightSplitted[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            //Some times there is not height and weight on player web page, for example : https://archive.fiba.com/pages/eng/fa/player/p/pid/65121/sid/4728/tid/379/_/2010_FIBA_World_Championship/index.html
            // in this case position is set to blank.
            log.info("unable to get height and weight: text to process (" + heightAndWeight + ") file: (" + playerRelativePath + ")");
        }
        basketScrapedPlayer.setWeight(null);

        String birth = playerInfoElement.select("div:contains(DOB)").text();
        DateFormat format = new SimpleDateFormat("dd MMM.yyyy");
        try {
            basketScrapedPlayer.setBirthDate(format.parse(birth.split(":")[1].replace("\u00A0", " ")));
        } catch (IndexOutOfBoundsException e) {
            //Some times there is not birth on player web page, for example : https://archive.fiba.com/pages/eng/fa/player/p/pid/49690/sid/4728/tid/282/_/2010_FIBA_World_Championship/index.html
            // in this case position is set to blank.
            log.info("unable to get birth date: text to process (" + birth + ") file: (" + playerRelativePath + ")");
        }

        String playerPosition = playerInfoElement.select("div:contains(Position)").text().replace("\u00A0", " ");
        try {
            basketScrapedPlayer.setPosition(playerPosition.split(": ")[1]);
        } catch (IndexOutOfBoundsException e) {
            //Some times there is not Position on player web page, for example : https://archive.fiba.com/pages/eng/fa/player/p/pid/49690/sid/4728/tid/282/_/2010_FIBA_World_Championship/index.html
            // in this case position is set to blank.
            log.info("unable to get position: text to process (" + playerPosition + ") file: (" + playerRelativePath + ")");
        }
        basketScrapedPlayer.setCountry(playerInfoElement.select("div:contains(Place)").text().replace("\u00A0", " ").split("\\(|\\)")[1]);

        String name = capitalizeFully(doc.select("div.player").select("div.name").select("strong").text().replace("\u00A0", " ").toLowerCase());
        basketScrapedPlayer.setPlayerName(name);

        return basketScrapedPlayer;
    }

    @Override
    public String getPlayerKey(String playerRelativePath) {
        String[] playerUrlSplitted = playerRelativePath.split("/|\\.");
        return playerUrlSplitted[7];
    }

    @Override
    public String getPlayerUrl(String playerRelativePath) {
        return "https://archive.fiba.com" + playerRelativePath;
    }

    @Override
    public String getOriginName() {
        return "fiba world cup";
    }

    protected Boolean isValidFileNameMatch(String fileNameMatch) {
        return Boolean.TRUE;
    }

    protected BasketScrapedMatchTeamScore getTeamInformation(String searchKey, Element scoreBoxElements, Integer type) {
        String teamName = scoreBoxElements.select("tr." + searchKey).get(0).select("td.country").select("a").text();
        String teamUrl = scoreBoxElements.select("tr." + searchKey).get(0).select("td.country").select("a").attr("href");
        List<Node> scoreNodeList = scoreBoxElements.select("tr." + searchKey).get(0).childNodes();
        String teamScore = scoreNodeList.get(scoreNodeList.size() - 4).childNode(0).toString();
        String[] split1 = teamUrl.split("tid");
        String[] split2 = split1[1].split("/");
        String teamKey = split2[1];

        return BasketScrapedMatchTeamScore.builder()
                .basketTeamName(teamName)
                .basketTeamWebKey(teamKey)
                .score(Integer.parseInt(teamScore))
                .basketMatchTeamType(BasketMatchTeamType.fromCode(type))
                .build();
    }

    protected Date getMatchDate(Document doc) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String dateText = doc.select("div.date").get(0).text().split("Date: ")[1];
        return format.parse(dateText);
    }

    public List<BasketScrapedMatchStat> createBasketReferenceMatchStats(Document doc) {
        List<BasketScrapedMatchStat> result = new ArrayList<>();
        Elements elements = doc.select("table.boxS").get(0).select("tr.odd,tr.even").not(":has(td[colspan])");
        for (Element element : elements) {
            BasketScrapedMatchStat basketScrapedMatchStat = createBasketReferenceMatchStats(element);
            basketScrapedMatchStat.setType(BasketMatchTeamType.HOME.getCode());
            result.add(basketScrapedMatchStat);
        }
        elements = doc.select("table.boxS").get(1).select("tr.odd,tr.even").not(":has(td[colspan])");
        for (Element element : elements) {
            BasketScrapedMatchStat basketScrapedMatchStat = createBasketReferenceMatchStats(element);
            basketScrapedMatchStat.setType(BasketMatchTeamType.AWAY.getCode());
            result.add(basketScrapedMatchStat);
        }
        return result;
    }

    protected BasketScrapedMatchStat createBasketReferenceMatchStats(Element playerElement) {
        String playerUrl = playerElement.select("a").attr("href");
        String playerKey = playerUrl.split("/pid/|/sid/")[1];
        BasketScrapedMatchStat basketScrapedMatchStat = BasketScrapedMatchStat.builder().build();
        basketScrapedMatchStat.setBasketReferenceKey(playerKey);
        basketScrapedMatchStat.setPlayerUrl(playerUrl);
        try {
            basketScrapedMatchStat.setPoints(getStat(playerElement, "pts"));
            basketScrapedMatchStat.setAssists(getStat(playerElement, "a"));
            basketScrapedMatchStat.setBlocks(getStat(playerElement, "bs"));
            basketScrapedMatchStat.setRebounds(getStat(playerElement, "tot"));
            basketScrapedMatchStat.setMinutes(getStringStat(playerElement));
            basketScrapedMatchStat.setFieldGoal(getFieldStat(playerElement, 0, 0));
            basketScrapedMatchStat.setFieldGoalAttempts(getFieldStat(playerElement, 0, 1));
            basketScrapedMatchStat.setFieldGoal3Points(getFieldStat(playerElement, 2, 0));
            basketScrapedMatchStat.setFieldGoal3PointsAttempts(getFieldStat(playerElement, 2, 1));
            basketScrapedMatchStat.setFieldThrows(getFieldStat(playerElement, 3, 0));
            basketScrapedMatchStat.setFieldThrowsAttempts(getFieldStat(playerElement, 3, 1));
            basketScrapedMatchStat.setSteals(getStat(playerElement, "st"));
            basketScrapedMatchStat.setPersonalFouls(getStat(playerElement, "pf"));
            basketScrapedMatchStat.setTurnOvers(getStat(playerElement, "t"));
        } catch (IndexOutOfBoundsException e) {
            basketScrapedMatchStat.setIssue(playerElement.text());
        }
        return basketScrapedMatchStat;
    }

    private int getStat(Element playerElement, String text) {
        String stat = playerElement.select("td." + text).get(0).text();
        if ("".equals(stat)) {
            return 0;
        }
        return Integer.parseInt(stat);
    }

    private int getFieldStat(Element playerElement, Integer index, Integer index2) {
        String stat = playerElement.select("td.ma").get(index).text().split("/")[index2];
        if ("".equals(stat)) {
            return 0;
        }
        return Integer.parseInt(stat);
    }

    private String getStringStat(Element playerElement) {
        return playerElement.select("td.min").get(0).text();
    }

}