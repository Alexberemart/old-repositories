package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketEventEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.Date;

@JsonAutoDetect
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketEvent {

    protected String id;
    protected Date date;
    protected BasketEventType type;
    protected BasketEventLevel basketEventLevel;
    protected Integer value;
    protected BasketPlayer basketPlayer;
    protected String webKey;

    public void setType(Integer i) {
        this.type = BasketEventType.fromCode(i);
    }

    public void setBasketEventType(BasketEventType i) {
        this.type = i;
    }

    public void setBasketEventLevel(BasketEventLevel i) {
        this.basketEventLevel = i;
    }

    public void setBasketEventLevel(Integer i) {
        this.basketEventLevel = BasketEventLevel.fromCode(i);
    }

    public BasketEventEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketEventEntity basketEventEntity = modelMapper.map(this, BasketEventEntity.class);
        return basketEventEntity;
    }

}
