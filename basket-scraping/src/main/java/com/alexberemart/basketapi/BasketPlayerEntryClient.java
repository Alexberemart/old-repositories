package com.alexberemart.basketapi;

import com.alexberemart.basketapi.dto.BasketPlayerEntrySaveDto;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient(value = "basket-api")
@RequestMapping("/basket-player-entry")
@ApiIgnore
public interface BasketPlayerEntryClient {

    @RequestMapping(method = RequestMethod.POST)
    String save(@RequestBody BasketPlayerEntrySaveDto basketPlayerEntrySaveDto);

}
