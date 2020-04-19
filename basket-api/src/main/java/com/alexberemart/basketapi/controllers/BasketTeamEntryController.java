package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.model.BasketTeamEntry;
import com.alexberemart.basketapi.services.BasketTeamEntryServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("basket-team-entry")
@AllArgsConstructor
public class BasketTeamEntryController {

    protected BasketTeamEntryServices basketTeamEntryServices;

    @PostMapping
    public String save(@RequestBody BasketTeamEntry basketTeamEntry) {
        return basketTeamEntryServices.save(basketTeamEntry).getId();
    }

}
