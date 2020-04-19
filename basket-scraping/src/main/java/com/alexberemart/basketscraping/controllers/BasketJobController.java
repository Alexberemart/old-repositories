package com.alexberemart.basketscraping.controllers;

import com.alexberemart.basketscraping.dto.BasketJobDto;
import com.alexberemart.basketscraping.exception.IllegalBasketJobException;
import com.alexberemart.basketscraping.exception.IllegalSeasonException;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.services.BasketJobServices;
import com.alexberemart.basketscraping.usecase.ProcessNextBasketJob;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/basket-job")
@AllArgsConstructor
public class BasketJobController {

    protected BasketJobServices basketJobServices;
    protected ProcessNextBasketJob processNextBasketJob;

    @GetMapping("/existBasketJobByWebKeyLikeNonFinished/{webKey}")
    public Boolean existBasketJobByWebKeyLikeNonFinished(@PathVariable("webKey") String webKey) {
        return basketJobServices.existBasketJobByWebKeyLikeNonFinished(webKey);
    }

    @GetMapping("/restartJobs")
    public List<BasketJob> restartJobs() {
        return basketJobServices.restartJobs();
    }

    @PostMapping(value = "/createjob")
    public ResponseEntity<BasketJob> createJob(@RequestBody BasketJobDto basketJobDto) {
        return ResponseEntity.ok(basketJobServices.createJob(basketJobDto));
    }

    @GetMapping("/processNextBasketJob")
    public void processNextBasketJob() throws IOException, ParseException, IllegalSeasonException, IllegalBasketJobException {
        processNextBasketJob.execute();
    }

}
