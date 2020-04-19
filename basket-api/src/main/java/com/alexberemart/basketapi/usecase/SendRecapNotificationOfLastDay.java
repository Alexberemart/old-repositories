package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.dto.BestScorer;
import com.alexberemart.basketapi.dto.MatchScore;
import com.alexberemart.basketapi.exception.MalformedBasketTeamEntryException;
import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.model.BasketTeamEntry;
import com.alexberemart.basketapi.services.BasketMatchServices;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import com.alexberemart.basketapi.services.BasketTeamEntryServices;
import com.alexberemart.basketapi.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.MalformedInputException;
import java.util.*;

@Service
@AllArgsConstructor
public class SendRecapNotificationOfLastDay {

    protected BasketMatchServices basketMatchServices;
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    protected BasketTeamEntryServices basketTeamEntryServices;
    protected EmailService emailService;
    private TemplateEngine templateEngine;

    public List execute() throws MalformedBasketTeamEntryException {

        BasketMatch basketMatch = basketMatchServices.findFirstByOrderByDateDesc();
        List<BasketPlayerEntry> basketPlayerEntries = basketPlayerEntryServices.findByBasketMatch_DateOrderByPointsDesc(basketMatch.getDate());
        List<BasketTeamEntry> basketTeamEntries = basketTeamEntryServices.findByBasketMatch_DateOrderByBasketMatch_IdDescTypeAsc(basketMatch.getDate());
        emailService.sendHTMLMessage(
                "alexberemart@gmail.com",
                "NBA Recap",
                getMailContent(basketMatch.getDate(), basketTeamEntries, basketPlayerEntries));
        return null;
    }

    protected String getMailContent(Date date, List<BasketTeamEntry> basketTeamEntries, List<BasketPlayerEntry> basketPlayerEntries) throws MalformedBasketTeamEntryException {
        Context context = new Context();
        context.setVariable("date", date);

        List<MatchScore> matchScoreList = getMailMatchScoresContent(basketTeamEntries);
        List<BestScorer> bestScorerList = getMailBestScorersContent(basketPlayerEntries);

        context.setVariable("bestScorerList", bestScorerList);
        context.setVariable("matchScoreList", matchScoreList);

        return templateEngine.process("mailTemplate", context);
    }

    private List<BestScorer> getMailBestScorersContent(List<BasketPlayerEntry> basketPlayerEntries) {
        List<BestScorer> bestScorerList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BestScorer bestScorer = BestScorer.builder()
                    .player(basketPlayerEntries.get(i).getBasketPlayer().getName())
                    .points(basketPlayerEntries.get(i).getPoints())
                    .build();
            bestScorerList.add(bestScorer);
        }
        return bestScorerList;
    }

    protected List<MatchScore> getMailMatchScoresContent(List<BasketTeamEntry> basketTeamEntries) throws MalformedBasketTeamEntryException {
        List<MatchScore> matchScoreList = new ArrayList<>();
        HashMap<String, HashMap<Integer, BasketTeamEntry>> matches = new HashMap<>();
        for (BasketTeamEntry basketTeamEntry : basketTeamEntries) {
            String matchId = getMatchId(basketTeamEntry);
            matches.computeIfAbsent(matchId, k -> new HashMap<>());
            Integer typeCode = getEntryTypeCode(basketTeamEntry);
            matches.get(matchId).put(typeCode, basketTeamEntry);
        }

        for (Map.Entry<String, HashMap<Integer, BasketTeamEntry>> match : matches.entrySet()) {
            BasketTeamEntry local = match.getValue().get(BasketMatchTeamType.HOME.getCode());
            BasketTeamEntry away = match.getValue().get(BasketMatchTeamType.AWAY.getCode());
            MatchScore matchScore = MatchScore.builder()
                    .localTeam(getTeamName(local))
                    .localPoints(local.getPoints())
                    .awayTeam(getTeamName(away))
                    .awayPoints(away.getPoints())
                    .build();
            matchScoreList.add(matchScore);
        }
        return matchScoreList;
    }

    private String getTeamName(BasketTeamEntry basketTeamEntry) throws MalformedBasketTeamEntryException {
        try{
            return basketTeamEntry.getBasketTeam().getName();
        } catch (NullPointerException e){
            throw new MalformedBasketTeamEntryException("unable to get team name");
        }
    }

    private Integer getEntryTypeCode(BasketTeamEntry basketTeamEntry) throws MalformedBasketTeamEntryException {
        try{
            return basketTeamEntry.getType().getCode();
        } catch (NullPointerException e){
            throw new MalformedBasketTeamEntryException("unable to get entry type (local or away)");
        }
    }

    private String getMatchId(BasketTeamEntry basketTeamEntry) throws MalformedBasketTeamEntryException {
        try {
            return basketTeamEntry.getBasketMatch().getId();
        } catch (NullPointerException e) {
            throw new MalformedBasketTeamEntryException("unable to get match id");
        }
    }
}