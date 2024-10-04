package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductDistributorAllocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductDistributorAllocation;
import com.mycompany.myapp.repository.ProductDistributorAllocationRepository;
import com.mycompany.myapp.repository.search.ProductDistributorAllocationSearchRepository;
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
 * Integration tests for the {@link ProductDistributorAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductDistributorAllocationResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DISTRIBUTOR_ID = 1;
    private static final Integer UPDATED_DISTRIBUTOR_ID = 2;

    private static final Boolean DEFAULT_IS_ALLOCATED = false;
    private static final Boolean UPDATED_IS_ALLOCATED = true;

    private static final UUID DEFAULT_PRODUCT_DISTRIBUTOR_ALLOCATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_PRODUCT_DISTRIBUTOR_ALLOCATION_ID = UUID.randomUUID();

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/product-distributor-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-distributor-allocations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductDistributorAllocationRepository productDistributorAllocationRepository;

    @Autowired
    private ProductDistributorAllocationSearchRepository productDistributorAllocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductDistributorAllocationMockMvc;

    private ProductDistributorAllocation productDistributorAllocation;

    private ProductDistributorAllocation insertedProductDistributorAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDistributorAllocation createEntity() {
        return new ProductDistributorAllocation()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .distributorId(DEFAULT_DISTRIBUTOR_ID)
            .isAllocated(DEFAULT_IS_ALLOCATED)
            .productDistributorAllocationId(DEFAULT_PRODUCT_DISTRIBUTOR_ALLOCATION_ID)
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
    public static ProductDistributorAllocation createUpdatedEntity() {
        return new ProductDistributorAllocation()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistributorAllocationId(UPDATED_PRODUCT_DISTRIBUTOR_ALLOCATION_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        productDistributorAllocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductDistributorAllocation != null) {
            productDistributorAllocationRepository.delete(insertedProductDistributorAllocation);
            productDistributorAllocationSearchRepository.delete(insertedProductDistributorAllocation);
            insertedProductDistributorAllocation = null;
        }
    }

    @Test
    @Transactional
    void createProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        // Create the ProductDistributorAllocation
        var returnedProductDistributorAllocation = om.readValue(
            restProductDistributorAllocationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDistributorAllocation))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDistributorAllocation.class
        );

        // Validate the ProductDistributorAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductDistributorAllocationUpdatableFieldsEquals(
            returnedProductDistributorAllocation,
            getPersistedProductDistributorAllocation(returnedProductDistributorAllocation)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductDistributorAllocation = returnedProductDistributorAllocation;
    }

    @Test
    @Transactional
    void createProductDistributorAllocationWithExistingId() throws Exception {
        // Create the ProductDistributorAllocation with an existing ID
        productDistributorAllocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductDistributorAllocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductDistributorAllocations() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);

        // Get all the productDistributorAllocationList
        restProductDistributorAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDistributorAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].distributorId").value(hasItem(DEFAULT_DISTRIBUTOR_ID)))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(
                jsonPath("$.[*].productDistributorAllocationId").value(hasItem(DEFAULT_PRODUCT_DISTRIBUTOR_ALLOCATION_ID.toString()))
            )
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getProductDistributorAllocation() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);

        // Get the productDistributorAllocation
        restProductDistributorAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, productDistributorAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productDistributorAllocation.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.distributorId").value(DEFAULT_DISTRIBUTOR_ID))
            .andExpect(jsonPath("$.isAllocated").value(DEFAULT_IS_ALLOCATED.booleanValue()))
            .andExpect(jsonPath("$.productDistributorAllocationId").value(DEFAULT_PRODUCT_DISTRIBUTOR_ALLOCATION_ID.toString()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductDistributorAllocation() throws Exception {
        // Get the productDistributorAllocation
        restProductDistributorAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductDistributorAllocation() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDistributorAllocationSearchRepository.save(productDistributorAllocation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());

        // Update the productDistributorAllocation
        ProductDistributorAllocation updatedProductDistributorAllocation = productDistributorAllocationRepository
            .findById(productDistributorAllocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductDistributorAllocation are not directly saved in db
        em.detach(updatedProductDistributorAllocation);
        updatedProductDistributorAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistributorAllocationId(UPDATED_PRODUCT_DISTRIBUTOR_ALLOCATION_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductDistributorAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductDistributorAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductDistributorAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductDistributorAllocationToMatchAllProperties(updatedProductDistributorAllocation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductDistributorAllocation> productDistributorAllocationSearchList = Streamable.of(
                    productDistributorAllocationSearchRepository.findAll()
                ).toList();
                ProductDistributorAllocation testProductDistributorAllocationSearch = productDistributorAllocationSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertProductDistributorAllocationAllPropertiesEquals(
                    testProductDistributorAllocationSearch,
                    updatedProductDistributorAllocation
                );
            });
    }

    @Test
    @Transactional
    void putNonExistingProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        productDistributorAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDistributorAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDistributorAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        productDistributorAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistributorAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        productDistributorAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistributorAllocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductDistributorAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDistributorAllocation using partial update
        ProductDistributorAllocation partialUpdatedProductDistributorAllocation = new ProductDistributorAllocation();
        partialUpdatedProductDistributorAllocation.setId(productDistributorAllocation.getId());

        partialUpdatedProductDistributorAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistributorAllocationId(UPDATED_PRODUCT_DISTRIBUTOR_ALLOCATION_ID)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductDistributorAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDistributorAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductDistributorAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductDistributorAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductDistributorAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductDistributorAllocation, productDistributorAllocation),
            getPersistedProductDistributorAllocation(productDistributorAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductDistributorAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDistributorAllocation using partial update
        ProductDistributorAllocation partialUpdatedProductDistributorAllocation = new ProductDistributorAllocation();
        partialUpdatedProductDistributorAllocation.setId(productDistributorAllocation.getId());

        partialUpdatedProductDistributorAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistributorAllocationId(UPDATED_PRODUCT_DISTRIBUTOR_ALLOCATION_ID)
            .productId(UPDATED_PRODUCT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductDistributorAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDistributorAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductDistributorAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductDistributorAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductDistributorAllocationUpdatableFieldsEquals(
            partialUpdatedProductDistributorAllocation,
            getPersistedProductDistributorAllocation(partialUpdatedProductDistributorAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        productDistributorAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDistributorAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDistributorAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        productDistributorAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistributorAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductDistributorAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        productDistributorAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistributorAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDistributorAllocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDistributorAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductDistributorAllocation() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);
        productDistributorAllocationRepository.save(productDistributorAllocation);
        productDistributorAllocationSearchRepository.save(productDistributorAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productDistributorAllocation
        restProductDistributorAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productDistributorAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistributorAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductDistributorAllocation() throws Exception {
        // Initialize the database
        insertedProductDistributorAllocation = productDistributorAllocationRepository.saveAndFlush(productDistributorAllocation);
        productDistributorAllocationSearchRepository.save(productDistributorAllocation);

        // Search the productDistributorAllocation
        restProductDistributorAllocationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productDistributorAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDistributorAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].distributorId").value(hasItem(DEFAULT_DISTRIBUTOR_ID)))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(
                jsonPath("$.[*].productDistributorAllocationId").value(hasItem(DEFAULT_PRODUCT_DISTRIBUTOR_ALLOCATION_ID.toString()))
            )
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return productDistributorAllocationRepository.count();
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

    protected ProductDistributorAllocation getPersistedProductDistributorAllocation(
        ProductDistributorAllocation productDistributorAllocation
    ) {
        return productDistributorAllocationRepository.findById(productDistributorAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedProductDistributorAllocationToMatchAllProperties(
        ProductDistributorAllocation expectedProductDistributorAllocation
    ) {
        assertProductDistributorAllocationAllPropertiesEquals(
            expectedProductDistributorAllocation,
            getPersistedProductDistributorAllocation(expectedProductDistributorAllocation)
        );
    }

    protected void assertPersistedProductDistributorAllocationToMatchUpdatableProperties(
        ProductDistributorAllocation expectedProductDistributorAllocation
    ) {
        assertProductDistributorAllocationAllUpdatablePropertiesEquals(
            expectedProductDistributorAllocation,
            getPersistedProductDistributorAllocation(expectedProductDistributorAllocation)
        );
    }
}
