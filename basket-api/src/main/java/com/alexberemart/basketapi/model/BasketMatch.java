package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.Date;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketMatch {

    private String id;
    private Date date;
    private Season season;
    private String gameKey;
}
