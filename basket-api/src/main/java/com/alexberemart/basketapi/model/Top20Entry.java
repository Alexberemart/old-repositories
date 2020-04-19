package com.alexberemart.basketapi.model;

import com.alexberemart.basketapi.entities.Top20EntryEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.Date;

@JsonAutoDetect
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Top20Entry {

    protected String id;
    protected Integer position;
    protected Date date;
    protected BasketPlayer basketPlayer;
    protected Float rating;
    protected String webKey;

    public Top20EntryEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        Top20EntryEntity top20EntryEntity = modelMapper.map(this, Top20EntryEntity.class);
        return top20EntryEntity;
    }

}
