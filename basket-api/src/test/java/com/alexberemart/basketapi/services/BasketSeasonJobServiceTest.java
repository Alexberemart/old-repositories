package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketSeasonJobEntity;
import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.repositories.BasketSeasonJobRepository;
import com.alexberemart.basketapi.repositories.BasketTeamEntryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasketSeasonJobServiceTest {

    @Mock
    protected BasketSeasonJobRepository basketSeasonJobRepository;
    protected BasketSeasonJobServices basketSeasonJobServices;

    @Before
    public void setUp() {
        basketSeasonJobServices = new BasketSeasonJobServices(basketSeasonJobRepository);
    }

    @Test
    public void save() {

        BasketSeasonJob basketSeasonJob = BasketSeasonJob.builder()
                .webKey("webKey")
                .state(BasketSeasonJobState.INIT)
                .build();

        BasketSeasonJobEntity basketSeasonJobEntity = BasketSeasonJobEntity.builder()
                .webKey("webKey")
                .state(0)
                .build();

        when(basketSeasonJobRepository.save(any(BasketSeasonJobEntity.class)))
                .thenReturn(basketSeasonJobEntity);

        basketSeasonJobServices.save(basketSeasonJob);
    }

    @Test
    public void findNonFinished() {
        String webKey = "webKey";
        BasketSeasonJobEntity basketSeasonJobEntity = BasketSeasonJobEntity.builder()
                .webKey(webKey)
                .build();

        List<BasketSeasonJobEntity> basketSeasonJobEntities = new ArrayList<>();
        basketSeasonJobEntities.add(basketSeasonJobEntity);

        when(basketSeasonJobRepository.findByStateNot(any()))
                .thenReturn(basketSeasonJobEntities);

        List<BasketSeasonJob> basketSeasonJobList = basketSeasonJobServices.findNonFinished();
        Assert.assertEquals(webKey, basketSeasonJobList.get(0).getWebKey());
    }

}
