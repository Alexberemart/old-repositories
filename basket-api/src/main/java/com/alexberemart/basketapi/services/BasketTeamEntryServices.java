package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.alexberemart.basketapi.model.BasketTeamEntry;
import com.alexberemart.basketapi.repositories.BasketTeamEntryLimited;
import com.alexberemart.basketapi.repositories.BasketTeamEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketTeamEntryServices {

    protected BasketTeamEntryRepository basketTeamEntryRepository;

    public BasketTeamEntry save(BasketTeamEntry basketTeamEntry) {

        if (basketTeamEntry.getBasketMatch() == null) {
            throw new IllegalArgumentException("basket Match must not be null");
        }

        return basketTeamEntryRepository.save(basketTeamEntry.toEntity()).toModel();
    }

    public List<BasketTeamEntry> findByBasketMatch_Id(String basketMatchId){
        return toModelList(basketTeamEntryRepository.findByBasketMatch_Id(basketMatchId));
    }

    public List<BasketTeamEntry> findByBasketMatch_DateOrderByBasketMatch_IdDescTypeAsc(Date date){
        return toModelList(basketTeamEntryRepository.findByBasketMatch_DateOrderByBasketMatch_IdDescTypeAsc(date));
    }

    public BasketTeamEntry findByBasketPlayerEntry(BasketPlayerEntry basketPlayerEntry){
        return basketTeamEntryRepository.findByBasketMatch_IdAndType(basketPlayerEntry.getBasketMatch().getId(), basketPlayerEntry.getBasketMatchTeamType()).toModel();
    }

    public BasketTeamEntry getLastTeamEntryFromDate(Date basketMatchDate, String basketTeamId){
        List<BasketTeamEntryEntity> result = basketTeamEntryRepository.findTopByBasketMatch_DateLessThanAndAndBasketTeam_IdOrderByBasketMatch_DateDesc(basketMatchDate, basketTeamId);
        if (result.size() == 0){
            return null;
        }
        return result.get(0).toModel();
    }

    public BasketTeamEntry getOtherTeamEntryFromMatch(String basketMatchId, String basketTeamId){
        return basketTeamEntryRepository.findTopByBasketMatch_IdAndAndBasketTeam_IdNot(basketMatchId, basketTeamId).get(0).toModel();
    }

    public List<BasketTeamEntryLimited> findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(Date date, String basketMatchId, String basketTeamId){
        return basketTeamEntryRepository.findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(date, basketMatchId, basketTeamId);
    }

    public BasketTeamEntry getLastTeamEntryByTeamAndDates(String basketTeamId, Date basketMatchDateStart, Date basketMatchDateEnd){
        List<BasketTeamEntryEntity> basketTeamEntryEntityList = basketTeamEntryRepository.findByBasketTeam_IdAndBasketMatch_DateBetweenOrderByBasketMatch_DateDesc(basketTeamId, basketMatchDateStart, basketMatchDateEnd);
        if (basketTeamEntryEntityList.size() == 0){
            return null;
        }
        return basketTeamEntryEntityList.get(0).toModel();
    }

    protected List<BasketTeamEntry> toModelList(Iterable<BasketTeamEntryEntity> basketMatchEntities) {
        List<BasketTeamEntry> result = new ArrayList<>();
        basketMatchEntities.forEach(basketTeamEntryEntity -> result.add(basketTeamEntryEntity.toModel()));
        return result;
    }

}
