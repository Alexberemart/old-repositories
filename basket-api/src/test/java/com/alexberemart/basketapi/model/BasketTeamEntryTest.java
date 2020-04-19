package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RunWith(JUnit4.class)
@ActiveProfiles(profiles = "test")
public class BasketTeamEntryTest {

    @Test
    public void toEntity() {
        Date date = new Date();
        String id = "id";
        String name = "name";
        String referenceId = "referenceId";
        BasketMatchTeamType basketMatchTeamType = BasketMatchTeamType.HOME;
        int points = 10;
        BasketTeam basketTeam = BasketTeam.builder()
                .id(id)
                .name(name)
                .referenceId(referenceId)
                .build();

        BasketMatch basketMatch = BasketMatch.builder()
                .date(date)
                .build();

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .basketTeam(basketTeam)
                .basketMatch(basketMatch)
                .points(points)
                .type(basketMatchTeamType)
                .build();

        BasketTeamEntryEntity basketTeamEntity = basketTeamEntry.toEntity();
        Assert.assertEquals(basketTeam.getId(), basketTeamEntity.getBasketTeam().getId());
        Assert.assertEquals(basketTeam.getName(), basketTeamEntity.getBasketTeam().getName());
        Assert.assertEquals(basketTeam.getReferenceId(), basketTeamEntity.getBasketTeam().getReferenceId());
        Assert.assertEquals(basketMatch.getDate(), basketTeamEntity.getBasketMatch().getDate());
        Assert.assertEquals(basketTeamEntry.type.getCode(), basketTeamEntity.getType());
        Assert.assertEquals(points, basketTeamEntity.getPoints(), 0);
    }

    @Test
    public void isDoubleDouble() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date1 = df.parse("2013-01-01");
        Date date2 = df.parse("2014-01-01");
        Date date3 = df.parse("2015-01-01");

        BasketMatch basketMatch1 = BasketMatch.builder()
                .date(date1)
                .build();

        BasketMatch basketMatch2 = BasketMatch.builder()
                .date(date2)
                .build();

        BasketMatch basketMatch3 = BasketMatch.builder()
                .date(date3)
                .build();

        BasketPlayerEntry basketPlayerEntry1 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch1)
                .build();

        BasketPlayerEntry basketPlayerEntry2 = BasketPlayerEntry.builder()
                .points(null)
                .rebounds(null)
                .basketMatch(basketMatch2)
                .build();

        BasketPlayerEntry basketPlayerEntry3 = BasketPlayerEntry.builder()
                .points(0)
                .rebounds(0)
                .basketMatch(basketMatch3)
                .build();

        BasketPlayerEntry basketPlayerEntry4 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(null)
                .basketMatch(basketMatch3)
                .build();

        BasketPlayerEntry basketPlayerEntry5 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(null)
                .assists(20)
                .basketMatch(basketMatch3)
                .build();

        Boolean doubleDouble = basketPlayerEntry1.isDoubleDouble();
        Assert.assertEquals(Boolean.TRUE, doubleDouble);
        doubleDouble = basketPlayerEntry2.isDoubleDouble();
        Assert.assertEquals(Boolean.FALSE, doubleDouble);
        doubleDouble = basketPlayerEntry3.isDoubleDouble();
        Assert.assertEquals(Boolean.FALSE, doubleDouble);
        doubleDouble = basketPlayerEntry4.isDoubleDouble();
        Assert.assertEquals(Boolean.FALSE, doubleDouble);
        doubleDouble = basketPlayerEntry5.isDoubleDouble();
        Assert.assertEquals(Boolean.TRUE, doubleDouble);
    }
}