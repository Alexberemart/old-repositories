package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.controllers.dto.BasketEventDto;
import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketEventLevel;
import com.alexberemart.basketapi.services.BasketEventServices;
import com.alexberemart.basketapi.usecase.CreateEventsFromMatchId;
import com.alexberemart.basketapi.usecase.GetDoubleDoubleEventFromSeason;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class BasketEventController {

    protected BasketEventServices basketEventServices;
    protected CreateEventsFromMatchId createEventsFromMatchId;
    protected GetDoubleDoubleEventFromSeason getDoubleDoubleEventFromSeason;

    @RequestMapping("/get")
    public List<BasketEvent> getAllEvents() {
        return getDoubleDoubleEventFromSeason.execute("NBA2017");
    }

    @GetMapping("/findByWebKeyAndLevel/{gameKey}/{basketEventLevel}")
    public List<BasketEvent> findByWebKeyAndLevel(
            @PathVariable("gameKey") String gameKey,
            @PathVariable("basketEventLevel") Integer basketEventLevelCode) {
        return basketEventServices.findByWebKeyAndLevel(gameKey, BasketEventLevel.fromCode(basketEventLevelCode));
    }

    @PostMapping("/createDoubleDoubleEventFromMatch")
    public List<BasketEvent> createDoubleDoubleEventFromMatch(
            @RequestBody String gameKey) {
        return createEventsFromMatchId.execute(gameKey);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    @RequestMapping("/events")
    public List<BasketEventDto> findAllSortedByDateAndCountEvent() {
        return basketEventServices.findAllSortedByDateAndCountEvent(0, 2000);
    }

}
