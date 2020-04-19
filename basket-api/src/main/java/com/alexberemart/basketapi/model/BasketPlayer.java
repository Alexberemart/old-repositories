package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.Objects;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketPlayer {

    protected String id;
    protected String name;
    protected String referenceId;
    protected Date birthDate;
    protected String country;
    protected BasketOrigin basketOrigin;

    public BasketPlayerEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketPlayerEntity basketPlayerEntity = modelMapper.map(this, BasketPlayerEntity.class);
        return basketPlayerEntity;
    }
}
