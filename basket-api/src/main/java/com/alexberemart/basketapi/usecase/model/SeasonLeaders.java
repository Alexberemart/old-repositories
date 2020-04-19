package com.alexberemart.basketapi.usecase.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SeasonLeaders {

    String seasonId;
    String seasonName;
    String basketPlayerIdLeaderByPoints;
    String basketPlayerNameLeaderByPoints;
    Integer points;
    String basketPlayerIdLeaderByRebounds;
    String basketPlayerNameLeaderByRebounds;
    Integer rebounds;
}
