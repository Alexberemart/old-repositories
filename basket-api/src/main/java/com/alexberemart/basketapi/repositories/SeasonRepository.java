package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.SeasonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends CrudRepository<SeasonEntity, String> {

    List<SeasonEntity> findByWebKey(@Param("webKey") String webKey);
}
