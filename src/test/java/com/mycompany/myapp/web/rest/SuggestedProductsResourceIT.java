package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SuggestedProductsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SuggestedProducts;
import com.mycompany.myapp.repository.SuggestedProductsRepository;
import com.mycompany.myapp.repository.search.SuggestedProductsSearchRepository;
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
 * Integration tests for the {@link SuggestedProductsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuggestedProductsResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_APPROVE = false;
    private static final Boolean UPDATED_IS_APPROVE = true;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_SUGGESTED_BY_DISTRICT = 1;
    private static final Integer UPDATED_SUGGESTED_BY_DISTRICT = 2;

    private static final Integer DEFAULT_SUGGESTED_BY_USER_ID = 1;
    private static final Integer UPDATED_SUGGESTED_BY_USER_ID = 2;

    private static final Long DEFAULT_SUGGESTED_PRODUCT_ID = 1L;
    private static final Long UPDATED_SUGGESTED_PRODUCT_ID = 2L;

    private static final LocalDate DEFAULT_SUGGESTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUGGESTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SUGGESTION_ID = 1;
    private static final Integer UPDATED_SUGGESTION_ID = 2;

    private static final String ENTITY_API_URL = "/api/suggested-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/suggested-products/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SuggestedProductsRepository suggestedProductsRepository;

    @Autowired
    private SuggestedProductsSearchRepository suggestedProductsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuggestedProductsMockMvc;

    private SuggestedProducts suggestedProducts;

    private SuggestedProducts insertedSuggestedProducts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuggestedProducts createEntity() {
        return new SuggestedProducts()
            .isActive(DEFAULT_IS_ACTIVE)
            .isApprove(DEFAULT_IS_APPROVE)
            .productId(DEFAULT_PRODUCT_ID)
            .suggestedByDistrict(DEFAULT_SUGGESTED_BY_DISTRICT)
            .suggestedByUserId(DEFAULT_SUGGESTED_BY_USER_ID)
            .suggestedProductId(DEFAULT_SUGGESTED_PRODUCT_ID)
            .suggestionDate(DEFAULT_SUGGESTION_DATE)
            .suggestionId(DEFAULT_SUGGESTION_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuggestedProducts createUpdatedEntity() {
        return new SuggestedProducts()
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedByDistrict(UPDATED_SUGGESTED_BY_DISTRICT)
            .suggestedByUserId(UPDATED_SUGGESTED_BY_USER_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .suggestionDate(UPDATED_SUGGESTION_DATE)
            .suggestionId(UPDATED_SUGGESTION_ID);
    }

    @BeforeEach
    public void initTest() {
        suggestedProducts = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSuggestedProducts != null) {
            suggestedProductsRepository.delete(insertedSuggestedProducts);
            suggestedProductsSearchRepository.delete(insertedSuggestedProducts);
            insertedSuggestedProducts = null;
        }
    }

    @Test
    @Transactional
    void createSuggestedProducts() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        // Create the SuggestedProducts
        var returnedSuggestedProducts = om.readValue(
            restSuggestedProductsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suggestedProducts)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SuggestedProducts.class
        );

        // Validate the SuggestedProducts in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSuggestedProductsUpdatableFieldsEquals(returnedSuggestedProducts, getPersistedSuggestedProducts(returnedSuggestedProducts));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSuggestedProducts = returnedSuggestedProducts;
    }

    @Test
    @Transactional
    void createSuggestedProductsWithExistingId() throws Exception {
        // Create the SuggestedProducts with an existing ID
        suggestedProducts.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuggestedProductsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suggestedProducts)))
            .andExpect(status().isBadRequest());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSuggestedProducts() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);

        // Get all the suggestedProductsList
        restSuggestedProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestedProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isApprove").value(hasItem(DEFAULT_IS_APPROVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].suggestedByDistrict").value(hasItem(DEFAULT_SUGGESTED_BY_DISTRICT)))
            .andExpect(jsonPath("$.[*].suggestedByUserId").value(hasItem(DEFAULT_SUGGESTED_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].suggestedProductId").value(hasItem(DEFAULT_SUGGESTED_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].suggestionDate").value(hasItem(DEFAULT_SUGGESTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].suggestionId").value(hasItem(DEFAULT_SUGGESTION_ID)));
    }

    @Test
    @Transactional
    void getSuggestedProducts() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);

        // Get the suggestedProducts
        restSuggestedProductsMockMvc
            .perform(get(ENTITY_API_URL_ID, suggestedProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(suggestedProducts.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isApprove").value(DEFAULT_IS_APPROVE.booleanValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.suggestedByDistrict").value(DEFAULT_SUGGESTED_BY_DISTRICT))
            .andExpect(jsonPath("$.suggestedByUserId").value(DEFAULT_SUGGESTED_BY_USER_ID))
            .andExpect(jsonPath("$.suggestedProductId").value(DEFAULT_SUGGESTED_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.suggestionDate").value(DEFAULT_SUGGESTION_DATE.toString()))
            .andExpect(jsonPath("$.suggestionId").value(DEFAULT_SUGGESTION_ID));
    }

    @Test
    @Transactional
    void getNonExistingSuggestedProducts() throws Exception {
        // Get the suggestedProducts
        restSuggestedProductsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSuggestedProducts() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        suggestedProductsSearchRepository.save(suggestedProducts);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());

        // Update the suggestedProducts
        SuggestedProducts updatedSuggestedProducts = suggestedProductsRepository.findById(suggestedProducts.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSuggestedProducts are not directly saved in db
        em.detach(updatedSuggestedProducts);
        updatedSuggestedProducts
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedByDistrict(UPDATED_SUGGESTED_BY_DISTRICT)
            .suggestedByUserId(UPDATED_SUGGESTED_BY_USER_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .suggestionDate(UPDATED_SUGGESTION_DATE)
            .suggestionId(UPDATED_SUGGESTION_ID);

        restSuggestedProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuggestedProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSuggestedProducts))
            )
            .andExpect(status().isOk());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSuggestedProductsToMatchAllProperties(updatedSuggestedProducts);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SuggestedProducts> suggestedProductsSearchList = Streamable.of(suggestedProductsSearchRepository.findAll()).toList();
                SuggestedProducts testSuggestedProductsSearch = suggestedProductsSearchList.get(searchDatabaseSizeAfter - 1);

                assertSuggestedProductsAllPropertiesEquals(testSuggestedProductsSearch, updatedSuggestedProducts);
            });
    }

    @Test
    @Transactional
    void putNonExistingSuggestedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        suggestedProducts.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuggestedProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suggestedProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(suggestedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuggestedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        suggestedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuggestedProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(suggestedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuggestedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        suggestedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuggestedProductsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suggestedProducts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSuggestedProductsWithPatch() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suggestedProducts using partial update
        SuggestedProducts partialUpdatedSuggestedProducts = new SuggestedProducts();
        partialUpdatedSuggestedProducts.setId(suggestedProducts.getId());

        partialUpdatedSuggestedProducts
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .suggestionDate(UPDATED_SUGGESTION_DATE)
            .suggestionId(UPDATED_SUGGESTION_ID);

        restSuggestedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuggestedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuggestedProducts))
            )
            .andExpect(status().isOk());

        // Validate the SuggestedProducts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuggestedProductsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSuggestedProducts, suggestedProducts),
            getPersistedSuggestedProducts(suggestedProducts)
        );
    }

    @Test
    @Transactional
    void fullUpdateSuggestedProductsWithPatch() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suggestedProducts using partial update
        SuggestedProducts partialUpdatedSuggestedProducts = new SuggestedProducts();
        partialUpdatedSuggestedProducts.setId(suggestedProducts.getId());

        partialUpdatedSuggestedProducts
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedByDistrict(UPDATED_SUGGESTED_BY_DISTRICT)
            .suggestedByUserId(UPDATED_SUGGESTED_BY_USER_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .suggestionDate(UPDATED_SUGGESTION_DATE)
            .suggestionId(UPDATED_SUGGESTION_ID);

        restSuggestedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuggestedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuggestedProducts))
            )
            .andExpect(status().isOk());

        // Validate the SuggestedProducts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuggestedProductsUpdatableFieldsEquals(
            partialUpdatedSuggestedProducts,
            getPersistedSuggestedProducts(partialUpdatedSuggestedProducts)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSuggestedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        suggestedProducts.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuggestedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, suggestedProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(suggestedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuggestedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        suggestedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuggestedProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(suggestedProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuggestedProducts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        suggestedProducts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuggestedProductsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(suggestedProducts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuggestedProducts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSuggestedProducts() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);
        suggestedProductsRepository.save(suggestedProducts);
        suggestedProductsSearchRepository.save(suggestedProducts);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the suggestedProducts
        restSuggestedProductsMockMvc
            .perform(delete(ENTITY_API_URL_ID, suggestedProducts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(suggestedProductsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSuggestedProducts() throws Exception {
        // Initialize the database
        insertedSuggestedProducts = suggestedProductsRepository.saveAndFlush(suggestedProducts);
        suggestedProductsSearchRepository.save(suggestedProducts);

        // Search the suggestedProducts
        restSuggestedProductsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + suggestedProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestedProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isApprove").value(hasItem(DEFAULT_IS_APPROVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].suggestedByDistrict").value(hasItem(DEFAULT_SUGGESTED_BY_DISTRICT)))
            .andExpect(jsonPath("$.[*].suggestedByUserId").value(hasItem(DEFAULT_SUGGESTED_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].suggestedProductId").value(hasItem(DEFAULT_SUGGESTED_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].suggestionDate").value(hasItem(DEFAULT_SUGGESTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].suggestionId").value(hasItem(DEFAULT_SUGGESTION_ID)));
    }

    protected long getRepositoryCount() {
        return suggestedProductsRepository.count();
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

    protected SuggestedProducts getPersistedSuggestedProducts(SuggestedProducts suggestedProducts) {
        return suggestedProductsRepository.findById(suggestedProducts.getId()).orElseThrow();
    }

    protected void assertPersistedSuggestedProductsToMatchAllProperties(SuggestedProducts expectedSuggestedProducts) {
        assertSuggestedProductsAllPropertiesEquals(expectedSuggestedProducts, getPersistedSuggestedProducts(expectedSuggestedProducts));
    }

    protected void assertPersistedSuggestedProductsToMatchUpdatableProperties(SuggestedProducts expectedSuggestedProducts) {
        assertSuggestedProductsAllUpdatablePropertiesEquals(
            expectedSuggestedProducts,
            getPersistedSuggestedProducts(expectedSuggestedProducts)
        );
    }
}
