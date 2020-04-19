package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketTeam;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;

@Table(name = "basket_teams")
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketTeamEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    protected String name;

    @Column(unique = true)
    protected String referenceId;

    public BasketTeam toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, BasketTeam.class);
    }
}
