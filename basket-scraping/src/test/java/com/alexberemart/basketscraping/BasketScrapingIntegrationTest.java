package com.alexberemart.basketscraping;

import com.alexberemart.basketapi.BasketEventClient;
import com.alexberemart.basketapi.BasketMatchClient;
import com.alexberemart.basketapi.BasketOriginClient;
import com.alexberemart.basketapi.BasketPlayerClient;
import com.alexberemart.basketapi.BasketPlayerEntryClient;
import com.alexberemart.basketapi.BasketTeamClient;
import com.alexberemart.basketapi.BasketTeamEntryClient;
import com.alexberemart.basketapi.SeasonClient;
import com.alexberemart.basketscraping.config.AppConfiguration;
import com.alexberemart.basketscraping.services.WebScrapingServices;
import com.alexberemart.common_test_utils.IntegrationTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AppConfiguration.class})
public abstract class BasketScrapingIntegrationTest extends IntegrationTest {

    @MockBean
    protected WebScrapingServices webScrapingServices;
    @MockBean
    protected BasketMatchClient basketMatchClient;
    @MockBean
    protected BasketOriginClient basketOriginClient;
    @MockBean
    protected BasketPlayerClient basketPlayerClient;
    @MockBean
    protected BasketPlayerEntryClient basketPlayerEntryClient;
    @MockBean
    protected BasketTeamEntryClient basketTeamEntryClient;
    @MockBean
    protected SeasonClient seasonClient;
    @MockBean
    protected BasketTeamClient basketTeamClient;
    @MockBean
    protected BasketEventClient basketEventClient;

}