package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.entities.BasketMatchEntity;
import com.alexberemart.basketapi.entities.BasketTeamEntity;
import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.alexberemart.basketapi.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@RunWith(JUnit4.class)
@ActiveProfiles(profiles = "test")
public class BasketTeamEntryEntityTest {

    @Test
    public void toModel(){
        Date date = new Date();
        String id = "id";
        String name = "name";
        String referenceId = "referenceId";
        int points = 10;
        BasketMatchTeamType teamType = BasketMatchTeamType.AWAY;

        BasketTeamEntity basketTeamEntity = BasketTeamEntity.builder()
                .id(id)
                .name(name)
                .referenceId(referenceId)
                .build();

        BasketMatchEntity basketMatchEntity = BasketMatchEntity.builder()
                .date(date)
                .build();

        BasketTeamEntryEntity basketTeamEntryEntity = BasketTeamEntryEntity.builder()
                .basketTeam(basketTeamEntity)
                .basketMatch(basketMatchEntity)
                .type(teamType.getCode())
                .points(points)
                .build();

        BasketTeamEntry basketTeamEntry = basketTeamEntryEntity.toModel();
        Assert.assertEquals(basketTeamEntryEntity.getPoints(), basketTeamEntry.getPoints());
        Assert.assertEquals(basketTeamEntryEntity.getBasketMatch().date, basketTeamEntry.getBasketMatch().getDate());
        Assert.assertEquals(basketTeamEntryEntity.getBasketTeam().referenceId, basketTeamEntry.getBasketTeam().getReferenceId());
        Assert.assertEquals(teamType, basketTeamEntry.getType());
    }
}