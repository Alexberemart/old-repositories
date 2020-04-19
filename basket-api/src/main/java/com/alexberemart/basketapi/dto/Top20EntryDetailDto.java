package com.alexberemart.basketapi.dto;

import com.alexberemart.basketapi.model.BasketEvent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@JsonAutoDetect
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Top20EntryDetailDto {

    protected BasketEvent basketEvent;
    protected Integer count;
}
