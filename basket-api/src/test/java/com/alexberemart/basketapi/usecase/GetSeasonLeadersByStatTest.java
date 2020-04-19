package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import com.alexberemart.basketapi.services.SeasonServices;
import com.alexberemart.basketapi.usecase.model.SeasonLeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "test")
public class GetSeasonLeadersByStatTest {

    @Mock
    public SeasonServices seasonServices;
    @Mock
    public BasketPlayerEntryServices basketPlayerEntryServices;
    protected GetSeasonLeadersByStat getSeasonLeadersByStat;

    @Before
    public void setUp() {
        getSeasonLeadersByStat = new GetSeasonLeadersByStat(
                seasonServices,
                basketPlayerEntryServices);
    }

    @Test
    public void execute() {

        Season season = Season.builder()
                .build();

        BasketPlayer basketPlayer = BasketPlayer.builder()
                .build();

        int points = 10;
        BasketPlayerEntry basketPlayerEntry = BasketPlayerEntry.builder()
                .basketPlayer(basketPlayer)
                .points(points)
                .build();

        when(seasonServices.findAll())
                .thenReturn(Arrays.asList(season));

        when(basketPlayerEntryServices.getSeasonLeaderByPoints(season))
                .thenReturn(basketPlayerEntry);

        when(basketPlayerEntryServices.getSeasonLeaderByRebounds(season))
                .thenReturn(basketPlayerEntry);

        List<SeasonLeaders> seasonLeadersList = getSeasonLeadersByStat.execute();
        Assert.assertEquals(1, seasonLeadersList.size());
        Assert.assertEquals(points, seasonLeadersList.get(0).getPoints(), 0);
    }

}
