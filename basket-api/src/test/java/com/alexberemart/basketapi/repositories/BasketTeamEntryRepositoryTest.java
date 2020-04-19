package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.base.BasketApiPersistenceTest;
import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BasketTeamEntryRepositoryTest extends BasketApiPersistenceTest {

    @Autowired
    protected BasketTeamEntryRepository basketTeamEntryRepository;

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-teams.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-team-entries.xml"})
    public void findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-20");
        List<BasketTeamEntryLimited> basketTeamEntryLimitedList = basketTeamEntryRepository.findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(date, "team1id", "team2id");
        Assert.assertEquals(3, basketTeamEntryLimitedList.size());
        Assert.assertEquals(1, basketTeamEntryLimitedList.get(0).getCount(), 0);
        Assert.assertEquals("gameKey93", basketTeamEntryLimitedList.get(0).getBasketMatch().getGameKey());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-teams.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-team-entries.xml"})
    public void findByBasketTeam_IdAndBasketMatch_DateBetweenOrderByBasketMatch_DateDesc() throws ParseException {
        Date dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-02");
        Date dateStart = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01");
        List<BasketTeamEntryEntity> basketTeamEntryEntityList = basketTeamEntryRepository.findByBasketTeam_IdAndBasketMatch_DateBetweenOrderByBasketMatch_DateDesc("team1id", dateStart, dateEnd);
        Assert.assertEquals(1, basketTeamEntryEntityList.size());

        dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-04");
        dateStart = new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-05");
        basketTeamEntryEntityList = basketTeamEntryRepository.findByBasketTeam_IdAndBasketMatch_DateBetweenOrderByBasketMatch_DateDesc("team1id", dateStart, dateEnd);
        Assert.assertEquals(0, basketTeamEntryEntityList.size());
    }
}