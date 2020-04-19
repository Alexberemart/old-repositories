package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.services.BasketEventServices;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import com.alexberemart.basketapi.services.SeasonServices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "test")
public class GetDoubleDoubleEventFromSeasonTest {

    @Mock
    protected BasketEventServices basketEventServices;
    @Mock
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    protected GetDoubleDoubleEventFromSeason getDoubleDoubleEventFromSeason;

    @Before
    public void setUp() {
        getDoubleDoubleEventFromSeason = new GetDoubleDoubleEventFromSeason(
                basketEventServices,
                basketPlayerEntryServices);
    }

    @Test
    public void getDoubleDoubleEvent() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date1 = df.parse("2013-01-01");
        Date date2 = df.parse("2014-01-01");
        Date date3 = df.parse("2015-01-01");
        Date date4 = df.parse("2016-01-01");
        Date date5 = df.parse("2017-01-01");
        Date date6 = df.parse("2018-01-01");

        String seasonKey = "seasonKey";

        Season season = Season.builder()
                .build();

        BasketPlayer basketPlayer1 = BasketPlayer.builder()
                .build();

        BasketPlayer basketPlayer2 = BasketPlayer.builder()
                .build();

        BasketMatch basketMatch1 = BasketMatch.builder()
                .date(date1)
                .build();

        BasketMatch basketMatch2 = BasketMatch.builder()
                .date(date2)
                .build();

        BasketMatch basketMatch3 = BasketMatch.builder()
                .date(date3)
                .build();

        BasketMatch basketMatch4 = BasketMatch.builder()
                .date(date4)
                .build();

        BasketMatch basketMatch5 = BasketMatch.builder()
                .date(date5)
                .build();

        BasketMatch basketMatch6 = BasketMatch.builder()
                .date(date6)
                .build();

        BasketPlayerEntry basketPlayerEntry1 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch1)
                .basketPlayer(basketPlayer1)
                .build();

        BasketPlayerEntry basketPlayerEntry2 = BasketPlayerEntry.builder()
                .points(null)
                .rebounds(null)
                .basketMatch(basketMatch2)
                .basketPlayer(basketPlayer1)
                .build();

        BasketPlayerEntry basketPlayerEntry3 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch3)
                .basketPlayer(basketPlayer1)
                .build();

        BasketPlayerEntry basketPlayerEntry4 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch4)
                .basketPlayer(basketPlayer1)
                .build();

        BasketPlayerEntry basketPlayerEntry5 = BasketPlayerEntry.builder()
                .points(0)
                .rebounds(0)
                .basketMatch(basketMatch5)
                .basketPlayer(basketPlayer1)
                .build();

        BasketPlayerEntry basketPlayerEntry6 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch6)
                .basketPlayer(basketPlayer1)
                .build();

        BasketPlayerEntry basketPlayerEntry11 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch1)
                .basketPlayer(basketPlayer2)
                .build();

        BasketPlayerEntry basketPlayerEntry12 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch2)
                .basketPlayer(basketPlayer2)
                .build();

        BasketPlayerEntry basketPlayerEntry13 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch3)
                .basketPlayer(basketPlayer2)
                .build();

        when(basketPlayerEntryServices.findGroupedByPlayerBySeason(seasonKey))
                .thenReturn(Arrays.asList(
                        basketPlayer1,
                        basketPlayer2
                ));

        when(basketPlayerEntryServices.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(basketPlayer1, seasonKey))
                .thenReturn(Arrays.asList(
                        basketPlayerEntry1,
                        basketPlayerEntry2,
                        basketPlayerEntry3,
                        basketPlayerEntry4,
                        basketPlayerEntry5,
                        basketPlayerEntry6
                ));

        when(basketPlayerEntryServices.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(basketPlayer2, seasonKey))
                .thenReturn(Arrays.asList(
                        basketPlayerEntry11,
                        basketPlayerEntry12,
                        basketPlayerEntry13
                ));

        Answer<BasketEvent> answer = new Answer<BasketEvent>() {
            public BasketEvent answer(InvocationOnMock invocation) throws Throwable {
                BasketPlayerEntry basketPlayerEntry = (BasketPlayerEntry) invocation.getArgument(1);
                return BasketEvent.builder()
                        .date(basketPlayerEntry.getBasketMatch().getDate())
                        .value(invocation.getArgument(0))
                        .type(invocation.getArgument(2))
                        .basketEventLevel(invocation.getArgument(3))
                        .basketPlayer(basketPlayerEntry.getBasketPlayer())
                        .webKey(invocation.getArgument(4))
                        .build();
            }
        };

        when(basketEventServices.createAndSaveEvent(
                any(Integer.class),
                any(BasketPlayerEntry.class),
                any(BasketEventType.class),
                any(BasketEventLevel.class),
                any(String.class)))
                .thenAnswer(answer);

        List<BasketEvent> downloaded = getDoubleDoubleEventFromSeason.execute(seasonKey);
        verify(basketEventServices, times(1)).deleteBySeason(seasonKey);
        Assert.assertEquals(10, downloaded.size());
        BasketEvent basketEvent = downloaded.get(0);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 1, basketEvent.getValue());
        Assert.assertEquals(basketPlayer1, basketEvent.getBasketPlayer());
        Assert.assertEquals(seasonKey, basketEvent.getWebKey());

        basketEvent = downloaded.get(1);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 2, basketEvent.getValue());
        Assert.assertEquals(basketPlayer1, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(2);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 3, basketEvent.getValue());
        Assert.assertEquals(basketPlayer1, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(3);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE_STREAK, basketEvent.getType());
        Assert.assertEquals((Integer) 2, basketEvent.getValue());
        Assert.assertEquals(basketPlayer1, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(4);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 4, basketEvent.getValue());
        Assert.assertEquals(basketPlayer1, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(5);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 1, basketEvent.getValue());
        Assert.assertEquals(basketPlayer2, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(6);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 2, basketEvent.getValue());
        Assert.assertEquals(basketPlayer2, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(7);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE_STREAK, basketEvent.getType());
        Assert.assertEquals((Integer) 2, basketEvent.getValue());
        Assert.assertEquals(basketPlayer2, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(8);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE, basketEvent.getType());
        Assert.assertEquals((Integer) 3, basketEvent.getValue());
        Assert.assertEquals(basketPlayer2, basketEvent.getBasketPlayer());

        basketEvent = downloaded.get(9);
        Assert.assertEquals(BasketEventType.DOUBLE_DOUBLE_STREAK, basketEvent.getType());
        Assert.assertEquals((Integer) 3, basketEvent.getValue());
        Assert.assertEquals(basketPlayer2, basketEvent.getBasketPlayer());
    }

}
