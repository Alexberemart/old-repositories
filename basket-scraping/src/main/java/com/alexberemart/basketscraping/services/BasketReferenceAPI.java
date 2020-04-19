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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

@Service
@AllArgsConstructor
@Log
public class BasketReferenceAPI implements BasketAPI {

    protected WebScrapingServices webScrapingServices;
    protected DocumentServices documentServices;

    @Override
    public Date getSeasonStartDate(String basketJobWebKey) throws ParseException {

        boolean matches = isValidFileNameMatch(basketJobWebKey);
        if (!matches) {
            throw new IllegalArgumentException("wrong web key");
        }

        String token = "_";
        String[] keySplitted = basketJobWebKey.split(token);

        String startDate = (Integer.parseInt(keySplitted[1]) - 1) + "1001";
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.parse(startDate);
    }

    @Override
    public String getSeasonWebKey(String basketJobWebKey) {

        boolean matches = isValidFileNameMatch(basketJobWebKey);
        if (!matches) {
            throw new IllegalArgumentException("wrong web key");
        }

        String token = "_";
        String[] keySplitted = basketJobWebKey.split(token);

        return keySplitted[0] + token + keySplitted[1];
    }

    @Override
    public String getGameKeyFromGameList(Element element) {
        return element.attr("csk");
    }

    @Override
    public Elements getGameList(Document doc) {
        return doc.select("tr").select("th[csk]");
    }

    @Override
    public String getSeasonUrl(String key) {
        return "http://www.basketball-reference.com/leagues/" + key + ".html";
    }

    @Override
    public String getMatchUrl(String key) {
        return "http://www.basketball-reference.com/boxscores/" + key + ".html";
    }

    @Override
    public BasketScrapedMatch createMatch(String gameKey, Season season, CloudDocument cloudDocument) throws ParseException {
        BasketScrapedMatch basketScrapedMatch = BasketScrapedMatch.builder().build();
        basketScrapedMatch.setGameKey(gameKey);
        basketScrapedMatch.setDate(getMatchDate(gameKey));
        basketScrapedMatch.setSeason(season);
        Document doc = documentServices.parseFile(cloudDocument.getDocument());
        basketScrapedMatch.setBasketScrapedMatchTeamScoreList(createScrapedMatchTeamScoreList(doc, gameKey));
        basketScrapedMatch.setBasketScrapedMatchStatList(createBasketReferenceMatchStats(doc));
        return basketScrapedMatch;
    }

    @Override
    public List<BasketScrapedMatchTeamScore> createScrapedMatchTeamScoreList(Document doc, String gameKey) {
        Element scoreBoxElement = getScoreBoxElements(doc);

        if (scoreBoxElement.childNodes().size() == 0) {
            throw new RuntimeException("El elemento " + gameKey + " no tiene scorebox ¿Puede que haya que descargarlo de nuevo?");
        }

        List<BasketScrapedMatchTeamScore> referenceTeamScoreList = new ArrayList<>();
        Elements teamScoreBoxElements = scoreBoxElement.select("div[itemprop=performer]");

        if (teamScoreBoxElements.size() > 2) {
            throw new IllegalArgumentException();
        }

        Integer type = 1;

        for (Element teamScoreBoxElement : teamScoreBoxElements) {
            Element teamElement = teamScoreBoxElement.select("a[href~=teams]").get(0);
            String[] split = teamElement.attr("href").split("\\/");
            String teamKey = split[2];
            String teamName = teamElement.text();
            Element scoreElement = teamScoreBoxElement.parent().select("div.score").get(0);
            String score = scoreElement.text();

            BasketScrapedMatchTeamScore basketScrapedMatchTeamScore = BasketScrapedMatchTeamScore.builder()
                    .basketTeamWebKey(teamKey)
                    .basketTeamName(teamName)
                    .score(Integer.parseInt(score))
                    .basketMatchTeamType(BasketMatchTeamType.fromCode(type))
                    .build();
            referenceTeamScoreList.add(basketScrapedMatchTeamScore);
            type -= 1;
        }

        return referenceTeamScoreList;
    }

