package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketEventEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@RunWith(JUnit4.class)
@ActiveProfiles(profiles = "test")
public class BasketEventTest {

    @Test
    public void toEntity() {
        BasketPlayer basketPlayer = BasketPlayer.builder()
                .id("id")
                .build();

        Date date = new Date();
        BasketEventType type = BasketEventType.DOUBLE_DOUBLE_STREAK;
        BasketEventLevel level = BasketEventLevel.SEASON;
        BasketEvent basketEvent = BasketEvent.builder()
                .basketPlayer(basketPlayer)
                .type(type)
                .basketEventLevel(level)
                .date(date)
                .build();

        BasketEventEntity basketEventEntity = basketEvent.toEntity();
        Assert.assertEquals(basketPlayer.getId(), basketEventEntity.getBasketPlayer().getId());
        Assert.assertEquals(date, basketEventEntity.getDate());
        Assert.assertEquals(type.getCode(), basketEventEntity.getType());
        Assert.assertEquals(level.getCode(), basketEventEntity.getLevel());
        Assert.assertNotNull(basketEventEntity.getLevel());
    }
}