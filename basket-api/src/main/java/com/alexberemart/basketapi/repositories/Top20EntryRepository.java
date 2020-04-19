package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.Top20EntryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Top20EntryRepository extends PagingAndSortingRepository<Top20EntryEntity, String> {

    List<Top20EntryEntity> findByWebKey(String webKey);

}