package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
public class BasketMatchTeamPlayer {

    @Setter
    @Getter
    private String playerName;
    @Setter
    @Getter
    private Date birthDate;
    @Setter
    @Getter
    private String position;
    @Setter
    @Getter
    private String height;
    @Setter
    @Getter
    private String weight;
    @Setter
    @Getter
    private String country;
    @Setter
    @Getter
    private String issue;
    @Setter
    @Getter
    private String team;
    @Setter
    @Getter
    private Integer points;
    @Setter
    @Getter
    private Integer assists;
    @Setter
    @Getter
    private Integer blocks;
    @Setter
    @Getter
    private Integer rebounds;
    @Setter
    @Getter
    private Integer turnOvers;
    @Setter
    @Getter
    private String minutes;
    @Setter
    @Getter
    private Integer fieldGoal;
    @Setter
    @Getter
    private Integer fieldGoalAttempts;
    @Setter
    @Getter
    private Integer fieldGoal3Points;
    @Setter
    @Getter
    private Integer fieldGoal3PointsAttempts;
    @Setter
    @Getter
    private Integer fieldThrows;
    @Setter
    @Getter
    private Integer fieldThrowsAttempts;
    @Setter
    @Getter
    private Integer steals;
    @Setter
    @Getter
    private Integer personalFouls;
    @Setter
    @Getter
    private Integer type;

    public Integer getRanking() {
        if (issue == null) {
            return this.points +
                    this.assists +
                    this.rebounds +
                    this.fieldGoal +
                    this.fieldThrows +
                    this.steals +
                    this.blocks -
                    this.fieldGoalAttempts -
                    this.fieldThrowsAttempts -
                    this.turnOvers;
        }
        return null;
    }

    public Integer getSecondsPlayed() {
        if (issue == null) {
            String[] strings = new String[0];
            try {
                strings = minutes.split(":");
            }catch(NullPointerException e){
                System.out.println(e);
            }
            try {
                return Integer.parseInt(strings[0]) * 60 +
                        Integer.parseInt(strings[1]);
            }catch(NumberFormatException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}
