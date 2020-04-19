package com.alexberemart.basketapi.repositories;

import com.alexberemart.basketapi.entities.BasketPlayerEntity;
import com.alexberemart.basketapi.entities.BasketPlayerEntryEntity;
import com.alexberemart.basketapi.model.BasketPlayer;
import com.alexberemart.basketapi.repositories.model.IFindSumStatGroupedByPlayerBySeason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BasketPlayerEntryRepository extends CrudRepository<BasketPlayerEntryEntity, String> {

    List<BasketPlayerEntryEntity> findByBasketMatch_DateOrderByPointsDesc(Date MatchDate, Pageable pageable);

    @Query(value = "select bpe.basketPlayer from BasketPlayerEntryEntity bpe group by bpe.basketPlayer")
    List<Object> findGroupedByPlayer();

    @Query(value = "select bpe.basketPlayer as basketPlayer, " +
            "sum(bpe.points) as points " +
            "from BasketPlayerEntryEntity bpe " +
            "where (:country is null or bpe.basketPlayer.country = :country) " +
            "group by bpe.basketPlayer")
    Page<SumStatsGruopedByPlayer> sumStatsGroupedByPlayer(Pageable pageable, @Param("country") String country);

    @Query(value = "select bpe.basketPlayer " +
            "from BasketPlayerEntryEntity bpe, BasketMatchEntity bm " +
            "where bm = bpe.basketMatch " +
            "AND bm.season.webKey = :seasonWebKey " +
            "group by bpe.basketPlayer")
    List<Object> findGroupedByPlayerBySeason(@Param("seasonWebKey") String seasonWebKey);

    @Query(value = "select bpe.basketPlayer AS basketPlayer, SUM(bpe.points) AS statValue " +
            "from BasketPlayerEntryEntity bpe, BasketMatchEntity bm " +
            "where bm = bpe.basketMatch " +
            "AND bm.season.id = :seasonId " +
            "group by bpe.basketPlayer " +
            "order by statValue DESC")
    List<IFindSumStatGroupedByPlayerBySeason> findSumPointsGroupedByPlayerBySeason(@Param("seasonId") String seasonId);

    @Query(value = "select bpe.basketPlayer AS basketPlayer, SUM(bpe.rebounds) AS statValue " +
            "from BasketPlayerEntryEntity bpe, BasketMatchEntity bm " +
            "where bm = bpe.basketMatch " +
            "AND bm.season.id = :seasonId " +
            "group by bpe.basketPlayer " +
            "order by statValue DESC")
    List<IFindSumStatGroupedByPlayerBySeason> findSumReboundsGroupedByPlayerBySeason(@Param("seasonId") String seasonId);

    List<BasketPlayerEntryEntity> findByBasketPlayerOrderByBasketMatch_DateAsc(BasketPlayerEntity basketPlayerEntity);
    List<BasketPlayerEntryEntity> findByBasketPlayerAndBasketMatch_Season_WebKeyOrderByBasketMatch_DateAsc(BasketPlayerEntity basketPlayerEntity, String seasonWebKey);
    List<BasketPlayerEntryEntity> findByBasketMatch_GameKey(String gameKey);

    List<BasketPlayerEntryEntity> findAllByOrderByBasketMatch_DateDesc();

    List<BasketPlayerEntryEntity> findByBasketMatch_season_WebKeyOrderByBasketMatch_DateDesc(String seasonWebKey);

    List<BasketPlayerEntryEntity> findByBasketMatch_DateLessThanAndBasketMatch_DateGreaterThanEqual(Date before, Date after);

    @Query(value = "select bpe.basketPlayer " +
            "from BasketPlayerEntryEntity bpe " +
            "where bpe.basketMatch.season.basketOrigin.id = :basketOriginId " +
            "group by bpe.basketPlayer")
    List<BasketPlayerEntity> groupByBasketMatch_Season_BasketOrigin_Id(@Param("basketOriginId") String basketOriginId);
}

