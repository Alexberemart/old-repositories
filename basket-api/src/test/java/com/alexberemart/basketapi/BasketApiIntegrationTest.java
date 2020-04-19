package com.alexberemart.basketapi;

import com.alexberemart.basketapi.config.AppConfiguration;
import com.alexberemart.common_test_utils.IntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AppConfiguration.class})
public abstract class BasketApiIntegrationTest extends IntegrationTest {

}