package com.cs499.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.myapp.domain.Buyer;

import com.cs499.myapp.repository.BuyerRepository;
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
 * REST controller for managing Buyer.
 */
@RestController
@RequestMapping("/api")
public class BuyerResource {

    private final Logger log = LoggerFactory.getLogger(BuyerResource.class);
        
    @Inject
    private BuyerRepository buyerRepository;

    /**
     * POST  /buyers : Create a new buyer.
     *
     * @param buyer the buyer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new buyer, or with status 400 (Bad Request) if the buyer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/buyers")
    @Timed
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) throws URISyntaxException {
        log.debug("REST request to save Buyer : {}", buyer);
        if (buyer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("buyer", "idexists", "A new buyer cannot already have an ID")).body(null);
        }
        Buyer result = buyerRepository.save(buyer);
        return ResponseEntity.created(new URI("/api/buyers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("buyer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /buyers : Updates an existing buyer.
     *
     * @param buyer the buyer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated buyer,
     * or with status 400 (Bad Request) if the buyer is not valid,
     * or with status 500 (Internal Server Error) if the buyer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/buyers")
    @Timed
    public ResponseEntity<Buyer> updateBuyer(@RequestBody Buyer buyer) throws URISyntaxException {
        log.debug("REST request to update Buyer : {}", buyer);
        if (buyer.getId() == null) {
            return createBuyer(buyer);
        }
        Buyer result = buyerRepository.save(buyer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("buyer", buyer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /buyers : get all the buyers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of buyers in body
     */
    @GetMapping("/buyers")
    @Timed
    public List<Buyer> getAllBuyers() {
        log.debug("REST request to get all Buyers");
        List<Buyer> buyers = buyerRepository.findAllWithEagerRelationships();
        return buyers;
    }

    /**
     * GET  /buyers/:id : get the "id" buyer.
     *
     * @param id the id of the buyer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the buyer, or with status 404 (Not Found)
     */
    @GetMapping("/buyers/{id}")
    @Timed
    public ResponseEntity<Buyer> getBuyer(@PathVariable Long id) {
        log.debug("REST request to get Buyer : {}", id);
        Buyer buyer = buyerRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(buyer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /buyers/:id : delete the "id" buyer.
     *
     * @param id the id of the buyer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/buyers/{id}")
    @Timed
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long id) {
        log.debug("REST request to delete Buyer : {}", id);
        buyerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("buyer", id.toString())).build();
    }

}
