package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.entities.BasketPlayerEntryEntity;
import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;

@RunWith(JUnit4.class)
@ActiveProfiles(profiles = "test")
public class BasketPlayerEntryEntityTest {

    @Test
    public void toModel(){
        BasketMatchTeamType teamType = BasketMatchTeamType.AWAY;

        BasketPlayerEntryEntity basketPlayerEntryEntity = BasketPlayerEntryEntity.builder()
                .type(teamType.getCode())
                .build();

        BasketPlayerEntry basketPlayerEntry = basketPlayerEntryEntity.toModel();
        Assert.assertEquals(basketPlayerEntryEntity.getType(), basketPlayerEntry.getBasketMatchTeamType().getCode());
    }
}