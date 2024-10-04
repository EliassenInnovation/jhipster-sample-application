package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductManufacturerAllocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductManufacturerAllocation;
import com.mycompany.myapp.repository.ProductManufacturerAllocationRepository;
import com.mycompany.myapp.repository.search.ProductManufacturerAllocationSearchRepository;
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
 * Integration tests for the {@link ProductManufacturerAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductManufacturerAllocationResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ALLOCATED = false;
    private static final Boolean UPDATED_IS_ALLOCATED = true;

    private static final Integer DEFAULT_MANUFACTURE_ID = 1;
    private static final Integer UPDATED_MANUFACTURE_ID = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final UUID DEFAULT_PRODUCT_MANUFACTURER_ALLOCATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_PRODUCT_MANUFACTURER_ALLOCATION_ID = UUID.randomUUID();

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/product-manufacturer-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-manufacturer-allocations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductManufacturerAllocationRepository productManufacturerAllocationRepository;

    @Autowired
    private ProductManufacturerAllocationSearchRepository productManufacturerAllocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductManufacturerAllocationMockMvc;

    private ProductManufacturerAllocation productManufacturerAllocation;

    private ProductManufacturerAllocation insertedProductManufacturerAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductManufacturerAllocation createEntity() {
        return new ProductManufacturerAllocation()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isAllocated(DEFAULT_IS_ALLOCATED)
            .manufactureId(DEFAULT_MANUFACTURE_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .productManufacturerAllocationId(DEFAULT_PRODUCT_MANUFACTURER_ALLOCATION_ID)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductManufacturerAllocation createUpdatedEntity() {
        return new ProductManufacturerAllocation()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .manufactureId(UPDATED_MANUFACTURE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productManufacturerAllocationId(UPDATED_PRODUCT_MANUFACTURER_ALLOCATION_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        productManufacturerAllocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductManufacturerAllocation != null) {
            productManufacturerAllocationRepository.delete(insertedProductManufacturerAllocation);
            productManufacturerAllocationSearchRepository.delete(insertedProductManufacturerAllocation);
            insertedProductManufacturerAllocation = null;
        }
    }

    @Test
    @Transactional
    void createProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        // Create the ProductManufacturerAllocation
        var returnedProductManufacturerAllocation = om.readValue(
            restProductManufacturerAllocationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(productManufacturerAllocation))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductManufacturerAllocation.class
        );

        // Validate the ProductManufacturerAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductManufacturerAllocationUpdatableFieldsEquals(
            returnedProductManufacturerAllocation,
            getPersistedProductManufacturerAllocation(returnedProductManufacturerAllocation)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductManufacturerAllocation = returnedProductManufacturerAllocation;
    }

    @Test
    @Transactional
    void createProductManufacturerAllocationWithExistingId() throws Exception {
        // Create the ProductManufacturerAllocation with an existing ID
        productManufacturerAllocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductManufacturerAllocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductManufacturerAllocations() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);

        // Get all the productManufacturerAllocationList
        restProductManufacturerAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productManufacturerAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(jsonPath("$.[*].manufactureId").value(hasItem(DEFAULT_MANUFACTURE_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(
                jsonPath("$.[*].productManufacturerAllocationId").value(hasItem(DEFAULT_PRODUCT_MANUFACTURER_ALLOCATION_ID.toString()))
            )
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getProductManufacturerAllocation() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);

        // Get the productManufacturerAllocation
        restProductManufacturerAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, productManufacturerAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productManufacturerAllocation.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isAllocated").value(DEFAULT_IS_ALLOCATED.booleanValue()))
            .andExpect(jsonPath("$.manufactureId").value(DEFAULT_MANUFACTURE_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productManufacturerAllocationId").value(DEFAULT_PRODUCT_MANUFACTURER_ALLOCATION_ID.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductManufacturerAllocation() throws Exception {
        // Get the productManufacturerAllocation
        restProductManufacturerAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductManufacturerAllocation() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productManufacturerAllocationSearchRepository.save(productManufacturerAllocation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());

        // Update the productManufacturerAllocation
        ProductManufacturerAllocation updatedProductManufacturerAllocation = productManufacturerAllocationRepository
            .findById(productManufacturerAllocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductManufacturerAllocation are not directly saved in db
        em.detach(updatedProductManufacturerAllocation);
        updatedProductManufacturerAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .manufactureId(UPDATED_MANUFACTURE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productManufacturerAllocationId(UPDATED_PRODUCT_MANUFACTURER_ALLOCATION_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductManufacturerAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductManufacturerAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductManufacturerAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductManufacturerAllocationToMatchAllProperties(updatedProductManufacturerAllocation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductManufacturerAllocation> productManufacturerAllocationSearchList = Streamable.of(
                    productManufacturerAllocationSearchRepository.findAll()
                ).toList();
                ProductManufacturerAllocation testProductManufacturerAllocationSearch = productManufacturerAllocationSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertProductManufacturerAllocationAllPropertiesEquals(
                    testProductManufacturerAllocationSearch,
                    updatedProductManufacturerAllocation
                );
            });
    }

    @Test
    @Transactional
    void putNonExistingProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        productManufacturerAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductManufacturerAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productManufacturerAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        productManufacturerAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductManufacturerAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        productManufacturerAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductManufacturerAllocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductManufacturerAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productManufacturerAllocation using partial update
        ProductManufacturerAllocation partialUpdatedProductManufacturerAllocation = new ProductManufacturerAllocation();
        partialUpdatedProductManufacturerAllocation.setId(productManufacturerAllocation.getId());

        partialUpdatedProductManufacturerAllocation
            .isAllocated(UPDATED_IS_ALLOCATED)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductManufacturerAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductManufacturerAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductManufacturerAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductManufacturerAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductManufacturerAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductManufacturerAllocation, productManufacturerAllocation),
            getPersistedProductManufacturerAllocation(productManufacturerAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductManufacturerAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productManufacturerAllocation using partial update
        ProductManufacturerAllocation partialUpdatedProductManufacturerAllocation = new ProductManufacturerAllocation();
        partialUpdatedProductManufacturerAllocation.setId(productManufacturerAllocation.getId());

        partialUpdatedProductManufacturerAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .manufactureId(UPDATED_MANUFACTURE_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productManufacturerAllocationId(UPDATED_PRODUCT_MANUFACTURER_ALLOCATION_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductManufacturerAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductManufacturerAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductManufacturerAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductManufacturerAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductManufacturerAllocationUpdatableFieldsEquals(
            partialUpdatedProductManufacturerAllocation,
            getPersistedProductManufacturerAllocation(partialUpdatedProductManufacturerAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        productManufacturerAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductManufacturerAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productManufacturerAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        productManufacturerAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductManufacturerAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductManufacturerAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        productManufacturerAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductManufacturerAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productManufacturerAllocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductManufacturerAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductManufacturerAllocation() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);
        productManufacturerAllocationRepository.save(productManufacturerAllocation);
        productManufacturerAllocationSearchRepository.save(productManufacturerAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productManufacturerAllocation
        restProductManufacturerAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productManufacturerAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productManufacturerAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductManufacturerAllocation() throws Exception {
        // Initialize the database
        insertedProductManufacturerAllocation = productManufacturerAllocationRepository.saveAndFlush(productManufacturerAllocation);
        productManufacturerAllocationSearchRepository.save(productManufacturerAllocation);

        // Search the productManufacturerAllocation
        restProductManufacturerAllocationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productManufacturerAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productManufacturerAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(jsonPath("$.[*].manufactureId").value(hasItem(DEFAULT_MANUFACTURE_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(
                jsonPath("$.[*].productManufacturerAllocationId").value(hasItem(DEFAULT_PRODUCT_MANUFACTURER_ALLOCATION_ID.toString()))
            )
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return productManufacturerAllocationRepository.count();
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

    protected ProductManufacturerAllocation getPersistedProductManufacturerAllocation(
        ProductManufacturerAllocation productManufacturerAllocation
    ) {
        return productManufacturerAllocationRepository.findById(productManufacturerAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedProductManufacturerAllocationToMatchAllProperties(
        ProductManufacturerAllocation expectedProductManufacturerAllocation
    ) {
        assertProductManufacturerAllocationAllPropertiesEquals(
            expectedProductManufacturerAllocation,
            getPersistedProductManufacturerAllocation(expectedProductManufacturerAllocation)
        );
    }

    protected void assertPersistedProductManufacturerAllocationToMatchUpdatableProperties(
        ProductManufacturerAllocation expectedProductManufacturerAllocation
    ) {
        assertProductManufacturerAllocationAllUpdatablePropertiesEquals(
            expectedProductManufacturerAllocation,
            getPersistedProductManufacturerAllocation(expectedProductManufacturerAllocation)
        );
    }
}
