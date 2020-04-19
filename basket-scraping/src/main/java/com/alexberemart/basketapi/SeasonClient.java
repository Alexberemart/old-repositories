package com.alexberemart.basketapi;

import com.alexberemart.basketapi.model.Season;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient("basket-api")
@ApiIgnore
public interface SeasonClient {

    @RequestMapping(method = RequestMethod.POST, value = "/season")
    Season saveIfNotExist(
            @RequestBody Season season);

}
