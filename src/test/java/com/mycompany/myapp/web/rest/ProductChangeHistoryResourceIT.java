package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductChangeHistoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductChangeHistory;
import com.mycompany.myapp.repository.ProductChangeHistoryRepository;
import com.mycompany.myapp.repository.search.ProductChangeHistorySearchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link ProductChangeHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductChangeHistoryResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final UUID DEFAULT_HISTORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_HISTORY_ID = UUID.randomUUID();

    private static final Integer DEFAULT_IOC_CATEGORY_ID = 1;
    private static final Integer UPDATED_IOC_CATEGORY_ID = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final String DEFAULT_SELECTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SELECTION_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-change-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-change-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductChangeHistoryRepository productChangeHistoryRepository;

    @Autowired
    private ProductChangeHistorySearchRepository productChangeHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductChangeHistoryMockMvc;

    private ProductChangeHistory productChangeHistory;

    private ProductChangeHistory insertedProductChangeHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductChangeHistory createEntity() {
        return new ProductChangeHistory()
            .createdBy(DEFAULT_CREATED_BY)
            .dateCreated(DEFAULT_DATE_CREATED)
            .historyId(DEFAULT_HISTORY_ID)
            .iocCategoryId(DEFAULT_IOC_CATEGORY_ID)
            .isActive(DEFAULT_IS_ACTIVE)
            .productId(DEFAULT_PRODUCT_ID)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .selectionType(DEFAULT_SELECTION_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductChangeHistory createUpdatedEntity() {
        return new ProductChangeHistory()
            .createdBy(UPDATED_CREATED_BY)
            .dateCreated(UPDATED_DATE_CREATED)
            .historyId(UPDATED_HISTORY_ID)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .selectionType(UPDATED_SELECTION_TYPE);
    }

    @BeforeEach
    public void initTest() {
        productChangeHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductChangeHistory != null) {
            productChangeHistoryRepository.delete(insertedProductChangeHistory);
            productChangeHistorySearchRepository.delete(insertedProductChangeHistory);
            insertedProductChangeHistory = null;
        }
    }

    @Test
    @Transactional
    void createProductChangeHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        // Create the ProductChangeHistory
        var returnedProductChangeHistory = om.readValue(
            restProductChangeHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productChangeHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductChangeHistory.class
        );

        // Validate the ProductChangeHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductChangeHistoryUpdatableFieldsEquals(
            returnedProductChangeHistory,
            getPersistedProductChangeHistory(returnedProductChangeHistory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductChangeHistory = returnedProductChangeHistory;
    }

    @Test
    @Transactional
    void createProductChangeHistoryWithExistingId() throws Exception {
        // Create the ProductChangeHistory with an existing ID
        productChangeHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductChangeHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productChangeHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductChangeHistories() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);

        // Get all the productChangeHistoryList
        restProductChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productChangeHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].historyId").value(hasItem(DEFAULT_HISTORY_ID.toString())))
            .andExpect(jsonPath("$.[*].iocCategoryId").value(hasItem(DEFAULT_IOC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].selectionType").value(hasItem(DEFAULT_SELECTION_TYPE)));
    }

    @Test
    @Transactional
    void getProductChangeHistory() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);

        // Get the productChangeHistory
        restProductChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productChangeHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productChangeHistory.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.historyId").value(DEFAULT_HISTORY_ID.toString()))
            .andExpect(jsonPath("$.iocCategoryId").value(DEFAULT_IOC_CATEGORY_ID))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.selectionType").value(DEFAULT_SELECTION_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingProductChangeHistory() throws Exception {
        // Get the productChangeHistory
        restProductChangeHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductChangeHistory() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productChangeHistorySearchRepository.save(productChangeHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());

        // Update the productChangeHistory
        ProductChangeHistory updatedProductChangeHistory = productChangeHistoryRepository
            .findById(productChangeHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductChangeHistory are not directly saved in db
        em.detach(updatedProductChangeHistory);
        updatedProductChangeHistory
            .createdBy(UPDATED_CREATED_BY)
            .dateCreated(UPDATED_DATE_CREATED)
            .historyId(UPDATED_HISTORY_ID)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .selectionType(UPDATED_SELECTION_TYPE);

        restProductChangeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductChangeHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductChangeHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductChangeHistoryToMatchAllProperties(updatedProductChangeHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductChangeHistory> productChangeHistorySearchList = Streamable.of(
                    productChangeHistorySearchRepository.findAll()
                ).toList();
                ProductChangeHistory testProductChangeHistorySearch = productChangeHistorySearchList.get(searchDatabaseSizeAfter - 1);

                assertProductChangeHistoryAllPropertiesEquals(testProductChangeHistorySearch, updatedProductChangeHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductChangeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        productChangeHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductChangeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productChangeHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productChangeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductChangeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        productChangeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductChangeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productChangeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductChangeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        productChangeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductChangeHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productChangeHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductChangeHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productChangeHistory using partial update
        ProductChangeHistory partialUpdatedProductChangeHistory = new ProductChangeHistory();
        partialUpdatedProductChangeHistory.setId(productChangeHistory.getId());

        partialUpdatedProductChangeHistory.dateCreated(UPDATED_DATE_CREATED).isActive(UPDATED_IS_ACTIVE);

        restProductChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductChangeHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductChangeHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductChangeHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductChangeHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductChangeHistory, productChangeHistory),
            getPersistedProductChangeHistory(productChangeHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductChangeHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productChangeHistory using partial update
        ProductChangeHistory partialUpdatedProductChangeHistory = new ProductChangeHistory();
        partialUpdatedProductChangeHistory.setId(productChangeHistory.getId());

        partialUpdatedProductChangeHistory
            .createdBy(UPDATED_CREATED_BY)
            .dateCreated(UPDATED_DATE_CREATED)
            .historyId(UPDATED_HISTORY_ID)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .selectionType(UPDATED_SELECTION_TYPE);

        restProductChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductChangeHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductChangeHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductChangeHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductChangeHistoryUpdatableFieldsEquals(
            partialUpdatedProductChangeHistory,
            getPersistedProductChangeHistory(partialUpdatedProductChangeHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductChangeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        productChangeHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productChangeHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productChangeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductChangeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        productChangeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productChangeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductChangeHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        productChangeHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductChangeHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productChangeHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductChangeHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductChangeHistory() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);
        productChangeHistoryRepository.save(productChangeHistory);
        productChangeHistorySearchRepository.save(productChangeHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productChangeHistory
        restProductChangeHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productChangeHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productChangeHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductChangeHistory() throws Exception {
        // Initialize the database
        insertedProductChangeHistory = productChangeHistoryRepository.saveAndFlush(productChangeHistory);
        productChangeHistorySearchRepository.save(productChangeHistory);

        // Search the productChangeHistory
        restProductChangeHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productChangeHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productChangeHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].historyId").value(hasItem(DEFAULT_HISTORY_ID.toString())))
            .andExpect(jsonPath("$.[*].iocCategoryId").value(hasItem(DEFAULT_IOC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].selectionType").value(hasItem(DEFAULT_SELECTION_TYPE)));
    }

    protected long getRepositoryCount() {
        return productChangeHistoryRepository.count();
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

    protected ProductChangeHistory getPersistedProductChangeHistory(ProductChangeHistory productChangeHistory) {
        return productChangeHistoryRepository.findById(productChangeHistory.getId()).orElseThrow();
    }

    protected void assertPersistedProductChangeHistoryToMatchAllProperties(ProductChangeHistory expectedProductChangeHistory) {
        assertProductChangeHistoryAllPropertiesEquals(
            expectedProductChangeHistory,
            getPersistedProductChangeHistory(expectedProductChangeHistory)
        );
    }

    protected void assertPersistedProductChangeHistoryToMatchUpdatableProperties(ProductChangeHistory expectedProductChangeHistory) {
        assertProductChangeHistoryAllUpdatablePropertiesEquals(
            expectedProductChangeHistory,
            getPersistedProductChangeHistory(expectedProductChangeHistory)
        );
    }
}
