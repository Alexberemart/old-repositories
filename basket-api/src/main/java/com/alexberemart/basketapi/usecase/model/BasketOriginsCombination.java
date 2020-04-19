package com.alexberemart.basketapi.usecase.model;

import com.alexberemart.basketapi.model.BasketOrigin;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BasketOriginsCombination {

    BasketOrigin basketOrigin1;
    BasketOrigin basketOrigin2;
}
