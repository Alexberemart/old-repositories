package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.BasketTeamEntity;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.repositories.BasketTeamRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = {RepositoryConfiguration.class})
public class BasketTeamServiceTest {

    @Mock
    protected BasketTeamRepository basketTeamRepository;
    protected BasketTeamServices basketTeamServices;

    @Before
    public void setUp() {
        basketTeamServices = new BasketTeamServices(basketTeamRepository);
        basketTeamServices = Mockito.spy(basketTeamServices);
    }

    @Test
    public void save() {

        when(basketTeamRepository.save(any(BasketTeamEntity.class)))
                .thenReturn(new BasketTeamEntity());

        BasketTeam basketTeam = BasketTeam.builder()
                .id("1L")
                .name("alex")
                .referenceId("alex")
                .build();
        basketTeamServices.save(basketTeam);
        verify(basketTeamRepository).save(any(BasketTeamEntity.class));
    }

    @Test
    public void saveIfNotExist() {
        String teamName = "alex";
        String teamReferenceId = "alex";

        BasketTeam basketTeam = BasketTeam.builder()
                .name(teamName)
                .build();

        doReturn(basketTeam)
                .when(basketTeamServices)
                .save(any(BasketTeam.class));

        BasketTeam savedBasketTeam = basketTeamServices.saveIfNotExist(teamName, teamReferenceId);
        verify(basketTeamServices).save(any(BasketTeam.class));
        Assert.assertEquals(teamName, savedBasketTeam.getName());
    }

    @Test
    public void saveIfNotExistWhenTeamAlreadyExists() {
        String teamName = "alex";
        String teamReferenceId = "alex";

        BasketTeamEntity basketTeamEntity = BasketTeamEntity.builder()
                .name(teamName)
                .build();

        when(basketTeamRepository.findByReferenceId(teamReferenceId))
                .thenReturn(Arrays.asList(basketTeamEntity));

        BasketTeam basketTeam = basketTeamServices.saveIfNotExist(teamName, teamReferenceId);
        verify(basketTeamServices, times(0)).save(any(BasketTeam.class));
        verify(basketTeamRepository).findByReferenceId(teamReferenceId);
        Assert.assertEquals(teamName, basketTeam.getName());
    }
}
