package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;

public interface SumStatsGruopedByPlayer {
    BasketPlayerEntity getBasketPlayer();

    Integer getPoints();
}
