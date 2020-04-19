package com.alexberemart.basketscraping.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasketScrapedMatchStat {

    protected String basketReferenceKey;
    protected String playerUrl;
    protected String issue;
    private Integer points;
    private Integer assists;
    private Integer blocks;
    private Integer rebounds;
    private Integer turnOvers;
    private String minutes;
    private Integer fieldGoal;
    private Integer fieldGoalAttempts;
    private Integer fieldGoal3Points;
    private Integer fieldGoal3PointsAttempts;
    private Integer fieldThrows;
    private Integer fieldThrowsAttempts;
    private Integer steals;
    private Integer personalFouls;
    private Integer type;
}
