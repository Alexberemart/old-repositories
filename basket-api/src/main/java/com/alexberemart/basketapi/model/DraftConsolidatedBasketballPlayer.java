package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.DraftConsolidatedBasketballPlayerEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DraftConsolidatedBasketballPlayer {

    protected BasketPlayer basketPlayer1;
    protected BasketPlayer basketPlayer2;
    protected Double nameScoring;
    protected Double countryScoring;
    protected Double totalScoring;

    public DraftConsolidatedBasketballPlayerEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        DraftConsolidatedBasketballPlayerEntity draftConsolidatedBasketballPlayerEntity = modelMapper.map(this, DraftConsolidatedBasketballPlayerEntity.class);
        return draftConsolidatedBasketballPlayerEntity;
    }

    public Double getTotalScoring(){
        return (nameScoring + countryScoring) / 2;
    }
}
