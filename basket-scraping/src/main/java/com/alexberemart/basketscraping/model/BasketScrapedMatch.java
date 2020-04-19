package com.alexberemart.basketscraping.model;

import com.alexberemart.basketapi.model.Season;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
@Getter
@Setter
@Builder
public class BasketScrapedMatch {

    protected Date date;
    protected Season season;
    protected String gameKey;
    protected List<BasketScrapedMatchStat> basketScrapedMatchStatList;
    protected List<BasketScrapedMatchTeamScore> basketScrapedMatchTeamScoreList;
    protected List<BasketScrapedPlayer> basketScrapedPlayers;

}
