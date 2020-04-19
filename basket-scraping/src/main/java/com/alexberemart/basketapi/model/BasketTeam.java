package com.alexberemart.basketapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@JsonAutoDetect
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketTeam {

    protected String id;
    protected String name;
    protected String referenceId;

}
