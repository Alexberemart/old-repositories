package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.base.BasketApiPersistenceTest;
import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.BasketMatchEntity;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BasketMatchRepositoryTest extends BasketApiPersistenceTest {

    @Autowired
    protected BasketMatchRepository basketMatchRepository;

    @Test
    @Transactional
    public void save() {
        BasketMatchEntity basketMatchEntity = new BasketMatchEntity();
        BasketMatchEntity save = basketMatchRepository.save(basketMatchEntity);
        Assert.assertNotNull(save.getId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailIfSaveWithoutPlayer() {
        BasketMatchEntity basketMatchEntity = BasketMatchEntity.builder().build();
        BasketMatchEntity save = basketMatchRepository.save(basketMatchEntity);
        Assert.assertNotNull(save.getId());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-matches.xml"})
    public void findByGameKey() {
        List<BasketMatchEntity> basketMatchEntities = basketMatchRepository.findByGameKey("gameKey91");
        Assert.assertEquals(1, basketMatchEntities.size());
        basketMatchEntities = basketMatchRepository.findByGameKey("gameKey1");
        Assert.assertEquals(0, basketMatchEntities.size());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-matches.xml"})
    public void findFirstByOrderByDateDesc() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = df.parse("2018-01-03");
        BasketMatchEntity basketMatchEntity = basketMatchRepository.findFirstByOrderByDateDesc();
        Assert.assertEquals(date, basketMatchEntity.getDate());
    }

}
