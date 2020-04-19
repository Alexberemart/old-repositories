package com.alexberemart.basketscraping.model;

import com.alexberemart.basketapi.model.BasketMatch;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
public class EuroLeagueMatch extends BasketMatch {

    @Getter private List<EuroLeagueMatchTeam> euroLeagueMatchTeamList = new ArrayList<>();
}
