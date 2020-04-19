package com.alexberemart.basketscraping.repositories;

import com.alexberemart.basketscraping.entities.BasketJobEntity;
import com.alexberemart.basketscraping.model.BasketJob;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "basketJob", path = "basketJob")
public interface BasketJobRepository extends CrudRepository<BasketJobEntity, String> {

    List<BasketJobEntity> findByState(@Param("state") Integer state);
    List<BasketJobEntity> findByStateNotOrderByPriorityDesc(@Param("state") Integer state);
    List<BasketJobEntity> findByWebKey(@Param("webKey") String webKey);
    List<BasketJobEntity> findByWebKeyLikeAndStateNot(String webKey, Integer state);
    List<BasketJobEntity> findByRestartJob(Boolean restartJob);

}