package com.alexberemart.basketscraping;

import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public interface CompetitionTest {

    @Test
    void downloadSeason() throws IOException;

    @Test
    void scrape() throws IOException, ParseException;

    @Test
    void scrapeWhenBasketMatchHasBeenScrapedBefore() throws IOException, ParseException;

    @Test
    void processNextBasketJob() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException;
}
