package com.alexberemart.basketscraping.usecase;

import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.services.BasketJobServices;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
@Log
public class CreateBasketJobsFromYear {

    protected BasketJobServices basketJobServices;

    public List<BasketJob> execute(Integer year) {

        List<BasketJob> result = new ArrayList<>();

        switch (year) {
            case 2012:
                result.add(basketJobServices.createJob("NBA_" + year + "_games-december", ((year - 1) * 100) + 12, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-january", (year * 100) + 1, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-february", (year * 100) + 2, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-march", (year * 100) + 3, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-april", (year * 100) + 4, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-may", (year * 100) + 5, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-june", (year * 100) + 6, Boolean.FALSE));
                break;
            default:
                result.add(basketJobServices.createJob("NBA_" + year + "_games-october", ((year - 1) * 100) + 10, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-november", ((year - 1) * 100) + 11, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-december", ((year - 1) * 100) + 12, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-january", (year * 100) + 1, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-february", (year * 100) + 2, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-march", (year * 100) + 3, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-april", (year * 100) + 4, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-may", (year * 100) + 5, Boolean.FALSE));
                result.add(basketJobServices.createJob("NBA_" + year + "_games-june", (year * 100) + 6, Boolean.FALSE));
                break;
        }

        return result;
    }

}