package com.alexberemart.basketapi.controllers.dto;

import com.alexberemart.basketapi.model.BasketEvent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@JsonAutoDetect
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketEventDto {

    protected BasketEvent basketEvent;
    protected Integer count;
}
