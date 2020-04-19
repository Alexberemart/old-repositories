package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.controllers.dto.BasketPlayerEntrySaveDto;
import com.alexberemart.basketapi.dto.BasketPlayerEntrySumedByPlayerDto;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/basket-player-entry")
@AllArgsConstructor
public class BasketPlayerEntryController {

    protected BasketPlayerEntryServices basketTeamEntryServices;

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    @RequestMapping(path = "findGroupedByPlayer")
    public List<BasketPlayer> findGroupedByPlayer() {
        return basketTeamEntryServices.findGroupedByPlayer();
    }

    @CrossOrigin
    @RequestMapping(path = "sumStatsGroupedByPlayer")
    public Page<BasketPlayerEntrySumedByPlayerDto> sumStatsGroupedByPlayer(
            Pageable pageable,
            @RequestParam("country") Optional<String> country) {

        String countryToFilter = null;

        if (country.isPresent()) {
            countryToFilter = country.get();
        }

        return basketTeamEntryServices.sumStatsGroupedByPlayer(pageable, countryToFilter);
    }

    @PostMapping
    public String save(@RequestBody BasketPlayerEntrySaveDto basketPlayerEntry){
        return basketTeamEntryServices.save(basketPlayerEntry).getId();
    }

}


