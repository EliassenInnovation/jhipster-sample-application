package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AllergenMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AllergenMst;
import com.mycompany.myapp.repository.AllergenMstRepository;
import com.mycompany.myapp.repository.search.AllergenMstSearchRepository;
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
 * Integration tests for the {@link AllergenMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllergenMstResourceIT {

    private static final String DEFAULT_ALLERGEN_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGEN_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_ALLERGEN_ID = 1;
    private static final Integer UPDATED_ALLERGEN_ID = 2;

    private static final String DEFAULT_ALLERGEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGEN_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/allergen-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/allergen-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllergenMstRepository allergenMstRepository;

    @Autowired
    private AllergenMstSearchRepository allergenMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllergenMstMockMvc;

    private AllergenMst allergenMst;

    private AllergenMst insertedAllergenMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllergenMst createEntity() {
        return new AllergenMst().allergenGroup(DEFAULT_ALLERGEN_GROUP).allergenId(DEFAULT_ALLERGEN_ID).allergenName(DEFAULT_ALLERGEN_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllergenMst createUpdatedEntity() {
        return new AllergenMst().allergenGroup(UPDATED_ALLERGEN_GROUP).allergenId(UPDATED_ALLERGEN_ID).allergenName(UPDATED_ALLERGEN_NAME);
    }

    @BeforeEach
    public void initTest() {
        allergenMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAllergenMst != null) {
            allergenMstRepository.delete(insertedAllergenMst);
            allergenMstSearchRepository.delete(insertedAllergenMst);
            insertedAllergenMst = null;
        }
    }

    @Test
    @Transactional
    void createAllergenMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        // Create the AllergenMst
        var returnedAllergenMst = om.readValue(
            restAllergenMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergenMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AllergenMst.class
        );

        // Validate the AllergenMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAllergenMstUpdatableFieldsEquals(returnedAllergenMst, getPersistedAllergenMst(returnedAllergenMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAllergenMst = returnedAllergenMst;
    }

    @Test
    @Transactional
    void createAllergenMstWithExistingId() throws Exception {
        // Create the AllergenMst with an existing ID
        allergenMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllergenMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergenMst)))
            .andExpect(status().isBadRequest());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAllergenMsts() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);

        // Get all the allergenMstList
        restAllergenMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allergenMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].allergenGroup").value(hasItem(DEFAULT_ALLERGEN_GROUP)))
            .andExpect(jsonPath("$.[*].allergenId").value(hasItem(DEFAULT_ALLERGEN_ID)))
            .andExpect(jsonPath("$.[*].allergenName").value(hasItem(DEFAULT_ALLERGEN_NAME)));
    }

    @Test
    @Transactional
    void getAllergenMst() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);

        // Get the allergenMst
        restAllergenMstMockMvc
            .perform(get(ENTITY_API_URL_ID, allergenMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allergenMst.getId().intValue()))
            .andExpect(jsonPath("$.allergenGroup").value(DEFAULT_ALLERGEN_GROUP))
            .andExpect(jsonPath("$.allergenId").value(DEFAULT_ALLERGEN_ID))
            .andExpect(jsonPath("$.allergenName").value(DEFAULT_ALLERGEN_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAllergenMst() throws Exception {
        // Get the allergenMst
        restAllergenMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllergenMst() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergenMstSearchRepository.save(allergenMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());

        // Update the allergenMst
        AllergenMst updatedAllergenMst = allergenMstRepository.findById(allergenMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllergenMst are not directly saved in db
        em.detach(updatedAllergenMst);
        updatedAllergenMst.allergenGroup(UPDATED_ALLERGEN_GROUP).allergenId(UPDATED_ALLERGEN_ID).allergenName(UPDATED_ALLERGEN_NAME);

        restAllergenMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAllergenMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAllergenMst))
            )
            .andExpect(status().isOk());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllergenMstToMatchAllProperties(updatedAllergenMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AllergenMst> allergenMstSearchList = Streamable.of(allergenMstSearchRepository.findAll()).toList();
                AllergenMst testAllergenMstSearch = allergenMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertAllergenMstAllPropertiesEquals(testAllergenMstSearch, updatedAllergenMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingAllergenMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        allergenMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergenMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allergenMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allergenMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllergenMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        allergenMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergenMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allergenMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllergenMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        allergenMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergenMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergenMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAllergenMstWithPatch() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergenMst using partial update
        AllergenMst partialUpdatedAllergenMst = new AllergenMst();
        partialUpdatedAllergenMst.setId(allergenMst.getId());

        partialUpdatedAllergenMst.allergenId(UPDATED_ALLERGEN_ID);

        restAllergenMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergenMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllergenMst))
            )
            .andExpect(status().isOk());

        // Validate the AllergenMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllergenMstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAllergenMst, allergenMst),
            getPersistedAllergenMst(allergenMst)
        );
    }

    @Test
    @Transactional
    void fullUpdateAllergenMstWithPatch() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergenMst using partial update
        AllergenMst partialUpdatedAllergenMst = new AllergenMst();
        partialUpdatedAllergenMst.setId(allergenMst.getId());

        partialUpdatedAllergenMst.allergenGroup(UPDATED_ALLERGEN_GROUP).allergenId(UPDATED_ALLERGEN_ID).allergenName(UPDATED_ALLERGEN_NAME);

        restAllergenMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergenMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllergenMst))
            )
            .andExpect(status().isOk());

        // Validate the AllergenMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllergenMstUpdatableFieldsEquals(partialUpdatedAllergenMst, getPersistedAllergenMst(partialUpdatedAllergenMst));
    }

    @Test
    @Transactional
    void patchNonExistingAllergenMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        allergenMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergenMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allergenMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allergenMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllergenMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        allergenMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergenMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allergenMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllergenMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        allergenMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergenMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allergenMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllergenMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAllergenMst() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);
        allergenMstRepository.save(allergenMst);
        allergenMstSearchRepository.save(allergenMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the allergenMst
        restAllergenMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, allergenMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(allergenMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAllergenMst() throws Exception {
        // Initialize the database
        insertedAllergenMst = allergenMstRepository.saveAndFlush(allergenMst);
        allergenMstSearchRepository.save(allergenMst);

        // Search the allergenMst
        restAllergenMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + allergenMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allergenMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].allergenGroup").value(hasItem(DEFAULT_ALLERGEN_GROUP)))
            .andExpect(jsonPath("$.[*].allergenId").value(hasItem(DEFAULT_ALLERGEN_ID)))
            .andExpect(jsonPath("$.[*].allergenName").value(hasItem(DEFAULT_ALLERGEN_NAME)));
    }

    protected long getRepositoryCount() {
        return allergenMstRepository.count();
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

    protected AllergenMst getPersistedAllergenMst(AllergenMst allergenMst) {
        return allergenMstRepository.findById(allergenMst.getId()).orElseThrow();
    }

    protected void assertPersistedAllergenMstToMatchAllProperties(AllergenMst expectedAllergenMst) {
        assertAllergenMstAllPropertiesEquals(expectedAllergenMst, getPersistedAllergenMst(expectedAllergenMst));
    }

    protected void assertPersistedAllergenMstToMatchUpdatableProperties(AllergenMst expectedAllergenMst) {
        assertAllergenMstAllUpdatablePropertiesEquals(expectedAllergenMst, getPersistedAllergenMst(expectedAllergenMst));
    }
}
