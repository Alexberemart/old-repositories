package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.base.BasketApiPersistenceTest;
import com.alexberemart.basketapi.config.RepositoryConfiguration;
import com.alexberemart.basketapi.entities.SeasonEntity;
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

import java.util.List;

public class SeasonRepositoryTest extends BasketApiPersistenceTest {

    @Autowired
    protected SeasonRepository seasonRepository;

    @Test
    @Transactional
    public void save() {
        SeasonEntity basketMatchEntity = new SeasonEntity();
        SeasonEntity save = seasonRepository.save(basketMatchEntity);
        Assert.assertNotNull(save.getId());
    }

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml",
            "classpath:datasets/seasons.xml"
    })
    public void findByWebKey() {
        List<SeasonEntity> seasonEntities = seasonRepository.findByWebKey("webKey91");
        Assert.assertEquals(1, seasonEntities.size());
        seasonEntities = seasonRepository.findByWebKey("otherWebKey");
        Assert.assertEquals(0, seasonEntities.size());
    }

}
