package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.Date;

@Table(name = "top_20_entries")
@Entity
@JsonAutoDetect
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Top20EntryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    protected String id;

    @Column(name = "position", nullable = false)
    protected Integer position;

    @Column(name = "date", nullable = false)
    protected Date date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "basket_player_id", nullable = false)
    protected BasketPlayerEntity basketPlayer;

    @Column(name = "rating", nullable = false)
    protected Float rating;

    @Column(name = "web_key", nullable = false)
    protected String webKey;

    public Top20Entry toModel() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Top20Entry.class);
    }
}