    @Override
    public Element getScoreBoxElements(Document matchDoc) {
        return matchDoc.select("div.scoreBox").get(0);
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

        String heightAndWeight = doc.select("p:contains(kg):contains(lb)").get(0).childNode(3).toString();
        String[] heightAndWeightSplitted = heightAndWeight.split("\\(|,|;|\\)|cm|kg");
        basketScrapedPlayer.setHeight(heightAndWeightSplitted[2]);
        basketScrapedPlayer.setWeight(heightAndWeightSplitted[5]);

        String birth = doc.select("span[itemprop=birthDate]").attr("data-birth");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        basketScrapedPlayer.setBirthDate(format.parse(birth));

        String position = doc.select("p:contains(Position)").get(0).childNode(2).toString();
        basketScrapedPlayer.setPosition(position.substring(1).replace(" and ", "|").replace(" ▪ ", ""));

        try {
            String country = getCountry(doc);
            basketScrapedPlayer.setCountry(country);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warning("it's not possible to get country information from player " + basketScrapedPlayer.getBasketReferenceKey());
        }

        String name = doc.select("h1[itemprop=name]").get(0).text();
        basketScrapedPlayer.setPlayerName(name);

        return basketScrapedPlayer;
    }

    @Override
    public String getPlayerKey(String playerRelativePath) {
        String[] playerUrlSplitted = playerRelativePath.split("/|\\.");
        return playerUrlSplitted[3];
    }

    @Override
    public String getPlayerUrl(String playerRelativePath) {
        return "http://www.basketball-reference.com" + playerRelativePath;
    }

    @Override
    public String getOriginName() {
        return "basket reference";
    }

    protected Boolean isValidFileNameMatch(String fileNameMatch) {
        return fileNameMatch.matches("^NBA_\\w{4}_(games)-(\\w+)$");
    }

    protected String getCountry(Document doc) {
        return doc.select("p:contains(Born)").select("a[href~=country]").attr("href").split("country=|&")[1];
    }

    private Date getMatchDate(String gameKey) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.parse(gameKey.substring(0, 8));
    }

    public List<BasketScrapedMatchStat> createBasketReferenceMatchStats(Document doc) {
        List<BasketScrapedMatchStat> result = new ArrayList<>();
        Elements teamElements = doc.select("div[id~=all_box]").select("div.table_wrapper[id~=basic]");

        if (teamElements.size() != 2) {
            throw new RuntimeException();
        }

        Integer type = 1;
        for (Element teamElement : teamElements) {
            teamElement.select("div.section_heading").select("span[id~=basic]");
            Elements elements = teamElement.select("div.overthrow[id~=basic]").select("tbody").select("tr:not(.thead)");
            for (Element element : elements) {
                BasketScrapedMatchStat basketScrapedMatchStat = createBasketReferenceMatchStats(element);
                basketScrapedMatchStat.setType(type);
                result.add(basketScrapedMatchStat);
            }
            type -= 1;
        }

        return result;
    }


    private BasketScrapedMatchStat createBasketReferenceMatchStats(Element playerElement) {
        String playerUrl = playerElement.select("a").attr("href");
        BasketScrapedMatchStat basketScrapedMatchStat = BasketScrapedMatchStat.builder().build();
        basketScrapedMatchStat.setPlayerUrl(playerUrl);
        basketScrapedMatchStat.setBasketReferenceKey(playerUrl.split("[/.]")[3]);
        try {
            basketScrapedMatchStat.setPoints(getStat(playerElement, "pts"));
            basketScrapedMatchStat.setAssists(getStat(playerElement, "ast"));
            basketScrapedMatchStat.setBlocks(getStat(playerElement, "blk"));
            basketScrapedMatchStat.setRebounds(getStat(playerElement, "trb"));
            basketScrapedMatchStat.setMinutes(getStringStat(playerElement, "mp"));
            basketScrapedMatchStat.setFieldGoal(getStat(playerElement, "fg"));
            basketScrapedMatchStat.setFieldGoalAttempts(getStat(playerElement, "fga"));
            basketScrapedMatchStat.setFieldGoal3Points(getStat(playerElement, "fg3"));
            basketScrapedMatchStat.setFieldGoal3PointsAttempts(getStat(playerElement, "fg3a"));
            basketScrapedMatchStat.setFieldThrows(getStat(playerElement, "ft"));
            basketScrapedMatchStat.setFieldThrowsAttempts(getStat(playerElement, "fta"));
            basketScrapedMatchStat.setSteals(getStat(playerElement, "stl"));
            basketScrapedMatchStat.setPersonalFouls(getStat(playerElement, "pf"));
            basketScrapedMatchStat.setTurnOvers(getStat(playerElement, "tov"));
        } catch (IndexOutOfBoundsException e) {
            basketScrapedMatchStat.setIssue(playerElement.text());
        }
        return basketScrapedMatchStat;
    }

    private int getStat(Element playerElement, String text) {
        String stat = playerElement.select("td[data-stat=" + text + "]").get(0).text();
        if ("".equals(stat)) {
            return 0;
        }
        return Integer.parseInt(stat);
    }

    private String getStringStat(Element playerElement, String text) {
        return playerElement.select("td[data-stat=" + text + "]").get(0).text();
    }
}