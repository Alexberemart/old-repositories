package com.alexberemart.basketapi.controllers.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BasketTeamSaveIfNotExistDto {
    String teamName;
    String teamKey;
}
