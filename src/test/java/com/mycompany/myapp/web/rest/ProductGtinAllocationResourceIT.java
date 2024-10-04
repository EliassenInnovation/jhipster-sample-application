package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductGtinAllocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductGtinAllocation;
import com.mycompany.myapp.repository.ProductGtinAllocationRepository;
import com.mycompany.myapp.repository.search.ProductGtinAllocationSearchRepository;
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
 * Integration tests for the {@link ProductGtinAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductGtinAllocationResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_G_TIN = "AAAAAAAAAA";
    private static final String UPDATED_G_TIN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_PRODUCT_GTIN_ID = 1;
    private static final Integer UPDATED_PRODUCT_GTIN_ID = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/product-gtin-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-gtin-allocations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductGtinAllocationRepository productGtinAllocationRepository;

    @Autowired
    private ProductGtinAllocationSearchRepository productGtinAllocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductGtinAllocationMockMvc;

    private ProductGtinAllocation productGtinAllocation;

    private ProductGtinAllocation insertedProductGtinAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductGtinAllocation createEntity() {
        return new ProductGtinAllocation()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .gTIN(DEFAULT_G_TIN)
            .isActive(DEFAULT_IS_ACTIVE)
            .productGtinId(DEFAULT_PRODUCT_GTIN_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductGtinAllocation createUpdatedEntity() {
        return new ProductGtinAllocation()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .gTIN(UPDATED_G_TIN)
            .isActive(UPDATED_IS_ACTIVE)
            .productGtinId(UPDATED_PRODUCT_GTIN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        productGtinAllocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductGtinAllocation != null) {
            productGtinAllocationRepository.delete(insertedProductGtinAllocation);
            productGtinAllocationSearchRepository.delete(insertedProductGtinAllocation);
            insertedProductGtinAllocation = null;
        }
    }

    @Test
    @Transactional
    void createProductGtinAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        // Create the ProductGtinAllocation
        var returnedProductGtinAllocation = om.readValue(
            restProductGtinAllocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGtinAllocation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductGtinAllocation.class
        );

        // Validate the ProductGtinAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductGtinAllocationUpdatableFieldsEquals(
            returnedProductGtinAllocation,
            getPersistedProductGtinAllocation(returnedProductGtinAllocation)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductGtinAllocation = returnedProductGtinAllocation;
    }

    @Test
    @Transactional
    void createProductGtinAllocationWithExistingId() throws Exception {
        // Create the ProductGtinAllocation with an existing ID
        productGtinAllocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductGtinAllocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGtinAllocation)))
            .andExpect(status().isBadRequest());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductGtinAllocations() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);

        // Get all the productGtinAllocationList
        restProductGtinAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productGtinAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productGtinId").value(hasItem(DEFAULT_PRODUCT_GTIN_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getProductGtinAllocation() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);

        // Get the productGtinAllocation
        restProductGtinAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, productGtinAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productGtinAllocation.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.gTIN").value(DEFAULT_G_TIN))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.productGtinId").value(DEFAULT_PRODUCT_GTIN_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductGtinAllocation() throws Exception {
        // Get the productGtinAllocation
        restProductGtinAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductGtinAllocation() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productGtinAllocationSearchRepository.save(productGtinAllocation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());

        // Update the productGtinAllocation
        ProductGtinAllocation updatedProductGtinAllocation = productGtinAllocationRepository
            .findById(productGtinAllocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductGtinAllocation are not directly saved in db
        em.detach(updatedProductGtinAllocation);
        updatedProductGtinAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .gTIN(UPDATED_G_TIN)
            .isActive(UPDATED_IS_ACTIVE)
            .productGtinId(UPDATED_PRODUCT_GTIN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductGtinAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductGtinAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductGtinAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductGtinAllocationToMatchAllProperties(updatedProductGtinAllocation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductGtinAllocation> productGtinAllocationSearchList = Streamable.of(
                    productGtinAllocationSearchRepository.findAll()
                ).toList();
                ProductGtinAllocation testProductGtinAllocationSearch = productGtinAllocationSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductGtinAllocationAllPropertiesEquals(testProductGtinAllocationSearch, updatedProductGtinAllocation);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductGtinAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        productGtinAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductGtinAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productGtinAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productGtinAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductGtinAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        productGtinAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGtinAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productGtinAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductGtinAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        productGtinAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGtinAllocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productGtinAllocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductGtinAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productGtinAllocation using partial update
        ProductGtinAllocation partialUpdatedProductGtinAllocation = new ProductGtinAllocation();
        partialUpdatedProductGtinAllocation.setId(productGtinAllocation.getId());

        partialUpdatedProductGtinAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .gTIN(UPDATED_G_TIN)
            .isActive(UPDATED_IS_ACTIVE)
            .productGtinId(UPDATED_PRODUCT_GTIN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductGtinAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductGtinAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductGtinAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductGtinAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductGtinAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductGtinAllocation, productGtinAllocation),
            getPersistedProductGtinAllocation(productGtinAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductGtinAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productGtinAllocation using partial update
        ProductGtinAllocation partialUpdatedProductGtinAllocation = new ProductGtinAllocation();
        partialUpdatedProductGtinAllocation.setId(productGtinAllocation.getId());

        partialUpdatedProductGtinAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .gTIN(UPDATED_G_TIN)
            .isActive(UPDATED_IS_ACTIVE)
            .productGtinId(UPDATED_PRODUCT_GTIN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductGtinAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductGtinAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductGtinAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductGtinAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductGtinAllocationUpdatableFieldsEquals(
            partialUpdatedProductGtinAllocation,
            getPersistedProductGtinAllocation(partialUpdatedProductGtinAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductGtinAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        productGtinAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductGtinAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productGtinAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productGtinAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductGtinAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        productGtinAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGtinAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productGtinAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductGtinAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        productGtinAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductGtinAllocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productGtinAllocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductGtinAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductGtinAllocation() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);
        productGtinAllocationRepository.save(productGtinAllocation);
        productGtinAllocationSearchRepository.save(productGtinAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productGtinAllocation
        restProductGtinAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productGtinAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productGtinAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductGtinAllocation() throws Exception {
        // Initialize the database
        insertedProductGtinAllocation = productGtinAllocationRepository.saveAndFlush(productGtinAllocation);
        productGtinAllocationSearchRepository.save(productGtinAllocation);

        // Search the productGtinAllocation
        restProductGtinAllocationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productGtinAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productGtinAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productGtinId").value(hasItem(DEFAULT_PRODUCT_GTIN_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return productGtinAllocationRepository.count();
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

    protected ProductGtinAllocation getPersistedProductGtinAllocation(ProductGtinAllocation productGtinAllocation) {
        return productGtinAllocationRepository.findById(productGtinAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedProductGtinAllocationToMatchAllProperties(ProductGtinAllocation expectedProductGtinAllocation) {
        assertProductGtinAllocationAllPropertiesEquals(
            expectedProductGtinAllocation,
            getPersistedProductGtinAllocation(expectedProductGtinAllocation)
        );
    }

    protected void assertPersistedProductGtinAllocationToMatchUpdatableProperties(ProductGtinAllocation expectedProductGtinAllocation) {
        assertProductGtinAllocationAllUpdatablePropertiesEquals(
            expectedProductGtinAllocation,
            getPersistedProductGtinAllocation(expectedProductGtinAllocation)
        );
    }
}
