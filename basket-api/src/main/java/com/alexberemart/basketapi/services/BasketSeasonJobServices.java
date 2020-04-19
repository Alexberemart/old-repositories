package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketSeasonJobEntity;
import com.alexberemart.basketapi.model.BasketSeasonJob;
import com.alexberemart.basketapi.model.BasketSeasonJobState;
import com.alexberemart.basketapi.repositories.BasketSeasonJobRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketSeasonJobServices {

    protected BasketSeasonJobRepository basketSeasonJobRepository;

    public BasketSeasonJob save(BasketSeasonJob basketSeasonJob) {

        if (basketSeasonJob.getWebKey() == null) {
            throw new IllegalArgumentException("Web Key must be reported");
        }

        return basketSeasonJobRepository.save(basketSeasonJob.toEntity()).toModel();
    }

    public List<BasketSeasonJob> findNonFinished() {
        List<BasketSeasonJob> result = new ArrayList<>();
        List<BasketSeasonJobEntity> basketSeasonJobEntities = basketSeasonJobRepository.findByStateNot(BasketSeasonJobState.FINISHED.getCode());
        for (BasketSeasonJobEntity basketSeasonJobEntity : basketSeasonJobEntities) {
            result.add(basketSeasonJobEntity.toModel());
        }
        return result;
    }

}
