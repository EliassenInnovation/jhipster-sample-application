package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductH7Asserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductH7;
import com.mycompany.myapp.repository.ProductH7Repository;
import com.mycompany.myapp.repository.search.ProductH7SearchRepository;
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
 * Integration tests for the {@link ProductH7Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductH7ResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-h-7-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-h-7-s/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductH7Repository productH7Repository;

    @Autowired
    private ProductH7SearchRepository productH7SearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductH7MockMvc;

    private ProductH7 productH7;

    private ProductH7 insertedProductH7;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductH7 createEntity() {
        return new ProductH7()
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
    public static ProductH7 createUpdatedEntity() {
        return new ProductH7()
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);
    }

    @BeforeEach
    public void initTest() {
        productH7 = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductH7 != null) {
            productH7Repository.delete(insertedProductH7);
            productH7SearchRepository.delete(insertedProductH7);
            insertedProductH7 = null;
        }
    }

    @Test
    @Transactional
    void createProductH7() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        // Create the ProductH7
        var returnedProductH7 = om.readValue(
            restProductH7MockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductH7.class
        );

        // Validate the ProductH7 in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductH7UpdatableFieldsEquals(returnedProductH7, getPersistedProductH7(returnedProductH7));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductH7 = returnedProductH7;
    }

    @Test
    @Transactional
    void createProductH7WithExistingId() throws Exception {
        // Create the ProductH7 with an existing ID
        productH7.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductH7MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7)))
            .andExpect(status().isBadRequest());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductH7s() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);

        // Get all the productH7List
        restProductH7MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productH7.getId().intValue())))
            .andExpect(jsonPath("$.[*].gtinUpc").value(hasItem(DEFAULT_GTIN_UPC)))
            .andExpect(jsonPath("$.[*].h7KeywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iOCGroup").value(hasItem(DEFAULT_I_OC_GROUP)))
            .andExpect(jsonPath("$.[*].productH7Id").value(hasItem(DEFAULT_PRODUCT_H_7_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }

    @Test
    @Transactional
    void getProductH7() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);

        // Get the productH7
        restProductH7MockMvc
            .perform(get(ENTITY_API_URL_ID, productH7.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productH7.getId().intValue()))
            .andExpect(jsonPath("$.gtinUpc").value(DEFAULT_GTIN_UPC))
            .andExpect(jsonPath("$.h7KeywordId").value(DEFAULT_H_7_KEYWORD_ID))
            .andExpect(jsonPath("$.iOCGroup").value(DEFAULT_I_OC_GROUP))
            .andExpect(jsonPath("$.productH7Id").value(DEFAULT_PRODUCT_H_7_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProductH7() throws Exception {
        // Get the productH7
        restProductH7MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductH7() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productH7SearchRepository.save(productH7);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());

        // Update the productH7
        ProductH7 updatedProductH7 = productH7Repository.findById(productH7.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductH7 are not directly saved in db
        em.detach(updatedProductH7);
        updatedProductH7
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);

        restProductH7MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductH7.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductH7))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductH7ToMatchAllProperties(updatedProductH7);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductH7> productH7SearchList = Streamable.of(productH7SearchRepository.findAll()).toList();
                ProductH7 testProductH7Search = productH7SearchList.get(searchDatabaseSizeAfter - 1);

                assertProductH7AllPropertiesEquals(testProductH7Search, updatedProductH7);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductH7() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        productH7.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductH7MockMvc
            .perform(
                put(ENTITY_API_URL_ID, productH7.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductH7() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        productH7.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7MockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productH7))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductH7() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        productH7.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductH7WithPatch() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productH7 using partial update
        ProductH7 partialUpdatedProductH7 = new ProductH7();
        partialUpdatedProductH7.setId(productH7.getId());

        partialUpdatedProductH7.iOCGroup(UPDATED_I_OC_GROUP).productId(UPDATED_PRODUCT_ID);

        restProductH7MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductH7.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductH7))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7 in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductH7UpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductH7, productH7),
            getPersistedProductH7(productH7)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductH7WithPatch() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productH7 using partial update
        ProductH7 partialUpdatedProductH7 = new ProductH7();
        partialUpdatedProductH7.setId(productH7.getId());

        partialUpdatedProductH7
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);

        restProductH7MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductH7.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductH7))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7 in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductH7UpdatableFieldsEquals(partialUpdatedProductH7, getPersistedProductH7(partialUpdatedProductH7));
    }

    @Test
    @Transactional
    void patchNonExistingProductH7() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        productH7.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductH7MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productH7.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productH7))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductH7() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        productH7.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productH7))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductH7() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        productH7.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7MockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productH7)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductH7 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductH7() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);
        productH7Repository.save(productH7);
        productH7SearchRepository.save(productH7);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productH7
        restProductH7MockMvc
            .perform(delete(ENTITY_API_URL_ID, productH7.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7SearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductH7() throws Exception {
        // Initialize the database
        insertedProductH7 = productH7Repository.saveAndFlush(productH7);
        productH7SearchRepository.save(productH7);

        // Search the productH7
        restProductH7MockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productH7.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productH7.getId().intValue())))
            .andExpect(jsonPath("$.[*].gtinUpc").value(hasItem(DEFAULT_GTIN_UPC)))
            .andExpect(jsonPath("$.[*].h7KeywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iOCGroup").value(hasItem(DEFAULT_I_OC_GROUP)))
            .andExpect(jsonPath("$.[*].productH7Id").value(hasItem(DEFAULT_PRODUCT_H_7_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }

    protected long getRepositoryCount() {
        return productH7Repository.count();
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

    protected ProductH7 getPersistedProductH7(ProductH7 productH7) {
        return productH7Repository.findById(productH7.getId()).orElseThrow();
    }

    protected void assertPersistedProductH7ToMatchAllProperties(ProductH7 expectedProductH7) {
        assertProductH7AllPropertiesEquals(expectedProductH7, getPersistedProductH7(expectedProductH7));
    }

    protected void assertPersistedProductH7ToMatchUpdatableProperties(ProductH7 expectedProductH7) {
        assertProductH7AllUpdatablePropertiesEquals(expectedProductH7, getPersistedProductH7(expectedProductH7));
    }
}
