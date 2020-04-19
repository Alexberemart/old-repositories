package com.alexberemart.basketapi.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect
@Builder
public class BasketOriginSaveDto {

    protected String name;

}
