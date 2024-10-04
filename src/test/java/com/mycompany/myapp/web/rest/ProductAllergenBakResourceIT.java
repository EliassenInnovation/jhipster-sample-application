package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductAllergenBakAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductAllergenBak;
import com.mycompany.myapp.repository.ProductAllergenBakRepository;
import com.mycompany.myapp.repository.search.ProductAllergenBakSearchRepository;
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
 * Integration tests for the {@link ProductAllergenBakResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductAllergenBakResourceIT {

    private static final Integer DEFAULT_ALLERGEN_ID = 1;
    private static final Integer UPDATED_ALLERGEN_ID = 2;

    private static final String DEFAULT_ALLERGEN_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGEN_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_ALLERGEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGEN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_G_TIN = "AAAAAAAAAA";
    private static final String UPDATED_G_TIN = "BBBBBBBBBB";

    private static final String DEFAULT_G_TINUPC = "AAAAAAAAAA";
    private static final String UPDATED_G_TINUPC = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_ALLERGEN_ID = 1;
    private static final Integer UPDATED_PRODUCT_ALLERGEN_ID = 2;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_U_PC = "AAAAAAAAAA";
    private static final String UPDATED_U_PC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-allergen-baks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-allergen-baks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductAllergenBakRepository productAllergenBakRepository;

    @Autowired
    private ProductAllergenBakSearchRepository productAllergenBakSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAllergenBakMockMvc;

    private ProductAllergenBak productAllergenBak;

    private ProductAllergenBak insertedProductAllergenBak;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAllergenBak createEntity() {
        return new ProductAllergenBak()
            .allergenId(DEFAULT_ALLERGEN_ID)
            .allergenGroup(DEFAULT_ALLERGEN_GROUP)
            .allergenName(DEFAULT_ALLERGEN_NAME)
            .description(DEFAULT_DESCRIPTION)
            .gTIN(DEFAULT_G_TIN)
            .gTINUPC(DEFAULT_G_TINUPC)
            .productAllergenId(DEFAULT_PRODUCT_ALLERGEN_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .uPC(DEFAULT_U_PC);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAllergenBak createUpdatedEntity() {
        return new ProductAllergenBak()
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .allergenName(UPDATED_ALLERGEN_NAME)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN)
            .gTINUPC(UPDATED_G_TINUPC)
            .productAllergenId(UPDATED_PRODUCT_ALLERGEN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC);
    }

    @BeforeEach
    public void initTest() {
        productAllergenBak = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductAllergenBak != null) {
            productAllergenBakRepository.delete(insertedProductAllergenBak);
            productAllergenBakSearchRepository.delete(insertedProductAllergenBak);
            insertedProductAllergenBak = null;
        }
    }

    @Test
    @Transactional
    void createProductAllergenBak() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        // Create the ProductAllergenBak
        var returnedProductAllergenBak = om.readValue(
            restProductAllergenBakMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAllergenBak)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductAllergenBak.class
        );

        // Validate the ProductAllergenBak in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductAllergenBakUpdatableFieldsEquals(
            returnedProductAllergenBak,
            getPersistedProductAllergenBak(returnedProductAllergenBak)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductAllergenBak = returnedProductAllergenBak;
    }

    @Test
    @Transactional
    void createProductAllergenBakWithExistingId() throws Exception {
        // Create the ProductAllergenBak with an existing ID
        productAllergenBak.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAllergenBakMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAllergenBak)))
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductAllergenBaks() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);

        // Get all the productAllergenBakList
        restProductAllergenBakMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAllergenBak.getId().intValue())))
            .andExpect(jsonPath("$.[*].allergenId").value(hasItem(DEFAULT_ALLERGEN_ID)))
            .andExpect(jsonPath("$.[*].allergenGroup").value(hasItem(DEFAULT_ALLERGEN_GROUP)))
            .andExpect(jsonPath("$.[*].allergenName").value(hasItem(DEFAULT_ALLERGEN_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].gTINUPC").value(hasItem(DEFAULT_G_TINUPC)))
            .andExpect(jsonPath("$.[*].productAllergenId").value(hasItem(DEFAULT_PRODUCT_ALLERGEN_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)));
    }

    @Test
    @Transactional
    void getProductAllergenBak() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);

        // Get the productAllergenBak
        restProductAllergenBakMockMvc
            .perform(get(ENTITY_API_URL_ID, productAllergenBak.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAllergenBak.getId().intValue()))
            .andExpect(jsonPath("$.allergenId").value(DEFAULT_ALLERGEN_ID))
            .andExpect(jsonPath("$.allergenGroup").value(DEFAULT_ALLERGEN_GROUP))
            .andExpect(jsonPath("$.allergenName").value(DEFAULT_ALLERGEN_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.gTIN").value(DEFAULT_G_TIN))
            .andExpect(jsonPath("$.gTINUPC").value(DEFAULT_G_TINUPC))
            .andExpect(jsonPath("$.productAllergenId").value(DEFAULT_PRODUCT_ALLERGEN_ID))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.uPC").value(DEFAULT_U_PC));
    }

    @Test
    @Transactional
    void getNonExistingProductAllergenBak() throws Exception {
        // Get the productAllergenBak
        restProductAllergenBakMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductAllergenBak() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAllergenBakSearchRepository.save(productAllergenBak);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());

        // Update the productAllergenBak
        ProductAllergenBak updatedProductAllergenBak = productAllergenBakRepository.findById(productAllergenBak.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductAllergenBak are not directly saved in db
        em.detach(updatedProductAllergenBak);
        updatedProductAllergenBak
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .allergenName(UPDATED_ALLERGEN_NAME)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN)
            .gTINUPC(UPDATED_G_TINUPC)
            .productAllergenId(UPDATED_PRODUCT_ALLERGEN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC);

        restProductAllergenBakMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductAllergenBak.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductAllergenBak))
            )
            .andExpect(status().isOk());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductAllergenBakToMatchAllProperties(updatedProductAllergenBak);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductAllergenBak> productAllergenBakSearchList = Streamable.of(
                    productAllergenBakSearchRepository.findAll()
                ).toList();
                ProductAllergenBak testProductAllergenBakSearch = productAllergenBakSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductAllergenBakAllPropertiesEquals(testProductAllergenBakSearch, updatedProductAllergenBak);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductAllergenBak() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        productAllergenBak.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAllergenBakMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAllergenBak.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAllergenBak))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAllergenBak() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        productAllergenBak.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenBakMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAllergenBak))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAllergenBak() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        productAllergenBak.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenBakMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAllergenBak)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductAllergenBakWithPatch() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAllergenBak using partial update
        ProductAllergenBak partialUpdatedProductAllergenBak = new ProductAllergenBak();
        partialUpdatedProductAllergenBak.setId(productAllergenBak.getId());

        partialUpdatedProductAllergenBak
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN);

        restProductAllergenBakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAllergenBak.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAllergenBak))
            )
            .andExpect(status().isOk());

        // Validate the ProductAllergenBak in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAllergenBakUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductAllergenBak, productAllergenBak),
            getPersistedProductAllergenBak(productAllergenBak)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductAllergenBakWithPatch() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAllergenBak using partial update
        ProductAllergenBak partialUpdatedProductAllergenBak = new ProductAllergenBak();
        partialUpdatedProductAllergenBak.setId(productAllergenBak.getId());

        partialUpdatedProductAllergenBak
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .allergenName(UPDATED_ALLERGEN_NAME)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN)
            .gTINUPC(UPDATED_G_TINUPC)
            .productAllergenId(UPDATED_PRODUCT_ALLERGEN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC);

        restProductAllergenBakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAllergenBak.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAllergenBak))
            )
            .andExpect(status().isOk());

        // Validate the ProductAllergenBak in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAllergenBakUpdatableFieldsEquals(
            partialUpdatedProductAllergenBak,
            getPersistedProductAllergenBak(partialUpdatedProductAllergenBak)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductAllergenBak() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        productAllergenBak.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAllergenBakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAllergenBak.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAllergenBak))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAllergenBak() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        productAllergenBak.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenBakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAllergenBak))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAllergenBak() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        productAllergenBak.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenBakMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productAllergenBak)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAllergenBak in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductAllergenBak() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);
        productAllergenBakRepository.save(productAllergenBak);
        productAllergenBakSearchRepository.save(productAllergenBak);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productAllergenBak
        restProductAllergenBakMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAllergenBak.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenBakSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductAllergenBak() throws Exception {
        // Initialize the database
        insertedProductAllergenBak = productAllergenBakRepository.saveAndFlush(productAllergenBak);
        productAllergenBakSearchRepository.save(productAllergenBak);

        // Search the productAllergenBak
        restProductAllergenBakMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productAllergenBak.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAllergenBak.getId().intValue())))
            .andExpect(jsonPath("$.[*].allergenId").value(hasItem(DEFAULT_ALLERGEN_ID)))
            .andExpect(jsonPath("$.[*].allergenGroup").value(hasItem(DEFAULT_ALLERGEN_GROUP)))
            .andExpect(jsonPath("$.[*].allergenName").value(hasItem(DEFAULT_ALLERGEN_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].gTINUPC").value(hasItem(DEFAULT_G_TINUPC)))
            .andExpect(jsonPath("$.[*].productAllergenId").value(hasItem(DEFAULT_PRODUCT_ALLERGEN_ID)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)));
    }

    protected long getRepositoryCount() {
        return productAllergenBakRepository.count();
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

    protected ProductAllergenBak getPersistedProductAllergenBak(ProductAllergenBak productAllergenBak) {
        return productAllergenBakRepository.findById(productAllergenBak.getId()).orElseThrow();
    }

    protected void assertPersistedProductAllergenBakToMatchAllProperties(ProductAllergenBak expectedProductAllergenBak) {
        assertProductAllergenBakAllPropertiesEquals(expectedProductAllergenBak, getPersistedProductAllergenBak(expectedProductAllergenBak));
    }

    protected void assertPersistedProductAllergenBakToMatchUpdatableProperties(ProductAllergenBak expectedProductAllergenBak) {
        assertProductAllergenBakAllUpdatablePropertiesEquals(
            expectedProductAllergenBak,
            getPersistedProductAllergenBak(expectedProductAllergenBak)
        );
    }
}
