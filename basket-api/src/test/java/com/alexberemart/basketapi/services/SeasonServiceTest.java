package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.SeasonEntity;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketapi.repositories.SeasonRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = {RepositoryConfiguration.class})
public class SeasonServiceTest {

    @Mock
    protected SeasonRepository seasonRepository;
    protected SeasonServices seasonServices;

    @Before
    public void setUp() {
        seasonServices = new SeasonServices(
                seasonRepository);
    }

    @Test
    public void save() {

        when(seasonRepository.save(any(SeasonEntity.class)))
                .thenReturn(SeasonEntity.builder()
                        .build());

        Season season = Season.builder()
                .id("id")
                .build();
        seasonServices.save(season);
    }

    @Test
    public void shouldntSaveBecauseBasketPlayerHasSameReference() {

        when(seasonRepository.save(any(SeasonEntity.class)))
                .thenReturn(new SeasonEntity());

        String firstReference = "alex";
        Season season = Season.builder()
                .id("1L")
                .webKey(firstReference)
                .build();
        Season firstSeasonSaved = seasonServices.saveIfNotExist(season);
        Assert.assertNotNull(firstSeasonSaved);
        Season secondSeason = Season.builder()
                .id("2L")
                .webKey(firstReference)
                .build();

        when(seasonRepository.findByWebKey(secondSeason.getWebKey()))
                .thenReturn(Arrays.asList(seasonServices.toEntity(season)));

        Season secondSeasonSaved = seasonServices.saveIfNotExist(secondSeason);
        Assert.assertEquals(season.getId(), secondSeasonSaved.getId());
    }

    @Test
    public void shouldSaveBecauseBasketPlayerHasOtherReference() {

        when(seasonRepository.save(any(SeasonEntity.class)))
                .thenReturn(new SeasonEntity());

        String firstReference = "alex";
        Season season = Season.builder()
                .id("1L")
                .webKey(firstReference)
                .build();
        Season firstSeasonSaved = seasonServices.saveIfNotExist(season);
        Assert.assertNotNull(firstSeasonSaved);

        String otherReference = "alex1";
        Season thirdSeason = Season.builder()
                .id("1L")
                .webKey(otherReference)
                .build();
        Season thirdSeasonSaved = seasonServices.saveIfNotExist(thirdSeason);
        Assert.assertNotNull(thirdSeasonSaved);
    }

    @Test
    public void toEntity() {
        Date startDate = new Date();
        Season season = Season.builder()
                .startDate(startDate)
                .build();
        SeasonEntity seasonEntity = seasonServices.toEntity(season);
        Assert.assertEquals(startDate, seasonEntity.getStartDate());
    }

    @Test
    public void findByWebKey() {

        when(seasonRepository.findByWebKey("91"))
                .thenReturn(Arrays.asList(SeasonEntity.builder()
                        .build()));

        Season season = seasonServices.findByWebKey("91");
        Assert.assertNotNull(season);
        season = seasonServices.findByWebKey("92");
        Assert.assertNull(season);
    }

}
