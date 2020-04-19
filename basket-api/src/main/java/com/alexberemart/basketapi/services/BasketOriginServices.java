package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.controllers.dto.BasketOriginSaveDto;
import com.alexberemart.basketapi.entities.BasketOriginEntity;
import com.alexberemart.basketapi.model.BasketOrigin;
import com.alexberemart.basketapi.repositories.BasketOriginRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketOriginServices {

    protected BasketOriginRepository basketOriginRepository;

    public BasketOrigin save(BasketOriginSaveDto basketOriginSaveDto) {
        return save(basketOriginSaveDto.toModel());
    }

    public BasketOrigin save(BasketOrigin basketOrigin) {
        return basketOriginRepository.save(basketOrigin.toEntity()).toModel();
    }

    public BasketOrigin saveIfNotExist(BasketOriginSaveDto basketOriginSaveDto) {
        return saveIfNotExist(basketOriginSaveDto.toModel());
    }

    public BasketOrigin saveIfNotExist(BasketOrigin basketOrigin) {
        List<BasketOriginEntity> basketOriginList = basketOriginRepository.findByName(basketOrigin.getName());
        if (basketOriginList.size() != 0) {
            return basketOriginList.get(0).toModel();
        }
        return save(basketOrigin);
    }

    public BasketOrigin findByName(String name){
        return toModelList(basketOriginRepository.findByName(name)).get(0);
    }

    public List<BasketOrigin> findAll(){
        return toModelList(basketOriginRepository.findAll());
    }

    protected List<BasketOrigin> toModelList(Iterable<BasketOriginEntity> basketOriginEntities) {
        List<BasketOrigin> result = new ArrayList<>();
        basketOriginEntities.forEach(basketMatchEntity -> result.add(basketMatchEntity.toModel()));
        return result;
    }

}
