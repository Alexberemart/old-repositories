package com.alexberemart.basketscraping.repositories;

import com.alexberemart.basketscraping.model.CloudDocument;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public interface CloudRepository {

    List<CloudDocument> getFilesFromFolder(String folderName) throws IOException;

    void saveFile(Document doc, String fileName, String path) throws IOException;

    void saveFile(Document doc, String absolutePath) throws IOException;

    CloudDocument getFile(String absolutePath) throws IOException;

    Boolean existFile(String absolutePath) throws IOException;
}
