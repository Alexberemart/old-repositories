package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.SeasonEntity;
import com.alexberemart.basketapi.model.Season;
import com.alexberemart.basketapi.repositories.SeasonRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class SeasonServices {

    protected SeasonRepository seasonRepository;

    public Season save(Season season) {
        return fromEntity(seasonRepository.save(toEntity(season)));
    }

    public Season saveIfNotExist(Season season) {
        List<SeasonEntity> seasonEntities = seasonRepository.findByWebKey(season.getWebKey());
        if (seasonEntities.size() != 0) {
            return fromEntity(seasonEntities.get(0));
        }
        return save(season);
    }

    protected SeasonEntity toEntity(Season season) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(season, SeasonEntity.class);
    }

    protected Season fromEntity(SeasonEntity seasonEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(seasonEntity, Season.class);
    }

    public Season findOne(String id) {
        return fromEntity(seasonRepository.findById(id).get());
    }

    public Season findByWebKey(String id) {
        List<SeasonEntity> seasonEntities = seasonRepository.findByWebKey(id);

        if (seasonEntities.size() == 0) {
            return null;
        }

        return fromEntity(seasonEntities.get(0));
    }

    public List<Season> findAll() {
        return toModelList(seasonRepository.findAll());
    }

    protected List<Season> toModelList(Iterable<SeasonEntity> seasonEntities) {
        List<Season> result = new ArrayList<>();
        seasonEntities.forEach(seasonEntity -> result.add(seasonEntity.toModel()));
        return result;
    }
}
