package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.base.BasketApiPersistenceTest;
import com.alexberemart.basketapi.entities.BasketMatchEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntryEntity;
import com.alexberemart.basketapi.repositories.model.IFindSumStatGroupedByPlayerBySeason;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BasketPlayerEntryRepositoryTest extends BasketApiPersistenceTest {

    @Autowired
    protected BasketPlayerEntryRepository basketPlayerEntryRepository;
    @Autowired
    protected BasketPlayerRepository basketPlayerRepository;
    @Autowired
    protected BasketMatchRepository basketMatchRepository;

    @Test
    @Transactional
    public void save() {

        BasketPlayerEntity basketPlayer = new BasketPlayerEntity();
        basketPlayerRepository.save(basketPlayer);

        BasketMatchEntity basketMatchEntity = new BasketMatchEntity();
        basketMatchRepository.save(basketMatchEntity);

        Integer rebounds = 2;
        Integer assists = 2;
        BasketPlayerEntryEntity basketPlayerEntry = BasketPlayerEntryEntity.builder()
                .basketPlayer(basketPlayer)
                .basketMatch(basketMatchEntity)
                .rebounds(rebounds)
                .assists(assists)
                .build();

        BasketPlayerEntryEntity save = basketPlayerEntryRepository.save(basketPlayerEntry);
        Assert.assertNotNull(save.getId());
        Assert.assertEquals(rebounds, save.getRebounds());
        Assert.assertEquals(assists, save.getAssists());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailIfSaveWithoutPlayer() {
        BasketPlayerEntryEntity basketPlayerEntry = BasketPlayerEntryEntity.builder().build();
        BasketPlayerEntryEntity save = basketPlayerEntryRepository.save(basketPlayerEntry);
        Assert.assertNotNull(save.getId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailIfSaveWithoutMatch() {

        BasketPlayerEntity basketPlayer = new BasketPlayerEntity();
        basketPlayerRepository.save(basketPlayer);

        BasketPlayerEntryEntity basketPlayerEntry = BasketPlayerEntryEntity.builder()
                .basketPlayer(basketPlayer)
                .build();
        BasketPlayerEntryEntity save = basketPlayerEntryRepository.save(basketPlayerEntry);
        Assert.assertNotNull(save.getId());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-player-entries.xml"})
    public void findAll() {
        Iterable<BasketPlayerEntryEntity> basketPlayerEntryIterable = basketPlayerEntryRepository.findAll();
        List<BasketPlayerEntryEntity> basketPlayerEntries = new ArrayList<>();
        basketPlayerEntryIterable.forEach(basketPlayerEntries::add);
        Assert.assertEquals(10, basketPlayerEntries.size());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-player-entries.xml"})
    public void findByBasketMatch_DateOrderByPoints() throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Iterable<BasketPlayerEntryEntity> basketPlayerEntryIterable = basketPlayerEntryRepository.findByBasketMatch_DateOrderByPointsDesc(df.parse("2017-01-01"), new PageRequest(0, 5));
        List<BasketPlayerEntryEntity> basketPlayerEntries = new ArrayList<>();
        basketPlayerEntryIterable.forEach(basketPlayerEntries::add);
        Assert.assertEquals(5, basketPlayerEntries.size());

        basketPlayerEntryIterable = basketPlayerEntryRepository.findByBasketMatch_DateOrderByPointsDesc(df.parse("2017-01-01"), new PageRequest(0, 1));
        basketPlayerEntries = new ArrayList<>();
        basketPlayerEntryIterable.forEach(basketPlayerEntries::add);
        Assert.assertEquals(1, basketPlayerEntries.size());

        basketPlayerEntryIterable = basketPlayerEntryRepository.findByBasketMatch_DateOrderByPointsDesc(df.parse("2017-01-02"), new PageRequest(0, 5));
        basketPlayerEntries = new ArrayList<>();
        basketPlayerEntryIterable.forEach(basketPlayerEntries::add);
        Assert.assertEquals(0, basketPlayerEntries.size());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-player-entries.xml"})
    public void findGroupedByPlayer() {
        List<Object> groupedByPlayer = basketPlayerEntryRepository.findGroupedByPlayer();
        List<BasketPlayerEntity> basketPlayerEntryList = (List<BasketPlayerEntity>) (Object) groupedByPlayer;
        Assert.assertNotNull(groupedByPlayer);
        Assert.assertEquals(5, basketPlayerEntryList.size());
        Assert.assertEquals("91", basketPlayerEntryList.get(0).getId());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-player-entries.xml"})
    public void findByBasketPlayerOrderByBasketMatch_DateAsc() {

        BasketPlayerEntity basketPlayerEntity = BasketPlayerEntity.builder()
                .id("91")
                .build();

        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findByBasketPlayerOrderByBasketMatch_DateAsc(basketPlayerEntity);
        Assert.assertNotNull(basketPlayerEntries);
        Assert.assertEquals((Integer) 11, basketPlayerEntries.get(0).getPoints());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-player-entries.xml"})
    public void findGroupedByPlayerBySeason() {
        List<Object> groupedByPlayer = basketPlayerEntryRepository.findGroupedByPlayerBySeason("webKey91");
        List<BasketPlayerEntity> basketPlayerEntryList = (List<BasketPlayerEntity>) (Object) groupedByPlayer;
        Assert.assertEquals(5, basketPlayerEntryList.size());
        groupedByPlayer = basketPlayerEntryRepository.findGroupedByPlayerBySeason("92");
        basketPlayerEntryList = (List<BasketPlayerEntity>) (Object) groupedByPlayer;
        Assert.assertEquals(0, basketPlayerEntryList.size());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-matches.xml",
            "classpath:datasets/basket-player-entries.xml"})
    public void findTop1PointsGroupedByPlayerBySeason() {
        List<IFindSumStatGroupedByPlayerBySeason> groupedByPlayer = basketPlayerEntryRepository.findSumPointsGroupedByPlayerBySeason("91");
        Assert.assertEquals(5, groupedByPlayer.size());
        Assert.assertEquals(35, groupedByPlayer.get(0).getStatValue(), 0);
        Assert.assertEquals("95", groupedByPlayer.get(0).getBasketPlayer().getId());
    }
}