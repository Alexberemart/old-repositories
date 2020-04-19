package com.alexberemart.basketscraping.model;

import com.alexberemart.basketscraping.entities.BasketJobEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@JsonAutoDetect
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasketJob {

    protected String id;
    protected String webKey;
    @Builder.Default
    protected BasketJobState state = BasketJobState.INIT;
    protected Integer priority;
    protected Boolean restartJob;
    protected OriginType originType;

    public void setState(Integer i) {
        this.state = BasketJobState.fromCode(i);
    }

    public void setState(BasketJobState i) {
        this.state = i;
    }

    public void setOriginType(Integer i) {
        this.originType = OriginType.fromCode(i);
    }

    public void setOriginType(OriginType i) {
        this.originType = i;
    }

    public BasketJobEntity toEntity() {
        ModelMapper modelMapper = new ModelMapper();
        BasketJobEntity basketJobEntity = modelMapper.map(this, BasketJobEntity.class);
        return basketJobEntity;
    }

}
