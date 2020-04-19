package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.controllers.dto.BasketOriginSaveDto;
import com.alexberemart.basketapi.entities.BasketOriginEntity;
import com.alexberemart.basketapi.model.BasketOrigin;
import com.alexberemart.basketapi.repositories.BasketOriginRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = {RepositoryConfiguration.class})
public class BasketOriginServiceTest {

    @Mock
    protected BasketOriginRepository basketOriginRepository;
    protected BasketOriginServices basketOriginServices;

    @Before
    public void setUp() {
        basketOriginServices = new BasketOriginServices(basketOriginRepository);
        basketOriginServices = spy(basketOriginServices);
    }

    @Test
    public void save() {

        when(basketOriginRepository.save(any(BasketOriginEntity.class)))
                .thenReturn(new BasketOriginEntity());

        BasketOrigin basketOrigin = BasketOrigin.builder()
                .id("1L")
                .name("alex")
                .build();
        basketOriginServices.save(basketOrigin);
    }

    @Test
    public void saveDto() {

        doReturn(new BasketOrigin()).when(basketOriginServices).save(any(BasketOrigin.class));

        BasketOriginSaveDto basketOrigin = BasketOriginSaveDto.builder()
                .name("alex")
                .build();
        basketOriginServices.save(basketOrigin);
    }

}
