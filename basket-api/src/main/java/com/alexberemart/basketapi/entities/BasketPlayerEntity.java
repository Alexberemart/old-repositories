package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketPlayer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.Date;

@Table(name = "basket_players")
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketPlayerEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    protected String id;

    protected String name;

    @Column(unique = true, nullable = false)
    protected String referenceId;

    @Column(nullable = false)
    protected Date birthDate;

    @Column
    protected String country;

    public BasketPlayer toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, BasketPlayer.class);
    }
}
