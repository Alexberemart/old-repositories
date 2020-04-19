package com.alexberemart.basketscraping.entities;

import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.OriginType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "basket_jobs")
@Entity
@JsonAutoDetect
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketJobEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Setter
    @Getter
    protected String id;

    @Column(name = "state", nullable = false)
    @Getter
    protected Integer state;

    @Column(name = "web_key", nullable = false, unique = true)
    @Getter
    @Setter
    protected String webKey;

    @Column(name = "priority")
    @Getter
    @Setter
    protected Integer priority;

    @Column(name = "restart")
    @Getter
    @Setter
    protected Boolean restartJob;

    @Column(name = "origin_type", nullable = false)
    @Getter
    protected Integer originType;

    public BasketJob toModel() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<BasketJobEntity, BasketJob>() {
            @Override
            protected void configure() {
                map().setState(source.getState());
                map().setOriginType(source.getOriginType());
            }
        });
        return modelMapper.map(this, BasketJob.class);
    }

    public void setState(BasketJobState state) {
        this.state = state.getCode();
    }

    public void setOriginType(OriginType originType) {
        if (originType != null) {
            this.originType = originType.getCode();
        }
    }
}
