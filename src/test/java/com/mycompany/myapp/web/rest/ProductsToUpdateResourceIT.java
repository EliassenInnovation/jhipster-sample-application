package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductsToUpdateAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductsToUpdate;
import com.mycompany.myapp.repository.ProductsToUpdateRepository;
import com.mycompany.myapp.repository.search.ProductsToUpdateSearchRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ProductsToUpdateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsToUpdateResourceIT {

    private static final String DEFAULT_MAX_GLN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MAX_GLN_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_MANUFACTURER_ID = 1;
    private static final Integer UPDATED_MAX_MANUFACTURER_ID = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/products-to-updates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/products-to-updates/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductsToUpdateRepository productsToUpdateRepository;

    @Autowired
    private ProductsToUpdateSearchRepository productsToUpdateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsToUpdateMockMvc;

    private ProductsToUpdate productsToUpdate;

    private ProductsToUpdate insertedProductsToUpdate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsToUpdate createEntity() {
        return new ProductsToUpdate()
            .maxGLNCode(DEFAULT_MAX_GLN_CODE)
            .maxManufacturerID(DEFAULT_MAX_MANUFACTURER_ID)
            .productId(DEFAULT_PRODUCT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsToUpdate createUpdatedEntity() {
        return new ProductsToUpdate()
            .maxGLNCode(UPDATED_MAX_GLN_CODE)
            .maxManufacturerID(UPDATED_MAX_MANUFACTURER_ID)
            .productId(UPDATED_PRODUCT_ID);
    }

    @BeforeEach
    public void initTest() {
        productsToUpdate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductsToUpdate != null) {
            productsToUpdateRepository.delete(insertedProductsToUpdate);
            productsToUpdateSearchRepository.delete(insertedProductsToUpdate);
            insertedProductsToUpdate = null;
        }
    }

    @Test
    @Transactional
    void createProductsToUpdate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        // Create the ProductsToUpdate
        var returnedProductsToUpdate = om.readValue(
            restProductsToUpdateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productsToUpdate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductsToUpdate.class
        );

        // Validate the ProductsToUpdate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductsToUpdateUpdatableFieldsEquals(returnedProductsToUpdate, getPersistedProductsToUpdate(returnedProductsToUpdate));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductsToUpdate = returnedProductsToUpdate;
    }

    @Test
    @Transactional
    void createProductsToUpdateWithExistingId() throws Exception {
        // Create the ProductsToUpdate with an existing ID
        productsToUpdate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsToUpdateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productsToUpdate)))
            .andExpect(status().isBadRequest());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductsToUpdates() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);

        // Get all the productsToUpdateList
        restProductsToUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsToUpdate.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxGLNCode").value(hasItem(DEFAULT_MAX_GLN_CODE)))
            .andExpect(jsonPath("$.[*].maxManufacturerID").value(hasItem(DEFAULT_MAX_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())));
    }

    @Test
    @Transactional
    void getProductsToUpdate() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);

        // Get the productsToUpdate
        restProductsToUpdateMockMvc
            .perform(get(ENTITY_API_URL_ID, productsToUpdate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productsToUpdate.getId().intValue()))
            .andExpect(jsonPath("$.maxGLNCode").value(DEFAULT_MAX_GLN_CODE))
            .andExpect(jsonPath("$.maxManufacturerID").value(DEFAULT_MAX_MANUFACTURER_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductsToUpdate() throws Exception {
        // Get the productsToUpdate
        restProductsToUpdateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductsToUpdate() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productsToUpdateSearchRepository.save(productsToUpdate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());

        // Update the productsToUpdate
        ProductsToUpdate updatedProductsToUpdate = productsToUpdateRepository.findById(productsToUpdate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductsToUpdate are not directly saved in db
        em.detach(updatedProductsToUpdate);
        updatedProductsToUpdate
            .maxGLNCode(UPDATED_MAX_GLN_CODE)
            .maxManufacturerID(UPDATED_MAX_MANUFACTURER_ID)
            .productId(UPDATED_PRODUCT_ID);

        restProductsToUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductsToUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductsToUpdate))
            )
            .andExpect(status().isOk());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductsToUpdateToMatchAllProperties(updatedProductsToUpdate);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductsToUpdate> productsToUpdateSearchList = Streamable.of(productsToUpdateSearchRepository.findAll()).toList();
                ProductsToUpdate testProductsToUpdateSearch = productsToUpdateSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductsToUpdateAllPropertiesEquals(testProductsToUpdateSearch, updatedProductsToUpdate);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductsToUpdate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        productsToUpdate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsToUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsToUpdate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productsToUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductsToUpdate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        productsToUpdate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsToUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productsToUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductsToUpdate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        productsToUpdate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsToUpdateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productsToUpdate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductsToUpdateWithPatch() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productsToUpdate using partial update
        ProductsToUpdate partialUpdatedProductsToUpdate = new ProductsToUpdate();
        partialUpdatedProductsToUpdate.setId(productsToUpdate.getId());

        partialUpdatedProductsToUpdate.maxGLNCode(UPDATED_MAX_GLN_CODE).productId(UPDATED_PRODUCT_ID);

        restProductsToUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsToUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductsToUpdate))
            )
            .andExpect(status().isOk());

        // Validate the ProductsToUpdate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductsToUpdateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductsToUpdate, productsToUpdate),
            getPersistedProductsToUpdate(productsToUpdate)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductsToUpdateWithPatch() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productsToUpdate using partial update
        ProductsToUpdate partialUpdatedProductsToUpdate = new ProductsToUpdate();
        partialUpdatedProductsToUpdate.setId(productsToUpdate.getId());

        partialUpdatedProductsToUpdate
            .maxGLNCode(UPDATED_MAX_GLN_CODE)
            .maxManufacturerID(UPDATED_MAX_MANUFACTURER_ID)
            .productId(UPDATED_PRODUCT_ID);

        restProductsToUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsToUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductsToUpdate))
            )
            .andExpect(status().isOk());

        // Validate the ProductsToUpdate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductsToUpdateUpdatableFieldsEquals(
            partialUpdatedProductsToUpdate,
            getPersistedProductsToUpdate(partialUpdatedProductsToUpdate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductsToUpdate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        productsToUpdate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsToUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productsToUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productsToUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductsToUpdate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        productsToUpdate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsToUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productsToUpdate))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductsToUpdate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        productsToUpdate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsToUpdateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productsToUpdate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsToUpdate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductsToUpdate() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);
        productsToUpdateRepository.save(productsToUpdate);
        productsToUpdateSearchRepository.save(productsToUpdate);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productsToUpdate
        restProductsToUpdateMockMvc
            .perform(delete(ENTITY_API_URL_ID, productsToUpdate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productsToUpdateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductsToUpdate() throws Exception {
        // Initialize the database
        insertedProductsToUpdate = productsToUpdateRepository.saveAndFlush(productsToUpdate);
        productsToUpdateSearchRepository.save(productsToUpdate);

        // Search the productsToUpdate
        restProductsToUpdateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productsToUpdate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsToUpdate.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxGLNCode").value(hasItem(DEFAULT_MAX_GLN_CODE)))
            .andExpect(jsonPath("$.[*].maxManufacturerID").value(hasItem(DEFAULT_MAX_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())));
    }

    protected long getRepositoryCount() {
        return productsToUpdateRepository.count();
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

    protected ProductsToUpdate getPersistedProductsToUpdate(ProductsToUpdate productsToUpdate) {
        return productsToUpdateRepository.findById(productsToUpdate.getId()).orElseThrow();
    }

    protected void assertPersistedProductsToUpdateToMatchAllProperties(ProductsToUpdate expectedProductsToUpdate) {
        assertProductsToUpdateAllPropertiesEquals(expectedProductsToUpdate, getPersistedProductsToUpdate(expectedProductsToUpdate));
    }

    protected void assertPersistedProductsToUpdateToMatchUpdatableProperties(ProductsToUpdate expectedProductsToUpdate) {
        assertProductsToUpdateAllUpdatablePropertiesEquals(
            expectedProductsToUpdate,
            getPersistedProductsToUpdate(expectedProductsToUpdate)
        );
    }
}
