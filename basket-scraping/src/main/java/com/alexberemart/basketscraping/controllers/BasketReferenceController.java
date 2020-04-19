package com.alexberemart.basketscraping.controllers;

import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.usecase.CreateBasketJobsFromYear;
import com.alexberemart.basketscraping.usecase.ProcessNextBasketJob;
import com.alexberemart.basketscraping.usecase.ScrapeAllFileFromBasketJob;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/basketreference")
@AllArgsConstructor
@CrossOrigin
public class BasketReferenceController {

    protected ProcessNextBasketJob processNextBasketJob;
    protected ScrapeAllFileFromBasketJob scrapeAllFileFromBasketJob;
    protected BasketReferenceAPI basketReferenceAPI;
    protected CreateBasketJobsFromYear createBasketJobsFromYear;

    @GetMapping("/scrape/season/{season}")
    public void processSeason(@PathVariable("season") String season) throws IOException, ParseException, IllegalSeasonException {
        scrapeAllFileFromBasketJob.execute(basketReferenceAPI, season);
    }

    @GetMapping(value = "/createJobFromYear/{year}")
    public ResponseEntity<List<BasketJob>> createJobFromYear(@PathVariable("year") Integer year) {
        return ResponseEntity.ok(createBasketJobsFromYear.execute(year));
    }

}
