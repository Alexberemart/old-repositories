package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketPlayerEntry {

    protected String id;
    protected BasketPlayer basketPlayer;
    protected BasketMatch basketMatch;
    protected Integer points;
    protected Integer rebounds;
    protected Integer assists;
    protected BasketMatchTeamType basketMatchTeamType;

    public void setBasketMatchTeamType(Integer i) {
        this.basketMatchTeamType = BasketMatchTeamType.fromCode(i);
    }

    public void setBasketMatchTeamType(BasketMatchTeamType i) {
        this.basketMatchTeamType = i;
    }

}
