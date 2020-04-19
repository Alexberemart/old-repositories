package com.alexberemart.basketscraping;

import com.alexberemart.basketscraping.model.CloudDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class IntegrationTestUtils {

    public static Document getDocument(String filePath) throws IOException {
        File input = new File(filePath);
        return Jsoup.parse(input, "UTF-8", "http://example.com/");
    }

    public static CloudDocument getCloudDocument(String championshipPath, String playerKey) throws IOException {
        String filePath = "src/test/resources/" + championshipPath + playerKey + ".html";
        Document doc = getDocument(filePath);
        return CloudDocument.builder()
                .key(playerKey + ".html")
                .document(doc.outerHtml())
                .build();
    }
}
