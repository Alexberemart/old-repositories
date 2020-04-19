package com.alexberemart.basketapi;

import com.alexberemart.basketapi.dto.BasketOriginSaveDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient("basket-api")
@RequestMapping("basket-origin")
@ApiIgnore
public interface BasketOriginClient {

    @RequestMapping(method = RequestMethod.POST)
    String save(@RequestBody BasketOriginSaveDto basketOriginSaveDto);
}
