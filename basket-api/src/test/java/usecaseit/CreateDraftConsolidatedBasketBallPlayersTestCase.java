package usecaseit;

import com.alexberemart.basketapi.BasketApiIntegrationTest;
import com.alexberemart.basketapi.model.DraftConsolidatedBasketballPlayer;
import com.alexberemart.basketapi.usecase.CreateDraftConsolidatedBasketBallPlayers;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CreateDraftConsolidatedBasketBallPlayersTestCase extends BasketApiIntegrationTest {

    @Autowired
    protected CreateDraftConsolidatedBasketBallPlayers createDraftConsolidatedBasketBallPlayers;

    @Test
    @Transactional
    @DatabaseSetup(value = {
            "classpath:datasets/basket-origins_similarity.xml",
            "classpath:datasets/basket-players_similarity.xml",
            "classpath:datasets/seasons_similarity.xml",
            "classpath:datasets/basket-matches_similarity.xml",
            "classpath:datasets/basket-player-entries_similarity.xml"})
    public void execute() {
        //https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
        List<DraftConsolidatedBasketballPlayer> consolidatedBasketballPlayerList = createDraftConsolidatedBasketBallPlayers.execute(0.0);
        Assert.assertEquals(4, consolidatedBasketballPlayerList.size());
    }

}