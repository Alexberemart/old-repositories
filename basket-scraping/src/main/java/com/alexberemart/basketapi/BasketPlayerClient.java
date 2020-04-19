package com.alexberemart.basketapi;

import com.alexberemart.basketapi.dto.BasketPlayerSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketPlayer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.websocket.server.PathParam;
import java.util.List;

@FeignClient("basket-api")
@RequestMapping("basket-player")
@ApiIgnore
public interface BasketPlayerClient {

    @RequestMapping(method = RequestMethod.GET)
    List<BasketPlayer> findAll();

    @RequestMapping(method = RequestMethod.POST)
    BasketPlayer save(@RequestBody BasketPlayer newBasketPlayer);

    @RequestMapping(method = RequestMethod.POST, value = "saveIfNotExist")
    String saveIfNotExist(@RequestBody BasketPlayerSaveIfNotExistDto basketPlayerSaveIfNotExistDto);

    @RequestMapping(method = RequestMethod.GET, value = "getByReferenceId")
    BasketPlayer getByReferenceId(@RequestParam("referenceId") String referenceId);
}
