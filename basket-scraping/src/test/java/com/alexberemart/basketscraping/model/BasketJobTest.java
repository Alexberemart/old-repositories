package com.alexberemart.basketscraping.model;

import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.OriginType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;

@RunWith(JUnit4.class)
@ActiveProfiles(profiles = "test")
public class BasketJobTest {

    @Test
    public void ModelShoulntChangeAfterToModel() throws IOException, ParseException {
        BasketJob basketJob = BasketJob.builder()
                .webKey("key")
                .build();
        BasketJob basketJob1 = basketJob.toEntity().toModel();
        Assert.assertEquals(basketJob1.getState(), basketJob.getState());
        Assert.assertEquals(basketJob1.getWebKey(), basketJob.getWebKey());
        basketJob.setState(BasketJobState.DOWNLOADED);
        basketJob.setOriginType(OriginType.FIBA_WORLD_CUP);
        basketJob1 = basketJob.toEntity().toModel();
        Assert.assertEquals(basketJob1.getState(), basketJob.getState());
        Assert.assertEquals(basketJob1.getOriginType(), basketJob.getOriginType());
    }
}
