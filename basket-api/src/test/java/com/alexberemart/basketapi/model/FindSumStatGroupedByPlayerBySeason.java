package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.repositories.model.IFindSumStatGroupedByPlayerBySeason;
import lombok.Builder;

@Builder
public class FindSumStatGroupedByPlayerBySeason implements IFindSumStatGroupedByPlayerBySeason {

    BasketPlayerEntity basketPlayerEntity;
    Integer statValue;

    @Override
    public BasketPlayerEntity getBasketPlayer() {
        return basketPlayerEntity;
    }

    @Override
    public Integer getStatValue() {
        return statValue;
    }
}
