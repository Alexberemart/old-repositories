package com.alexberemart.basketapi.usecase;

import com.alexberemart.basketapi.model.BasketOrigin;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.model.DraftConsolidatedBasketballPlayer;
import com.alexberemart.basketapi.services.BasketOriginServices;
import com.alexberemart.basketapi.services.BasketPlayerEntryServices;
import com.alexberemart.basketapi.services.DraftConsolidatedBasketballPlayerServices;
import com.alexberemart.basketapi.usecase.model.BasketOriginsCombination;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.alexberemart.common_utils.MathUtils.round;
import static com.alexberemart.common_utils.StringUtils.similarity;

@Service
@AllArgsConstructor
@Log
public class CreateDraftConsolidatedBasketBallPlayers {

    protected BasketPlayerEntryServices basketPlayerEntryServices;
    protected BasketOriginServices basketOriginServices;
    protected DraftConsolidatedBasketballPlayerServices draftConsolidatedBasketballPlayerServices;

    public List<DraftConsolidatedBasketballPlayer> execute(Double threshold) {
        List<DraftConsolidatedBasketballPlayer> result = new ArrayList<>();

        draftConsolidatedBasketballPlayerServices.deleteAll();
        List<BasketOrigin> basketOriginList = basketOriginServices.findAll();

        log.info("start to combine basket origins");

        List<BasketOriginsCombination> basketOriginsCombinationList = new ArrayList<>();
        int startPosition = 0;
        while (basketOriginList.size() > startPosition + 1) {
            BasketOrigin firstBasketOrigin = BasketOrigin.builder().build();
            for (BasketOrigin basketOrigin : basketOriginList) {
                if (basketOriginList.indexOf(basketOrigin) == startPosition) {
                    firstBasketOrigin = basketOrigin;
                } else {
                    basketOriginsCombinationList.add(BasketOriginsCombination.builder()
                            .basketOrigin1(firstBasketOrigin)
                            .basketOrigin2(basketOrigin)
                            .build());
                }
            }
            startPosition ++;
        }

        log.info("end to combine basket origins");
        log.info("start to combine basket players");

        for (BasketOriginsCombination basketOriginsCombination : basketOriginsCombinationList) {
            List<BasketPlayer> basketPlayerList1 = basketPlayerEntryServices.getBasketPlayerByBasketOriginId(basketOriginsCombination.getBasketOrigin1().getId());
            List<BasketPlayer> basketPlayerList2 = basketPlayerEntryServices.getBasketPlayerByBasketOriginId(basketOriginsCombination.getBasketOrigin2().getId());
            Integer totalElements = basketPlayerList1.size();
            Integer counter = 0;
            for (BasketPlayer basketPlayer1 : basketPlayerList1) {
                for (BasketPlayer basketPlayer2 : basketPlayerList2) {

                    if (basketPlayer1.getId().equals(basketPlayer2.getId())){
                        throw new RuntimeException("same player is reported by two different origins: player_id (" +
                                basketPlayer1.getId() + ") origin_id_1: (" +
                                basketOriginsCombination.getBasketOrigin1().getId() + ") origin_id_2: (" +
                                basketOriginsCombination.getBasketOrigin2().getId());
                    }

                    DraftConsolidatedBasketballPlayer draftConsolidatedBasketballPlayer = DraftConsolidatedBasketballPlayer.builder()
                            .basketPlayer1(basketPlayer1)
                            .basketPlayer2(basketPlayer2)
                            .nameScoring(similarity(basketPlayer1.getName(), basketPlayer2.getName()))
                            .countryScoring(similarity(basketPlayer1.getCountry(), basketPlayer2.getCountry()))
                            .build();
                    if (draftConsolidatedBasketballPlayer.getTotalScoring() >= threshold) {
                        result.add(draftConsolidatedBasketballPlayerServices.save(draftConsolidatedBasketballPlayer));
                    }
                }
                counter ++;
                log.info("player " + counter.toString() + " combined (" + round(((double) counter / totalElements) * 100, 1) + "%)");
            }
        }

        log.info("end to combine basket players");

        return result;
    }
}
