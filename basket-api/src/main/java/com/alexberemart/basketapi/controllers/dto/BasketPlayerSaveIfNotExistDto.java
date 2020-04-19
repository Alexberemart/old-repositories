package com.alexberemart.basketapi.controllers.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonAutoDetect
@Builder
public class BasketPlayerSaveIfNotExistDto {

    protected String name;
    protected String referenceId;
    protected Date birthDate;
    protected String country;
    protected String basketOriginName;
}
