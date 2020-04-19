package com.alexberemart.basketscraping.repositories;

import com.alexberemart.basketscraping.BasketScrapingIntegrationTest;
import com.alexberemart.basketscraping.entities.BasketJobEntity;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

@DatabaseSetup(value = "classpath:datasets/basket-jobs.xml")
@TestExecutionListeners(value = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class BasketJobRepositoryTest extends BasketScrapingIntegrationTest {

    @Autowired
    protected BasketJobRepository basketJobRepository;

    @Test
    public void findByDownloaded() {
        basketJobRepository.findAll();
    }

    @Test
    public void findByWebKey() {
        List<BasketJobEntity> basketJobEntityList = basketJobRepository.findByWebKey("key");
        Assert.assertEquals(1, basketJobEntityList.size());
        basketJobEntityList = basketJobRepository.findByWebKey("key1");
        Assert.assertEquals(0, basketJobEntityList.size());
    }

    @Test
    public void findByStateNot() {
        List<BasketJobEntity> basketJobEntityList = basketJobRepository.findByStateNotOrderByPriorityDesc(BasketJobState.DOWNLOADED.getCode());
        Assert.assertEquals(0, basketJobEntityList.size());
        basketJobEntityList = basketJobRepository.findByStateNotOrderByPriorityDesc(BasketJobState.EVENTS_FINISHED.getCode());
        Assert.assertEquals(1, basketJobEntityList.size());
    }

    @Test
    public void findByWebKeyLikeAndStateNot() {
        List<BasketJobEntity> basketJobEntityList = basketJobRepository.findByWebKeyLikeAndStateNot("k", BasketJobState.EVENTS_FINISHED.getCode());
        Assert.assertEquals(0, basketJobEntityList.size());
        basketJobEntityList = basketJobRepository.findByWebKeyLikeAndStateNot("k%", BasketJobState.DOWNLOADED.getCode());
        Assert.assertEquals(0, basketJobEntityList.size());
        basketJobEntityList = basketJobRepository.findByWebKeyLikeAndStateNot("k%", BasketJobState.EVENTS_FINISHED.getCode());
        Assert.assertEquals(1, basketJobEntityList.size());
    }

    @Test
    public void findByRestartJob() {
        basketJobRepository.findByRestartJob(Boolean.TRUE);
    }
}
