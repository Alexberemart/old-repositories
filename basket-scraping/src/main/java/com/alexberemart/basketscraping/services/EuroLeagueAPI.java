package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.model.EuroLeagueMatch;
import com.alexberemart.basketscraping.model.EuroLeagueMatchTeam;
import com.alexberemart.basketscraping.model.EuroLeagueMatchTeamPlayer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
public class EuroLeagueAPI {

    protected EuroLeagueMatchServices euroLeagueMatchServices;
    Logger logger = Logger.getLogger(this.getClass().toString());

    @Autowired
    public EuroLeagueAPI(EuroLeagueMatchServices euroLeagueMatchServices) {
        this.euroLeagueMatchServices = euroLeagueMatchServices;
    }

    public List<EuroLeagueMatch> scraping(String gameNumber, String phaseTypeCode, String seasonCode) throws IOException, ParseException {
        List<EuroLeagueMatch> euroLeagueMatchList = new ArrayList<>();
        List<String> gameKeyArray = new ArrayList<>();
        Document doc = Jsoup.connect(getURL(gameNumber, phaseTypeCode, seasonCode)).get();
        Elements matchElements = doc.select("div.game").select("div.played").select("a");
        for (Element element : matchElements) {
            euroLeagueMatchList.add(euroLeagueMatchServices.getMatch(element.attr("href")));
        }
        return euroLeagueMatchList;
    }

    public String getAllMatches(String seasonCode) throws IOException, ParseException {
        List<EuroLeagueMatch> euroLeagueMatchList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            euroLeagueMatchList.addAll(scraping(String.valueOf(i), "RS", seasonCode));
            logger.info("round " + i + " finished !!!");
        }
        return scrapingCsv(euroLeagueMatchList);
    }

    public String scrapingCsv(List<EuroLeagueMatch> euroLeagueMatchList) throws IOException, ParseException {
        String result = "id;date;name;points;team;issue\n";
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        for (EuroLeagueMatch euroLeagueMatch : euroLeagueMatchList) {
            for (EuroLeagueMatchTeam euroLeagueMatchTeam : euroLeagueMatch.getEuroLeagueMatchTeamList()) {
                for (EuroLeagueMatchTeamPlayer euroLeagueMatchTeamPlayer : euroLeagueMatchTeam.getEuroLeagueMatchTeamPlayerList()) {
                    result += "pending;";
                    result += dt1.format(euroLeagueMatch.getDate()) + ";";
                    result += euroLeagueMatchTeamPlayer.getPlayerName() + ";";
                    result += euroLeagueMatchTeamPlayer.getPoints() + ";";
                    result += euroLeagueMatchTeam.getTeamName() + ";";
                    result += euroLeagueMatchTeamPlayer.getIssue() + "\n";
                }
            }
        }
        return result;
    }

    private String getURL(String gameNumber, String phaseTypeCode, String seasonCode) {
//        return "http://www.euroleague.net/main/results?gamenumber=" + gameNumber + "&phasetypecode=RS%20%20%20%20%20%20%20%20&seasoncode=E2016";
        return "http://www.euroleague.net/main/results?gamenumber=" + gameNumber + "&phasetypecode=" + phaseTypeCode + "&seasoncode=" + seasonCode;
    }

}
