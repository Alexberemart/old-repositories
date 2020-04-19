package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.services.BasketMatchServices;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/basket-match")
@AllArgsConstructor
public class BasketMatchController {

    protected BasketMatchServices basketMatchServices;

    @RequestMapping()
    public List<BasketMatch> getAllEvents() {
        return basketMatchServices.findAll();
    }

    @RequestMapping(value = "lastGame/{id}/{id1}")
    public BasketMatch getLastGameBetweenTeams(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @PathVariable("id") String basketMatchId,
            @PathVariable("id1") String basketMatchId1) {
        return basketMatchServices.getLastGameBetweenTeams(date, basketMatchId, basketMatchId1);
    }

    @RequestMapping(value = "findBySeason_WebKey")
    public List<BasketMatch> findBySeason_WebKey(
            @RequestParam("seasonWebKey") String seasonWebKey){
        return basketMatchServices.findBySeason_WebKey(seasonWebKey);
    }

    @RequestMapping(value = "findBySeason_IdIn")
    public List<BasketMatch> findBySeason_IdIn(
            @RequestParam("seasonId") String seasonId){
        List<String> seasonIdList = Arrays.asList(seasonId.split("\\|"));
        return basketMatchServices.findBySeason_IdIn(seasonIdList);
    }

    @GetMapping(value = "findByGameKey")
    public List<BasketMatch> findByGameKey(
            @RequestParam("gameKey") String gameKey){
        return basketMatchServices.findByGameKey(gameKey);
    }

    @PostMapping
    public BasketMatch save(
            @RequestBody BasketMatch basketMatch){
        return basketMatchServices.save(basketMatch);
    }

}
