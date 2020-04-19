package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketSeasonJobEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketSeasonJobRepository extends CrudRepository<BasketSeasonJobEntity, String> {

    List<BasketSeasonJobEntity> findByStateNot(Integer state);
}