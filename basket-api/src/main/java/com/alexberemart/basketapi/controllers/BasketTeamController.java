package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.controllers.dto.BasketTeamSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.services.BasketTeamServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("basket-team")
@AllArgsConstructor
public class BasketTeamController {

    protected BasketTeamServices basketTeamServices;

    @PostMapping("saveIfNotExist")
    public BasketTeam saveIfNotExist(@RequestBody BasketTeamSaveIfNotExistDto basketTeamSaveIfNotExistDto) {
        return basketTeamServices.saveIfNotExist(basketTeamSaveIfNotExistDto.getTeamName(), basketTeamSaveIfNotExistDto.getTeamKey());
    }

}
