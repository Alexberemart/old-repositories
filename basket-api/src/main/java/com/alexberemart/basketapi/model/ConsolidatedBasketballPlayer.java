package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.entities.DraftConsolidatedBasketballPlayerEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidatedBasketballPlayer {

    protected String id;
    protected String name;
    protected Date birthDate;
    protected String country;
    protected List<BasketPlayerEntity> basketPlayerList;

    public DraftConsolidatedBasketballPlayerEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        DraftConsolidatedBasketballPlayerEntity draftConsolidatedBasketballPlayerEntity = modelMapper.map(this, DraftConsolidatedBasketballPlayerEntity.class);
        return draftConsolidatedBasketballPlayerEntity;
    }
}
