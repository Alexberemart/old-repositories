package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.controllers.dto.BasketPlayerSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.services.BasketPlayerServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("basket-player")
@AllArgsConstructor
@CrossOrigin
public class BasketPlayerController {

    protected BasketPlayerServices basketPlayerServices;

    @GetMapping
    public List<BasketPlayer> findAll() {
        return basketPlayerServices.findAll();
    }

    @PostMapping
    BasketPlayer save(@RequestBody BasketPlayer newBasketPlayer) {
        return basketPlayerServices.save(newBasketPlayer);
    }

    @PostMapping(value = "saveIfNotExist")
    String saveIfNotExist(@RequestBody BasketPlayerSaveIfNotExistDto basketPlayerSaveIfNotExistDto) {
        return basketPlayerServices.saveIfNotExist(basketPlayerSaveIfNotExistDto).getId();
    }

    @GetMapping(value = "getByReferenceId")
    public BasketPlayer getByReferenceId(@RequestParam("referenceId") String referenceId) {
        return basketPlayerServices.getByReferenceId(referenceId);
    }

}


