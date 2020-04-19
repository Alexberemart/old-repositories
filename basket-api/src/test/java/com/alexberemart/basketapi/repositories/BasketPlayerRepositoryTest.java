package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.base.BasketApiPersistenceTest;
import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.BasketOriginEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BasketPlayerRepositoryTest extends BasketApiPersistenceTest {

    @Autowired
    protected BasketPlayerRepository basketPlayerRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/basket-players.xml"
    })
    @Transactional
    public void findByName() {
        Iterable<BasketPlayerEntity> basketPlayerIterable = basketPlayerRepository.findByName("alex");
        List<BasketPlayerEntity> basketPlayerList = new ArrayList<>();
        basketPlayerIterable.forEach(basketPlayerList::add);
        Assert.assertEquals(1, basketPlayerList.size());
    }

    @Test
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/basket-players.xml"
    })
    @Transactional
    public void findByReferenceId() {
        Iterable<BasketPlayerEntity> basketPlayerIterable = basketPlayerRepository.findByReferenceId("alex");
        List<BasketPlayerEntity> basketPlayerList = new ArrayList<>();
        basketPlayerIterable.forEach(basketPlayerList::add);
        Assert.assertEquals(1, basketPlayerList.size());
    }

    @Test
    @Transactional
    public void save() {
        Date birthDate = new Date();

        BasketOriginEntity basketOriginEntity = BasketOriginEntity.builder()
                .id("id")
                .build();

        BasketPlayerEntity basketPlayer = BasketPlayerEntity.builder()
                .referenceId("alex99")
                .birthDate(birthDate)
                .referenceId("id")
                .build();
        BasketPlayerEntity save = basketPlayerRepository.save(basketPlayer);
        Assert.assertNotNull(save.getId());
        Assert.assertEquals(birthDate, save.getBirthDate());
        em.flush();
        em.clear();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUnique() {
        BasketPlayerEntity basketPlayer = new BasketPlayerEntity();
        basketPlayer.setReferenceId("alex99");
        basketPlayerRepository.save(basketPlayer);
        basketPlayer = new BasketPlayerEntity();
        basketPlayer.setReferenceId("alex99");
        basketPlayerRepository.save(basketPlayer);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldThrowErrorBecauseBirthDateIsNotReported() {
        BasketPlayerEntity basketPlayer = BasketPlayerEntity.builder()
                .referenceId("alex99")
                .build();
        basketPlayerRepository.save(basketPlayer);
    }

}
