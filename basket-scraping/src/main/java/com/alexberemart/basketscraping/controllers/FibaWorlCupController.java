package com.alexberemart.basketscraping.controllers;

import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.services.BasketReferenceAPI;
import com.alexberemart.basketscraping.services.FibaWorldCupAPI;
import com.alexberemart.basketscraping.usecase.CreateBasketJobsFromYear;
import com.alexberemart.basketscraping.usecase.ProcessNextBasketJob;
import com.alexberemart.basketscraping.usecase.ScrapeAllFileFromBasketJob;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/fiba_world_cup")
@AllArgsConstructor
@CrossOrigin
public class FibaWorlCupController {

    protected ProcessNextBasketJob processNextBasketJob;
    protected ScrapeAllFileFromBasketJob scrapeAllFileFromBasketJob;
    protected FibaWorldCupAPI fibaWorldCupAPI;
    protected CreateBasketJobsFromYear createBasketJobsFromYear;

    @GetMapping(value = "/createJobFromYear/{year}")
    public ResponseEntity<List<BasketJob>> createJobFromYear(@PathVariable("year") Integer year) {
        return ResponseEntity.ok(createBasketJobsFromYear.execute(year));
    }

}
