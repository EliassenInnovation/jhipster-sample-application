package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductH7OldAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductH7Old;
import com.mycompany.myapp.repository.ProductH7OldRepository;
import com.mycompany.myapp.repository.search.ProductH7OldSearchRepository;
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
 * Integration tests for the {@link ProductH7OldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductH7OldResourceIT {

    private static final String DEFAULT_GTIN_UPC = "AAAAAAAAAA";
    private static final String UPDATED_GTIN_UPC = "BBBBBBBBBB";

    private static final Integer DEFAULT_H_7_KEYWORD_ID = 1;
    private static final Integer UPDATED_H_7_KEYWORD_ID = 2;

    private static final String DEFAULT_I_OC_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_I_OC_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_H_7_ID = 1;
    private static final Integer UPDATED_PRODUCT_H_7_ID = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-h-7-olds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-h-7-olds/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductH7OldRepository productH7OldRepository;

    @Autowired
    private ProductH7OldSearchRepository productH7OldSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductH7OldMockMvc;

    private ProductH7Old productH7Old;

    private ProductH7Old insertedProductH7Old;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductH7Old createEntity() {
        return new ProductH7Old()
            .gtinUpc(DEFAULT_GTIN_UPC)
            .h7KeywordId(DEFAULT_H_7_KEYWORD_ID)
            .iOCGroup(DEFAULT_I_OC_GROUP)
            .productH7Id(DEFAULT_PRODUCT_H_7_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .productName(DEFAULT_PRODUCT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductH7Old createUpdatedEntity() {
        return new ProductH7Old()
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);
    }

    @BeforeEach
    public void initTest() {
        productH7Old = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductH7Old != null) {
            productH7OldRepository.delete(insertedProductH7Old);
            productH7OldSearchRepository.delete(insertedProductH7Old);
            insertedProductH7Old = null;
        }
    }

    @Test
    @Transactional
    void createProductH7Old() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        // Create the ProductH7Old
        var returnedProductH7Old = om.readValue(
            restProductH7OldMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7Old)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductH7Old.class
        );

        // Validate the ProductH7Old in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductH7OldUpdatableFieldsEquals(returnedProductH7Old, getPersistedProductH7Old(returnedProductH7Old));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductH7Old = returnedProductH7Old;
    }

    @Test
    @Transactional
    void createProductH7OldWithExistingId() throws Exception {
        // Create the ProductH7Old with an existing ID
        productH7Old.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductH7OldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7Old)))
            .andExpect(status().isBadRequest());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductH7Olds() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);

        // Get all the productH7OldList
        restProductH7OldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productH7Old.getId().intValue())))
            .andExpect(jsonPath("$.[*].gtinUpc").value(hasItem(DEFAULT_GTIN_UPC)))
            .andExpect(jsonPath("$.[*].h7KeywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iOCGroup").value(hasItem(DEFAULT_I_OC_GROUP)))
            .andExpect(jsonPath("$.[*].productH7Id").value(hasItem(DEFAULT_PRODUCT_H_7_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }

    @Test
    @Transactional
    void getProductH7Old() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);

        // Get the productH7Old
        restProductH7OldMockMvc
            .perform(get(ENTITY_API_URL_ID, productH7Old.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productH7Old.getId().intValue()))
            .andExpect(jsonPath("$.gtinUpc").value(DEFAULT_GTIN_UPC))
            .andExpect(jsonPath("$.h7KeywordId").value(DEFAULT_H_7_KEYWORD_ID))
            .andExpect(jsonPath("$.iOCGroup").value(DEFAULT_I_OC_GROUP))
            .andExpect(jsonPath("$.productH7Id").value(DEFAULT_PRODUCT_H_7_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProductH7Old() throws Exception {
        // Get the productH7Old
        restProductH7OldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductH7Old() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productH7OldSearchRepository.save(productH7Old);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());

        // Update the productH7Old
        ProductH7Old updatedProductH7Old = productH7OldRepository.findById(productH7Old.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductH7Old are not directly saved in db
        em.detach(updatedProductH7Old);
        updatedProductH7Old
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);

        restProductH7OldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductH7Old.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductH7Old))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductH7OldToMatchAllProperties(updatedProductH7Old);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductH7Old> productH7OldSearchList = Streamable.of(productH7OldSearchRepository.findAll()).toList();
                ProductH7Old testProductH7OldSearch = productH7OldSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductH7OldAllPropertiesEquals(testProductH7OldSearch, updatedProductH7Old);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductH7Old() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        productH7Old.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductH7OldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productH7Old.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productH7Old))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductH7Old() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        productH7Old.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7OldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productH7Old))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductH7Old() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        productH7Old.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7OldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7Old)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductH7OldWithPatch() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productH7Old using partial update
        ProductH7Old partialUpdatedProductH7Old = new ProductH7Old();
        partialUpdatedProductH7Old.setId(productH7Old.getId());

        partialUpdatedProductH7Old
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID);

        restProductH7OldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductH7Old.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductH7Old))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7Old in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductH7OldUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductH7Old, productH7Old),
            getPersistedProductH7Old(productH7Old)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductH7OldWithPatch() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productH7Old using partial update
        ProductH7Old partialUpdatedProductH7Old = new ProductH7Old();
        partialUpdatedProductH7Old.setId(productH7Old.getId());

        partialUpdatedProductH7Old
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);

        restProductH7OldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductH7Old.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductH7Old))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7Old in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductH7OldUpdatableFieldsEquals(partialUpdatedProductH7Old, getPersistedProductH7Old(partialUpdatedProductH7Old));
    }

    @Test
    @Transactional
    void patchNonExistingProductH7Old() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        productH7Old.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductH7OldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productH7Old.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productH7Old))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductH7Old() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        productH7Old.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7OldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productH7Old))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductH7Old() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        productH7Old.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7OldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productH7Old)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductH7Old in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductH7Old() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);
        productH7OldRepository.save(productH7Old);
        productH7OldSearchRepository.save(productH7Old);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productH7Old
        restProductH7OldMockMvc
            .perform(delete(ENTITY_API_URL_ID, productH7Old.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7OldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductH7Old() throws Exception {
        // Initialize the database
        insertedProductH7Old = productH7OldRepository.saveAndFlush(productH7Old);
        productH7OldSearchRepository.save(productH7Old);

        // Search the productH7Old
        restProductH7OldMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productH7Old.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productH7Old.getId().intValue())))
            .andExpect(jsonPath("$.[*].gtinUpc").value(hasItem(DEFAULT_GTIN_UPC)))
            .andExpect(jsonPath("$.[*].h7KeywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iOCGroup").value(hasItem(DEFAULT_I_OC_GROUP)))
            .andExpect(jsonPath("$.[*].productH7Id").value(hasItem(DEFAULT_PRODUCT_H_7_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }

    protected long getRepositoryCount() {
        return productH7OldRepository.count();
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

    protected ProductH7Old getPersistedProductH7Old(ProductH7Old productH7Old) {
        return productH7OldRepository.findById(productH7Old.getId()).orElseThrow();
    }

    protected void assertPersistedProductH7OldToMatchAllProperties(ProductH7Old expectedProductH7Old) {
        assertProductH7OldAllPropertiesEquals(expectedProductH7Old, getPersistedProductH7Old(expectedProductH7Old));
    }

    protected void assertPersistedProductH7OldToMatchUpdatableProperties(ProductH7Old expectedProductH7Old) {
        assertProductH7OldAllUpdatablePropertiesEquals(expectedProductH7Old, getPersistedProductH7Old(expectedProductH7Old));
    }
}
