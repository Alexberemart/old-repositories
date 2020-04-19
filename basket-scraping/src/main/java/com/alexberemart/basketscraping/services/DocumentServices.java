package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.model.CloudDocument;
import com.alexberemart.basketscraping.repositories.CloudRepository;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class DocumentServices {

    protected CloudRepository cloudRepository;

    public Document parseFile(String file) {
        return Jsoup.parse(file);
    }

    public void saveFile(Document doc, String playerUrl) throws IOException {
        cloudRepository.saveFile(doc, playerUrl);
    }

    public CloudDocument getFile(String playerUrl) throws IOException {
        return cloudRepository.getFile(playerUrl);
    }

    public Boolean existFile(String basketJobKey, String gameKey) throws IOException {
        return cloudRepository.existFile("/" + basketJobKey + gameKey);
    }

    public void saveFile(Document doc, String gameKey, String basketJobKey) throws IOException {
        cloudRepository.saveFile(doc, gameKey, basketJobKey);
    }
}
