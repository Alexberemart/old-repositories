package com.alexberemart.basketscraping.controllers;

import com.alexberemart.basketscraping.services.EuroLeagueAPI;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

@RestController
@RequestMapping("/euroleague")
@ApiIgnore
public class EuroLeagueController {

    EuroLeagueAPI euroLeagueAPI;

    @Autowired
    public EuroLeagueController(EuroLeagueAPI euroLeagueAPI) {
        this.euroLeagueAPI = euroLeagueAPI;
    }

//    @RequestMapping("/url/{url}")
//    public List<EuroLeagueMatch> scraping(@PathVariable("url") String url) throws IOException, ParseException {
//        return euroLeagueAPI.scraping(url);
//    }

    @RequestMapping("/csv/{season}")
    @ResponseBody
    public ResponseEntity scrapingCsv(@PathVariable("season") String season) throws IOException, ParseException {
        ResponseEntity respEntity = null;
        String scrapingCsv = euroLeagueAPI.getAllMatches(season);
        InputStream is = new ByteArrayInputStream(scrapingCsv.getBytes());
        byte[] out = IOUtils.toByteArray(is);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=alex.csv");

        respEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
        return respEntity;
    }

}
