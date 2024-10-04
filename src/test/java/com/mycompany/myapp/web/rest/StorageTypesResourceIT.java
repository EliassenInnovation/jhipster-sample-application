package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.StorageTypesAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StorageTypes;
import com.mycompany.myapp.repository.StorageTypesRepository;
import com.mycompany.myapp.repository.search.StorageTypesSearchRepository;
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
 * Integration tests for the {@link StorageTypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StorageTypesResourceIT {

    private static final Integer DEFAULT_STORAGE_TYPE_ID = 1;
    private static final Integer UPDATED_STORAGE_TYPE_ID = 2;

    private static final String DEFAULT_STORAGE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/storage-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/storage-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StorageTypesRepository storageTypesRepository;

    @Autowired
    private StorageTypesSearchRepository storageTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStorageTypesMockMvc;

    private StorageTypes storageTypes;

    private StorageTypes insertedStorageTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageTypes createEntity() {
        return new StorageTypes().storageTypeId(DEFAULT_STORAGE_TYPE_ID).storageTypeName(DEFAULT_STORAGE_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StorageTypes createUpdatedEntity() {
        return new StorageTypes().storageTypeId(UPDATED_STORAGE_TYPE_ID).storageTypeName(UPDATED_STORAGE_TYPE_NAME);
    }

    @BeforeEach
    public void initTest() {
        storageTypes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStorageTypes != null) {
            storageTypesRepository.delete(insertedStorageTypes);
            storageTypesSearchRepository.delete(insertedStorageTypes);
            insertedStorageTypes = null;
        }
    }

    @Test
    @Transactional
    void createStorageTypes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        // Create the StorageTypes
        var returnedStorageTypes = om.readValue(
            restStorageTypesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageTypes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StorageTypes.class
        );

        // Validate the StorageTypes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStorageTypesUpdatableFieldsEquals(returnedStorageTypes, getPersistedStorageTypes(returnedStorageTypes));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedStorageTypes = returnedStorageTypes;
    }

    @Test
    @Transactional
    void createStorageTypesWithExistingId() throws Exception {
        // Create the StorageTypes with an existing ID
        storageTypes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStorageTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageTypes)))
            .andExpect(status().isBadRequest());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllStorageTypes() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);

        // Get all the storageTypesList
        restStorageTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].storageTypeId").value(hasItem(DEFAULT_STORAGE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].storageTypeName").value(hasItem(DEFAULT_STORAGE_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getStorageTypes() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);

        // Get the storageTypes
        restStorageTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, storageTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storageTypes.getId().intValue()))
            .andExpect(jsonPath("$.storageTypeId").value(DEFAULT_STORAGE_TYPE_ID))
            .andExpect(jsonPath("$.storageTypeName").value(DEFAULT_STORAGE_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStorageTypes() throws Exception {
        // Get the storageTypes
        restStorageTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStorageTypes() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        storageTypesSearchRepository.save(storageTypes);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());

        // Update the storageTypes
        StorageTypes updatedStorageTypes = storageTypesRepository.findById(storageTypes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStorageTypes are not directly saved in db
        em.detach(updatedStorageTypes);
        updatedStorageTypes.storageTypeId(UPDATED_STORAGE_TYPE_ID).storageTypeName(UPDATED_STORAGE_TYPE_NAME);

        restStorageTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStorageTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStorageTypes))
            )
            .andExpect(status().isOk());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStorageTypesToMatchAllProperties(updatedStorageTypes);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<StorageTypes> storageTypesSearchList = Streamable.of(storageTypesSearchRepository.findAll()).toList();
                StorageTypes testStorageTypesSearch = storageTypesSearchList.get(searchDatabaseSizeAfter - 1);

                assertStorageTypesAllPropertiesEquals(testStorageTypesSearch, updatedStorageTypes);
            });
    }

    @Test
    @Transactional
    void putNonExistingStorageTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        storageTypes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storageTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchStorageTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        storageTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(storageTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStorageTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        storageTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageTypesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(storageTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateStorageTypesWithPatch() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageTypes using partial update
        StorageTypes partialUpdatedStorageTypes = new StorageTypes();
        partialUpdatedStorageTypes.setId(storageTypes.getId());

        restStorageTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageTypes))
            )
            .andExpect(status().isOk());

        // Validate the StorageTypes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageTypesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStorageTypes, storageTypes),
            getPersistedStorageTypes(storageTypes)
        );
    }

    @Test
    @Transactional
    void fullUpdateStorageTypesWithPatch() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the storageTypes using partial update
        StorageTypes partialUpdatedStorageTypes = new StorageTypes();
        partialUpdatedStorageTypes.setId(storageTypes.getId());

        partialUpdatedStorageTypes.storageTypeId(UPDATED_STORAGE_TYPE_ID).storageTypeName(UPDATED_STORAGE_TYPE_NAME);

        restStorageTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStorageTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStorageTypes))
            )
            .andExpect(status().isOk());

        // Validate the StorageTypes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStorageTypesUpdatableFieldsEquals(partialUpdatedStorageTypes, getPersistedStorageTypes(partialUpdatedStorageTypes));
    }

    @Test
    @Transactional
    void patchNonExistingStorageTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        storageTypes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStorageTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storageTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStorageTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        storageTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(storageTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStorageTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        storageTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStorageTypesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(storageTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StorageTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteStorageTypes() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);
        storageTypesRepository.save(storageTypes);
        storageTypesSearchRepository.save(storageTypes);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the storageTypes
        restStorageTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, storageTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(storageTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchStorageTypes() throws Exception {
        // Initialize the database
        insertedStorageTypes = storageTypesRepository.saveAndFlush(storageTypes);
        storageTypesSearchRepository.save(storageTypes);

        // Search the storageTypes
        restStorageTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + storageTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storageTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].storageTypeId").value(hasItem(DEFAULT_STORAGE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].storageTypeName").value(hasItem(DEFAULT_STORAGE_TYPE_NAME)));
    }

    protected long getRepositoryCount() {
        return storageTypesRepository.count();
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

    protected StorageTypes getPersistedStorageTypes(StorageTypes storageTypes) {
        return storageTypesRepository.findById(storageTypes.getId()).orElseThrow();
    }

    protected void assertPersistedStorageTypesToMatchAllProperties(StorageTypes expectedStorageTypes) {
        assertStorageTypesAllPropertiesEquals(expectedStorageTypes, getPersistedStorageTypes(expectedStorageTypes));
    }

    protected void assertPersistedStorageTypesToMatchUpdatableProperties(StorageTypes expectedStorageTypes) {
        assertStorageTypesAllUpdatablePropertiesEquals(expectedStorageTypes, getPersistedStorageTypes(expectedStorageTypes));
    }
}
