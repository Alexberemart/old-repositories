package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.ConsolidatedBasketballPlayer;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "consolidated_basketball_players")
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidatedBasketballPlayerEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    protected String id;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected Date birthDate;

    @Column
    protected String country;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "consolidated_basketball_player_id")
    protected List<BasketPlayerEntity> basketPlayerList;

    public ConsolidatedBasketballPlayer toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ConsolidatedBasketballPlayer.class);
    }
}
