package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketMatchEntity;
import com.alexberemart.basketapi.entities.BasketTeamEntity;
import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.repositories.BasketTeamEntryLimited;
import com.alexberemart.basketapi.repositories.BasketTeamEntryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasketTeamEntryServiceTest {

    @Mock
    protected BasketTeamEntryRepository basketTeamEntryRepository;
    protected BasketTeamEntryServices basketTeamEntryServices;

    @Before
    public void setUp() {
        basketTeamEntryServices = new BasketTeamEntryServices(basketTeamEntryRepository);
    }

    @Test
    public void save() {
        int points = 10;

        when(basketTeamEntryRepository.save(any(BasketTeamEntryEntity.class)))
                .thenReturn(BasketTeamEntryEntity.builder()
                        .id("id")
                        .points(points)
                        .build());

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .basketMatch(new BasketMatch())
                .type(BasketMatchTeamType.HOME)
                .basketTeam(new BasketTeam())
                .points(points)
                .build();

        BasketTeamEntry saveTeamEntry = basketTeamEntryServices.save(basketTeamEntry);
        verify(basketTeamEntryRepository).save(any(BasketTeamEntryEntity.class));
        Assert.assertNotNull(saveTeamEntry);
        Assert.assertEquals(points, saveTeamEntry.getPoints(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorBecauseBasketMatchIsNotReported(){

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .type(BasketMatchTeamType.HOME)
                .basketTeam(new BasketTeam())
                .build();

        basketTeamEntryServices.save(basketTeamEntry);
    }

    @Test
    public void toModelList(){

        BasketMatchTeamType basketMatchTeamType = BasketMatchTeamType.HOME;
        BasketTeamEntryEntity basketTeamEntryEntity = BasketTeamEntryEntity.builder()
                .type(basketMatchTeamType.getCode())
                .basketTeam(new BasketTeamEntity())
                .build();

        List<BasketTeamEntry> basketTeamEntryList = basketTeamEntryServices.toModelList(Arrays.asList(basketTeamEntryEntity));

        Assert.assertEquals(basketMatchTeamType, basketTeamEntryList.get(0).getType());
    }

    @Test
    public void getLastTeamEntryByTeamAndDates() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateStart = df.parse("2013-01-01");
        Date dateEnd = df.parse("2019-01-01");
        BasketMatchTeamType basketMatchTeamType = BasketMatchTeamType.HOME;

        BasketTeamEntryEntity basketTeamEntryEntity = BasketTeamEntryEntity.builder()
                .type(basketMatchTeamType.getCode())
                .basketTeam(new BasketTeamEntity())
                .build();

        String teamId = "team100";
        when(basketTeamEntryRepository.findByBasketTeam_IdAndBasketMatch_DateBetweenOrderByBasketMatch_DateDesc(teamId, dateStart, dateEnd))
                .thenReturn(Arrays.asList(basketTeamEntryEntity));

        BasketTeamEntry basketTeamEntry = basketTeamEntryServices.getLastTeamEntryByTeamAndDates(teamId, dateStart, dateEnd);

        Assert.assertEquals(basketMatchTeamType, basketTeamEntry.getType());
    }

    @Test
    public void findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = df.parse("2013-01-01");
        String teamId = "team100";
        String matchId = "match100";

        BasketMatchEntity basketMatchEntity = BasketMatchEntity.builder()
                .id(matchId)
                .build();

        BasketTeamEntryLimitedImpl basketTeamEntryLimited =  BasketTeamEntryLimitedImpl.builder()
                .basketMatchEntity(basketMatchEntity)
                .build();

        when(basketTeamEntryRepository.findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(date, matchId, teamId))
                .thenReturn(Arrays.asList(basketTeamEntryLimited));

        List<BasketTeamEntryLimited> basketTeamEntryLimiteds = basketTeamEntryServices.findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(date, matchId, teamId);

        Assert.assertEquals(matchId, basketTeamEntryLimiteds.get(0).getBasketMatch().getId());
    }

    @Test
    public void getOtherTeamEntryFromMatch(){

        String teamId = "team100";
        String otherTeamId = "team200";
        String matchId = "match100";

        BasketTeamEntity basketTeamEntity = BasketTeamEntity.builder()
                .id(otherTeamId)
                .build();

        BasketTeamEntryEntity basketTeamEntryEntity =  BasketTeamEntryEntity.builder()
                .basketTeam(basketTeamEntity)
                .build();

        when(basketTeamEntryRepository.findTopByBasketMatch_IdAndAndBasketTeam_IdNot(matchId, teamId))
                .thenReturn(Arrays.asList(basketTeamEntryEntity));

        BasketTeamEntry otherTeamEntryFromMatch = basketTeamEntryServices.getOtherTeamEntryFromMatch(matchId, teamId);

        Assert.assertEquals(otherTeamId, otherTeamEntryFromMatch.getBasketTeam().getId());
    }

    @Test
    public void getLastTeamEntryFromDate() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = df.parse("2013-01-01");
        String teamId = "team100";

        BasketTeamEntity basketTeamEntity = BasketTeamEntity.builder()
                .id(teamId)
                .build();

        BasketTeamEntryEntity basketTeamEntryEntity =  BasketTeamEntryEntity.builder()
                .basketTeam(basketTeamEntity)
                .build();

        when(basketTeamEntryRepository.findTopByBasketMatch_DateLessThanAndAndBasketTeam_IdOrderByBasketMatch_DateDesc(date, teamId))
                .thenReturn(Arrays.asList(basketTeamEntryEntity));

        BasketTeamEntry otherTeamEntryFromMatch = basketTeamEntryServices.getLastTeamEntryFromDate(date, teamId);

        Assert.assertEquals(teamId, otherTeamEntryFromMatch.getBasketTeam().getId());
    }

}
