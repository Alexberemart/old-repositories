package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.model.EuroLeagueMatchTeamPlayer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
public class EuroLeagueMatchTeamPlayerServices {

    public EuroLeagueMatchTeamPlayer getPlayer(Element playerElement) throws IOException {
        String playerName = playerElement.select("td.PlayerContainer").text();
        Elements playerStats = playerElement.select("td");
        EuroLeagueMatchTeamPlayer euroLeagueMatchPlayer = new EuroLeagueMatchTeamPlayer();
        euroLeagueMatchPlayer.setPlayerName(parsePlayerName(playerName));
        try {
            euroLeagueMatchPlayer.setPoints(Integer.parseInt(playerStats.get(3).text()));
        }catch(NumberFormatException e){
            euroLeagueMatchPlayer.setIssue(playerStats.get(3).text());
        }
        return euroLeagueMatchPlayer;
    }

    protected String parsePlayerName(String playerName) {
        String[] nameSplitted = playerName.split(", ");
        return WordUtils.capitalize(StringUtils.lowerCase(nameSplitted[1])) +
                " " +
                WordUtils.capitalize(StringUtils.lowerCase(nameSplitted[0]));
    }

}
