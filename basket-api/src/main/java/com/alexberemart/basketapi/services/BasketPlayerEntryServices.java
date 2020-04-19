package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.controllers.dto.BasketPlayerEntrySaveDto;
import com.alexberemart.basketapi.dto.BasketPlayerEntrySumedByPlayerDto;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntryEntity;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.repositories.BasketPlayerEntryRepository;
import com.alexberemart.basketapi.repositories.model.IFindSumStatGroupedByPlayerBySeason;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketPlayerEntryServices {

    protected BasketPlayerEntryRepository basketPlayerEntryRepository;
    protected BasketMatchServices basketMatchServices;
    protected BasketPlayerServices basketPlayerServices;
    private ModelMapper modelMapper;

    public BasketPlayerEntry save(BasketPlayerEntrySaveDto basketPlayerEntrySaveDto) {

        BasketPlayerEntry basketPlayerEntry = BasketPlayerEntry.builder()
                .points(basketPlayerEntrySaveDto.getPoints())
                .assists(basketPlayerEntrySaveDto.getAssists())
                .basketMatch(basketMatchServices.findById(basketPlayerEntrySaveDto.getBasketMatchId()))
                .basketMatchTeamType(BasketMatchTeamType.fromCode(basketPlayerEntrySaveDto.getBasketMatchTeamType()))
                .basketPlayer(basketPlayerServices.findById(basketPlayerEntrySaveDto.getBasketPlayerId()))
                .rating(basketPlayerEntrySaveDto.getRating())
                .rebounds(basketPlayerEntrySaveDto.getRebounds())
                .build();

        return save(basketPlayerEntry);

    }

    public BasketPlayerEntry save(BasketPlayerEntry basketPlayerEntry) {

        if (basketPlayerEntry.getBasketMatch() == null) {
            throw new IllegalArgumentException("basket Match must not be null");
        }

        return fromEntity(basketPlayerEntryRepository.save(toEntity(basketPlayerEntry)));
    }

    protected BasketPlayerEntryEntity toEntity(BasketPlayerEntry basketPlayerEntry) {
        ModelMapper modelMapper = new ModelMapper();
        BasketPlayerEntryEntity playerEntryEntity = modelMapper.map(basketPlayerEntry, BasketPlayerEntryEntity.class);
        return playerEntryEntity;
    }

    protected BasketPlayerEntry fromEntity(BasketPlayerEntryEntity basketPlayerEntryEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(basketPlayerEntryEntity, BasketPlayerEntry.class);
    }

    public List<BasketPlayerEntry> findByBasketMatch_DateOrderByPointsDesc(Date date) {
        List<BasketPlayerEntry> result = new ArrayList<>();
        List<BasketPlayerEntryEntity> playerEntryEntities = basketPlayerEntryRepository.findByBasketMatch_DateOrderByPointsDesc(date, new PageRequest(0, 5));
        for (BasketPlayerEntryEntity basketPlayerEntity : playerEntryEntities) {
            result.add(basketPlayerEntity.toModel());
        }
        return result;
    }

    public List<BasketPlayer> findGroupedByPlayer() {
        List<Object> groupedByPlayer = basketPlayerEntryRepository.findGroupedByPlayer();
        List<BasketPlayerEntity> basketPlayerEntityList = (List<BasketPlayerEntity>) (Object) groupedByPlayer;
        List<BasketPlayer> result = new ArrayList<>();
        for (BasketPlayerEntity basketPlayerEntity : basketPlayerEntityList) {
            result.add(basketPlayerEntity.toModel());
        }
        return result;
    }

    public Page<BasketPlayerEntrySumedByPlayerDto> sumStatsGroupedByPlayer(Pageable pageable, String country) {
        return basketPlayerEntryRepository.sumStatsGroupedByPlayer(pageable,country).map(BasketPlayerEntrySumedByPlayerDto::fromEntity);
    }

    public List<BasketPlayer> findGroupedByPlayerBySeason(String seasonId) {
        List<Object> groupedByPlayer = basketPlayerEntryRepository.findGroupedByPlayerBySeason(seasonId);
        List<BasketPlayerEntity> basketPlayerEntityList = (List<BasketPlayerEntity>) (Object) groupedByPlayer;
        List<BasketPlayer> result = new ArrayList<>();
        for (BasketPlayerEntity basketPlayerEntity : basketPlayerEntityList) {
            result.add(basketPlayerEntity.toModel());
        }
        return result;
    }

    public List<BasketPlayerEntry> findByBasketPlayerOrderByBasketMatch_DateAsc(BasketPlayer basketPlayer) {
        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findByBasketPlayerOrderByBasketMatch_DateAsc(basketPlayer.toEntity());
        List<BasketPlayerEntry> result = new ArrayList<>();
        for (BasketPlayerEntryEntity basketPlayerEntryEntity : basketPlayerEntries) {
            result.add(basketPlayerEntryEntity.toModel());
        }
        return result;
    }

    public List<BasketPlayerEntry> findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(BasketPlayer basketPlayer, String seasonId) {
        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(basketPlayer.toEntity(), seasonId);
        List<BasketPlayerEntry> result = new ArrayList<>();
        for (BasketPlayerEntryEntity basketPlayerEntryEntity : basketPlayerEntries) {
            result.add(basketPlayerEntryEntity.toModel());
        }
        return result;
    }

    public List<BasketPlayerEntry> findByBasketMatch(String gameKey) {
        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findByBasketMatch_GameKey(gameKey);
        List<BasketPlayerEntry> result = new ArrayList<>();
        for (BasketPlayerEntryEntity basketPlayerEntryEntity : basketPlayerEntries) {
            result.add(basketPlayerEntryEntity.toModel());
        }
        return result;
    }

    public List<BasketPlayerEntry> findAllByOrderByBasketMatch_DateDesc() {
        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findAllByOrderByBasketMatch_DateDesc();
        List<BasketPlayerEntry> result = new ArrayList<>();
        for (BasketPlayerEntryEntity basketPlayerEntryEntity : basketPlayerEntries) {
            result.add(basketPlayerEntryEntity.toModel());
        }
        return result;
    }

    public List<BasketPlayerEntryGroupedByPlayer> getSumRatingGroupedByBasketPlayer(Date before, Date start) {
        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findByBasketMatch_DateLessThanAndBasketMatch_DateGreaterThanEqual(before, start);
        List<BasketPlayerEntryGroupedByPlayer> result = new ArrayList<>();

        HashMap<String, List<BasketPlayerEntry>> hashMap = new HashMap<>();

        for (BasketPlayerEntryEntity basketPlayerEntryEntity : basketPlayerEntries) {

            if (!hashMap.containsKey(basketPlayerEntryEntity.getBasketPlayer().getId())) {
                List<BasketPlayerEntry> list = new ArrayList<>();
                list.add(basketPlayerEntryEntity.toModel());

                hashMap.put(basketPlayerEntryEntity.getBasketPlayer().getId(), list);
            } else {
                hashMap.get(basketPlayerEntryEntity.getBasketPlayer().getId()).add(basketPlayerEntryEntity.toModel());
            }

        }

        for (Map.Entry<String, List<BasketPlayerEntry>> entry : hashMap.entrySet()) {
            BasketPlayerEntryGroupedByPlayer basketPlayerEntryGroupedByPlayer = BasketPlayerEntryGroupedByPlayer.builder()
                    .basketPlayer(entry.getValue().get(0).getBasketPlayer())
                    .build();
            Float rating = 0f;
            for (BasketPlayerEntry basketPlayerEntry : entry.getValue()) {
                rating += basketPlayerEntry.getRating();
            }
            basketPlayerEntryGroupedByPlayer.setRating(rating);
            result.add(basketPlayerEntryGroupedByPlayer);
        }

        Collections.sort(result, (lhs, rhs) -> {
            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
            return lhs.getRating() > rhs.getRating() ? -1 : (lhs.getRating() < rhs.getRating()) ? 1 : 0;
        });

        return result;
    }

    public List<BasketPlayerEntry> findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(String seasonId) {
        List<BasketPlayerEntryEntity> basketPlayerEntries = basketPlayerEntryRepository.findByBasketMatch_season_WebKeyOrderByBasketMatch_DateDesc(seasonId);
        List<BasketPlayerEntry> result = new ArrayList<>();
        for (BasketPlayerEntryEntity basketPlayerEntryEntity : basketPlayerEntries) {
            result.add(basketPlayerEntryEntity.toModel());
        }
        return result;
    }

    protected Date getElementWithMinDate(List<BasketPlayerEntry> basketPlayerEntryList) {

        basketPlayerEntryList.sort((o1, o2) -> {
            if (o1.getBasketMatch().getDate() == o2.getBasketMatch().getDate())
                return 0;
            return o1.getBasketMatch().getDate().before(o2.getBasketMatch().getDate()) ? -1 : 1;
        });

        return basketPlayerEntryList.get(0).getBasketMatch().getDate();
    }

    protected Date getElementWithMaxDate(List<BasketPlayerEntry> basketPlayerEntryList) {

        basketPlayerEntryList.sort((o1, o2) -> {
            if (o1.getBasketMatch().getDate() == o2.getBasketMatch().getDate())
                return 0;
            return o1.getBasketMatch().getDate().after(o2.getBasketMatch().getDate()) ? -1 : 1;
        });

        return basketPlayerEntryList.get(0).getBasketMatch().getDate();
    }

    public BasketPlayerEntry getSeasonLeaderByPoints(Season season) {
        List<IFindSumStatGroupedByPlayerBySeason> groupedByPlayerBySeason = basketPlayerEntryRepository.findSumPointsGroupedByPlayerBySeason(season.getId());
        return BasketPlayerEntry.builder()
                .basketPlayer(groupedByPlayerBySeason.get(0).getBasketPlayer().toModel())
                .points(groupedByPlayerBySeason.get(0).getStatValue())
                .build();
    }

    public BasketPlayerEntry getSeasonLeaderByRebounds(Season season) {
        List<IFindSumStatGroupedByPlayerBySeason> groupedByPlayerBySeason = basketPlayerEntryRepository.findSumReboundsGroupedByPlayerBySeason(season.getId());
        return BasketPlayerEntry.builder()
                .basketPlayer(groupedByPlayerBySeason.get(0).getBasketPlayer().toModel())
                .rebounds(groupedByPlayerBySeason.get(0).getStatValue())
                .build();
    }

    public List<BasketPlayer> getBasketPlayerByBasketOriginId(String BasketOriginId) {
        return basketPlayerServices.toModelList(basketPlayerEntryRepository.groupByBasketMatch_Season_BasketOrigin_Id(BasketOriginId));
    }

}
