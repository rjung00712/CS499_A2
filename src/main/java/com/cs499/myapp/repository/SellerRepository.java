package com.cs499.myapp.repository;

import com.cs499.myapp.domain.Seller;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Seller entity.
 */
@SuppressWarnings("unused")
public interface SellerRepository extends JpaRepository<Seller,Long> {

    @Query("select distinct seller from Seller seller left join fetch seller.sells")
    List<Seller> findAllWithEagerRelationships();

    @Query("select seller from Seller seller left join fetch seller.sells where seller.id =:id")
    Seller findOneWithEagerRelationships(@Param("id") Long id);

}
