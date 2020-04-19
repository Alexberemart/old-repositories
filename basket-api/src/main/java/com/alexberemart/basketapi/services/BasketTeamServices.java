package com.alexberemart.basketapi.services;

import com.alexberemart.basketapi.entities.BasketTeamEntity;
import com.alexberemart.basketapi.model.BasketTeam;
import com.alexberemart.basketapi.repositories.BasketTeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by aberenguer on 06/07/2017.
 */
@Service
@AllArgsConstructor
public class BasketTeamServices {

    protected BasketTeamRepository basketTeamRepository;

    public BasketTeam saveIfNotExist(String teamName, String teamKey) {
        BasketTeam basketTeam =  BasketTeam.builder()
                .name(teamName)
                .referenceId(teamKey)
                .build();
        List<BasketTeamEntity> basketTeamEntityList = basketTeamRepository.findByReferenceId(basketTeam.getReferenceId());
        if (basketTeamEntityList.size() != 0) {
            return basketTeamEntityList.get(0).toModel();
        }
        return save(basketTeam);
    }

    public BasketTeam save(BasketTeam basketTeam) {
        return basketTeamRepository.save(basketTeam.toEntity()).toModel();
    }
}
