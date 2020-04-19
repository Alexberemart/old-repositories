package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.DraftConsolidatedBasketballPlayer;
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

@Table(name = "draft_consolidated_basketball_players")
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DraftConsolidatedBasketballPlayerEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_player_id_1", nullable = false)
    protected BasketPlayerEntity basketPlayer1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_player_id_2", nullable = false)
    protected BasketPlayerEntity basketPlayer2;

    @Column(name = "name_scoring")
    protected Float nameScoring;

    @Column(name = "country_scoring")
    protected Double countryScoring;

    @Column(name = "total_scoring")
    protected Double totalScoring;

    public DraftConsolidatedBasketballPlayer toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, DraftConsolidatedBasketballPlayer.class);
    }
}
