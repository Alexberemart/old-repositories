package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.Top20EntryEntity;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.model.Top20Entry;
import com.alexberemart.basketapi.model.BasketPlayerEntryGroupedByPlayer;
import com.alexberemart.basketapi.repositories.Top20EntryRepository;
import com.alexberemart.basketapi.utils.TimeServices;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class Top20EntryServices {

    protected Top20EntryRepository top20EntryRepository;
    protected BasketPlayerEntryServices basketPlayerEntryServices;
    protected TimeServices timeServices;

    public List<Top20Entry> createAll(String seasonWebKey) {
        List<Top20EntryEntity> top20EntryEntityList = top20EntryRepository.findByWebKey(seasonWebKey);
        top20EntryRepository.deleteAll(top20EntryEntityList);

        List<BasketPlayerEntry> basketPlayerEntryList = basketPlayerEntryServices.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(seasonWebKey);

        Date end = basketPlayerEntryServices.getElementWithMaxDate(basketPlayerEntryList);

        Date start = basketPlayerEntryServices.getElementWithMinDate(basketPlayerEntryList);

        List<DateTime> mondays = timeServices.getAllMondays(start, end);

        for (DateTime dateTime : mondays) {
            DateTime startFourWeeks = dateTime.minusWeeks(4);
            List<BasketPlayerEntryGroupedByPlayer> top20EntryDetailList2 = basketPlayerEntryServices.getSumRatingGroupedByBasketPlayer(dateTime.toDate(), startFourWeeks.toDate());
            Float lastValueToSave = top20EntryDetailList2.get(19).getRating().floatValue();
            top20EntryDetailList2.removeIf(c -> c.getRating().floatValue() < lastValueToSave);
            Integer position = 0;
            Float lastValue = 0f;
            Integer gap = 1;
            for (BasketPlayerEntryGroupedByPlayer basketPlayerEntryGroupedByPlayer : top20EntryDetailList2) {
                Float ratingValue = basketPlayerEntryGroupedByPlayer.getRating().floatValue();
                if (!ratingValue.equals(lastValue)) {
                    position += gap;
                    gap = 1;
                } else {
                    gap += 1;
                }
                Top20Entry top20Entry = Top20Entry.builder()
                        .position(position)
                        .basketPlayer(basketPlayerEntryGroupedByPlayer.getBasketPlayer())
                        .date(dateTime.toDate())
                        .rating(ratingValue)
                        .webKey(seasonWebKey)
                        .build();
                top20EntryRepository.save(top20Entry.toEntity());
                lastValue = ratingValue;
            }
        }

        return null;
    }
}
