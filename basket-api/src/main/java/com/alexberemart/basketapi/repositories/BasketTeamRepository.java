package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketTeamRepository extends JpaRepository<BasketTeamEntity, String> {

    List<BasketTeamEntity> findByReferenceId(String referenceId);
}