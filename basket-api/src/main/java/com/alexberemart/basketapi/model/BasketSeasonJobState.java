package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@JsonAutoDetect
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BasketSeasonJobState {

    INIT(0, "Init"),
    EVENTS_GENERATED(1, "EVENTS_GENERATED"),
    FINISHED(2, "Finished");

    protected Integer code;
    protected String description;

    public BasketSeasonJobState getNextState(){
        switch (this.code){
            case 0:
                return BasketSeasonJobState.EVENTS_GENERATED;
            case 1:
                return BasketSeasonJobState.FINISHED;
            default:
                return BasketSeasonJobState.FINISHED;
        }
    }

    public static BasketSeasonJobState fromCode(Integer code) {
        for (BasketSeasonJobState b : BasketSeasonJobState.values()) {
            if (Objects.equals(b.code, code)) {
                return b;
            }
        }
        return null;
    }
}
