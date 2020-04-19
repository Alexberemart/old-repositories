package com.alexberemart.basketscraping;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("basket-scraping")
@RequestMapping("/basket-job")
public interface BasketJobClient {

    @RequestMapping(method = RequestMethod.GET, value = "/existBasketJobByWebKeyLikeNonFinished/{webKey}")
    Boolean existBasketJobByWebKeyLikeNonFinished(@PathVariable("webKey") String webKey);

}
