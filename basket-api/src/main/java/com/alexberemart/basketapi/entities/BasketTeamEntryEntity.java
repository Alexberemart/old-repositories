package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketMatchTeamType;
import com.alexberemart.basketapi.model.BasketTeamEntry;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.*;

@Table(name = "basket_team_entries",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"basket_team_id", "basket_match_id"}),
                @UniqueConstraint(columnNames = {"basket_match_id", "type"})})
@Entity
@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketTeamEntryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_team_id", nullable = false)
    protected BasketTeamEntity basketTeam;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_match_id", nullable = false)
    protected BasketMatchEntity basketMatch;

    @Column(name = "type", nullable = false)
    @Getter
    protected Integer type;

    @Column
    protected Integer points;

    public BasketTeamEntry toModel() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<BasketTeamEntryEntity, BasketTeamEntry>() {
            @Override
            protected void configure() {
                map().setType(source.getType());
            }
        });
        return modelMapper.map(this, BasketTeamEntry.class);
    }

    public void setType(BasketMatchTeamType type) {
        this.type = type.getCode();
    }
}
