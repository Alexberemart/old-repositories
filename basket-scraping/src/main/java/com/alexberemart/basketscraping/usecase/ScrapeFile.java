package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketapi.*;
import com.alexberemart.basketapi.dto.BasketOriginSaveDto;
import com.alexberemart.basketapi.dto.BasketPlayerEntrySaveDto;
import com.alexberemart.basketapi.dto.BasketPlayerSaveIfNotExistDto;
import com.alexberemart.basketapi.dto.BasketTeamSaveIfNotExistDto;
import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.model.BasketTeamEntry;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketscraping.model.*;
import com.alexberemart.basketscraping.services.BasketAPI;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class ScrapeFile {

    protected BasketMatchClient basketMatchClient;
    protected BasketOriginClient basketOriginClient;
    protected BasketPlayerClient basketPlayerClient;
    protected BasketPlayerEntryClient basketPlayerEntryClient;
    protected BasketTeamClient basketTeamClient;
    protected BasketTeamEntryClient basketTeamEntryClient;

    public BasketScrapedMatch execute(
            CloudDocument cloudDocument,
            Season season,
            BasketAPI basketAPI) throws IOException, ParseException {
        String gameKey = cloudDocument.getKey().split("\\.")[0];
        List<BasketMatch> basketMatches = basketMatchClient.findByGameKey(gameKey);
        if (basketMatches.size() != 0) {
            return null;
        }
        try {
            BasketScrapedMatch BasketScrapedMatch = basketAPI.createMatch(gameKey, season, cloudDocument);
            saveEntities(basketAPI, season, BasketScrapedMatch);
            return BasketScrapedMatch;
        } catch (NumberFormatException e) {
            throw new RuntimeException("error scraping file: " + cloudDocument.getKey());
        }
    }

    @Transactional
    protected void saveEntities(BasketAPI basketAPI, Season season, BasketScrapedMatch BasketScrapedMatch) throws IOException, ParseException {
        BasketMatch basketMatch = saveMatch(BasketScrapedMatch, season);
        BasketScrapedMatch.setBasketScrapedPlayers(getBasketScrapedPlayers(basketAPI, BasketScrapedMatch.getBasketScrapedMatchStatList()));
        savePlayers(basketAPI, BasketScrapedMatch.getBasketScrapedPlayers());
        savePlayerEntries(BasketScrapedMatch, basketMatch);
        saveTeamEntries(BasketScrapedMatch, basketMatch);
    }

    protected List<BasketScrapedPlayer> getBasketScrapedPlayers(BasketAPI basketAPI, List<BasketScrapedMatchStat> basketScrapedMatchStatList) throws IOException, ParseException {
        List<BasketScrapedPlayer> result = new ArrayList<>();
        for (BasketScrapedMatchStat BasketScrapedMatchStat : basketScrapedMatchStatList) {

            BasketScrapedPlayer basketScrapedPlayer = basketAPI.getPlayer(BasketScrapedMatchStat.getPlayerUrl());
            result.add(basketScrapedPlayer);
        }
        return result;
    }

    protected List<String> savePlayers(BasketAPI basketAPI, List<BasketScrapedPlayer> basketScrapedPlayerList){
        Objects.requireNonNull(basketScrapedPlayerList, "basketScrapedPlayerList");
        String basket_reference = basketAPI.getOriginName();
        List<String> result = new ArrayList<>();
        for (BasketScrapedPlayer basketScrapedPlayer : basketScrapedPlayerList) {

            BasketPlayer basketPlayer = BasketPlayer.builder()
                    .name(basketScrapedPlayer.getPlayerName())
                    .referenceId(basketScrapedPlayer.getBasketReferenceKey())
                    .birthDate(basketScrapedPlayer.getBirthDate())
                    .country(basketScrapedPlayer.getCountry())
                    .build();

            BasketPlayerSaveIfNotExistDto basketPlayerSaveIfNotExistDto = BasketPlayerSaveIfNotExistDto.builder()
                    .name(basketPlayer.getName())
                    .birthDate(basketPlayer.getBirthDate())
                    .country(basketPlayer.getCountry())
                    .referenceId(basketPlayer.getReferenceId())
                    .basketOriginName(basket_reference)
                    .build();

            BasketOriginSaveDto basketOriginSaveDto = BasketOriginSaveDto.builder()
                    .name(basket_reference)
                    .build();

            basketOriginClient.save(basketOriginSaveDto);
            String basketPlayerSavedId = basketPlayerClient.saveIfNotExist(basketPlayerSaveIfNotExistDto);
            result.add(basketPlayerSavedId);
        }
        return result;
    }

    protected List<String> savePlayerEntries(BasketScrapedMatch BasketScrapedMatch, BasketMatch basketMatch) {
        List<String> result = new ArrayList<>();
        for (BasketScrapedMatchStat BasketScrapedMatchStat : BasketScrapedMatch.getBasketScrapedMatchStatList()) {

            BasketPlayer basketPlayer = basketPlayerClient.getByReferenceId(BasketScrapedMatchStat.getBasketReferenceKey());

            BasketPlayerEntrySaveDto basketPlayerEntrySaveDto = BasketPlayerEntrySaveDto.builder()
                    .basketPlayerId(basketPlayer.getId())
                    .basketMatchId(basketMatch.getId())
                    .points(BasketScrapedMatchStat.getPoints())
                    .assists(BasketScrapedMatchStat.getAssists())
                    .rebounds(BasketScrapedMatchStat.getRebounds())
                    .basketMatchTeamType(BasketScrapedMatchStat.getType())
                    .build();
            result.add(basketPlayerEntryClient.save(basketPlayerEntrySaveDto));
        }
        return result;
    }

    protected List<String> saveTeamEntries(BasketScrapedMatch BasketScrapedMatch, BasketMatch basketMatch) {
        List<String> result = new ArrayList<>();
        for (BasketScrapedMatchTeamScore basketScrapedMatchTeamScore : BasketScrapedMatch.getBasketScrapedMatchTeamScoreList()) {

            BasketTeamSaveIfNotExistDto basketTeamSaveIfNotExistDto = BasketTeamSaveIfNotExistDto
                    .builder()
                    .teamKey(basketScrapedMatchTeamScore.getBasketTeamWebKey())
                    .teamName(basketScrapedMatchTeamScore.getBasketTeamName())
                    .build();

            BasketTeam basketTeam = basketTeamClient.saveIfNotExist(basketTeamSaveIfNotExistDto);

            BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                    .basketMatch(basketMatch)
                    .basketTeam(basketTeam)
                    .points(basketScrapedMatchTeamScore.getScore())
                    .type(basketScrapedMatchTeamScore.getBasketMatchTeamType())
                    .build();

            result.add(basketTeamEntryClient.save(basketTeamEntry));
        }
        return result;
    }

    protected BasketMatch saveMatch(BasketScrapedMatch BasketScrapedMatch, Season season) {
        BasketMatch basketMatch = BasketMatch.builder()
                .gameKey(BasketScrapedMatch.getGameKey())
                .date(BasketScrapedMatch.getDate())
                .season(season)
                .build();
        return basketMatchClient.save(basketMatch);
    }

}