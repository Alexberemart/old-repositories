package com.alexberemart.basketscraping.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonAutoDetect
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BasketJobState {

    INIT(0, "Init"),
    DOWNLOADED(1, "Downloaded"),
    PROCESSED(2, "Processed"),
    EVENTS_FINISHED(3, "Events Finished");

    protected Integer code;
    protected String description;

    public BasketJobState getNextState(){
        switch (this.code){
            case 0:
                return BasketJobState.DOWNLOADED;
            case 1:
                return BasketJobState.PROCESSED;
            default:
                return BasketJobState.EVENTS_FINISHED;
        }
    }

    public static BasketJobState fromCode(Integer code) {
        for (BasketJobState b : BasketJobState.values()) {
            if (Objects.equals(b.code, code)) {
                return b;
            }
        }
        return null;
    }

}
