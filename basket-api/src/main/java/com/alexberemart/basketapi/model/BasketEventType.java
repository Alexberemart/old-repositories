package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@JsonAutoDetect
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BasketEventType {

    DOUBLE_DOUBLE_STREAK(0, "Double double streak"),
    DOUBLE_DOUBLE(1, "Double double");

    protected Integer code;
    protected String description;

    public static BasketEventType fromCode(Integer code) {
        for (BasketEventType b : BasketEventType.values()) {
            if (Objects.equals(b.code, code)) {
                return b;
            }
        }
        return null;
    }

}
