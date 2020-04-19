package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.usecase.CreateDraftConsolidatedBasketBallPlayers;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/consolidated")
@AllArgsConstructor
public class ConsolidatedController {

    protected CreateDraftConsolidatedBasketBallPlayers createDraftConsolidatedBasketBallPlayers;

    @GetMapping("/players/{threshold}")
    @ResponseBody
    public ResponseEntity consolidatedPlayers(@Validated @DecimalMin("0.5") @DecimalMax("1") @PathVariable("threshold") Double threshold) {
        return ResponseEntity.ok(createDraftConsolidatedBasketBallPlayers.execute(threshold));
    }

}
