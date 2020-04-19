package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@JsonAutoDetect
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketPlayerEntryGroupedByPlayer {

    protected BasketPlayer basketPlayer;
    protected Float rating;

}
