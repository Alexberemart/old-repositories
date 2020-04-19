package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketTeamEntry {

    protected String id;
    protected BasketTeam basketTeam;
    protected BasketMatch basketMatch;
    protected Integer points;
    protected BasketMatchTeamType type;

}
