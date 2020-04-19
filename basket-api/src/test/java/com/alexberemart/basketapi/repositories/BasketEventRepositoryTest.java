package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.base.BasketApiPersistenceTest;
import com.alexberemart.basketapi.entities.BasketEventEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class BasketEventRepositoryTest extends BasketApiPersistenceTest {

    @Autowired
    protected BasketEventRepository basketEventRepository;
    @Autowired
    protected BasketPlayerRepository basketPlayerRepository;

    @Test
    @Transactional
    public void save() {
        BasketPlayerEntity basketPlayerEntity = BasketPlayerEntity.builder()
                .birthDate(new Date())
                .build();
        basketPlayerRepository.save(basketPlayerEntity);

        BasketEventEntity basketEventEntity = BasketEventEntity.builder()
                .date(new Date())
                .type(0)
                .value(10)
                .basketPlayer(basketPlayerEntity)
                .build();
        BasketEventEntity eventEntity = basketEventRepository.save(basketEventEntity);
        Assert.assertNotNull(eventEntity.getId());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-events.xml"})
    public void findBySeason_Id() {
        List<BasketEventEntity> basketEventEntities = basketEventRepository.findByWebKey("91");
        Assert.assertEquals(1, basketEventEntities.size());
        basketEventEntities = basketEventRepository.findByWebKey("92");
        Assert.assertEquals(0, basketEventEntities.size());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-events.xml"})
    public void findByWebKeyAndLevel() {
        List<BasketEventEntity> basketEventEntities = basketEventRepository.findByWebKeyAndLevel("91", 0);
        Assert.assertEquals(1, basketEventEntities.size());
        basketEventEntities = basketEventRepository.findByWebKeyAndLevel("92", 0);
        Assert.assertEquals(0, basketEventEntities.size());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml",
            "classpath:datasets/basket-players.xml",
            "classpath:datasets/basket-events.xml"})
    public void findGroupByLevelAndTypeAndValue() {
        List<BasketEventLimited> basketEventEntities = basketEventRepository.findGroupByLevelAndTypeAndValue();
        Assert.assertEquals(1, basketEventEntities.size());
        Assert.assertEquals((Integer) 1, basketEventEntities.get(0).getValue());
    }
}