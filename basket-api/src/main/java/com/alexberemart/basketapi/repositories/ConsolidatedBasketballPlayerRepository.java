package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.ConsolidatedBasketballPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsolidatedBasketballPlayerRepository extends JpaRepository<ConsolidatedBasketballPlayerEntity, String> {

}