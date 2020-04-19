package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.controllers.dto.BasketEventDto;
import com.alexberemart.basketapi.entities.BasketEventEntity;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.repositories.BasketEventRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasketEventServicesTest {

    @Mock
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    @Mock
    protected SeasonServices seasonServices;
    @Mock
    protected BasketEventRepository basketEventRepository;
    protected BasketEventServices basketEventServices;

    @Before
    public void setUp() {
        basketEventServices = new BasketEventServices(
                basketEventRepository,
                seasonServices);
        basketEventServices = Mockito.spy(basketEventServices);
    }

    @Test
    public void deleteBySeason() {
        String seasonKey = "seasonKey";

        BasketEventEntity basketEventEntity = BasketEventEntity.builder()
                .build();

        List<BasketEventEntity> basketEventEntities = Arrays.asList(
                basketEventEntity
        );

        when(basketEventRepository.findByWebKeyAndLevel(seasonKey, 1))
                .thenReturn(basketEventEntities);

        List<BasketEvent> basketEvents = basketEventServices.deleteBySeason(seasonKey);
        Assert.assertEquals(1, basketEvents.size());
        verify(basketEventRepository).deleteAll(basketEventEntities);
    }

    @Test
    public void createAndSaveEvent() throws ParseException {
        Season season = new Season();
        String webKey = "webKey";

        BasketPlayer basketPlayer1 = BasketPlayer.builder()
                .build();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date1 = df.parse("2013-01-01");

        BasketMatch basketMatch1 = BasketMatch.builder()
                .date(date1)
                .build();

        BasketPlayerEntry basketPlayerEntry1 = BasketPlayerEntry.builder()
                .points(10)
                .rebounds(10)
                .basketMatch(basketMatch1)
                .build();

        Answer<BasketEvent> answer = new Answer<BasketEvent>() {
            public BasketEvent answer(InvocationOnMock invocation) throws Throwable {
                BasketEvent basketEvent = invocation.getArgument(0);
                if (basketEvent.getBasketEventLevel() == null){
                    throw new Exception();
                }
                return basketEvent;
            }
        };

        doAnswer(answer).when(basketEventServices).save(any(BasketEvent.class));

        BasketEvent basketEvent = basketEventServices.createAndSaveEvent(1, basketPlayerEntry1, BasketEventType.DOUBLE_DOUBLE, BasketEventLevel.MATCH, webKey);

        Assert.assertNotNull(basketEvent);
    }

    @Test
    public void save() {

        BasketEvent basketEvent = BasketEvent.builder()
                .type(BasketEventType.DOUBLE_DOUBLE)
                .webKey("webKey")
                .basketEventLevel(BasketEventLevel.MATCH)
                .build();

        when(basketEventRepository.save(any(BasketEventEntity.class)))
                .thenReturn(basketEvent.toEntity());

        basketEventServices.save(basketEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAErrorBecauseTypeIsNull() {
        BasketEvent basketEvent = BasketEvent.builder().build();
        basketEventServices.save(basketEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAErrorBecauseWebKeyIsNull() {
        BasketEvent basketEvent = BasketEvent.builder()
                .type(BasketEventType.DOUBLE_DOUBLE)
                .build();
        basketEventServices.save(basketEvent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAErrorBecauseLevelIsNull() {
        BasketEvent basketEvent = BasketEvent.builder()
                .type(BasketEventType.DOUBLE_DOUBLE)
                .webKey("webKey")
                .build();
        basketEventServices.save(basketEvent);
    }

    @Test
    public void findByWebKeyAndLevel() {
        String webKey = "91";
        BasketEventLevel basketEventLevel = BasketEventLevel.MATCH;
        when(basketEventRepository.findByWebKeyAndLevel(webKey, basketEventLevel.getCode()))
                .thenReturn(Arrays.asList(BasketEventEntity.builder()
                        .webKey(webKey)
                        .build()));

        List<BasketEvent> basketEvents = basketEventServices.findByWebKeyAndLevel(webKey, basketEventLevel);
        Assert.assertEquals(1, basketEvents.size());
        Assert.assertEquals(webKey, basketEvents.get(0).getWebKey());

    }

    @Test
    public void findAllWithPage() {
        String gameKey = "gameKey";

        BasketEventEntity basketEventEntity = BasketEventEntity.builder()
                .webKey(gameKey)
                .build();

        int page = 1;
        int size = 20;
        PageRequest pageable = new PageRequest(page, size);
        when(basketEventRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Arrays.asList(basketEventEntity)));

        List<BasketEvent> basketEvents = basketEventServices.findAll(page, size);
        verify(basketEventRepository).findAll(pageable);
        Assert.assertEquals(1, basketEvents.size());
    }

    @Test
    public void findAll() {
        String gameKey = "gameKey";

        BasketEventEntity basketEventEntity = BasketEventEntity.builder()
                .webKey(gameKey)
                .build();

        when(basketEventRepository.findAll())
                .thenReturn(Arrays.asList(basketEventEntity));

        List<BasketEvent> basketEvents = basketEventServices.findAll();
        verify(basketEventRepository).findAll();
        Assert.assertEquals(1, basketEvents.size());
    }

    @Test
    public void findAllSortedByDateAndCountEvent() {
        String gameKey = "gameKey";
        int page = 1;
        int size = 20;
        Integer countEventMatch = 2;
        Integer countEventSeason = 3;

        BasketEvent basketEvent1 = BasketEvent.builder()
                .webKey(gameKey)
                .value(1)
                .basketEventLevel(BasketEventLevel.MATCH)
                .type(BasketEventType.DOUBLE_DOUBLE)
                .build();

        BasketEvent basketEvent2 = BasketEvent.builder()
                .webKey(gameKey)
                .value(1)
                .basketEventLevel(BasketEventLevel.SEASON)
                .type(BasketEventType.DOUBLE_DOUBLE_STREAK)
                .build();

        BasketEventLimitedImpl basketEventLimited1 = BasketEventLimitedImpl.builder()
                .value(1)
                .level(0)
                .type(1)
                .count(countEventMatch)
                .build();
        BasketEventLimitedImpl basketEventLimited2 = BasketEventLimitedImpl.builder()
                .value(1)
                .level(1)
                .type(0)
                .count(countEventSeason)
                .build();

        when(basketEventRepository.findGroupByLevelAndTypeAndValue())
                .thenReturn(Arrays.asList(basketEventLimited1, basketEventLimited2));

        doReturn(Arrays.asList(basketEvent1, basketEvent2))
                .when(basketEventServices)
                .findAllSortedByDate(page, size);

        List<BasketEventDto> basketEvents = basketEventServices.findAllSortedByDateAndCountEvent(page, size);
        verify(basketEventRepository).findGroupByLevelAndTypeAndValue();
        Assert.assertEquals(2, basketEvents.size());
        Assert.assertEquals(basketEvent1, basketEvents.get(0).getBasketEvent());
        Assert.assertEquals(countEventMatch, basketEvents.get(0).getCount());
        Assert.assertEquals(basketEvent2, basketEvents.get(1).getBasketEvent());
        Assert.assertEquals(countEventSeason, basketEvents.get(1).getCount());
    }
}