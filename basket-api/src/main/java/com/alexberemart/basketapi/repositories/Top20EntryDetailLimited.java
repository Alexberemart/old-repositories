package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;

public interface Top20EntryDetailLimited {
    BasketPlayerEntity getPlayerEntry();

    Double getTotal();
}
