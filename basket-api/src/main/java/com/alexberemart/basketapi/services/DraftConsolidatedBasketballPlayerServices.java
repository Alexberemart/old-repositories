package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.model.DraftConsolidatedBasketballPlayer;
import com.alexberemart.basketapi.repositories.DraftConsolidatedBasketballPlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log
public class DraftConsolidatedBasketballPlayerServices {

    protected DraftConsolidatedBasketballPlayerRepository draftConsolidatedBasketballPlayerRepository;

    public DraftConsolidatedBasketballPlayer save(DraftConsolidatedBasketballPlayer draftConsolidatedBasketballPlayer) {
        return draftConsolidatedBasketballPlayerRepository.save(draftConsolidatedBasketballPlayer.toEntity()).toModel();
    }

    public void deleteAll() {
        draftConsolidatedBasketballPlayerRepository.deleteAll();
    }
}
