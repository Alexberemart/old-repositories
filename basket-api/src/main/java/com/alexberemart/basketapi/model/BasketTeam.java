package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketTeamEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketTeam {

    protected String id;
    protected String name;
    protected String referenceId;

    public BasketTeamEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketTeamEntity basketTeamEntity = modelMapper.map(this, BasketTeamEntity.class);
        return basketTeamEntity;
    }
}
