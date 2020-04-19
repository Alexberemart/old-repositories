package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.entities.BasketEventEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@RunWith(JUnit4.class)
@ActiveProfiles(profiles = "test")
public class BasketEventEntityTest {

    @Test
    public void toModel(){
        BasketPlayerEntity basketPlayerEntity = BasketPlayerEntity.builder()
                .id("id")
                .build();

        Date date = new Date();
        BasketEventType type = BasketEventType.DOUBLE_DOUBLE_STREAK;
        BasketEventLevel level =  BasketEventLevel.MATCH;
        BasketEventEntity basketEventEntity = BasketEventEntity.builder()
                .basketPlayer(basketPlayerEntity)
                .type(type.getCode())
                .date(date)
                .level(level.getCode())
                .build();

        BasketEvent basketEvent = basketEventEntity.toModel();
        Assert.assertEquals(basketPlayerEntity.getId(), basketEvent.getBasketPlayer().getId());
        Assert.assertEquals(date, basketEvent.getDate());
        Assert.assertEquals(type.getCode(), basketEvent.getType().getCode());
        Assert.assertEquals(level.getCode(), basketEvent.getBasketEventLevel().getCode());
    }
}