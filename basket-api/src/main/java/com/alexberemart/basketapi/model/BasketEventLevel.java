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
public enum BasketEventLevel {

    MATCH(0, "match"),
    SEASON(1, "season");

    protected Integer code;
    protected String description;

    public static BasketEventLevel fromCode(Integer code) {
        for (BasketEventLevel b : BasketEventLevel.values()) {
            if (Objects.equals(b.code, code)) {
                return b;
            }
        }
        return null;
    }

}
