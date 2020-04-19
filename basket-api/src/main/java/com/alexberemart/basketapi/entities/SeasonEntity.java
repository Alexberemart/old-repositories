package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketTeamEntry;
import com.alexberemart.basketapi.model.Season;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seasons")
@Builder
@JsonAutoDetect
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeasonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    protected Date startDate;

    @Column(name = "web_key", nullable = false)
    protected String webKey;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_origin_id", nullable = false)
    protected BasketOriginEntity basketOrigin;

    public Season toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Season.class);
    }
}
