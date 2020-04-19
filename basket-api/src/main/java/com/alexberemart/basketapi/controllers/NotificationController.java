package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.exception.MalformedBasketTeamEntryException;
import com.alexberemart.basketapi.usecase.SendRecapNotificationOfLastDay;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationController {

    protected SendRecapNotificationOfLastDay sendRecapNotificationOfLastDay;

    @GetMapping("/SendRecapNotificationOfLastDay")
    public List SendRecapNotificationOfLastDay() throws IOException, MalformedBasketTeamEntryException {
        return sendRecapNotificationOfLastDay.execute();
    }

}
