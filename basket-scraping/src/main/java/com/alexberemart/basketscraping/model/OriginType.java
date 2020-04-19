package com.alexberemart.basketscraping.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@JsonAutoDetect
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OriginType {

    BASKET_REFERENCE(0, "basket-referece"),
    FIBA_WORLD_CUP(1, "fiba-world-cup");

    protected Integer code;
    protected String description;

    public static OriginType fromCode(Integer code) {
        for (OriginType b : OriginType.values()) {
            if (Objects.equals(b.code, code)) {
                return b;
            }
        }
        return null;
    }

}
