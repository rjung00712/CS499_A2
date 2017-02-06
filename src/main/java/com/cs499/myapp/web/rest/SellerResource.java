package com.cs499.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.myapp.domain.Seller;

import com.cs499.myapp.repository.SellerRepository;
import com.cs499.myapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Seller.
 */
@RestController
@RequestMapping("/api")
public class SellerResource {

    private final Logger log = LoggerFactory.getLogger(SellerResource.class);
        
    @Inject
    private SellerRepository sellerRepository;

    /**
     * POST  /sellers : Create a new seller.
     *
     * @param seller the seller to create
     * @return the ResponseEntity with status 201 (Created) and with body the new seller, or with status 400 (Bad Request) if the seller has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sellers")
    @Timed
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws URISyntaxException {
        log.debug("REST request to save Seller : {}", seller);
        if (seller.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("seller", "idexists", "A new seller cannot already have an ID")).body(null);
        }
        Seller result = sellerRepository.save(seller);
        return ResponseEntity.created(new URI("/api/sellers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("seller", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sellers : Updates an existing seller.
     *
     * @param seller the seller to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated seller,
     * or with status 400 (Bad Request) if the seller is not valid,
     * or with status 500 (Internal Server Error) if the seller couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sellers")
    @Timed
    public ResponseEntity<Seller> updateSeller(@RequestBody Seller seller) throws URISyntaxException {
        log.debug("REST request to update Seller : {}", seller);
        if (seller.getId() == null) {
            return createSeller(seller);
        }
        Seller result = sellerRepository.save(seller);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("seller", seller.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sellers : get all the sellers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sellers in body
     */
    @GetMapping("/sellers")
    @Timed
    public List<Seller> getAllSellers() {
        log.debug("REST request to get all Sellers");
        List<Seller> sellers = sellerRepository.findAllWithEagerRelationships();
        return sellers;
    }

    /**
     * GET  /sellers/:id : get the "id" seller.
     *
     * @param id the id of the seller to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the seller, or with status 404 (Not Found)
     */
    @GetMapping("/sellers/{id}")
    @Timed
    public ResponseEntity<Seller> getSeller(@PathVariable Long id) {
        log.debug("REST request to get Seller : {}", id);
        Seller seller = sellerRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(seller)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sellers/:id : delete the "id" seller.
     *
     * @param id the id of the seller to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sellers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        log.debug("REST request to delete Seller : {}", id);
        sellerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("seller", id.toString())).build();
    }

}
