package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketMatchEntity;

public interface BasketTeamEntryLimited {
    BasketMatchEntity getBasketMatch();

    Integer getCount();
}
