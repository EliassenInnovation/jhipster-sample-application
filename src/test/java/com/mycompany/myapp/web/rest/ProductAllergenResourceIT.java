package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductAllergenAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductAllergen;
import com.mycompany.myapp.repository.ProductAllergenRepository;
import com.mycompany.myapp.repository.search.ProductAllergenSearchRepository;
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
 * Integration tests for the {@link ProductAllergenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductAllergenResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-allergens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-allergens/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductAllergenRepository productAllergenRepository;

    @Autowired
    private ProductAllergenSearchRepository productAllergenSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAllergenMockMvc;

    private ProductAllergen productAllergen;

    private ProductAllergen insertedProductAllergen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAllergen createEntity() {
        return new ProductAllergen()
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
    public static ProductAllergen createUpdatedEntity() {
        return new ProductAllergen()
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
        productAllergen = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductAllergen != null) {
            productAllergenRepository.delete(insertedProductAllergen);
            productAllergenSearchRepository.delete(insertedProductAllergen);
            insertedProductAllergen = null;
        }
    }

    @Test
    @Transactional
    void createProductAllergen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        // Create the ProductAllergen
        var returnedProductAllergen = om.readValue(
            restProductAllergenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAllergen)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductAllergen.class
        );

        // Validate the ProductAllergen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductAllergenUpdatableFieldsEquals(returnedProductAllergen, getPersistedProductAllergen(returnedProductAllergen));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductAllergen = returnedProductAllergen;
    }

    @Test
    @Transactional
    void createProductAllergenWithExistingId() throws Exception {
        // Create the ProductAllergen with an existing ID
        productAllergen.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAllergenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAllergen)))
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductAllergens() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);

        // Get all the productAllergenList
        restProductAllergenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAllergen.getId().intValue())))
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
    void getProductAllergen() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);

        // Get the productAllergen
        restProductAllergenMockMvc
            .perform(get(ENTITY_API_URL_ID, productAllergen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAllergen.getId().intValue()))
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
    void getNonExistingProductAllergen() throws Exception {
        // Get the productAllergen
        restProductAllergenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductAllergen() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productAllergenSearchRepository.save(productAllergen);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());

        // Update the productAllergen
        ProductAllergen updatedProductAllergen = productAllergenRepository.findById(productAllergen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductAllergen are not directly saved in db
        em.detach(updatedProductAllergen);
        updatedProductAllergen
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .allergenName(UPDATED_ALLERGEN_NAME)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN)
            .gTINUPC(UPDATED_G_TINUPC)
            .productAllergenId(UPDATED_PRODUCT_ALLERGEN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC);

        restProductAllergenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductAllergen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductAllergen))
            )
            .andExpect(status().isOk());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductAllergenToMatchAllProperties(updatedProductAllergen);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductAllergen> productAllergenSearchList = Streamable.of(productAllergenSearchRepository.findAll()).toList();
                ProductAllergen testProductAllergenSearch = productAllergenSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductAllergenAllPropertiesEquals(testProductAllergenSearch, updatedProductAllergen);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductAllergen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        productAllergen.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAllergenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAllergen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAllergen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAllergen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        productAllergen.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productAllergen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAllergen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        productAllergen.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productAllergen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductAllergenWithPatch() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAllergen using partial update
        ProductAllergen partialUpdatedProductAllergen = new ProductAllergen();
        partialUpdatedProductAllergen.setId(productAllergen.getId());

        partialUpdatedProductAllergen
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .description(UPDATED_DESCRIPTION)
            .gTINUPC(UPDATED_G_TINUPC)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC);

        restProductAllergenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAllergen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAllergen))
            )
            .andExpect(status().isOk());

        // Validate the ProductAllergen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAllergenUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductAllergen, productAllergen),
            getPersistedProductAllergen(productAllergen)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductAllergenWithPatch() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productAllergen using partial update
        ProductAllergen partialUpdatedProductAllergen = new ProductAllergen();
        partialUpdatedProductAllergen.setId(productAllergen.getId());

        partialUpdatedProductAllergen
            .allergenId(UPDATED_ALLERGEN_ID)
            .allergenGroup(UPDATED_ALLERGEN_GROUP)
            .allergenName(UPDATED_ALLERGEN_NAME)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN)
            .gTINUPC(UPDATED_G_TINUPC)
            .productAllergenId(UPDATED_PRODUCT_ALLERGEN_ID)
            .productId(UPDATED_PRODUCT_ID)
            .uPC(UPDATED_U_PC);

        restProductAllergenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAllergen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductAllergen))
            )
            .andExpect(status().isOk());

        // Validate the ProductAllergen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductAllergenUpdatableFieldsEquals(
            partialUpdatedProductAllergen,
            getPersistedProductAllergen(partialUpdatedProductAllergen)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductAllergen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        productAllergen.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAllergenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAllergen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAllergen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAllergen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        productAllergen.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productAllergen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAllergen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        productAllergen.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAllergenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productAllergen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAllergen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductAllergen() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);
        productAllergenRepository.save(productAllergen);
        productAllergenSearchRepository.save(productAllergen);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productAllergen
        restProductAllergenMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAllergen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productAllergenSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductAllergen() throws Exception {
        // Initialize the database
        insertedProductAllergen = productAllergenRepository.saveAndFlush(productAllergen);
        productAllergenSearchRepository.save(productAllergen);

        // Search the productAllergen
        restProductAllergenMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productAllergen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAllergen.getId().intValue())))
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
        return productAllergenRepository.count();
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

    protected ProductAllergen getPersistedProductAllergen(ProductAllergen productAllergen) {
        return productAllergenRepository.findById(productAllergen.getId()).orElseThrow();
    }

    protected void assertPersistedProductAllergenToMatchAllProperties(ProductAllergen expectedProductAllergen) {
        assertProductAllergenAllPropertiesEquals(expectedProductAllergen, getPersistedProductAllergen(expectedProductAllergen));
    }

    protected void assertPersistedProductAllergenToMatchUpdatableProperties(ProductAllergen expectedProductAllergen) {
        assertProductAllergenAllUpdatablePropertiesEquals(expectedProductAllergen, getPersistedProductAllergen(expectedProductAllergen));
    }
}
