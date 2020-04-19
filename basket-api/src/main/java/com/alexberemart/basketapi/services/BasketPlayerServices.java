package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.controllers.dto.BasketPlayerSaveIfNotExistDto;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.model.BasketOrigin;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.repositories.BasketPlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketPlayerServices {

    protected BasketPlayerRepository basketPlayerRepository;
    protected BasketOriginServices basketOriginServices;

    public BasketPlayer save(BasketPlayer basketPlayer) {

        if (basketPlayer.getBirthDate() == null) {
            throw new IllegalArgumentException("birth date must not be null");
        }

        if (basketPlayer.getBasketOrigin() == null) {
            throw new IllegalArgumentException("basket origin must not be null");
        }

        return basketPlayerRepository.save(basketPlayer.toEntity()).toModel();
    }

    public BasketPlayer saveIfNotExist(BasketPlayerSaveIfNotExistDto basketPlayerSaveIfNotExistDto) {

        BasketOrigin basketOrigin;

        try{
            basketOrigin = basketOriginServices.findByName(basketPlayerSaveIfNotExistDto.getBasketOriginName());
        }catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Doesn't exist basket origin with name " + basketPlayerSaveIfNotExistDto.getBasketOriginName());
        }

        BasketPlayer basketPlayer = BasketPlayer.builder()
                .name(basketPlayerSaveIfNotExistDto.getName())
                .referenceId(basketPlayerSaveIfNotExistDto.getReferenceId())
                .birthDate(basketPlayerSaveIfNotExistDto.getBirthDate())
                .country(basketPlayerSaveIfNotExistDto.getCountry())
                .basketOrigin(basketOrigin)
                .build();
        return saveIfNotExist(basketPlayer);
    }

    public BasketPlayer saveIfNotExist(BasketPlayer basketPlayer) {
        List<BasketPlayerEntity> basketPlayerList = basketPlayerRepository.findByReferenceId(basketPlayer.getReferenceId());
        if (basketPlayerList.size() != 0) {
            return basketPlayerList.get(0).toModel();
        }
        return save(basketPlayer);
    }

    public BasketPlayer getByReferenceId(String referenceId) {
        return basketPlayerRepository.findByReferenceId(referenceId).get(0).toModel();
    }

    public List<BasketPlayer> findAll() {
        return toModelList(basketPlayerRepository.findAll());
    }

    protected List<BasketPlayer> toModelList(Iterable<BasketPlayerEntity> basketMatchEntities) {
        List<BasketPlayer> result = new ArrayList<>();
        basketMatchEntities.forEach(basketMatchEntity -> result.add(basketMatchEntity.toModel()));
        return result;
    }

    public BasketPlayer findById(String id) {
        return basketPlayerRepository.findById(id).get().toModel();
    }

}
