package com.cs499.myapp.web.rest;

import com.cs499.myapp.MyappApp;

import com.cs499.myapp.domain.Buyer;
import com.cs499.myapp.repository.BuyerRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BuyerResource REST controller.
 *
 * @see BuyerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class BuyerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Inject
    private BuyerRepository buyerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBuyerMockMvc;

    private Buyer buyer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuyerResource buyerResource = new BuyerResource();
        ReflectionTestUtils.setField(buyerResource, "buyerRepository", buyerRepository);
        this.restBuyerMockMvc = MockMvcBuilders.standaloneSetup(buyerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Buyer createEntity(EntityManager em) {
        Buyer buyer = new Buyer()
                .name(DEFAULT_NAME)
                .address(DEFAULT_ADDRESS);
        return buyer;
    }

    @Before
    public void initTest() {
        buyer = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuyer() throws Exception {
        int databaseSizeBeforeCreate = buyerRepository.findAll().size();

        // Create the Buyer

        restBuyerMockMvc.perform(post("/api/buyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buyer)))
            .andExpect(status().isCreated());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeCreate + 1);
        Buyer testBuyer = buyerList.get(buyerList.size() - 1);
        assertThat(testBuyer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBuyer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createBuyerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = buyerRepository.findAll().size();

        // Create the Buyer with an existing ID
        Buyer existingBuyer = new Buyer();
        existingBuyer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuyerMockMvc.perform(post("/api/buyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBuyer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBuyers() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        // Get all the buyerList
        restBuyerMockMvc.perform(get("/api/buyers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(buyer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);

        // Get the buyer
        restBuyerMockMvc.perform(get("/api/buyers/{id}", buyer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(buyer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBuyer() throws Exception {
        // Get the buyer
        restBuyerMockMvc.perform(get("/api/buyers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Update the buyer
        Buyer updatedBuyer = buyerRepository.findOne(buyer.getId());
        updatedBuyer
                .name(UPDATED_NAME)
                .address(UPDATED_ADDRESS);

        restBuyerMockMvc.perform(put("/api/buyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBuyer)))
            .andExpect(status().isOk());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
        Buyer testBuyer = buyerList.get(buyerList.size() - 1);
        assertThat(testBuyer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBuyer.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingBuyer() throws Exception {
        int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

        // Create the Buyer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBuyerMockMvc.perform(put("/api/buyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buyer)))
            .andExpect(status().isCreated());

        // Validate the Buyer in the database
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBuyer() throws Exception {
        // Initialize the database
        buyerRepository.saveAndFlush(buyer);
        int databaseSizeBeforeDelete = buyerRepository.findAll().size();

        // Get the buyer
        restBuyerMockMvc.perform(delete("/api/buyers/{id}", buyer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Buyer> buyerList = buyerRepository.findAll();
        assertThat(buyerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
