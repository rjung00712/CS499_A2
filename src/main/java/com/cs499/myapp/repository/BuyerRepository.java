package com.cs499.myapp.repository;

import com.cs499.myapp.domain.Buyer;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Buyer entity.
 */
@SuppressWarnings("unused")
public interface BuyerRepository extends JpaRepository<Buyer,Long> {

    @Query("select distinct buyer from Buyer buyer left join fetch buyer.transacts")
    List<Buyer> findAllWithEagerRelationships();

    @Query("select buyer from Buyer buyer left join fetch buyer.transacts where buyer.id =:id")
    Buyer findOneWithEagerRelationships(@Param("id") Long id);

}
