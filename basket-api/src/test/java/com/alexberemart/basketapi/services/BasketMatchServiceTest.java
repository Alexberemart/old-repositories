package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.BasketMatchEntity;
import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.repositories.BasketMatchRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = {RepositoryConfiguration.class})
public class BasketMatchServiceTest {

    @Mock
    protected BasketMatchRepository basketMatchRepository;
    @Mock
    protected BasketTeamEntryServices basketTeamEntryServices;
    protected BasketMatchServicesImpl basketMatchServicesImpl;

    @Before
    public void setUp() {
        basketMatchServicesImpl = new BasketMatchServicesImpl(
                basketMatchRepository,
                basketTeamEntryServices);
    }

    @Test
    public void save() {

        when(basketMatchRepository.save(any(BasketMatchEntity.class)))
                .thenReturn(BasketMatchEntity.builder()
                        .build());

        BasketMatch basketMatch = BasketMatch.builder()
                .gameKey("gameKey")
                .date(new Date())
                .build();
        basketMatchServicesImpl.save(basketMatch);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAErrorBecauseDateIsNotReported() {

        BasketMatch basketMatch = BasketMatch.builder()
                .gameKey("gameKey")
                .build();
        basketMatchServicesImpl.save(basketMatch);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAErrorBecauseGameKeyIsNotReported() {

        BasketMatch basketMatch = BasketMatch.builder()
                .date(new Date())
                .build();
        basketMatchServicesImpl.save(basketMatch);
    }

    @Test
    public void toEntity() {
        String gameKey = "gameKey";
        BasketMatch basketReferenceMatch = BasketMatch.builder()
                .gameKey(gameKey)
                .build();
        BasketMatchEntity basketMatchEntity = basketMatchServicesImpl.toEntity(basketReferenceMatch);
        Assert.assertEquals(gameKey, basketMatchEntity.getGameKey());
    }

    @Test
    public void findByGameKey() {
        String gameKey = "gameKey";
        when(basketMatchRepository.findByGameKey(gameKey))
                .thenReturn(Arrays.asList(new BasketMatchEntity()));

        List<BasketMatch> basketMatches = basketMatchServicesImpl.findByGameKey(gameKey);
        Assert.assertEquals(1, basketMatches.size());
    }

    @Test
    public void findFirstByOrderByDateDesc() {
        String gameKey = "gameKey";

        BasketMatchEntity basketMatchEntity = BasketMatchEntity.builder()
                .gameKey(gameKey)
                .build();

        when(basketMatchRepository.findFirstByOrderByDateDesc())
                .thenReturn(basketMatchEntity);

        BasketMatch basketMatch = basketMatchServicesImpl.findFirstByOrderByDateDesc();
        Assert.assertEquals(gameKey, basketMatch.getGameKey());
    }

    @Test
    public void findBySeason_IdIn() {
        String gameKey = "gameKey";

        List<String> seasonIdList = new ArrayList<>();
        seasonIdList.add("season_1");

        BasketMatchEntity basketMatchEntity = BasketMatchEntity.builder()
                .gameKey(gameKey)
                .build();

        when(basketMatchRepository.findBySeason_IdIn(seasonIdList))
                .thenReturn(Arrays.asList(basketMatchEntity));

        List<BasketMatch> basketMatchList = basketMatchServicesImpl.findBySeason_IdIn(seasonIdList);
        Assert.assertEquals(gameKey, basketMatchList.get(0).getGameKey());
    }

    @Test
    public void findBySeason_WebKey() {
        String gameKey = "gameKey";
        String webKey = "webKey";

        List<String> seasonIdList = new ArrayList<>();
        seasonIdList.add("season_1");

        BasketMatchEntity basketMatchEntity = BasketMatchEntity.builder()
                .gameKey(gameKey)
                .build();

        when(basketMatchRepository.findBySeason_WebKey(webKey))
                .thenReturn(Arrays.asList(basketMatchEntity));

        List<BasketMatch> basketMatchList = basketMatchServicesImpl.findBySeason_WebKey(webKey);
        Assert.assertEquals(gameKey, basketMatchList.get(0).getGameKey());
    }

}
