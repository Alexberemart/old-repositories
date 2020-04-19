package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.dto.MatchScore;
import com.alexberemart.basketapi.exception.MalformedBasketTeamEntryException;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.services.*;
import com.alexberemart.basketapi.usecase.model.SeasonLeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.thymeleaf.TemplateEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "test")
public class SendRecapNotificationOFLastDayTest {

    @Mock
    protected BasketMatchServices basketMatchServices;
    @Mock
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    @Mock
    protected BasketTeamEntryServices basketTeamEntryServices;
    @Mock
    protected EmailService emailService;
    @Mock
    private TemplateEngine templateEngine;
    protected SendRecapNotificationOfLastDay sendRecapNotificationOfLastDay;

    @Before
    public void setUp() {
        sendRecapNotificationOfLastDay = new SendRecapNotificationOfLastDay(
                basketMatchServices,
                basketPlayerEntryServices,
                basketTeamEntryServices,
                emailService,
                templateEngine);
    }

    @Test
    public void getMailMatchScoresContentWithoutGames() throws MalformedBasketTeamEntryException {
        List<BasketTeamEntry> basketTeamEntryList = new ArrayList<>();
        List<MatchScore> content = sendRecapNotificationOfLastDay.getMailMatchScoresContent(basketTeamEntryList);
        Assert.assertEquals(0, content.size());
    }

    @Test(expected = MalformedBasketTeamEntryException.class)
    public void getMailMatchScoresContentMalformedBasketTeamEntryNotMatchIdFound() throws MalformedBasketTeamEntryException {
        List<BasketTeamEntry> basketTeamEntryList = new ArrayList<>();

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .id("id")
                .build();

        basketTeamEntryList.add(basketTeamEntry);

        List<MatchScore> content = sendRecapNotificationOfLastDay.getMailMatchScoresContent(basketTeamEntryList);
        Assert.assertEquals(0, content.size());
    }

    @Test(expected = MalformedBasketTeamEntryException.class)
    public void getMailMatchScoresContentMalformedBasketTeamEntryNotEntryTypeFound() throws MalformedBasketTeamEntryException {
        List<BasketTeamEntry> basketTeamEntryList = new ArrayList<>();

        BasketMatch basketMatch = BasketMatch.builder()
                .id("id")
                .build();

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .id("id")
                .basketMatch(basketMatch)
                .build();

        basketTeamEntryList.add(basketTeamEntry);

        List<MatchScore> content = sendRecapNotificationOfLastDay.getMailMatchScoresContent(basketTeamEntryList);
        Assert.assertEquals(0, content.size());
    }

    @Test(expected = MalformedBasketTeamEntryException.class)
    public void getMailMatchScoresContentMalformedBasketTeamEntryNotTeamNameFound() throws MalformedBasketTeamEntryException {
        List<BasketTeamEntry> basketTeamEntryList = new ArrayList<>();

        BasketMatch basketMatch = BasketMatch.builder()
                .id("id")
                .build();

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .id("id")
                .basketMatch(basketMatch)
                .type(BasketMatchTeamType.HOME)
                .build();

        basketTeamEntryList.add(basketTeamEntry);

        List<MatchScore> content = sendRecapNotificationOfLastDay.getMailMatchScoresContent(basketTeamEntryList);
        Assert.assertEquals(0, content.size());
    }

    @Test
    public void getMailMatchScoresContent() throws MalformedBasketTeamEntryException {
        List<BasketTeamEntry> basketTeamEntryList = new ArrayList<>();

        BasketMatch basketMatch = BasketMatch.builder()
                .id("id")
                .build();

        BasketTeam basketTeam = BasketTeam.builder()
                .name("teamName")
                .build();

        BasketTeam awayBasketTeam = BasketTeam.builder()
                .name("teamNameAway")
                .build();

        BasketTeamEntry basketTeamEntry = BasketTeamEntry.builder()
                .id("id")
                .basketMatch(basketMatch)
                .type(BasketMatchTeamType.HOME)
                .basketTeam(basketTeam)
                .build();

        basketTeamEntryList.add(basketTeamEntry);

        basketTeamEntry = BasketTeamEntry.builder()
                .id("id")
                .basketMatch(basketMatch)
                .type(BasketMatchTeamType.AWAY)
                .basketTeam(awayBasketTeam)
                .build();

        basketTeamEntryList.add(basketTeamEntry);

        List<MatchScore> content = sendRecapNotificationOfLastDay.getMailMatchScoresContent(basketTeamEntryList);
        Assert.assertEquals(1, content.size());
    }

}
