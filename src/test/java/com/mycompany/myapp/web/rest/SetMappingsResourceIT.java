package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SetMappingsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SetMappings;
import com.mycompany.myapp.repository.SetMappingsRepository;
import com.mycompany.myapp.repository.search.SetMappingsSearchRepository;
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
 * Integration tests for the {@link SetMappingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SetMappingsResourceIT {

    private static final Integer DEFAULT_I_D = 1;
    private static final Integer UPDATED_I_D = 2;

    private static final String DEFAULT_ONE_WORLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ONE_WORLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_SET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SET_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/set-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/set-mappings/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SetMappingsRepository setMappingsRepository;

    @Autowired
    private SetMappingsSearchRepository setMappingsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSetMappingsMockMvc;

    private SetMappings setMappings;

    private SetMappings insertedSetMappings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SetMappings createEntity() {
        return new SetMappings()
            .iD(DEFAULT_I_D)
            .oneWorldValue(DEFAULT_ONE_WORLD_VALUE)
            .productValue(DEFAULT_PRODUCT_VALUE)
            .setName(DEFAULT_SET_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SetMappings createUpdatedEntity() {
        return new SetMappings()
            .iD(UPDATED_I_D)
            .oneWorldValue(UPDATED_ONE_WORLD_VALUE)
            .productValue(UPDATED_PRODUCT_VALUE)
            .setName(UPDATED_SET_NAME);
    }

    @BeforeEach
    public void initTest() {
        setMappings = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSetMappings != null) {
            setMappingsRepository.delete(insertedSetMappings);
            setMappingsSearchRepository.delete(insertedSetMappings);
            insertedSetMappings = null;
        }
    }

    @Test
    @Transactional
    void createSetMappings() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        // Create the SetMappings
        var returnedSetMappings = om.readValue(
            restSetMappingsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setMappings)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SetMappings.class
        );

        // Validate the SetMappings in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSetMappingsUpdatableFieldsEquals(returnedSetMappings, getPersistedSetMappings(returnedSetMappings));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSetMappings = returnedSetMappings;
    }

    @Test
    @Transactional
    void createSetMappingsWithExistingId() throws Exception {
        // Create the SetMappings with an existing ID
        setMappings.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSetMappingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setMappings)))
            .andExpect(status().isBadRequest());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSetMappings() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);

        // Get all the setMappingsList
        restSetMappingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setMappings.getId().intValue())))
            .andExpect(jsonPath("$.[*].iD").value(hasItem(DEFAULT_I_D)))
            .andExpect(jsonPath("$.[*].oneWorldValue").value(hasItem(DEFAULT_ONE_WORLD_VALUE)))
            .andExpect(jsonPath("$.[*].productValue").value(hasItem(DEFAULT_PRODUCT_VALUE)))
            .andExpect(jsonPath("$.[*].setName").value(hasItem(DEFAULT_SET_NAME)));
    }

    @Test
    @Transactional
    void getSetMappings() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);

        // Get the setMappings
        restSetMappingsMockMvc
            .perform(get(ENTITY_API_URL_ID, setMappings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(setMappings.getId().intValue()))
            .andExpect(jsonPath("$.iD").value(DEFAULT_I_D))
            .andExpect(jsonPath("$.oneWorldValue").value(DEFAULT_ONE_WORLD_VALUE))
            .andExpect(jsonPath("$.productValue").value(DEFAULT_PRODUCT_VALUE))
            .andExpect(jsonPath("$.setName").value(DEFAULT_SET_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSetMappings() throws Exception {
        // Get the setMappings
        restSetMappingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSetMappings() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        setMappingsSearchRepository.save(setMappings);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());

        // Update the setMappings
        SetMappings updatedSetMappings = setMappingsRepository.findById(setMappings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSetMappings are not directly saved in db
        em.detach(updatedSetMappings);
        updatedSetMappings
            .iD(UPDATED_I_D)
            .oneWorldValue(UPDATED_ONE_WORLD_VALUE)
            .productValue(UPDATED_PRODUCT_VALUE)
            .setName(UPDATED_SET_NAME);

        restSetMappingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSetMappings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSetMappings))
            )
            .andExpect(status().isOk());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSetMappingsToMatchAllProperties(updatedSetMappings);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SetMappings> setMappingsSearchList = Streamable.of(setMappingsSearchRepository.findAll()).toList();
                SetMappings testSetMappingsSearch = setMappingsSearchList.get(searchDatabaseSizeAfter - 1);

                assertSetMappingsAllPropertiesEquals(testSetMappingsSearch, updatedSetMappings);
            });
    }

    @Test
    @Transactional
    void putNonExistingSetMappings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        setMappings.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetMappingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, setMappings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(setMappings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSetMappings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        setMappings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetMappingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(setMappings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSetMappings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        setMappings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetMappingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setMappings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSetMappingsWithPatch() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setMappings using partial update
        SetMappings partialUpdatedSetMappings = new SetMappings();
        partialUpdatedSetMappings.setId(setMappings.getId());

        partialUpdatedSetMappings.iD(UPDATED_I_D).productValue(UPDATED_PRODUCT_VALUE);

        restSetMappingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetMappings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSetMappings))
            )
            .andExpect(status().isOk());

        // Validate the SetMappings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSetMappingsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSetMappings, setMappings),
            getPersistedSetMappings(setMappings)
        );
    }

    @Test
    @Transactional
    void fullUpdateSetMappingsWithPatch() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setMappings using partial update
        SetMappings partialUpdatedSetMappings = new SetMappings();
        partialUpdatedSetMappings.setId(setMappings.getId());

        partialUpdatedSetMappings
            .iD(UPDATED_I_D)
            .oneWorldValue(UPDATED_ONE_WORLD_VALUE)
            .productValue(UPDATED_PRODUCT_VALUE)
            .setName(UPDATED_SET_NAME);

        restSetMappingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetMappings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSetMappings))
            )
            .andExpect(status().isOk());

        // Validate the SetMappings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSetMappingsUpdatableFieldsEquals(partialUpdatedSetMappings, getPersistedSetMappings(partialUpdatedSetMappings));
    }

    @Test
    @Transactional
    void patchNonExistingSetMappings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        setMappings.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetMappingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, setMappings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(setMappings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSetMappings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        setMappings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetMappingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(setMappings))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSetMappings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        setMappings.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetMappingsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(setMappings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SetMappings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSetMappings() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);
        setMappingsRepository.save(setMappings);
        setMappingsSearchRepository.save(setMappings);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the setMappings
        restSetMappingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, setMappings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setMappingsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSetMappings() throws Exception {
        // Initialize the database
        insertedSetMappings = setMappingsRepository.saveAndFlush(setMappings);
        setMappingsSearchRepository.save(setMappings);

        // Search the setMappings
        restSetMappingsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + setMappings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setMappings.getId().intValue())))
            .andExpect(jsonPath("$.[*].iD").value(hasItem(DEFAULT_I_D)))
            .andExpect(jsonPath("$.[*].oneWorldValue").value(hasItem(DEFAULT_ONE_WORLD_VALUE)))
            .andExpect(jsonPath("$.[*].productValue").value(hasItem(DEFAULT_PRODUCT_VALUE)))
            .andExpect(jsonPath("$.[*].setName").value(hasItem(DEFAULT_SET_NAME)));
    }

    protected long getRepositoryCount() {
        return setMappingsRepository.count();
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

    protected SetMappings getPersistedSetMappings(SetMappings setMappings) {
        return setMappingsRepository.findById(setMappings.getId()).orElseThrow();
    }

    protected void assertPersistedSetMappingsToMatchAllProperties(SetMappings expectedSetMappings) {
        assertSetMappingsAllPropertiesEquals(expectedSetMappings, getPersistedSetMappings(expectedSetMappings));
    }

    protected void assertPersistedSetMappingsToMatchUpdatableProperties(SetMappings expectedSetMappings) {
        assertSetMappingsAllUpdatablePropertiesEquals(expectedSetMappings, getPersistedSetMappings(expectedSetMappings));
    }
}
