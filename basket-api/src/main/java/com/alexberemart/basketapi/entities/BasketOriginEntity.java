package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketOrigin;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;

@Table(name = "basket_origins")
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketOriginEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    protected String id;

    @Column(nullable = false, unique = true)
    protected String name;

    public BasketOrigin toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, BasketOrigin.class);
    }
}
