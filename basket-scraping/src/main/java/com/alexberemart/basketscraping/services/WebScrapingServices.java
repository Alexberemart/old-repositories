package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.repositories.CloudRepository;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class WebScrapingServices {

    protected CloudRepository cloudRepository;

    public Document getDownloadedFile(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

}
