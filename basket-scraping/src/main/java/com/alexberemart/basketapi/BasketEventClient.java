package com.alexberemart.basketapi;

import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketEventLevel;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient("basket-api")
@RequestMapping("/event")
@ApiIgnore
public interface BasketEventClient {

    @RequestMapping(method = RequestMethod.GET, value = "/findByWebKeyAndLevel/{gameKey}/{basketEventLevel}")
    List<BasketEvent> findByWebKeyAndLevel(
            @PathVariable("gameKey") String gameKey,
            @PathVariable("basketEventLevel") Integer basketEventLevelCode);

    @RequestMapping(method = RequestMethod.POST, value = "/createDoubleDoubleEventFromMatch")
    List<BasketEvent> createDoubleDoubleEventFromMatch(
            @RequestBody String gameKey);

}
