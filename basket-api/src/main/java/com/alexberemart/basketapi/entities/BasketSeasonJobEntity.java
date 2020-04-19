package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketSeasonJob;
import com.alexberemart.basketapi.model.BasketSeasonJobState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.*;

@Table(name = "basket_season_jobs")
@Entity
@JsonAutoDetect
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketSeasonJobEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Setter
    @Getter
    protected String id;

    @Column(name = "state", nullable = false)
    @Getter
    protected Integer state;

    @Column(name = "web_key", nullable = false)
    @Getter
    @Setter
    protected String webKey;

    public BasketSeasonJob toModel() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<BasketSeasonJobEntity, BasketSeasonJob>() {
            @Override
            protected void configure() {
                map().setState(source.getState());
            }
        });
        return modelMapper.map(this, BasketSeasonJob.class);
    }

    public void setState(BasketSeasonJobState state) {
        this.state = state.getCode();
    }
}
