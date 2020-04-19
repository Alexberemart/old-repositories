package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "basketPlayer", path = "basketPlayer")
public interface BasketPlayerRepository extends JpaRepository<BasketPlayerEntity, String> {

    Iterable<BasketPlayerEntity> findByName(String name);

    List<BasketPlayerEntity> findByReferenceId(String referenceId);
}