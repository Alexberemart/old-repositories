package com.alexberemart.basketscraping.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
public class EuroLeagueMatchTeamPlayer {

    @Setter @Getter private String playerName;
    @Setter @Getter private Integer points;
    @Setter @Getter private String issue;
    @Setter @Getter private String team;
}
