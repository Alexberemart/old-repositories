package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.BasketPlayerEntry;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.*;

@Table(name = "basket_player_entries",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"basket_player_id", "basket_match_id"})})
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketPlayerEntryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_player_id", nullable = false)
    protected BasketPlayerEntity basketPlayer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_match_id", nullable = false)
    protected BasketMatchEntity basketMatch;

    @Column(name = "type", nullable = false)
    @Getter
    protected Integer type;

    @Column
    protected Integer points;

    @Column
    protected Integer rebounds;

    @Column
    protected Integer assists;

    public BasketPlayerEntry toModel() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<BasketPlayerEntryEntity, BasketPlayerEntry>() {
            @Override
            protected void configure() {
                map().setBasketMatchTeamType(source.getType());
            }
        });
        return modelMapper.map(this, BasketPlayerEntry.class);
    }

    public void setType(BasketMatchTeamType type) {
        this.type = type.getCode();
    }
}
