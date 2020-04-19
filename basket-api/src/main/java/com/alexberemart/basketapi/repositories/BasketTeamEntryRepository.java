package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketTeamEntryEntity;
import com.alexberemart.basketapi.model.BasketMatchTeamType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BasketTeamEntryRepository extends CrudRepository<BasketTeamEntryEntity, String> {

    List<BasketTeamEntryEntity> findByBasketMatch_Id(String basketMatchId);

    List<BasketTeamEntryEntity> findByBasketMatch_DateOrderByBasketMatch_IdDescTypeAsc(Date date);

    BasketTeamEntryEntity findByBasketMatch_IdAndType(String basketMatchId, BasketMatchTeamType basketMatchTeamType);

    List<BasketTeamEntryEntity> findTopByBasketMatch_DateLessThanAndAndBasketTeam_IdOrderByBasketMatch_DateDesc(Date basketMatchDate, String basketTeamId);

    List<BasketTeamEntryEntity> findTopByBasketMatch_IdAndAndBasketTeam_IdNot(String basketMatchId, String basketTeamId);

//    select basket_match_id, bm.date, COUNT(*)
//    from basket_team_entries bte, basket_matches bm
//    where (basket_team_id = '2c91808361ad3a8c0161ad3b76f30000'
//            Or basket_team_id = '2c91808361ad3a8c0161ad3b776f0001')
//    AND bm.id = bte.basket_match_id
//    AND bm.date < '2019-01-01'
//    group by basket_match_id, bm.date
//    order by bm.date desc

    @Query(value = "select bte.basketMatch as basketMatch, " +
            "COUNT(bte) as count " +
            "from BasketTeamEntryEntity bte " +
            "where (basket_team_id = :teamId1 " +
            "      Or basket_team_id = :teamId2) " +
            "AND bte.basketMatch.date < :basketMatchDate " +
            "group by bte.basketMatch.id " +
            "order by bte.basketMatch.date desc")
    List<BasketTeamEntryLimited> findGroupedByBasketMatch_DateLessThanAndBasketTeam_IdIn(
            @Param("basketMatchDate") Date basketMatchDate,
            @Param("teamId1") String teamId1,
            @Param("teamId2") String teamId2);

//    select basket_match_id, bm.date
//    from basket_team_entries bte, basket_matches bm
//    where basket_team_id = '4028810861ad88490161ad88d4070001'
//    AND bm.id = bte.basket_match_id
//    AND bm.date < '2018-02-26'
//    AND bm.date > '2018-02-23'
//    order by bm.date desc

    List<BasketTeamEntryEntity> findByBasketTeam_IdAndBasketMatch_DateBetweenOrderByBasketMatch_DateDesc(String basketTeamId, Date basketMatchDateStart, Date basketMatchDateEnd);
}

