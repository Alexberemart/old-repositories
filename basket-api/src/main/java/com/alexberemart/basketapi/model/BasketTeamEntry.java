package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketTeamEntry {

    protected String id;
    protected BasketTeam basketTeam;
    protected BasketMatch basketMatch;
    protected Integer points;
    protected BasketMatchTeamType type;

    public void setType(Integer i) {
        this.type = BasketMatchTeamType.fromCode(i);
    }

    public void setBasketMatchTeamType(BasketMatchTeamType i) {
        this.type = i;
    }

    public BasketTeamEntryEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketTeamEntryEntity basketTeamEntryEntity = modelMapper.map(this, BasketTeamEntryEntity.class);
        return basketTeamEntryEntity;
    }

}
