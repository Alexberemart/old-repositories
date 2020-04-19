package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketMatchEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketMatchRepository extends CrudRepository<BasketMatchEntity, String> {

    List<BasketMatchEntity> findByGameKey(String gameKey);

    List<BasketMatchEntity> findBySeason_WebKey(String seasonWebKey);

    List<BasketMatchEntity> findBySeason_IdIn(List<String> seasonIdList);

    BasketMatchEntity findFirstByOrderByDateDesc();

}
