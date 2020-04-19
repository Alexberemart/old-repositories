package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.model.EuroLeagueMatch;
import com.alexberemart.basketscraping.model.EuroLeagueMatchTeam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
public class EuroLeagueMatchServices {

    protected EuroLeagueMatchTeamServices euroLeagueMatchTeamServices;

    @Autowired
    public EuroLeagueMatchServices(EuroLeagueMatchTeamServices euroLeagueMatchTeamServices) {
        this.euroLeagueMatchTeamServices = euroLeagueMatchTeamServices;
    }

    public EuroLeagueMatch getMatch(String matchKey) throws IOException, ParseException {
        Document doc = Jsoup.connect("http://www.euroleague.net" + matchKey).get();
        String dateText = doc.select("div.dates").select("div.date").text();
        Elements teamElements = doc.select("div[class~=StatsContainer]");
        EuroLeagueMatch euroLeagueMatch = new EuroLeagueMatch();
        for (Element element : teamElements) {
            euroLeagueMatch.setDate(getDate(dateText));
            euroLeagueMatch.getEuroLeagueMatchTeamList().add(euroLeagueMatchTeamServices.getTeam(element));
        }
        return euroLeagueMatch;
    }

    private Date getDate(String dateText) throws ParseException {
        DateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        return format.parse(dateText);
    }
}
