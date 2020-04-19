package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum BasketMatchTeamType {

    HOME(0, "Home"),
    AWAY(1, "Away");

    protected Integer code;
    protected String description;

    public static BasketMatchTeamType fromCode(Integer code) {
        for (BasketMatchTeamType b : BasketMatchTeamType.values()) {
            if (Objects.equals(b.code, code)) {
                return b;
            }
        }
        return null;
    }

}
