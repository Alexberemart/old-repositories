package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketSeasonJobEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

@JsonAutoDetect
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BasketSeasonJob {

    public BasketSeasonJob() {
        this.state = BasketSeasonJobState.INIT;
    }

    protected String id;
    protected String webKey;
    protected BasketSeasonJobState state;

    public void setState(Integer i) {
        this.state = BasketSeasonJobState.fromCode(i);
    }

    public void setState(BasketSeasonJobState i) {
        this.state = i;
    }

    public BasketSeasonJobEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketSeasonJobEntity basketSeasonJobEntity = modelMapper.map(this, BasketSeasonJobEntity.class);
        return basketSeasonJobEntity;
    }

}
