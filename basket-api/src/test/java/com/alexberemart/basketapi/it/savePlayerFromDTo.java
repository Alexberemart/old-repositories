package com.alexberemart.basketapi.it;

import com.alexberemart.basketapi.base.BasketApiItTest;
import com.alexberemart.basketapi.controllers.dto.BasketPlayerSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.services.BasketPlayerServices;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

public class savePlayerFromDTo extends BasketApiItTest {

    @Autowired
    protected BasketPlayerServices basketPlayerServices;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins.xml"
    })
    @Transactional
    public void saveIfNotExist() {

        BasketPlayerSaveIfNotExistDto basketPlayerSaveIfNotExistDto = BasketPlayerSaveIfNotExistDto.builder()
                .birthDate(new Date())
                .basketOriginName("basket reference")
                .name("player")
                .referenceId("id")
                .build();

        BasketPlayer basketPlayer = basketPlayerServices.saveIfNotExist(basketPlayerSaveIfNotExistDto);
        em.flush();
        em.clear();
    }

}