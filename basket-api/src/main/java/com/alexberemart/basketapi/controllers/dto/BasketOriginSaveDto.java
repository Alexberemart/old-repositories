package com.alexberemart.basketapi.controllers.dto;

import com.alexberemart.basketapi.model.BasketOrigin;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@JsonAutoDetect
@Builder
public class BasketOriginSaveDto {

    protected String name;

    public BasketOrigin toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, BasketOrigin.class);
    }
}
