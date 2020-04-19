package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.DraftConsolidatedBasketballPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftConsolidatedBasketballPlayerRepository extends JpaRepository<DraftConsolidatedBasketballPlayerEntity, String> {

}