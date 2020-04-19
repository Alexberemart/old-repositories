package com.alexberemart.basketapi.repositories.model;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import lombok.Data;

public interface IFindSumStatGroupedByPlayerBySeason {

    BasketPlayerEntity getBasketPlayer();

    Integer getStatValue();
}
