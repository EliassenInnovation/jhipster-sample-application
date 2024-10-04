package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.StateInfoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StateInfo;
import com.mycompany.myapp.repository.StateInfoRepository;
import com.mycompany.myapp.repository.search.StateInfoSearchRepository;
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
 * Integration tests for the {@link StateInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateInfoResourceIT {

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_STATE_ID = 1;
    private static final Integer UPDATED_STATE_ID = 2;

    private static final String DEFAULT_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/state-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/state-infos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StateInfoRepository stateInfoRepository;

    @Autowired
    private StateInfoSearchRepository stateInfoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateInfoMockMvc;

    private StateInfo stateInfo;

    private StateInfo insertedStateInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateInfo createEntity() {
        return new StateInfo()
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .stateId(DEFAULT_STATE_ID)
            .stateName(DEFAULT_STATE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateInfo createUpdatedEntity() {
        return new StateInfo()
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .stateId(UPDATED_STATE_ID)
            .stateName(UPDATED_STATE_NAME);
    }

    @BeforeEach
    public void initTest() {
        stateInfo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStateInfo != null) {
            stateInfoRepository.delete(insertedStateInfo);
            stateInfoSearchRepository.delete(insertedStateInfo);
            insertedStateInfo = null;
        }
    }

    @Test
    @Transactional
    void createStateInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        // Create the StateInfo
        var returnedStateInfo = om.readValue(
            restStateInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateInfo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StateInfo.class
        );

        // Validate the StateInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStateInfoUpdatableFieldsEquals(returnedStateInfo, getPersistedStateInfo(returnedStateInfo));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedStateInfo = returnedStateInfo;
    }

    @Test
    @Transactional
    void createStateInfoWithExistingId() throws Exception {
        // Create the StateInfo with an existing ID
        stateInfo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateInfo)))
            .andExpect(status().isBadRequest());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllStateInfos() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);

        // Get all the stateInfoList
        restStateInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].stateId").value(hasItem(DEFAULT_STATE_ID)))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)));
    }

    @Test
    @Transactional
    void getStateInfo() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);

        // Get the stateInfo
        restStateInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, stateInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateInfo.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.stateId").value(DEFAULT_STATE_ID))
            .andExpect(jsonPath("$.stateName").value(DEFAULT_STATE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStateInfo() throws Exception {
        // Get the stateInfo
        restStateInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateInfo() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateInfoSearchRepository.save(stateInfo);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());

        // Update the stateInfo
        StateInfo updatedStateInfo = stateInfoRepository.findById(stateInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStateInfo are not directly saved in db
        em.detach(updatedStateInfo);
        updatedStateInfo.createdOn(UPDATED_CREATED_ON).isActive(UPDATED_IS_ACTIVE).stateId(UPDATED_STATE_ID).stateName(UPDATED_STATE_NAME);

        restStateInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStateInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStateInfo))
            )
            .andExpect(status().isOk());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStateInfoToMatchAllProperties(updatedStateInfo);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<StateInfo> stateInfoSearchList = Streamable.of(stateInfoSearchRepository.findAll()).toList();
                StateInfo testStateInfoSearch = stateInfoSearchList.get(searchDatabaseSizeAfter - 1);

                assertStateInfoAllPropertiesEquals(testStateInfoSearch, updatedStateInfo);
            });
    }

    @Test
    @Transactional
    void putNonExistingStateInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        stateInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateInfo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        stateInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        stateInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateStateInfoWithPatch() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateInfo using partial update
        StateInfo partialUpdatedStateInfo = new StateInfo();
        partialUpdatedStateInfo.setId(stateInfo.getId());

        partialUpdatedStateInfo.createdOn(UPDATED_CREATED_ON).isActive(UPDATED_IS_ACTIVE).stateId(UPDATED_STATE_ID);

        restStateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateInfo))
            )
            .andExpect(status().isOk());

        // Validate the StateInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStateInfo, stateInfo),
            getPersistedStateInfo(stateInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateStateInfoWithPatch() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateInfo using partial update
        StateInfo partialUpdatedStateInfo = new StateInfo();
        partialUpdatedStateInfo.setId(stateInfo.getId());

        partialUpdatedStateInfo
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .stateId(UPDATED_STATE_ID)
            .stateName(UPDATED_STATE_NAME);

        restStateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateInfo))
            )
            .andExpect(status().isOk());

        // Validate the StateInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateInfoUpdatableFieldsEquals(partialUpdatedStateInfo, getPersistedStateInfo(partialUpdatedStateInfo));
    }

    @Test
    @Transactional
    void patchNonExistingStateInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        stateInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        stateInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        stateInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stateInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteStateInfo() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);
        stateInfoRepository.save(stateInfo);
        stateInfoSearchRepository.save(stateInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the stateInfo
        restStateInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateInfoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchStateInfo() throws Exception {
        // Initialize the database
        insertedStateInfo = stateInfoRepository.saveAndFlush(stateInfo);
        stateInfoSearchRepository.save(stateInfo);

        // Search the stateInfo
        restStateInfoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + stateInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].stateId").value(hasItem(DEFAULT_STATE_ID)))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)));
    }

    protected long getRepositoryCount() {
        return stateInfoRepository.count();
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

    protected StateInfo getPersistedStateInfo(StateInfo stateInfo) {
        return stateInfoRepository.findById(stateInfo.getId()).orElseThrow();
    }

    protected void assertPersistedStateInfoToMatchAllProperties(StateInfo expectedStateInfo) {
        assertStateInfoAllPropertiesEquals(expectedStateInfo, getPersistedStateInfo(expectedStateInfo));
    }

    protected void assertPersistedStateInfoToMatchUpdatableProperties(StateInfo expectedStateInfo) {
        assertStateInfoAllUpdatablePropertiesEquals(expectedStateInfo, getPersistedStateInfo(expectedStateInfo));
    }
}
