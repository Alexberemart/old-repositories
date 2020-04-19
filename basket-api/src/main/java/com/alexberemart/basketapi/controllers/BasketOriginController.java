package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.controllers.dto.BasketOriginSaveDto;
import com.alexberemart.basketapi.model.BasketOrigin;
import com.alexberemart.basketapi.services.BasketOriginServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("basket-origin")
@AllArgsConstructor
public class BasketOriginController {

    protected BasketOriginServices basketOriginServices;

    @PostMapping
    String save(@RequestBody BasketOriginSaveDto basketOriginSaveDto) {
        return basketOriginServices.saveIfNotExist(basketOriginSaveDto).getId();
    }

}


