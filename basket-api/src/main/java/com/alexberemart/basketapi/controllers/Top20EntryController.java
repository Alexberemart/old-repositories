package com.alexberemart.basketapi.controllers;

import com.alexberemart.basketapi.model.Top20Entry;
import com.alexberemart.basketapi.services.Top20EntryServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/top20")
@AllArgsConstructor
public class Top20EntryController {

    protected Top20EntryServices top20EntryServices;

    @RequestMapping("/create/{seasonWebKey}")
    public List<Top20Entry> getAllEvents(@PathVariable("seasonWebKey") String seasonWebKey) {
        return top20EntryServices.createAll(seasonWebKey);
    }

}
