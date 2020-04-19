package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.base.BasketApiEntityTest;
import com.alexberemart.basketapi.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class Top20EntryEntityTest extends BasketApiEntityTest {

    @Test
    public void toModel() {
        Date date = new Date();
        int position = 1;
        float rating = 20f;
        String webKey = "NBA_WK";
        BasketPlayerEntity basketPlayerEntity = BasketPlayerEntity.builder()
                .build();
        String id = "id";
        Top20EntryEntity top20EntryEntity = Top20EntryEntity.builder()
                .id(id)
                .date(date)
                .position(position)
                .rating(rating)
                .webKey(webKey)
                .basketPlayer(basketPlayerEntity)
                .build();

        Top20Entry top20Entry = top20EntryEntity.toModel();
        Assert.assertEquals(id, top20EntryEntity.getId());
        Assert.assertEquals(date, top20Entry.getDate());
        Assert.assertEquals(position, top20Entry.getPosition(), 0);
        Assert.assertEquals(rating, top20Entry.getRating(), 0);
    }
}