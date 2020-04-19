package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.usecase.ProcessNextBasketSeasonJob;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basket-season-job")
@AllArgsConstructor
public class BasketSeasonJobController {

    protected ProcessNextBasketSeasonJob processNextBasketSeasonJob;

    @GetMapping("/processNext")
    public void processNextBasketSeasonJob() {
        processNextBasketSeasonJob.processNextBasketSeasonJob();
    }
}
