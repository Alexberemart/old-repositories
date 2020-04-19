package com.alexberemart.basketapi;

import com.alexberemart.basketapi.model.BasketMatch;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient("basket-api")
@RequestMapping("/basket-match")
@ApiIgnore
public interface BasketMatchClient {

    @RequestMapping(method = RequestMethod.GET)
    List<BasketMatch> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "lastGame/{id}/{id1}")
    BasketMatch getOtherTeamEntryFromMatch(@RequestParam("date") String date, @PathVariable("id") String id, @PathVariable("id1") String id1);

    @RequestMapping(method = RequestMethod.GET, value = "findBySeason_WebKey")
    List<BasketMatch> findBySeason_WebKey(@RequestParam("seasonWebKey") String seasonWebKey);

    @RequestMapping(method = RequestMethod.GET, value = "findByGameKey")
    List<BasketMatch> findByGameKey(@RequestParam("gameKey") String gameKey);

    @RequestMapping(method = RequestMethod.POST)
    BasketMatch save(@RequestBody BasketMatch basketMatch);
}
