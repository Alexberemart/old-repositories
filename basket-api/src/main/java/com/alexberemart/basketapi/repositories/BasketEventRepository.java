package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketEventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketEventRepository extends PagingAndSortingRepository<BasketEventEntity, String> {

    List<BasketEventEntity> findByWebKey(@Param("webKey") String webKey);
    List<BasketEventEntity> findByWebKeyAndLevel(String webKey, Integer level);

    @Query(value = "select be.level as level, " +
            "be.type as type, " +
            "be.value as value, " +
            "COUNT(be.value) as count " +
            "from BasketEventEntity be " +
            "group by be.level, be.type, be.value")
    List<BasketEventLimited> findGroupByLevelAndTypeAndValue();
}