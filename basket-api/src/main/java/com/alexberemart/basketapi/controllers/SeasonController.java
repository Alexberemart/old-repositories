package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketapi.services.SeasonServices;
import com.alexberemart.basketapi.usecase.GetSeasonLeadersByStat;
import com.alexberemart.basketapi.usecase.model.SeasonLeaders;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/season")
@AllArgsConstructor
public class SeasonController {

    protected SeasonServices seasonServices;
    protected GetSeasonLeadersByStat getSeasonLeadersByStat;

    @PostMapping
    public Season saveIfNotExist(
            @RequestBody Season season) {
        return seasonServices.saveIfNotExist(season);
    }

    @GetMapping(value = "getSeasonLeadersByStat")
    public List<SeasonLeaders> getSeasonLeadersByStat() {
        return getSeasonLeadersByStat.execute();
    }

}
