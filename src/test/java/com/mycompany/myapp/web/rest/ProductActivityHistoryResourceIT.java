package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductActivityHistoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductActivityHistory;
import com.mycompany.myapp.repository.ProductActivityHistoryRepository;
import com.mycompany.myapp.repository.search.ProductActivityHistorySearchRepository;
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
 * Integration tests for the {@link ProductActivityHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductActivityHistoryResourceIT {

    private static final Integer DEFAULT_ACTIVITY_ID = 1;
    private static final Integer UPDATED_ACTIVITY_ID = 2;

    private static final String DEFAULT_ACTIVITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_SUGGESTED_PRODUCT_ID = 1;
    private static final Integer UPDATED_SUGGESTED_PRODUCT_ID = 2;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/product-activity-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-activity-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductActivityHistoryRepository productActivityHistoryRepository;

    @Autowired
    private ProductActivityHistorySearchRepository productActivityHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductActivityHistoryMockMvc;

    private ProductActivityHistory productActivityHistory;

    private ProductActivityHistory insertedProductActivityHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductActivityHistory createEntity() {
        return new ProductActivityHistory()
            .activityId(DEFAULT_ACTIVITY_ID)
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .date(DEFAULT_DATE)
            .isActive(DEFAULT_IS_ACTIVE)
            .productId(DEFAULT_PRODUCT_ID)
            .suggestedProductId(DEFAULT_SUGGESTED_PRODUCT_ID)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductActivityHistory createUpdatedEntity() {
        return new ProductActivityHistory()
            .activityId(UPDATED_ACTIVITY_ID)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .date(UPDATED_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        productActivityHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductActivityHistory != null) {
            productActivityHistoryRepository.delete(insertedProductActivityHistory);
            productActivityHistorySearchRepository.delete(insertedProductActivityHistory);
            insertedProductActivityHistory = null;
        }
    }

    @Test
    @Transactional
    void createProductActivityHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        // Create the ProductActivityHistory
        var returnedProductActivityHistory = om.readValue(
            restProductActivityHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productActivityHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductActivityHistory.class
        );

        // Validate the ProductActivityHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductActivityHistoryUpdatableFieldsEquals(
            returnedProductActivityHistory,
            getPersistedProductActivityHistory(returnedProductActivityHistory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductActivityHistory = returnedProductActivityHistory;
    }

    @Test
    @Transactional
    void createProductActivityHistoryWithExistingId() throws Exception {
        // Create the ProductActivityHistory with an existing ID
        productActivityHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductActivityHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productActivityHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductActivityHistories() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);

        // Get all the productActivityHistoryList
        restProductActivityHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productActivityHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityId").value(hasItem(DEFAULT_ACTIVITY_ID)))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].suggestedProductId").value(hasItem(DEFAULT_SUGGESTED_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getProductActivityHistory() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);

        // Get the productActivityHistory
        restProductActivityHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productActivityHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productActivityHistory.getId().intValue()))
            .andExpect(jsonPath("$.activityId").value(DEFAULT_ACTIVITY_ID))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.suggestedProductId").value(DEFAULT_SUGGESTED_PRODUCT_ID))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductActivityHistory() throws Exception {
        // Get the productActivityHistory
        restProductActivityHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductActivityHistory() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productActivityHistorySearchRepository.save(productActivityHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());

        // Update the productActivityHistory
        ProductActivityHistory updatedProductActivityHistory = productActivityHistoryRepository
            .findById(productActivityHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductActivityHistory are not directly saved in db
        em.detach(updatedProductActivityHistory);
        updatedProductActivityHistory
            .activityId(UPDATED_ACTIVITY_ID)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .date(UPDATED_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductActivityHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductActivityHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductActivityHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductActivityHistoryToMatchAllProperties(updatedProductActivityHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductActivityHistory> productActivityHistorySearchList = Streamable.of(
                    productActivityHistorySearchRepository.findAll()
                ).toList();
                ProductActivityHistory testProductActivityHistorySearch = productActivityHistorySearchList.get(searchDatabaseSizeAfter - 1);

                assertProductActivityHistoryAllPropertiesEquals(testProductActivityHistorySearch, updatedProductActivityHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductActivityHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        productActivityHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductActivityHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productActivityHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productActivityHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductActivityHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        productActivityHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductActivityHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productActivityHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductActivityHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        productActivityHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductActivityHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productActivityHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductActivityHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productActivityHistory using partial update
        ProductActivityHistory partialUpdatedProductActivityHistory = new ProductActivityHistory();
        partialUpdatedProductActivityHistory.setId(productActivityHistory.getId());

        partialUpdatedProductActivityHistory
            .activityId(UPDATED_ACTIVITY_ID)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductActivityHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductActivityHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductActivityHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductActivityHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductActivityHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductActivityHistory, productActivityHistory),
            getPersistedProductActivityHistory(productActivityHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductActivityHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productActivityHistory using partial update
        ProductActivityHistory partialUpdatedProductActivityHistory = new ProductActivityHistory();
        partialUpdatedProductActivityHistory.setId(productActivityHistory.getId());

        partialUpdatedProductActivityHistory
            .activityId(UPDATED_ACTIVITY_ID)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .date(UPDATED_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .suggestedProductId(UPDATED_SUGGESTED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductActivityHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductActivityHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductActivityHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductActivityHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductActivityHistoryUpdatableFieldsEquals(
            partialUpdatedProductActivityHistory,
            getPersistedProductActivityHistory(partialUpdatedProductActivityHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductActivityHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        productActivityHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductActivityHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productActivityHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productActivityHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductActivityHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        productActivityHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductActivityHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productActivityHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductActivityHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        productActivityHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductActivityHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productActivityHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductActivityHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductActivityHistory() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);
        productActivityHistoryRepository.save(productActivityHistory);
        productActivityHistorySearchRepository.save(productActivityHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productActivityHistory
        restProductActivityHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productActivityHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productActivityHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductActivityHistory() throws Exception {
        // Initialize the database
        insertedProductActivityHistory = productActivityHistoryRepository.saveAndFlush(productActivityHistory);
        productActivityHistorySearchRepository.save(productActivityHistory);

        // Search the productActivityHistory
        restProductActivityHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productActivityHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productActivityHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityId").value(hasItem(DEFAULT_ACTIVITY_ID)))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].suggestedProductId").value(hasItem(DEFAULT_SUGGESTED_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return productActivityHistoryRepository.count();
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

    protected ProductActivityHistory getPersistedProductActivityHistory(ProductActivityHistory productActivityHistory) {
        return productActivityHistoryRepository.findById(productActivityHistory.getId()).orElseThrow();
    }

    protected void assertPersistedProductActivityHistoryToMatchAllProperties(ProductActivityHistory expectedProductActivityHistory) {
        assertProductActivityHistoryAllPropertiesEquals(
            expectedProductActivityHistory,
            getPersistedProductActivityHistory(expectedProductActivityHistory)
        );
    }

    protected void assertPersistedProductActivityHistoryToMatchUpdatableProperties(ProductActivityHistory expectedProductActivityHistory) {
        assertProductActivityHistoryAllUpdatablePropertiesEquals(
            expectedProductActivityHistory,
            getPersistedProductActivityHistory(expectedProductActivityHistory)
        );
    }
}
