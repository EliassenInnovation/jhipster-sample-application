package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductUpcAllocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductUpcAllocation;
import com.mycompany.myapp.repository.ProductUpcAllocationRepository;
import com.mycompany.myapp.repository.search.ProductUpcAllocationSearchRepository;
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
 * Integration tests for the {@link ProductUpcAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductUpcAllocationResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_PRODUCT_UPC_ID = 1;
    private static final Integer UPDATED_PRODUCT_UPC_ID = 2;

    private static final String DEFAULT_U_PC = "AAAAAAAAAA";
    private static final String UPDATED_U_PC = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/product-upc-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-upc-allocations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductUpcAllocationRepository productUpcAllocationRepository;

    @Autowired
    private ProductUpcAllocationSearchRepository productUpcAllocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductUpcAllocationMockMvc;

    private ProductUpcAllocation productUpcAllocation;

    private ProductUpcAllocation insertedProductUpcAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductUpcAllocation createEntity() {
        return new ProductUpcAllocation()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .productId(DEFAULT_PRODUCT_ID)
            .productUpcId(DEFAULT_PRODUCT_UPC_ID)
            .uPC(DEFAULT_U_PC)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductUpcAllocation createUpdatedEntity() {
        return new ProductUpcAllocation()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .productUpcId(UPDATED_PRODUCT_UPC_ID)
            .uPC(UPDATED_U_PC)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        productUpcAllocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductUpcAllocation != null) {
            productUpcAllocationRepository.delete(insertedProductUpcAllocation);
            productUpcAllocationSearchRepository.delete(insertedProductUpcAllocation);
            insertedProductUpcAllocation = null;
        }
    }

    @Test
    @Transactional
    void createProductUpcAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        // Create the ProductUpcAllocation
        var returnedProductUpcAllocation = om.readValue(
            restProductUpcAllocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productUpcAllocation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductUpcAllocation.class
        );

        // Validate the ProductUpcAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductUpcAllocationUpdatableFieldsEquals(
            returnedProductUpcAllocation,
            getPersistedProductUpcAllocation(returnedProductUpcAllocation)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductUpcAllocation = returnedProductUpcAllocation;
    }

    @Test
    @Transactional
    void createProductUpcAllocationWithExistingId() throws Exception {
        // Create the ProductUpcAllocation with an existing ID
        productUpcAllocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductUpcAllocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productUpcAllocation)))
            .andExpect(status().isBadRequest());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductUpcAllocations() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);

        // Get all the productUpcAllocationList
        restProductUpcAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productUpcAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productUpcId").value(hasItem(DEFAULT_PRODUCT_UPC_ID)))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getProductUpcAllocation() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);

        // Get the productUpcAllocation
        restProductUpcAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, productUpcAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productUpcAllocation.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productUpcId").value(DEFAULT_PRODUCT_UPC_ID))
            .andExpect(jsonPath("$.uPC").value(DEFAULT_U_PC))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductUpcAllocation() throws Exception {
        // Get the productUpcAllocation
        restProductUpcAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductUpcAllocation() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productUpcAllocationSearchRepository.save(productUpcAllocation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());

        // Update the productUpcAllocation
        ProductUpcAllocation updatedProductUpcAllocation = productUpcAllocationRepository
            .findById(productUpcAllocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductUpcAllocation are not directly saved in db
        em.detach(updatedProductUpcAllocation);
        updatedProductUpcAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .productUpcId(UPDATED_PRODUCT_UPC_ID)
            .uPC(UPDATED_U_PC)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductUpcAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductUpcAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductUpcAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductUpcAllocationToMatchAllProperties(updatedProductUpcAllocation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductUpcAllocation> productUpcAllocationSearchList = Streamable.of(
                    productUpcAllocationSearchRepository.findAll()
                ).toList();
                ProductUpcAllocation testProductUpcAllocationSearch = productUpcAllocationSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductUpcAllocationAllPropertiesEquals(testProductUpcAllocationSearch, updatedProductUpcAllocation);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductUpcAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        productUpcAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductUpcAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productUpcAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productUpcAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductUpcAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        productUpcAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUpcAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productUpcAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductUpcAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        productUpcAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUpcAllocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productUpcAllocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductUpcAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productUpcAllocation using partial update
        ProductUpcAllocation partialUpdatedProductUpcAllocation = new ProductUpcAllocation();
        partialUpdatedProductUpcAllocation.setId(productUpcAllocation.getId());

        partialUpdatedProductUpcAllocation
            .createdOn(UPDATED_CREATED_ON)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductUpcAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductUpcAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductUpcAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductUpcAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpcAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductUpcAllocation, productUpcAllocation),
            getPersistedProductUpcAllocation(productUpcAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductUpcAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productUpcAllocation using partial update
        ProductUpcAllocation partialUpdatedProductUpcAllocation = new ProductUpcAllocation();
        partialUpdatedProductUpcAllocation.setId(productUpcAllocation.getId());

        partialUpdatedProductUpcAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .productUpcId(UPDATED_PRODUCT_UPC_ID)
            .uPC(UPDATED_U_PC)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restProductUpcAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductUpcAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductUpcAllocation))
            )
            .andExpect(status().isOk());

        // Validate the ProductUpcAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpcAllocationUpdatableFieldsEquals(
            partialUpdatedProductUpcAllocation,
            getPersistedProductUpcAllocation(partialUpdatedProductUpcAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductUpcAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        productUpcAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductUpcAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productUpcAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productUpcAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductUpcAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        productUpcAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUpcAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productUpcAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductUpcAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        productUpcAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductUpcAllocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productUpcAllocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductUpcAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductUpcAllocation() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);
        productUpcAllocationRepository.save(productUpcAllocation);
        productUpcAllocationSearchRepository.save(productUpcAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productUpcAllocation
        restProductUpcAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, productUpcAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productUpcAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductUpcAllocation() throws Exception {
        // Initialize the database
        insertedProductUpcAllocation = productUpcAllocationRepository.saveAndFlush(productUpcAllocation);
        productUpcAllocationSearchRepository.save(productUpcAllocation);

        // Search the productUpcAllocation
        restProductUpcAllocationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productUpcAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productUpcAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productUpcId").value(hasItem(DEFAULT_PRODUCT_UPC_ID)))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return productUpcAllocationRepository.count();
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

    protected ProductUpcAllocation getPersistedProductUpcAllocation(ProductUpcAllocation productUpcAllocation) {
        return productUpcAllocationRepository.findById(productUpcAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedProductUpcAllocationToMatchAllProperties(ProductUpcAllocation expectedProductUpcAllocation) {
        assertProductUpcAllocationAllPropertiesEquals(
            expectedProductUpcAllocation,
            getPersistedProductUpcAllocation(expectedProductUpcAllocation)
        );
    }

    protected void assertPersistedProductUpcAllocationToMatchUpdatableProperties(ProductUpcAllocation expectedProductUpcAllocation) {
        assertProductUpcAllocationAllUpdatablePropertiesEquals(
            expectedProductUpcAllocation,
            getPersistedProductUpcAllocation(expectedProductUpcAllocation)
        );
    }
}
