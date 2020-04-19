package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.model.BasketPlayerEntryGroupedByPlayer;
import com.alexberemart.basketapi.repositories.Top20EntryRepository;
import com.alexberemart.basketapi.utils.TimeServices;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Top20EntryServicesTest {

    @Mock
    protected Top20EntryRepository top20EntryRepository;
    @Mock
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    @Mock
    protected TimeServices timeServices;
    protected Top20EntryServices top20EntryServices;

    @Before
    public void setUp() {
        top20EntryServices = new Top20EntryServices(
                top20EntryRepository,
                basketPlayerEntryServices,
                timeServices);
        top20EntryServices = Mockito.spy(top20EntryServices);
    }

    @Test
    public void createAll() throws ParseException {
        String seasonWebKey = "seasonWebKey";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateStart = df.parse("2013-01-01");
        Date dateEnd = df.parse("2013-01-01");

        DateTimeFormatter pattern = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime dateTime = pattern.parseDateTime("01/01/2017");

        List<BasketPlayerEntry> basketPlayerEntryList = new ArrayList<>();
        List<BasketPlayerEntryGroupedByPlayer> top20EntryDetailList2 = new ArrayList<>();

        for (int i = 1; i<30; i++) {
            BasketPlayerEntryGroupedByPlayer basketPlayerEntryGroupedByPlayer = BasketPlayerEntryGroupedByPlayer.builder()
                    .rating(1.0f)
                    .build();
            top20EntryDetailList2.add(basketPlayerEntryGroupedByPlayer);
        }

        when(timeServices.getAllMondays(dateStart, dateEnd)).thenReturn(Collections.singletonList(dateTime));
        when(basketPlayerEntryServices.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(seasonWebKey)).thenReturn(basketPlayerEntryList);
        when(basketPlayerEntryServices.getElementWithMinDate(basketPlayerEntryList)).thenReturn(dateStart);
        when(basketPlayerEntryServices.getElementWithMaxDate(basketPlayerEntryList)).thenReturn(dateEnd);
        when(basketPlayerEntryServices.getSumRatingGroupedByBasketPlayer(any(), any())).thenReturn(top20EntryDetailList2);

        top20EntryServices.createAll(seasonWebKey);
    }

}