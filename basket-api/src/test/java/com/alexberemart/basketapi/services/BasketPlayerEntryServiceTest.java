package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntryEntity;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.repositories.BasketPlayerEntryRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasketPlayerEntryServiceTest {

    @Mock
    protected BasketPlayerEntryRepository basketPlayerEntryRepository;
    @Mock
    protected BasketMatchServices basketMatchServices;
    @Mock
    protected BasketPlayerServices basketPlayerServices;
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    protected ModelMapper modelMapper;

    @Before
    public void setUp() {
        basketPlayerEntryServices = new BasketPlayerEntryServices(
                basketPlayerEntryRepository,
                basketMatchServices,
                basketPlayerServices,
                modelMapper);
    }

    @Test
    public void save() {

        Integer rebounds = 2;
        Integer assists = 2;

        when(basketPlayerEntryRepository.save(any(BasketPlayerEntryEntity.class)))
                .thenReturn(BasketPlayerEntryEntity.builder()
                        .rebounds(rebounds)
                        .assists(assists)
                        .build());

        BasketPlayerEntry basketPlayerEntry = BasketPlayerEntry.builder()
                .basketMatch(new BasketMatch())
                .rebounds(rebounds)
                .assists(assists)
                .basketMatchTeamType(BasketMatchTeamType.HOME)
                .build();
        BasketPlayerEntry savePlayerEntry = basketPlayerEntryServices.save(basketPlayerEntry);
        Assert.assertNotNull(savePlayerEntry);
        Assert.assertEquals(rebounds, savePlayerEntry.getRebounds());
        Assert.assertEquals(assists, savePlayerEntry.getAssists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorBecauseBasketMatchIsNotReported() {

        BasketPlayerEntry basketPlayerEntry = BasketPlayerEntry.builder()
                .build();
        BasketPlayerEntry savePlayerEntry = basketPlayerEntryServices.save(basketPlayerEntry);
        Assert.assertNotNull(savePlayerEntry);
    }

    @Test
    public void toEntity() {
        BasketMatch basketMatch = BasketMatch.builder()
                .id("id")
                .gameKey("gameKey")
                .build();
        Integer points = 10;
        BasketPlayerEntry basketReferenceMatch = BasketPlayerEntry.builder()
                .points(points)
                .basketMatch(basketMatch)
                .basketMatchTeamType(BasketMatchTeamType.HOME)
                .build();
        BasketPlayerEntryEntity basketMatchEntity = basketPlayerEntryServices.toEntity(basketReferenceMatch);
        Assert.assertEquals(points, basketMatchEntity.getPoints());
        Assert.assertNotNull(basketMatchEntity.getBasketMatch());
    }

    @Test
    public void findGroupedByPlayer() {
        BasketPlayerEntity basketPlayerEntity1 = BasketPlayerEntity.builder()
                .build();

        BasketPlayerEntity basketPlayerEntity2 = BasketPlayerEntity.builder()
                .build();

        when(basketPlayerEntryRepository.findGroupedByPlayer())
                .thenReturn(Arrays.asList(
                        basketPlayerEntity1,
                        basketPlayerEntity2
                ));

        List<BasketPlayer> basketPlayers = basketPlayerEntryServices.findGroupedByPlayer();
        Assert.assertEquals(2, basketPlayers.size());
    }

    @Test
    public void findByBasketPlayerOrderByBasketMatch_DateAsc() {
        BasketPlayerEntryEntity namesOnly1 = BasketPlayerEntryEntity.builder().build();
        BasketPlayerEntryEntity namesOnly2 = BasketPlayerEntryEntity.builder().build();

        BasketPlayer basketPlayer = BasketPlayer.builder()
                .build();

        when(basketPlayerEntryRepository.findByBasketPlayerOrderByBasketMatch_DateAsc(any(BasketPlayerEntity.class)))
                .thenReturn(Arrays.asList(
                        namesOnly1,
                        namesOnly2
                ));

        List<BasketPlayerEntry> basketPlayerEntries = basketPlayerEntryServices.findByBasketPlayerOrderByBasketMatch_DateAsc(basketPlayer);
        Assert.assertEquals(2, basketPlayerEntries.size());
    }

    @Test
    public void getElementWithMinDate() {

        DateTimeFormatter pattern = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime earlyDate = pattern.parseDateTime("01/01/2017");
        DateTime oldDate = pattern.parseDateTime("01/01/2018");

        BasketMatch earlyBasketMatch = BasketMatch.builder()
                .date(earlyDate.toDate())
                .build();

        BasketMatch oldBasketMatch = BasketMatch.builder()
                .date(oldDate.toDate())
                .build();

        BasketPlayerEntry earlyBasketPlayerEntry = BasketPlayerEntry.builder()
                .basketMatch(earlyBasketMatch)
                .build();

        BasketPlayerEntry oldBasketPlayerEntry = BasketPlayerEntry.builder()
                .basketMatch(oldBasketMatch)
                .build();

        List<BasketPlayerEntry> basketPlayerEntryList = new ArrayList<>();
        basketPlayerEntryList.add(earlyBasketPlayerEntry);
        basketPlayerEntryList.add(oldBasketPlayerEntry);

        Date minDate = basketPlayerEntryServices.getElementWithMinDate(basketPlayerEntryList);
        Assert.assertEquals(earlyDate.toDate(), minDate);
    }

    @Test
    public void getElementWithMaxDate() {

        DateTimeFormatter pattern = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime earlyDate = pattern.parseDateTime("01/01/2017");
        DateTime oldDate = pattern.parseDateTime("01/01/2018");

        BasketMatch earlyBasketMatch = BasketMatch.builder()
                .date(earlyDate.toDate())
                .build();

        BasketMatch oldBasketMatch = BasketMatch.builder()
                .date(oldDate.toDate())
                .build();

        BasketPlayerEntry earlyBasketPlayerEntry = BasketPlayerEntry.builder()
                .basketMatch(earlyBasketMatch)
                .build();

        BasketPlayerEntry oldBasketPlayerEntry = BasketPlayerEntry.builder()
                .basketMatch(oldBasketMatch)
                .build();

        List<BasketPlayerEntry> basketPlayerEntryList = new ArrayList<>();
        basketPlayerEntryList.add(earlyBasketPlayerEntry);
        basketPlayerEntryList.add(oldBasketPlayerEntry);

        Date maxDate = basketPlayerEntryServices.getElementWithMaxDate(basketPlayerEntryList);
        Assert.assertEquals(oldDate.toDate(), maxDate);
    }

    @Test
    public void getSeasonLeaderByPoints() {

        String seasonId = "season1";
        Season season = Season.builder()
                .id(seasonId)
                .build();

        String playerName = "pepe";
        BasketPlayerEntity basketPlayerEntity = BasketPlayerEntity.builder()
                .name(playerName)
                .build();

        int maxPoints = 10;
        FindSumStatGroupedByPlayerBySeason findSumPointsGroupedByPlayerBySeason = FindSumStatGroupedByPlayerBySeason.builder()
                .basketPlayerEntity(basketPlayerEntity)
                .statValue(maxPoints)
                .build();

        when(basketPlayerEntryRepository.findSumPointsGroupedByPlayerBySeason(seasonId))
                .thenReturn(Arrays.asList(
                        findSumPointsGroupedByPlayerBySeason
                ));

        BasketPlayerEntry basketPlayerEntry = basketPlayerEntryServices.getSeasonLeaderByPoints(season);
        Assert.assertEquals(maxPoints, basketPlayerEntry.getPoints(), 0);
        Assert.assertEquals(playerName, basketPlayerEntry.getBasketPlayer().getName());
    }

    @Test
    public void getSeasonLeaderByRebounds() {

        String seasonId = "season1";
        Season season = Season.builder()
                .id(seasonId)
                .build();

        String playerName = "pepe";
        BasketPlayerEntity basketPlayerEntity = BasketPlayerEntity.builder()
                .name(playerName)
                .build();

        int maxRebounds = 10;
        FindSumStatGroupedByPlayerBySeason findSumPointsGroupedByPlayerBySeason = FindSumStatGroupedByPlayerBySeason.builder()
                .basketPlayerEntity(basketPlayerEntity)
                .statValue(maxRebounds)
                .build();

        when(basketPlayerEntryRepository.findSumReboundsGroupedByPlayerBySeason(seasonId))
                .thenReturn(Arrays.asList(
                        findSumPointsGroupedByPlayerBySeason
                ));

        BasketPlayerEntry basketPlayerEntry = basketPlayerEntryServices.getSeasonLeaderByRebounds(season);
        Assert.assertEquals(maxRebounds, basketPlayerEntry.getRebounds(), 0);
        Assert.assertEquals(playerName, basketPlayerEntry.getBasketPlayer().getName());
    }

    @Test
    public void findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc() {

        String seasonId = "season1";

        Integer points = 10;
        BasketPlayerEntryEntity basketPlayerEntryEntity = BasketPlayerEntryEntity.builder()
                .points(points)
                .build();

        when(basketPlayerEntryRepository.findByBasketMatch_season_WebKeyOrderByBasketMatch_DateDesc(seasonId))
                .thenReturn(Arrays.asList(
                        basketPlayerEntryEntity
                ));

        List<BasketPlayerEntry> basketPlayerEntryList = basketPlayerEntryServices.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(seasonId);
        Assert.assertEquals(points, basketPlayerEntryList.get(0).getPoints(), 0);
    }

    @Test
    public void findAllByOrderByBasketMatch_DateDesc() {

        Integer points = 10;
        BasketPlayerEntryEntity basketPlayerEntryEntity = BasketPlayerEntryEntity.builder()
                .points(points)
                .build();

        when(basketPlayerEntryRepository.findAllByOrderByBasketMatch_DateDesc())
                .thenReturn(Arrays.asList(
                        basketPlayerEntryEntity
                ));

        List<BasketPlayerEntry> basketPlayerEntryList = basketPlayerEntryServices.findAllByOrderByBasketMatch_DateDesc();
        Assert.assertEquals(points, basketPlayerEntryList.get(0).getPoints(), 0);
    }

    @Test
    public void findByBasketMatch() {

        String gameKey = "gameKey";

        Integer points = 10;
        BasketPlayerEntryEntity basketPlayerEntryEntity = BasketPlayerEntryEntity.builder()
                .points(points)
                .build();

        when(basketPlayerEntryRepository.findByBasketMatch_GameKey(gameKey))
                .thenReturn(Arrays.asList(
                        basketPlayerEntryEntity
                ));

        List<BasketPlayerEntry> basketPlayerEntryList = basketPlayerEntryServices.findByBasketMatch(gameKey);
        Assert.assertEquals(points, basketPlayerEntryList.get(0).getPoints(), 0);
    }

}
