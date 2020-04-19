package com.alexberemart;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
@EnableScheduling
@Log
public class Application {

    @Value("${basket-scraping.url}")
    String basketScrapingUrl;

    @Value("${basket-scraping.restart-jobs.url}")
    String basketScrapingRestartJobsUrl;

    @Value("${basket-api.url}")
    String basketApiUrl;

    @Value("${basket-api.sendDailyBasketReferenceNotification.url}")
    String basketApiSendDailyBasketReferenceNotificationUrl;

    @Value("${basket-api.createDraftConsolidatedBasketBallPlayers.url}")
    String createDraftConsolidatedBasketBallPlayersUrl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(fixedRate = 30000)
    public void scheduleFixedRateTask() throws IOException {
        getNext(basketScrapingUrl);
        getNext(basketApiUrl);
    }

    @Scheduled(cron = "0 15 0/1 * * ?")
    public void restartBasketReferenceJobs() throws IOException {
        getNext(basketScrapingRestartJobsUrl);
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void sendDailyBasketReferenceNotification() throws IOException {
        getNext(basketApiSendDailyBasketReferenceNotificationUrl);
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void createDraftConsolidatedBasketBallPlayers() throws IOException {
        getNext(createDraftConsolidatedBasketBallPlayersUrl);
    }

    private void getNext(String urlTask) throws IOException {

        log.info("comienzo tarea contra la url " + urlTask);

        URL url = new URL(urlTask);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        String message = con.getResponseMessage();
        con.disconnect();

        log.info("termino tarea contra la url " + urlTask);
    }

}