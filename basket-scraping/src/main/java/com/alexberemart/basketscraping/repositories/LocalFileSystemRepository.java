package com.alexberemart.basketscraping.repositories;

import com.alexberemart.basketscraping.model.CloudDocument;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class LocalFileSystemRepository implements CloudRepository {

    @Value("${files.path}")
    String filesPath;

    @Override
    public List<CloudDocument> getFilesFromFolder(String folderName) throws IOException {
        List<CloudDocument> result = new ArrayList<>();
        File dir = new File(filesPath);
        FileFilter fileFilter = new WildcardFileFilter(folderName);
        File[] folder = dir.listFiles(fileFilter);
        if (folder != null) {
            List<File> files = listFilesForFolder(folder[0]);
            if (files != null) {
                for (File file : files) {
                    CloudDocument cloudDocument = getCloudDocument(file);
                    result.add(cloudDocument);
                }
            }
        }
        return result;
    }

    @Override
    public void saveFile(Document doc, String fileName, String path) throws IOException {
        final File f = new File(filesPath + "/" + path + "/" + fileName + ".html");
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
    }

    @Override
    public void saveFile(Document doc, String absolutePath) throws IOException {
        final File f = new File(filesPath + absolutePath);
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
    }

    @Override
    public CloudDocument getFile(String absolutePath) throws IOException {
        final File file = new File(filesPath + absolutePath);
        return getCloudDocument(file);
    }

    @Override
    public Boolean existFile(String absolutePath) throws IOException {
        final File file = new File(filesPath + absolutePath);
        return file.exists();
    }

    private List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if (!fileEntry.isHidden()) {
                    files.add(fileEntry);
                }
            }
        }
        return files;
    }

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private CloudDocument getCloudDocument(File file) throws IOException {
        CloudDocument cloudDocument = new CloudDocument();
        cloudDocument.setKey(file.getName());
        cloudDocument.setDocument(readFile(file.getAbsolutePath(), StandardCharsets.UTF_8));
        return cloudDocument;
    }
}
