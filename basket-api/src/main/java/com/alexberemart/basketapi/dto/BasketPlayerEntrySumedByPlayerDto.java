package com.alexberemart.basketapi.dto;

import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.repositories.SumStatsGruopedByPlayer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasketPlayerEntrySumedByPlayerDto {

    protected BasketPlayer basketPlayer;
    protected Integer points;

    public static BasketPlayerEntrySumedByPlayerDto fromEntity(SumStatsGruopedByPlayer o) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(o, BasketPlayerEntrySumedByPlayerDto.class);
    }
}
