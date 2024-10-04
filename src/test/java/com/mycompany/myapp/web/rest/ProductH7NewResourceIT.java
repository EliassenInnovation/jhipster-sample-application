package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductH7NewAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductH7New;
import com.mycompany.myapp.repository.ProductH7NewRepository;
import com.mycompany.myapp.repository.search.ProductH7NewSearchRepository;
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
 * Integration tests for the {@link ProductH7NewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductH7NewResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-h-7-news";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-h-7-news/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductH7NewRepository productH7NewRepository;

    @Autowired
    private ProductH7NewSearchRepository productH7NewSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductH7NewMockMvc;

    private ProductH7New productH7New;

    private ProductH7New insertedProductH7New;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductH7New createEntity() {
        return new ProductH7New()
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
    public static ProductH7New createUpdatedEntity() {
        return new ProductH7New()
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);
    }

    @BeforeEach
    public void initTest() {
        productH7New = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductH7New != null) {
            productH7NewRepository.delete(insertedProductH7New);
            productH7NewSearchRepository.delete(insertedProductH7New);
            insertedProductH7New = null;
        }
    }

    @Test
    @Transactional
    void createProductH7New() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        // Create the ProductH7New
        var returnedProductH7New = om.readValue(
            restProductH7NewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7New)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductH7New.class
        );

        // Validate the ProductH7New in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductH7NewUpdatableFieldsEquals(returnedProductH7New, getPersistedProductH7New(returnedProductH7New));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductH7New = returnedProductH7New;
    }

    @Test
    @Transactional
    void createProductH7NewWithExistingId() throws Exception {
        // Create the ProductH7New with an existing ID
        productH7New.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductH7NewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7New)))
            .andExpect(status().isBadRequest());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductH7News() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);

        // Get all the productH7NewList
        restProductH7NewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productH7New.getId().intValue())))
            .andExpect(jsonPath("$.[*].gtinUpc").value(hasItem(DEFAULT_GTIN_UPC)))
            .andExpect(jsonPath("$.[*].h7KeywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iOCGroup").value(hasItem(DEFAULT_I_OC_GROUP)))
            .andExpect(jsonPath("$.[*].productH7Id").value(hasItem(DEFAULT_PRODUCT_H_7_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }

    @Test
    @Transactional
    void getProductH7New() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);

        // Get the productH7New
        restProductH7NewMockMvc
            .perform(get(ENTITY_API_URL_ID, productH7New.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productH7New.getId().intValue()))
            .andExpect(jsonPath("$.gtinUpc").value(DEFAULT_GTIN_UPC))
            .andExpect(jsonPath("$.h7KeywordId").value(DEFAULT_H_7_KEYWORD_ID))
            .andExpect(jsonPath("$.iOCGroup").value(DEFAULT_I_OC_GROUP))
            .andExpect(jsonPath("$.productH7Id").value(DEFAULT_PRODUCT_H_7_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProductH7New() throws Exception {
        // Get the productH7New
        restProductH7NewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductH7New() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productH7NewSearchRepository.save(productH7New);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());

        // Update the productH7New
        ProductH7New updatedProductH7New = productH7NewRepository.findById(productH7New.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductH7New are not directly saved in db
        em.detach(updatedProductH7New);
        updatedProductH7New
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);

        restProductH7NewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductH7New.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductH7New))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductH7NewToMatchAllProperties(updatedProductH7New);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductH7New> productH7NewSearchList = Streamable.of(productH7NewSearchRepository.findAll()).toList();
                ProductH7New testProductH7NewSearch = productH7NewSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductH7NewAllPropertiesEquals(testProductH7NewSearch, updatedProductH7New);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductH7New() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        productH7New.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductH7NewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productH7New.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productH7New))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductH7New() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        productH7New.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7NewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productH7New))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductH7New() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        productH7New.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7NewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productH7New)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductH7NewWithPatch() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productH7New using partial update
        ProductH7New partialUpdatedProductH7New = new ProductH7New();
        partialUpdatedProductH7New.setId(productH7New.getId());

        partialUpdatedProductH7New.gtinUpc(UPDATED_GTIN_UPC).productName(UPDATED_PRODUCT_NAME);

        restProductH7NewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductH7New.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductH7New))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7New in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductH7NewUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductH7New, productH7New),
            getPersistedProductH7New(productH7New)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductH7NewWithPatch() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productH7New using partial update
        ProductH7New partialUpdatedProductH7New = new ProductH7New();
        partialUpdatedProductH7New.setId(productH7New.getId());

        partialUpdatedProductH7New
            .gtinUpc(UPDATED_GTIN_UPC)
            .h7KeywordId(UPDATED_H_7_KEYWORD_ID)
            .iOCGroup(UPDATED_I_OC_GROUP)
            .productH7Id(UPDATED_PRODUCT_H_7_ID)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME);

        restProductH7NewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductH7New.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductH7New))
            )
            .andExpect(status().isOk());

        // Validate the ProductH7New in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductH7NewUpdatableFieldsEquals(partialUpdatedProductH7New, getPersistedProductH7New(partialUpdatedProductH7New));
    }

    @Test
    @Transactional
    void patchNonExistingProductH7New() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        productH7New.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductH7NewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productH7New.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productH7New))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductH7New() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        productH7New.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7NewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productH7New))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductH7New() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        productH7New.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductH7NewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productH7New)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductH7New in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductH7New() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);
        productH7NewRepository.save(productH7New);
        productH7NewSearchRepository.save(productH7New);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productH7New
        restProductH7NewMockMvc
            .perform(delete(ENTITY_API_URL_ID, productH7New.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productH7NewSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductH7New() throws Exception {
        // Initialize the database
        insertedProductH7New = productH7NewRepository.saveAndFlush(productH7New);
        productH7NewSearchRepository.save(productH7New);

        // Search the productH7New
        restProductH7NewMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productH7New.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productH7New.getId().intValue())))
            .andExpect(jsonPath("$.[*].gtinUpc").value(hasItem(DEFAULT_GTIN_UPC)))
            .andExpect(jsonPath("$.[*].h7KeywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iOCGroup").value(hasItem(DEFAULT_I_OC_GROUP)))
            .andExpect(jsonPath("$.[*].productH7Id").value(hasItem(DEFAULT_PRODUCT_H_7_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)));
    }

    protected long getRepositoryCount() {
        return productH7NewRepository.count();
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

    protected ProductH7New getPersistedProductH7New(ProductH7New productH7New) {
        return productH7NewRepository.findById(productH7New.getId()).orElseThrow();
    }

    protected void assertPersistedProductH7NewToMatchAllProperties(ProductH7New expectedProductH7New) {
        assertProductH7NewAllPropertiesEquals(expectedProductH7New, getPersistedProductH7New(expectedProductH7New));
    }

    protected void assertPersistedProductH7NewToMatchUpdatableProperties(ProductH7New expectedProductH7New) {
        assertProductH7NewAllUpdatablePropertiesEquals(expectedProductH7New, getPersistedProductH7New(expectedProductH7New));
    }
}
