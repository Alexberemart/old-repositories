package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketOriginEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketOriginRepository extends JpaRepository<BasketOriginEntity, String> {

    List<BasketOriginEntity> findByName(String name);

}