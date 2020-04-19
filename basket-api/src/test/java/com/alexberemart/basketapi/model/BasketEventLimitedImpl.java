package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.repositories.BasketEventLimited;
import lombok.Builder;

@Builder
public class BasketEventLimitedImpl implements BasketEventLimited {

    Integer level;
    Integer type;
    Integer value;
    Integer count;

    @Override
    public Integer getLevel() {
        return level;
    }

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public Integer getCount() {
        return count;
    }
}
