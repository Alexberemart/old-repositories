package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.SpringContext;
import com.alexberemart.basketapi.services.BasketTeamEntryServices;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import javax.persistence.Transient;
import java.util.List;

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
    protected Float rating;

    public void setBasketMatchTeamType(Integer i) {
        this.basketMatchTeamType = BasketMatchTeamType.fromCode(i);
    }

    public void setBasketMatchTeamType(BasketMatchTeamType i) {
        this.basketMatchTeamType = i;
    }

    @Transient
    public Float getRating() {
        Float rating = 0f;
        if (getPoints() != null) {
            rating += getPoints();
        }
        if (getRebounds() != null) {
            rating += getRebounds();
        }
        if (getAssists() != null) {
            rating += getAssists();
        }

        BasketTeamEntryServices basketTeamEntryServices = getBasketTeamEntryServices();
        List<BasketTeamEntry> basketTeamEntryList = basketTeamEntryServices.findByBasketMatch_Id(getBasketMatch().getId());

        Integer myPoints = 0;
        Integer otherPoints = 0;

        for (BasketTeamEntry basketTeamEntry : basketTeamEntryList){
            if (basketTeamEntry.getType() == getBasketMatchTeamType()){
                myPoints = basketTeamEntry.getPoints();
            }else{
                otherPoints = basketTeamEntry.getPoints();
            }
        }

        if (myPoints < otherPoints){
            rating *= 0.5f;
        }

        return rating;
    }

    protected BasketTeamEntryServices getBasketTeamEntryServices() {
        return (BasketTeamEntryServices) SpringContext.getApplicationContext().getBean("basketOriginRepository");
    }

    public Boolean isDoubleDouble() {
        Integer featuresDoubleDigits = 0;

        if (this.getPoints() != null) {
            if (this.getPoints() >= 10) {
                featuresDoubleDigits += 1;
            }
        }

        if (this.getRebounds() != null) {
            if (this.getRebounds() >= 10) {
                featuresDoubleDigits += 1;
            }
        }

        if (this.getAssists() != null) {
            if (this.getAssists() >= 10) {
                featuresDoubleDigits += 1;
            }
        }

        return featuresDoubleDigits >= 2;
    }

}
