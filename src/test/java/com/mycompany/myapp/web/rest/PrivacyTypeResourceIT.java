package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PrivacyTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PrivacyType;
import com.mycompany.myapp.repository.PrivacyTypeRepository;
import com.mycompany.myapp.repository.search.PrivacyTypeSearchRepository;
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
 * Integration tests for the {@link PrivacyTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrivacyTypeResourceIT {

    private static final Integer DEFAULT_PRIVACY_TYPE_ID = 1;
    private static final Integer UPDATED_PRIVACY_TYPE_ID = 2;

    private static final String DEFAULT_PRIVACY_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRIVACY_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/privacy-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/privacy-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrivacyTypeRepository privacyTypeRepository;

    @Autowired
    private PrivacyTypeSearchRepository privacyTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrivacyTypeMockMvc;

    private PrivacyType privacyType;

    private PrivacyType insertedPrivacyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrivacyType createEntity() {
        return new PrivacyType().privacyTypeId(DEFAULT_PRIVACY_TYPE_ID).privacyTypeName(DEFAULT_PRIVACY_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrivacyType createUpdatedEntity() {
        return new PrivacyType().privacyTypeId(UPDATED_PRIVACY_TYPE_ID).privacyTypeName(UPDATED_PRIVACY_TYPE_NAME);
    }

    @BeforeEach
    public void initTest() {
        privacyType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPrivacyType != null) {
            privacyTypeRepository.delete(insertedPrivacyType);
            privacyTypeSearchRepository.delete(insertedPrivacyType);
            insertedPrivacyType = null;
        }
    }

    @Test
    @Transactional
    void createPrivacyType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        // Create the PrivacyType
        var returnedPrivacyType = om.readValue(
            restPrivacyTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privacyType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrivacyType.class
        );

        // Validate the PrivacyType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPrivacyTypeUpdatableFieldsEquals(returnedPrivacyType, getPersistedPrivacyType(returnedPrivacyType));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPrivacyType = returnedPrivacyType;
    }

    @Test
    @Transactional
    void createPrivacyTypeWithExistingId() throws Exception {
        // Create the PrivacyType with an existing ID
        privacyType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivacyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privacyType)))
            .andExpect(status().isBadRequest());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPrivacyTypes() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);

        // Get all the privacyTypeList
        restPrivacyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privacyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].privacyTypeId").value(hasItem(DEFAULT_PRIVACY_TYPE_ID)))
            .andExpect(jsonPath("$.[*].privacyTypeName").value(hasItem(DEFAULT_PRIVACY_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getPrivacyType() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);

        // Get the privacyType
        restPrivacyTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, privacyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(privacyType.getId().intValue()))
            .andExpect(jsonPath("$.privacyTypeId").value(DEFAULT_PRIVACY_TYPE_ID))
            .andExpect(jsonPath("$.privacyTypeName").value(DEFAULT_PRIVACY_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPrivacyType() throws Exception {
        // Get the privacyType
        restPrivacyTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrivacyType() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        privacyTypeSearchRepository.save(privacyType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());

        // Update the privacyType
        PrivacyType updatedPrivacyType = privacyTypeRepository.findById(privacyType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrivacyType are not directly saved in db
        em.detach(updatedPrivacyType);
        updatedPrivacyType.privacyTypeId(UPDATED_PRIVACY_TYPE_ID).privacyTypeName(UPDATED_PRIVACY_TYPE_NAME);

        restPrivacyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrivacyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPrivacyType))
            )
            .andExpect(status().isOk());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrivacyTypeToMatchAllProperties(updatedPrivacyType);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PrivacyType> privacyTypeSearchList = Streamable.of(privacyTypeSearchRepository.findAll()).toList();
                PrivacyType testPrivacyTypeSearch = privacyTypeSearchList.get(searchDatabaseSizeAfter - 1);

                assertPrivacyTypeAllPropertiesEquals(testPrivacyTypeSearch, updatedPrivacyType);
            });
    }

    @Test
    @Transactional
    void putNonExistingPrivacyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        privacyType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivacyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, privacyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(privacyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrivacyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        privacyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(privacyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrivacyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        privacyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(privacyType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePrivacyTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the privacyType using partial update
        PrivacyType partialUpdatedPrivacyType = new PrivacyType();
        partialUpdatedPrivacyType.setId(privacyType.getId());

        restPrivacyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrivacyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrivacyType))
            )
            .andExpect(status().isOk());

        // Validate the PrivacyType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrivacyTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrivacyType, privacyType),
            getPersistedPrivacyType(privacyType)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrivacyTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the privacyType using partial update
        PrivacyType partialUpdatedPrivacyType = new PrivacyType();
        partialUpdatedPrivacyType.setId(privacyType.getId());

        partialUpdatedPrivacyType.privacyTypeId(UPDATED_PRIVACY_TYPE_ID).privacyTypeName(UPDATED_PRIVACY_TYPE_NAME);

        restPrivacyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrivacyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrivacyType))
            )
            .andExpect(status().isOk());

        // Validate the PrivacyType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrivacyTypeUpdatableFieldsEquals(partialUpdatedPrivacyType, getPersistedPrivacyType(partialUpdatedPrivacyType));
    }

    @Test
    @Transactional
    void patchNonExistingPrivacyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        privacyType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivacyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, privacyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(privacyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrivacyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        privacyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(privacyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrivacyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        privacyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(privacyType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrivacyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePrivacyType() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);
        privacyTypeRepository.save(privacyType);
        privacyTypeSearchRepository.save(privacyType);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the privacyType
        restPrivacyTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, privacyType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(privacyTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPrivacyType() throws Exception {
        // Initialize the database
        insertedPrivacyType = privacyTypeRepository.saveAndFlush(privacyType);
        privacyTypeSearchRepository.save(privacyType);

        // Search the privacyType
        restPrivacyTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + privacyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privacyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].privacyTypeId").value(hasItem(DEFAULT_PRIVACY_TYPE_ID)))
            .andExpect(jsonPath("$.[*].privacyTypeName").value(hasItem(DEFAULT_PRIVACY_TYPE_NAME)));
    }

    protected long getRepositoryCount() {
        return privacyTypeRepository.count();
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

    protected PrivacyType getPersistedPrivacyType(PrivacyType privacyType) {
        return privacyTypeRepository.findById(privacyType.getId()).orElseThrow();
    }

    protected void assertPersistedPrivacyTypeToMatchAllProperties(PrivacyType expectedPrivacyType) {
        assertPrivacyTypeAllPropertiesEquals(expectedPrivacyType, getPersistedPrivacyType(expectedPrivacyType));
    }

    protected void assertPersistedPrivacyTypeToMatchUpdatableProperties(PrivacyType expectedPrivacyType) {
        assertPrivacyTypeAllUpdatablePropertiesEquals(expectedPrivacyType, getPersistedPrivacyType(expectedPrivacyType));
    }
}
