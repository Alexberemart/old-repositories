package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketOriginEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by aberenguer on 06/07/2017.
 */
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    protected String id;
    protected Date startDate;
    protected String webKey;
    protected BasketOrigin basketOrigin;
}
