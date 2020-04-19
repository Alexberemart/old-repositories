package com.alexberemart.basketapi;

import com.alexberemart.basketapi.dto.BasketTeamSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.model.BasketTeamEntry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient("basket-api")
@RequestMapping("basket-team")
@ApiIgnore
public interface BasketTeamClient {

    @RequestMapping(method = RequestMethod.GET)
    List<BasketTeamEntry> findByBasketMatch_Id(@RequestParam("basketMatchId") String basketMatchId);

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    BasketTeamEntry getLastTeamEntryFromDate(@RequestParam("date") String date, @PathVariable("id") String id);

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/{id1}")
    BasketTeamEntry getOtherTeamEntryFromMatch(@PathVariable("id") String id, @PathVariable("id1") String id1);

    @RequestMapping(method = RequestMethod.POST, value = "saveIfNotExist")
    BasketTeam saveIfNotExist(
            @RequestBody BasketTeamSaveIfNotExistDto basketTeamSaveIfNotExistDto);
}
