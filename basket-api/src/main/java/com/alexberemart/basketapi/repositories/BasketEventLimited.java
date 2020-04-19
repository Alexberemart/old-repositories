package com.alexberemart.basketapi.repositories;

public interface BasketEventLimited {
    Integer getLevel();

    Integer getType();

    Integer getValue();

    Integer getCount();
}
