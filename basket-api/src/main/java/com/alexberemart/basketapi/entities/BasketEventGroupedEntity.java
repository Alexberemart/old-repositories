package com.alexberemart.basketapi.entities;

import com.alexberemart.basketapi.model.BasketEvent;
import com.alexberemart.basketapi.model.BasketEventLevel;
import com.alexberemart.basketapi.model.BasketEventType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.*;
import java.util.Date;

@Table(name = "basket_events")
@Entity
@JsonAutoDetect
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketEventGroupedEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Setter
    @Getter
    protected String id;

    @Column(name = "date", nullable = false)
    @Getter
    @Setter
    protected Date date;

    @Column(name = "type", nullable = false)
    @Getter
    protected Integer type;

    @Column(name = "level", nullable = false)
    @Getter
    protected Integer level;

    @Column(name = "value", nullable = false)
    @Getter
    @Setter
    protected Integer value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "basket_player_id", nullable = false)
    @Getter
    @Setter
    protected BasketPlayerEntity basketPlayer;

    @Column(name = "web_key", nullable = false)
    @Getter
    @Setter
    protected String webKey;

    public BasketEvent toModel() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<BasketEventGroupedEntity, BasketEvent>() {
            @Override
            protected void configure() {
                map().setType(source.getType());
                map().setBasketEventLevel(source.getLevel());
            }
        });
        return modelMapper.map(this, BasketEvent.class);
    }

    public void setType(BasketEventType type) {
        this.type = type.getCode();
    }

    public void setLevel(BasketEventLevel level) {
        this.level = level.getCode();
    }
}
