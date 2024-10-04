package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReplacedProductsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReplacedProducts;
import com.mycompany.myapp.repository.ReplacedProductsRepository;
import com.mycompany.myapp.repository.search.ReplacedProductsSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReplacedProductsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReplacedProductsResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_REPLACED_BY_USER_ID = 1;
    private static final Integer UPDATED_REPLACED_BY_USER_ID = 2;

    private static final Integer DEFAULT_REPLACED_ID = 1;
    private static final Integer UPDATED_REPLACED_ID = 2;

    private static final Long DEFAULT_REPLACED_PRODUCT_ID = 1L;
    private static final Long UPDATED_REPLACED_PRODUCT_ID = 2L;

    private static final LocalDate DEFAULT_REPLACEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPLACEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final String ENTITY_API_URL = "/api/replaced-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/replaced-products/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReplacedProductsRepository replacedProductsRepository;

    @Autowired
    private ReplacedProductsSearchRepository replacedProductsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReplacedProductsMockMvc;

    private ReplacedProducts replacedProducts;

    private ReplacedProducts insertedReplacedProducts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReplacedProducts createEntity() {
        return new ReplacedProducts()
            .isActive(DEFAULT_IS_ACTIVE)
            .productId(DEFAULT_PRODUCT_ID)
            .replacedByUserId(DEFAULT_REPLACED_BY_USER_ID)
            .replacedId(DEFAULT_REPLACED_ID)
            .replacedProductId(DEFAULT_REPLACED_PRODUCT_ID)
            .replacementDate(DEFAULT_REPLACEMENT_DATE)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReplacedProducts createUpdatedEntity() {
        return new ReplacedProducts()
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .replacedByUserId(UPDATED_REPLACED_BY_USER_ID)
            .replacedId(UPDATED_REPLACED_ID)
            .replacedProductId(UPDATED_REPLACED_PRODUCT_ID)
            .replacementDate(UPDATED_REPLACEMENT_DATE)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID);
    }

    @BeforeEach
    public void initTest() {
        replacedProducts = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReplacedProducts != null) {
            replacedProductsRepository.delete(insertedReplacedProducts);
            replacedProductsSearchRepository.delete(insertedReplacedProducts);
            insertedReplacedProducts = null;
        }
    }

    @Test
    @Transactional
    void createReplacedProducts() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        // Create the ReplacedProducts
        var returnedReplacedProducts = om.readValue(
            restReplacedProductsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(replacedProducts)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReplacedProducts.class
        );

        // Validate the ReplacedProducts in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReplacedProductsUpdatableFieldsEquals(returnedReplacedProducts, getPersistedReplacedProducts(returnedReplacedProducts));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedReplacedProducts = returnedReplacedProducts;
    }

    @Test
    @Transactional
    void createReplacedProductsWithExistingId() throws Exception {
        // Create the ReplacedProducts with an existing ID
        replacedProducts.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restReplacedProductsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(replacedProducts)))
            .andExpect(status().isBadRequest());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllReplacedProducts() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);

        // Get all the replacedProductsList
        restReplacedProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(replacedProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].replacedByUserId").value(hasItem(DEFAULT_REPLACED_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].replacedId").value(hasItem(DEFAULT_REPLACED_ID)))
            .andExpect(jsonPath("$.[*].replacedProductId").value(hasItem(DEFAULT_REPLACED_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].replacementDate").value(hasItem(DEFAULT_REPLACEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)));
    }

    @Test
    @Transactional
    void getReplacedProducts() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);

        // Get the replacedProducts
        restReplacedProductsMockMvc
            .perform(get(ENTITY_API_URL_ID, replacedProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(replacedProducts.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.replacedByUserId").value(DEFAULT_REPLACED_BY_USER_ID))
            .andExpect(jsonPath("$.replacedId").value(DEFAULT_REPLACED_ID))
            .andExpect(jsonPath("$.replacedProductId").value(DEFAULT_REPLACED_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.replacementDate").value(DEFAULT_REPLACEMENT_DATE.toString()))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID));
    }

    @Test
    @Transactional
    void getNonExistingReplacedProducts() throws Exception {
        // Get the replacedProducts
        restReplacedProductsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReplacedProducts() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        replacedProductsSearchRepository.save(replacedProducts);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());

        // Update the replacedProducts
        ReplacedProducts updatedReplacedProducts = replacedProductsRepository.findById(replacedProducts.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReplacedProducts are not directly saved in db
        em.detach(updatedReplacedProducts);
        updatedReplacedProducts
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .replacedByUserId(UPDATED_REPLACED_BY_USER_ID)
            .replacedId(UPDATED_REPLACED_ID)
            .replacedProductId(UPDATED_REPLACED_PRODUCT_ID)
            .replacementDate(UPDATED_REPLACEMENT_DATE)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID);

        restReplacedProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReplacedProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReplacedProducts))
            )
            .andExpect(status().isOk());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReplacedProductsToMatchAllProperties(updatedReplacedProducts);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ReplacedProducts> replacedProductsSearchList = Streamable.of(replacedProductsSearchRepository.findAll()).toList();
                ReplacedProducts testReplacedProductsSearch = replacedProductsSearchList.get(searchDatabaseSizeAfter - 1);

                assertReplacedProductsAllPropertiesEquals(testReplacedProductsSearch, updatedReplacedProducts);
            });
    }

    @Test
    @Transactional
    void putNonExistingReplacedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        replacedProducts.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReplacedProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, replacedProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(replacedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchReplacedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        replacedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReplacedProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(replacedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReplacedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        replacedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReplacedProductsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(replacedProducts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateReplacedProductsWithPatch() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the replacedProducts using partial update
        ReplacedProducts partialUpdatedReplacedProducts = new ReplacedProducts();
        partialUpdatedReplacedProducts.setId(replacedProducts.getId());

        partialUpdatedReplacedProducts
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .replacedByUserId(UPDATED_REPLACED_BY_USER_ID)
            .replacedId(UPDATED_REPLACED_ID)
            .replacedProductId(UPDATED_REPLACED_PRODUCT_ID);

        restReplacedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReplacedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReplacedProducts))
            )
            .andExpect(status().isOk());

        // Validate the ReplacedProducts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReplacedProductsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReplacedProducts, replacedProducts),
            getPersistedReplacedProducts(replacedProducts)
        );
    }

    @Test
    @Transactional
    void fullUpdateReplacedProductsWithPatch() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the replacedProducts using partial update
        ReplacedProducts partialUpdatedReplacedProducts = new ReplacedProducts();
        partialUpdatedReplacedProducts.setId(replacedProducts.getId());

        partialUpdatedReplacedProducts
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .replacedByUserId(UPDATED_REPLACED_BY_USER_ID)
            .replacedId(UPDATED_REPLACED_ID)
            .replacedProductId(UPDATED_REPLACED_PRODUCT_ID)
            .replacementDate(UPDATED_REPLACEMENT_DATE)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID);

        restReplacedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReplacedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReplacedProducts))
            )
            .andExpect(status().isOk());

        // Validate the ReplacedProducts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReplacedProductsUpdatableFieldsEquals(
            partialUpdatedReplacedProducts,
            getPersistedReplacedProducts(partialUpdatedReplacedProducts)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReplacedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        replacedProducts.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReplacedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, replacedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(replacedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReplacedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        replacedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReplacedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(replacedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReplacedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        replacedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReplacedProductsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(replacedProducts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReplacedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteReplacedProducts() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);
        replacedProductsRepository.save(replacedProducts);
        replacedProductsSearchRepository.save(replacedProducts);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the replacedProducts
        restReplacedProductsMockMvc
            .perform(delete(ENTITY_API_URL_ID, replacedProducts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(replacedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchReplacedProducts() throws Exception {
        // Initialize the database
        insertedReplacedProducts = replacedProductsRepository.saveAndFlush(replacedProducts);
        replacedProductsSearchRepository.save(replacedProducts);

        // Search the replacedProducts
        restReplacedProductsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + replacedProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(replacedProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].replacedByUserId").value(hasItem(DEFAULT_REPLACED_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].replacedId").value(hasItem(DEFAULT_REPLACED_ID)))
            .andExpect(jsonPath("$.[*].replacedProductId").value(hasItem(DEFAULT_REPLACED_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].replacementDate").value(hasItem(DEFAULT_REPLACEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)));
    }

    protected long getRepositoryCount() {
        return replacedProductsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ReplacedProducts getPersistedReplacedProducts(ReplacedProducts replacedProducts) {
        return replacedProductsRepository.findById(replacedProducts.getId()).orElseThrow();
    }

    protected void assertPersistedReplacedProductsToMatchAllProperties(ReplacedProducts expectedReplacedProducts) {
        assertReplacedProductsAllPropertiesEquals(expectedReplacedProducts, getPersistedReplacedProducts(expectedReplacedProducts));
    }

    protected void assertPersistedReplacedProductsToMatchUpdatableProperties(ReplacedProducts expectedReplacedProducts) {
        assertReplacedProductsAllUpdatablePropertiesEquals(
            expectedReplacedProducts,
            getPersistedReplacedProducts(expectedReplacedProducts)
        );
    }
}
