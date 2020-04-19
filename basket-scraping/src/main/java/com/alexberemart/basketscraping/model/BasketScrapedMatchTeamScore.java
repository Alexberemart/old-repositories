package com.alexberemart.basketscraping.model;

import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.BasketTeam;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
@Getter
@Setter
@Builder
public class BasketScrapedMatchTeamScore {

    protected String basketTeamWebKey;
    protected String basketTeamName;
    protected Integer score;
    protected BasketMatchTeamType basketMatchTeamType;

}
