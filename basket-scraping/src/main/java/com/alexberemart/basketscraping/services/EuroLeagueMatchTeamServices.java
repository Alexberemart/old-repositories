package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.model.EuroLeagueMatchTeam;
import com.alexberemart.basketscraping.model.EuroLeagueMatchTeamPlayer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
public class EuroLeagueMatchTeamServices {

    protected EuroLeagueMatchTeamPlayerServices euroLeagueMatchTeamPlayerServices;

    @Autowired
    public EuroLeagueMatchTeamServices(EuroLeagueMatchTeamPlayerServices euroLeagueMatchTeamPlayerServices) {
        this.euroLeagueMatchTeamPlayerServices = euroLeagueMatchTeamPlayerServices;
    }

    public EuroLeagueMatchTeam getTeam(Element teamElement) throws IOException {
        String teamName = teamElement.select("[class~=teamname]").select("span").text();
        EuroLeagueMatchTeam euroLeagueMatchTeam = new EuroLeagueMatchTeam();
        euroLeagueMatchTeam.setTeamName(teamName);
        Elements playerElements = teamElement.select("tbody").select("tr:not(.team)");
        for (Element element : playerElements) {
            euroLeagueMatchTeam.getEuroLeagueMatchTeamPlayerList().add(euroLeagueMatchTeamPlayerServices.getPlayer(element));
        }
        return euroLeagueMatchTeam;
    }
}
