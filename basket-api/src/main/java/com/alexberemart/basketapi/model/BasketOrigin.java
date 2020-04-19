package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketOriginEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketOrigin {

    protected String id;
    protected String name;

    public BasketOriginEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketOriginEntity basketOriginEntity = modelMapper.map(this, BasketOriginEntity.class);
        return basketOriginEntity;
    }
}
