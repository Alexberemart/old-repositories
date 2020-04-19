package com.alexberemart.basketscraping.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketScrapedPlayer {

    protected String basketReferenceKey;
    private String playerName;
    private Date birthDate;
    private String position;
    private String height;
    private String weight;
    private String country;
}
