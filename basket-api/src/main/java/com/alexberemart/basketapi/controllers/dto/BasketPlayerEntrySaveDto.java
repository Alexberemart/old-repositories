package com.alexberemart.basketapi.controllers.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect
public class BasketPlayerEntrySaveDto {

    String id;
    String basketPlayerId;
    String basketMatchId;
    Integer points;
    Integer rebounds;
    Integer assists;
    Integer basketMatchTeamType;
}
