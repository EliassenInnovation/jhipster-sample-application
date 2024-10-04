package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductDistrictAllocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductDistrictAllocation;
import com.mycompany.myapp.repository.ProductDistrictAllocationRepository;
import com.mycompany.myapp.repository.search.ProductDistrictAllocationSearchRepository;
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
 * Integration tests for the {@link ProductDistrictAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductDistrictAllocationResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ALLOCATED = false;
    private static final Boolean UPDATED_IS_ALLOCATED = true;

    private static final UUID DEFAULT_PRODUCT_DISTRICT_ALLOCATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_PRODUCT_DISTRICT_ALLOCATION_ID = UUID.randomUUID();

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/product-district-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-district-allocations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductDistrictAllocationRepository productDistrictAllocationRepository;

    @Autowired
    private ProductDistrictAllocationSearchRepository productDistrictAllocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductDistrictAllocationMockMvc;

    private ProductDistrictAllocation productDistrictAllocation;

    private ProductDistrictAllocation insertedProductDistrictAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDistrictAllocation createEntity() {
        return new ProductDistrictAllocation()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isAllocated(DEFAULT_IS_ALLOCATED)
            .productDistrictAllocationId(DEFAULT_PRODUCT_DISTRICT_ALLOCATION_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDistrictAllocation createUpdatedEntity() {
        return new ProductDistrictAllocation()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistrictAllocationId(UPDATED_PRODUCT_DISTRICT_ALLOCATION_ID)
            .productId(UPDATED_PRODUCT_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        productDistrictAllocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductDistrictAllocation != null) {
            productDistrictAllocationRepository.delete(insertedProductDistrictAllocation);
            productDistrictAllocationSearchRepository.delete(insertedProductDistrictAllocation);
            insertedProductDistrictAllocation = null;
        }
    }

    @Test
    @Transactional
    void createProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        // Create the ProductDistrictAllocation
        var returnedProductDistrictAllocation = om.readValue(
            restProductDistrictAllocationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDistrictAllocation))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDistrictAllocation.class
        );

        // Validate the ProductDistrictAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductDistrictAllocationUpdatableFieldsEquals(
            returnedProductDistrictAllocation,
            getPersistedProductDistrictAllocation(returnedProductDistrictAllocation)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductDistrictAllocation = returnedProductDistrictAllocation;
    }

    @Test
    @Transactional
    void createProductDistrictAllocationWithExistingId() throws Exception {
        // Create the ProductDistrictAllocation with an existing ID
        productDistrictAllocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductDistrictAllocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDistrictAllocation)))
            .andExpect(status().isBadRequest());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductDistrictAllocations() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);

        // Get all the productDistrictAllocationList
        restProductDistrictAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDistrictAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(jsonPath("$.[*].productDistrictAllocationId").value(hasItem(DEFAULT_PRODUCT_DISTRICT_ALLOCATION_ID.toString())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getProductDistrictAllocation() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);

        // Get the productDistrictAllocation
        restProductDistrictAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, productDistrictAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productDistrictAllocation.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isAllocated").value(DEFAULT_IS_ALLOCATED.booleanValue()))
            .andExpect(jsonPath("$.productDistrictAllocationId").value(DEFAULT_PRODUCT_DISTRICT_ALLOCATION_ID.toString()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductDistrictAllocation() throws Exception {
        // Get the productDistrictAllocation
        restProductDistrictAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductDistrictAllocation() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productDistrictAllocationSearchRepository.save(productDistrictAllocation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());

        // Update the productDistrictAllocation
        ProductDistrictAllocation updatedProductDistrictAllocation = productDistrictAllocationRepository
            .findById(productDistrictAllocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductDistrictAllocation are not directly saved in db
        em.detach(updatedProductDistrictAllocation);
        updatedProductDistrictAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistrictAllocationId(UPDATED_PRODUCT_DISTRICT_ALLOCATION_ID)
            .productId(UPDATED_PRODUCT_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductDistrictAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductDistrictAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductDistrictAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductDistrictAllocationToMatchAllProperties(updatedProductDistrictAllocation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductDistrictAllocation> productDistrictAllocationSearchList = Streamable.of(
                    productDistrictAllocationSearchRepository.findAll()
                ).toList();
                ProductDistrictAllocation testProductDistrictAllocationSearch = productDistrictAllocationSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertProductDistrictAllocationAllPropertiesEquals(testProductDistrictAllocationSearch, updatedProductDistrictAllocation);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        productDistrictAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDistrictAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDistrictAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        productDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistrictAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        productDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistrictAllocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDistrictAllocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductDistrictAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDistrictAllocation using partial update
        ProductDistrictAllocation partialUpdatedProductDistrictAllocation = new ProductDistrictAllocation();
        partialUpdatedProductDistrictAllocation.setId(productDistrictAllocation.getId());

        partialUpdatedProductDistrictAllocation
            .createdBy(UPDATED_CREATED_BY)
            .productDistrictAllocationId(UPDATED_PRODUCT_DISTRICT_ALLOCATION_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDistrictAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductDistrictAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductDistrictAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductDistrictAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductDistrictAllocation, productDistrictAllocation),
            getPersistedProductDistrictAllocation(productDistrictAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductDistrictAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productDistrictAllocation using partial update
        ProductDistrictAllocation partialUpdatedProductDistrictAllocation = new ProductDistrictAllocation();
        partialUpdatedProductDistrictAllocation.setId(productDistrictAllocation.getId());

        partialUpdatedProductDistrictAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .productDistrictAllocationId(UPDATED_PRODUCT_DISTRICT_ALLOCATION_ID)
            .productId(UPDATED_PRODUCT_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDistrictAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductDistrictAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductDistrictAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductDistrictAllocationUpdatableFieldsEquals(
            partialUpdatedProductDistrictAllocation,
            getPersistedProductDistrictAllocation(partialUpdatedProductDistrictAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        productDistrictAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDistrictAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        productDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        productDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDistrictAllocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductDistrictAllocation() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);
        productDistrictAllocationRepository.save(productDistrictAllocation);
        productDistrictAllocationSearchRepository.save(productDistrictAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productDistrictAllocation
        restProductDistrictAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productDistrictAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductDistrictAllocation() throws Exception {
        // Initialize the database
        insertedProductDistrictAllocation = productDistrictAllocationRepository.saveAndFlush(productDistrictAllocation);
        productDistrictAllocationSearchRepository.save(productDistrictAllocation);

        // Search the productDistrictAllocation
        restProductDistrictAllocationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productDistrictAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDistrictAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(jsonPath("$.[*].productDistrictAllocationId").value(hasItem(DEFAULT_PRODUCT_DISTRICT_ALLOCATION_ID.toString())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return productDistrictAllocationRepository.count();
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

    protected ProductDistrictAllocation getPersistedProductDistrictAllocation(ProductDistrictAllocation productDistrictAllocation) {
        return productDistrictAllocationRepository.findById(productDistrictAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedProductDistrictAllocationToMatchAllProperties(
        ProductDistrictAllocation expectedProductDistrictAllocation
    ) {
        assertProductDistrictAllocationAllPropertiesEquals(
            expectedProductDistrictAllocation,
            getPersistedProductDistrictAllocation(expectedProductDistrictAllocation)
        );
    }

    protected void assertPersistedProductDistrictAllocationToMatchUpdatableProperties(
        ProductDistrictAllocation expectedProductDistrictAllocation
    ) {
        assertProductDistrictAllocationAllUpdatablePropertiesEquals(
            expectedProductDistrictAllocation,
            getPersistedProductDistrictAllocation(expectedProductDistrictAllocation)
        );
    }
}
