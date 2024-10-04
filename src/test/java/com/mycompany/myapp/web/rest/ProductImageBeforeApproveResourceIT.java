package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductImageBeforeApproveAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductImageBeforeApprove;
import com.mycompany.myapp.repository.ProductImageBeforeApproveRepository;
import com.mycompany.myapp.repository.search.ProductImageBeforeApproveSearchRepository;
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
 * Integration tests for the {@link ProductImageBeforeApproveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductImageBeforeApproveResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Integer DEFAULT_PRODUCT_IMAGE_ID = 1;
    private static final Integer UPDATED_PRODUCT_IMAGE_ID = 2;

    private static final String ENTITY_API_URL = "/api/product-image-before-approves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-image-before-approves/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductImageBeforeApproveRepository productImageBeforeApproveRepository;

    @Autowired
    private ProductImageBeforeApproveSearchRepository productImageBeforeApproveSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductImageBeforeApproveMockMvc;

    private ProductImageBeforeApprove productImageBeforeApprove;

    private ProductImageBeforeApprove insertedProductImageBeforeApprove;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductImageBeforeApprove createEntity() {
        return new ProductImageBeforeApprove()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .imageURL(DEFAULT_IMAGE_URL)
            .isActive(DEFAULT_IS_ACTIVE)
            .productId(DEFAULT_PRODUCT_ID)
            .productImageId(DEFAULT_PRODUCT_IMAGE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductImageBeforeApprove createUpdatedEntity() {
        return new ProductImageBeforeApprove()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .imageURL(UPDATED_IMAGE_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .productImageId(UPDATED_PRODUCT_IMAGE_ID);
    }

    @BeforeEach
    public void initTest() {
        productImageBeforeApprove = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductImageBeforeApprove != null) {
            productImageBeforeApproveRepository.delete(insertedProductImageBeforeApprove);
            productImageBeforeApproveSearchRepository.delete(insertedProductImageBeforeApprove);
            insertedProductImageBeforeApprove = null;
        }
    }

    @Test
    @Transactional
    void createProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        // Create the ProductImageBeforeApprove
        var returnedProductImageBeforeApprove = om.readValue(
            restProductImageBeforeApproveMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productImageBeforeApprove))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductImageBeforeApprove.class
        );

        // Validate the ProductImageBeforeApprove in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductImageBeforeApproveUpdatableFieldsEquals(
            returnedProductImageBeforeApprove,
            getPersistedProductImageBeforeApprove(returnedProductImageBeforeApprove)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductImageBeforeApprove = returnedProductImageBeforeApprove;
    }

    @Test
    @Transactional
    void createProductImageBeforeApproveWithExistingId() throws Exception {
        // Create the ProductImageBeforeApprove with an existing ID
        productImageBeforeApprove.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductImageBeforeApproveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productImageBeforeApprove)))
            .andExpect(status().isBadRequest());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductImageBeforeApproves() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);

        // Get all the productImageBeforeApproveList
        restProductImageBeforeApproveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productImageBeforeApprove.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].imageURL").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productImageId").value(hasItem(DEFAULT_PRODUCT_IMAGE_ID)));
    }

    @Test
    @Transactional
    void getProductImageBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);

        // Get the productImageBeforeApprove
        restProductImageBeforeApproveMockMvc
            .perform(get(ENTITY_API_URL_ID, productImageBeforeApprove.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productImageBeforeApprove.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.imageURL").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productImageId").value(DEFAULT_PRODUCT_IMAGE_ID));
    }

    @Test
    @Transactional
    void getNonExistingProductImageBeforeApprove() throws Exception {
        // Get the productImageBeforeApprove
        restProductImageBeforeApproveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductImageBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productImageBeforeApproveSearchRepository.save(productImageBeforeApprove);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());

        // Update the productImageBeforeApprove
        ProductImageBeforeApprove updatedProductImageBeforeApprove = productImageBeforeApproveRepository
            .findById(productImageBeforeApprove.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductImageBeforeApprove are not directly saved in db
        em.detach(updatedProductImageBeforeApprove);
        updatedProductImageBeforeApprove
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .imageURL(UPDATED_IMAGE_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .productImageId(UPDATED_PRODUCT_IMAGE_ID);

        restProductImageBeforeApproveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductImageBeforeApprove.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductImageBeforeApprove))
            )
            .andExpect(status().isOk());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductImageBeforeApproveToMatchAllProperties(updatedProductImageBeforeApprove);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductImageBeforeApprove> productImageBeforeApproveSearchList = Streamable.of(
                    productImageBeforeApproveSearchRepository.findAll()
                ).toList();
                ProductImageBeforeApprove testProductImageBeforeApproveSearch = productImageBeforeApproveSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertProductImageBeforeApproveAllPropertiesEquals(testProductImageBeforeApproveSearch, updatedProductImageBeforeApprove);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        productImageBeforeApprove.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductImageBeforeApproveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productImageBeforeApprove.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productImageBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        productImageBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageBeforeApproveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productImageBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        productImageBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageBeforeApproveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productImageBeforeApprove)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductImageBeforeApproveWithPatch() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productImageBeforeApprove using partial update
        ProductImageBeforeApprove partialUpdatedProductImageBeforeApprove = new ProductImageBeforeApprove();
        partialUpdatedProductImageBeforeApprove.setId(productImageBeforeApprove.getId());

        partialUpdatedProductImageBeforeApprove
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .productImageId(UPDATED_PRODUCT_IMAGE_ID);

        restProductImageBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductImageBeforeApprove.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductImageBeforeApprove))
            )
            .andExpect(status().isOk());

        // Validate the ProductImageBeforeApprove in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductImageBeforeApproveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductImageBeforeApprove, productImageBeforeApprove),
            getPersistedProductImageBeforeApprove(productImageBeforeApprove)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductImageBeforeApproveWithPatch() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productImageBeforeApprove using partial update
        ProductImageBeforeApprove partialUpdatedProductImageBeforeApprove = new ProductImageBeforeApprove();
        partialUpdatedProductImageBeforeApprove.setId(productImageBeforeApprove.getId());

        partialUpdatedProductImageBeforeApprove
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .imageURL(UPDATED_IMAGE_URL)
            .isActive(UPDATED_IS_ACTIVE)
            .productId(UPDATED_PRODUCT_ID)
            .productImageId(UPDATED_PRODUCT_IMAGE_ID);

        restProductImageBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductImageBeforeApprove.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductImageBeforeApprove))
            )
            .andExpect(status().isOk());

        // Validate the ProductImageBeforeApprove in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductImageBeforeApproveUpdatableFieldsEquals(
            partialUpdatedProductImageBeforeApprove,
            getPersistedProductImageBeforeApprove(partialUpdatedProductImageBeforeApprove)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        productImageBeforeApprove.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductImageBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productImageBeforeApprove.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productImageBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        productImageBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productImageBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductImageBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        productImageBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productImageBeforeApprove))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductImageBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductImageBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);
        productImageBeforeApproveRepository.save(productImageBeforeApprove);
        productImageBeforeApproveSearchRepository.save(productImageBeforeApprove);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productImageBeforeApprove
        restProductImageBeforeApproveMockMvc
            .perform(delete(ENTITY_API_URL_ID, productImageBeforeApprove.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productImageBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductImageBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductImageBeforeApprove = productImageBeforeApproveRepository.saveAndFlush(productImageBeforeApprove);
        productImageBeforeApproveSearchRepository.save(productImageBeforeApprove);

        // Search the productImageBeforeApprove
        restProductImageBeforeApproveMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productImageBeforeApprove.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productImageBeforeApprove.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].imageURL").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productImageId").value(hasItem(DEFAULT_PRODUCT_IMAGE_ID)));
    }

    protected long getRepositoryCount() {
        return productImageBeforeApproveRepository.count();
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

    protected ProductImageBeforeApprove getPersistedProductImageBeforeApprove(ProductImageBeforeApprove productImageBeforeApprove) {
        return productImageBeforeApproveRepository.findById(productImageBeforeApprove.getId()).orElseThrow();
    }

    protected void assertPersistedProductImageBeforeApproveToMatchAllProperties(
        ProductImageBeforeApprove expectedProductImageBeforeApprove
    ) {
        assertProductImageBeforeApproveAllPropertiesEquals(
            expectedProductImageBeforeApprove,
            getPersistedProductImageBeforeApprove(expectedProductImageBeforeApprove)
        );
    }

    protected void assertPersistedProductImageBeforeApproveToMatchUpdatableProperties(
        ProductImageBeforeApprove expectedProductImageBeforeApprove
    ) {
        assertProductImageBeforeApproveAllUpdatablePropertiesEquals(
            expectedProductImageBeforeApprove,
            getPersistedProductImageBeforeApprove(expectedProductImageBeforeApprove)
        );
    }
}
