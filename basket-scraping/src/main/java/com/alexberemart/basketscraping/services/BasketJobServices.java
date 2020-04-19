package com.alexberemart.basketscraping.services;

import com.alexberemart.basketscraping.dto.BasketJobDto;
import com.alexberemart.basketscraping.entities.BasketJobEntity;
import com.alexberemart.basketscraping.model.BasketJob;
import com.alexberemart.basketscraping.model.BasketJobState;
import com.alexberemart.basketscraping.model.OriginType;
import com.alexberemart.basketscraping.repositories.BasketJobRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketJobServices {

    protected BasketJobRepository basketJobRepository;

    final BasketJobState finishState = BasketJobState.EVENTS_FINISHED;

    public List<BasketJob> findDownloaded() throws IOException, ParseException {
        return getBasketJobByState(BasketJobState.DOWNLOADED);
    }

    public List<BasketJob> findNonDownloaded() throws IOException, ParseException {
        return getBasketJobByState(BasketJobState.INIT);
    }

    public List<BasketJob> findNonFinished() {
        List<BasketJob> result = new ArrayList<>();
        List<BasketJobEntity> basketJobEntities = basketJobRepository.findByStateNotOrderByPriorityDesc(finishState.getCode());
        for (BasketJobEntity basketJobEntity : basketJobEntities) {
            result.add(basketJobEntity.toModel());
        }
        return result;
    }

    public List<BasketJob> restartJobs() {
        List<BasketJob> result = new ArrayList<>();
        List<BasketJobEntity> basketJobEntities = basketJobRepository.findByRestartJob(Boolean.TRUE);
        for (BasketJobEntity basketJobEntity : basketJobEntities) {
            basketJobEntity.setState(BasketJobState.INIT);
            basketJobRepository.save(basketJobEntity);
            result.add(basketJobEntity.toModel());
        }
        return result;
    }

    public BasketJob save(BasketJob basketJob) {

        if (basketJob.getWebKey() == null){
            throw new IllegalArgumentException("Web Key must be reported");
        }

        return basketJobRepository.save(basketJob.toEntity()).toModel();
    }

    public Boolean existBasketJobByWebKeyLikeNonFinished(String webKey) {
        List<BasketJobEntity> basketJobEntities = basketJobRepository.findByWebKeyLikeAndStateNot(webKey, finishState.getCode());
        return basketJobEntities.size() > 0;
    }

    public BasketJob findOneByWebKey(String webKey) {
        List<BasketJobEntity> basketJobEntityList = basketJobRepository.findByWebKey(webKey);
        if (basketJobEntityList == null) {
            return null;
        }
        if (basketJobEntityList.size() == 0) {
            return null;
        }
        BasketJobEntity basketJobEntity = basketJobEntityList.get(0);
        if (basketJobEntity == null) {
            return null;
        }
        return basketJobEntity.toModel();
    }

    public BasketJob markAsDownloaded(String key) {
        BasketJob basketJob = findOneByWebKey(key);
        if (basketJob == null) {
            throw new RuntimeException("Basket Job with key " + key + " doesn't exists");
        } else {
            basketJob.setState(BasketJobState.DOWNLOADED);
            return save(basketJob);
        }
    }

    protected List<BasketJob> getBasketJobByState(BasketJobState basketJobState) {
        List<BasketJob> result = new ArrayList<>();
        List<BasketJobEntity> basketJobEntities = basketJobRepository.findByState(basketJobState.getCode());
        for (BasketJobEntity basketJobEntity : basketJobEntities) {
            result.add(basketJobEntity.toModel());
        }
        return result;
    }

    public BasketJob createJob(BasketJobDto basketJobDto) {
        BasketJob basketJob = BasketJob.builder()
                .webKey(basketJobDto.getWebKey())
                .originType(OriginType.fromCode(basketJobDto.getOriginType()))
                .build();
        return save(basketJob);
    }

    public BasketJob createJob(String webKey, Integer priority, Boolean restartJob) {
        BasketJob basketJob = BasketJob.builder()
                .webKey(webKey)
                .priority(priority)
                .restartJob(restartJob)
                .build();
        return save(basketJob);
    }
}
