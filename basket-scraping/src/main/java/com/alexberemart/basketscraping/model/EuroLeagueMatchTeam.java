package com.alexberemart.basketscraping.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
public class EuroLeagueMatchTeam {

    @Setter @Getter private String teamName;
    @Getter private List<EuroLeagueMatchTeamPlayer> euroLeagueMatchTeamPlayerList = new ArrayList<>();
}
