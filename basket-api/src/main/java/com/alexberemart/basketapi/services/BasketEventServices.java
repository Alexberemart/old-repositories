package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.controllers.dto.BasketEventDto;
import com.alexberemart.basketapi.entities.BasketEventEntity;
import com.alexberemart.basketapi.model.*;
import com.alexberemart.basketapi.repositories.BasketEventLimited;
import com.alexberemart.basketapi.repositories.BasketEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class BasketEventServices {

    protected BasketEventRepository basketEventRepository;
    protected SeasonServices seasonServices;

    public List<BasketEvent> deleteBySeason(String seasonWebKey) {
        List<BasketEvent> result = new ArrayList<>();
        List<BasketEventEntity> basketEventEntities = basketEventRepository.findByWebKeyAndLevel(seasonWebKey, BasketEventLevel.SEASON.getCode());
        for (BasketEventEntity basketEventEntity : basketEventEntities) {
            result.add(basketEventEntity.toModel());
        }
        basketEventRepository.deleteAll(basketEventEntities);
        return result;
    }

    public BasketEvent createAndSaveEvent(Integer seasonCount, BasketPlayerEntry basketPlayerEntry, BasketEventType basketEventType, BasketEventLevel basketEventLevel, String webKey) {
        BasketEvent basketEvent = BasketEvent.builder()
                .date(basketPlayerEntry.getBasketMatch().getDate())
                .value(seasonCount)
                .type(basketEventType)
                .basketEventLevel(basketEventLevel)
                .basketPlayer(basketPlayerEntry.getBasketPlayer())
                .webKey(webKey)
                .build();
        return save(basketEvent);
    }

    public BasketEvent save(BasketEvent basketEvent) {

        if (basketEvent.getType() == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        if (basketEvent.getWebKey() == null) {
            throw new IllegalArgumentException("webKey must not be null");
        }

        if (basketEvent.getBasketEventLevel() == null) {
            throw new IllegalArgumentException("level must not be null");
        }

        return basketEventRepository.save(basketEvent.toEntity()).toModel();
    }

    public List<BasketEvent> findByWebKeyAndLevel(String gameKey, BasketEventLevel basketEventLevel) {
        List<BasketEventEntity> basketEventEntities = basketEventRepository.findByWebKeyAndLevel(gameKey, basketEventLevel.getCode());
        return toModelList(basketEventEntities);
    }

    public List<BasketEvent> findAll() {
        return toModelList(basketEventRepository.findAll());
    }

    public List<BasketEvent> findAll(Integer page, Integer size) {
        return toModelList(basketEventRepository.findAll(PageRequest.of(page, size)));
    }

    public List<BasketEvent> findAllSortedByDate(Integer page, Integer size) {
        return toModelList(basketEventRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "date", "id")));
    }

    public List<BasketEventDto> findAllSortedByDateAndCountEvent(Integer page, Integer size) {
        List<BasketEventDto> result = new ArrayList<>();
        List<BasketEvent> basketEvents = findAllSortedByDate(page, size);
        List<BasketEventLimited> groupByLevelAndTypeAndValue = basketEventRepository.findGroupByLevelAndTypeAndValue();
        for (BasketEvent basketEvent : basketEvents) {
            for (BasketEventLimited entry : groupByLevelAndTypeAndValue) {
                if (BasketEventLevel.fromCode(entry.getLevel()) == basketEvent.getBasketEventLevel() &&
                        BasketEventType.fromCode(entry.getType()) == basketEvent.getType() &&
                        entry.getValue().equals(basketEvent.getValue())) {
                    BasketEventDto basketEventDto = BasketEventDto.builder()
                            .basketEvent(basketEvent)
                            .count(entry.getCount())
                            .build();
                    if (basketEventDto.getCount() <= 100) {
                        result.add(basketEventDto);
                    }
                }
            }
        }
        return result;
    }

    protected List<BasketEvent> toModelList(Iterable<BasketEventEntity> basketEventEntities) {
        List<BasketEvent> result = new ArrayList<>();
        basketEventEntities.forEach(basketEventEntity -> result.add(basketEventEntity.toModel()));
        return result;
    }
}
