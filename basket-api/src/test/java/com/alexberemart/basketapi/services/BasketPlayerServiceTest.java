package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.model.BasketOrigin;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.repositories.BasketPlayerRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = {RepositoryConfiguration.class})
public class BasketPlayerServiceTest {

    @Mock
    protected BasketPlayerRepository basketPlayerRepository;
    @Mock
    protected BasketOriginServices basketOriginServices;
    protected BasketPlayerServices basketPlayerServices;

    @Before
    public void setUp() {
        basketPlayerServices = new BasketPlayerServices(
                basketPlayerRepository,
                basketOriginServices);
    }

    @Test
    public void save() {

        when(basketPlayerRepository.save(any(BasketPlayerEntity.class)))
                .thenReturn(new BasketPlayerEntity());

        BasketPlayer basketPlayer = BasketPlayer.builder()
                .id("1L")
                .name("alex")
                .referenceId("alex")
                .birthDate(new Date())
                .basketOrigin(new BasketOrigin())
                .build();
        basketPlayerServices.save(basketPlayer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldntSaveBecauseBasketOriginIsNull() {

        BasketPlayer basketPlayer = BasketPlayer.builder()
                .id("1L")
                .name("alex")
                .referenceId("alex")
                .birthDate(new Date())
                .build();
        basketPlayerServices.save(basketPlayer);
    }

    @Test
    public void shouldntSaveBecauseBasketPlayerHasSameReference() {

        when(basketPlayerRepository.save(any(BasketPlayerEntity.class)))
                .thenReturn(new BasketPlayerEntity());

        String firstReference = "alex";
        BasketPlayer basketPlayer = BasketPlayer.builder()
                .id("1L")
                .referenceId(firstReference)
                .birthDate(new Date())
                .basketOrigin(new BasketOrigin())
                .build();
        BasketPlayer firstBasketPlayerSaved = basketPlayerServices.saveIfNotExist(basketPlayer);
        Assert.assertNotNull(firstBasketPlayerSaved);
        BasketPlayer secondPlayer = BasketPlayer.builder()
                .id("2L")
                .referenceId(firstReference)
                .birthDate(new Date())
                .basketOrigin(new BasketOrigin())
                .build();

        when(basketPlayerRepository.findByReferenceId(secondPlayer.getReferenceId()))
                .thenReturn(Arrays.asList(basketPlayer.toEntity()));

        BasketPlayer secondBasketPlayerSaved = basketPlayerServices.saveIfNotExist(secondPlayer);
        Assert.assertEquals(basketPlayer.getId(), secondBasketPlayerSaved.getId());
    }

    @Test
    public void shouldSaveBecauseBasketPlayerHasOtherReference() throws IOException, ParseException {

        when(basketPlayerRepository.save(any(BasketPlayerEntity.class)))
                .thenReturn(new BasketPlayerEntity());

        String firstReference = "alex";
        BasketPlayer basketPlayer = BasketPlayer.builder()
                .id("1L")
                .referenceId(firstReference)
                .birthDate(new Date())
                .basketOrigin(new BasketOrigin())
                .build();
        BasketPlayer firstBasketPlayerSaved = basketPlayerServices.saveIfNotExist(basketPlayer);
        Assert.assertNotNull(firstBasketPlayerSaved);

        String otherReference = "alex1";
        BasketPlayer thirdBasketPlayer = BasketPlayer.builder()
                .id("1L")
                .referenceId(otherReference)
                .birthDate(new Date())
                .basketOrigin(new BasketOrigin())
                .build();
        BasketPlayer thirdBasketPlayerSaved = basketPlayerServices.saveIfNotExist(thirdBasketPlayer);
        Assert.assertNotNull(thirdBasketPlayerSaved);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorBecauseBirthDateIsNotReported() {
        BasketPlayer basketPlayer = BasketPlayer.builder()
                .referenceId("alex99")
                .build();

        basketPlayerServices.save(basketPlayer);
    }

    @Test
    public void findByReferenceId() {

        String referenceId = "referenceId";
        when(basketPlayerRepository.findByReferenceId(referenceId))
                .thenReturn(Arrays.asList(BasketPlayerEntity.builder()
                        .referenceId(referenceId)
                        .build()));

        BasketPlayer basketPlayers = basketPlayerServices.getByReferenceId(referenceId);
        Assert.assertEquals(referenceId, basketPlayers.getReferenceId());
    }

}
