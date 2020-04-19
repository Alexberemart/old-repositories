package com.alexberemart.basketapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient("basket-api")
@RequestMapping("/notification")
@ApiIgnore
public interface NotificationClient {

    @RequestMapping(method = RequestMethod.GET, value = "/SendRecapNotificationOfLastDay")
    List SendRecapNotificationOfLastDay();

}
