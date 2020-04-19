package com.alexberemart.basketscraping.services;

import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.model.BasketScrapedMatch;
import com.alexberemart.basketscraping.model.BasketScrapedMatchTeamScore;
import com.alexberemart.basketscraping.model.BasketScrapedPlayer;
import com.alexberemart.basketscraping.model.CloudDocument;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
public interface BasketAPI {

    Date getSeasonStartDate(String basketJobWebKey) throws ParseException;

    String getSeasonWebKey(String basketJobWebKey);

    String getGameKeyFromGameList(Element element);

    Elements getGameList(Document doc);

    String getSeasonUrl(String key);

    String getMatchUrl(String key);

    BasketScrapedMatch createMatch(String gameKey, Season season, CloudDocument cloudDocument) throws ParseException;

    List<BasketScrapedMatchTeamScore> createScrapedMatchTeamScoreList(Document doc, String gameKey);

    Element getScoreBoxElements(Document matchDoc);

    BasketScrapedPlayer getPlayer(String playerRelativePath) throws IOException, ParseException;

    String getPlayerKey(String playerRelativePath);

    String getPlayerUrl(String playerRelativePath);

    String getOriginName();
}