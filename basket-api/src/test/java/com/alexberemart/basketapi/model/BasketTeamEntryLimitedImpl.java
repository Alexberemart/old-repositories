package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketMatchEntity;
import com.alexberemart.basketapi.repositories.BasketTeamEntryLimited;
import lombok.Builder;

@Builder
public class BasketTeamEntryLimitedImpl implements BasketTeamEntryLimited {

    BasketMatchEntity basketMatchEntity;
    Integer count;

    @Override
    public BasketMatchEntity getBasketMatch() {
        return basketMatchEntity;
    }

    @Override
    public Integer getCount() {
        return count;
    }
}
