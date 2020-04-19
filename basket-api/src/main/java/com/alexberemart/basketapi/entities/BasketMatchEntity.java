package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketMatch;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "basket_matches")
@Builder
@JsonAutoDetect
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketMatchEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    protected String id;

    @Column(nullable = false)
    protected Date date;

    @Column(nullable = false, unique = true)
    protected String gameKey;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private SeasonEntity season;

    public BasketMatch toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, BasketMatch.class);
    }
}
