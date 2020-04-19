package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketMatchEntity;
import com.alexberemart.basketapi.model.BasketMatch;
import com.alexberemart.basketapi.repositories.BasketMatchRepository;
import com.alexberemart.basketapi.repositories.BasketTeamEntryLimited;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class BasketMatchServicesImpl implements BasketMatchServices {

    protected BasketMatchRepository basketMatchRepository;
    protected BasketTeamEntryServices basketTeamEntryServices;

    @Override
    public BasketMatch save(BasketMatch basketMatch) {

        if (basketMatch.getDate() == null) {
            throw new IllegalArgumentException("date must not be null");
        }

        if (basketMatch.getGameKey() == null) {
            throw new IllegalArgumentException("gameKey must not be null");
        }

        return fromEntity(basketMatchRepository.save(toEntity(basketMatch)));
    }

    protected BasketMatchEntity toEntity(BasketMatch basketMatch) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(basketMatch, BasketMatchEntity.class);
    }

    protected BasketMatch fromEntity(BasketMatchEntity basketMatchEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(basketMatchEntity, BasketMatch.class);
    }

    @Override
    public List<BasketMatch> findByGameKey(String gameKey) {
        List<BasketMatch> result = new ArrayList<>();
        List<BasketMatchEntity> basketMatchEntities = basketMatchRepository.findByGameKey(gameKey);
        for (BasketMatchEntity basketMatchEntity : basketMatchEntities) {
            result.add(fromEntity(basketMatchEntity));
        }
        return result;
    }

    @Override
    public BasketMatch findById(String id) {
        return basketMatchRepository.findById(id).get().toModel();
    }

    @Override
    public List<BasketMatch> findAll() {
        return toModelList(basketMatchRepository.findAll());
    }

    @Override
    public BasketMatch getLastGameBetweenTeams(Date date, String basketMatchId, String basketTeamId) {
        List<BasketTeamEntryLimited> basketTeamEntryList = basketTeamEntryServices.findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(date, basketMatchId, basketTeamId);
        for (BasketTeamEntryLimited basketTeamEntryLimited : basketTeamEntryList) {
            if (basketTeamEntryLimited.getCount() > 1) {
                return basketTeamEntryLimited.getBasketMatch().toModel();
            }
        }
        return null;
    }

    protected List<BasketMatch> toModelList(Iterable<BasketMatchEntity> basketMatchEntities) {
        List<BasketMatch> result = new ArrayList<>();
        basketMatchEntities.forEach(basketMatchEntity -> result.add(basketMatchEntity.toModel()));
        return result;
    }

    @Override
    public List<BasketMatch> findBySeason_WebKey(String seasonWebKey) {
        return toModelList(basketMatchRepository.findBySeason_WebKey(seasonWebKey));
    }

    @Override
    public List<BasketMatch> findBySeason_IdIn(List<String> seasonIdList) {
        return toModelList(basketMatchRepository.findBySeason_IdIn(seasonIdList));
    }

    @Override
    public BasketMatch findFirstByOrderByDateDesc() {
        return basketMatchRepository.findFirstByOrderByDateDesc().toModel();
    }
}