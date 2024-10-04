package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RoleMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RoleMst;
import com.mycompany.myapp.repository.RoleMstRepository;
import com.mycompany.myapp.repository.search.RoleMstSearchRepository;
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
 * Integration tests for the {@link RoleMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleMstResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_PARENT_ROLE_ID = 1;
    private static final Integer UPDATED_PARENT_ROLE_ID = 2;

    private static final Integer DEFAULT_ROLE_ID = 1;
    private static final Integer UPDATED_ROLE_ID = 2;

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/role-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/role-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoleMstRepository roleMstRepository;

    @Autowired
    private RoleMstSearchRepository roleMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMstMockMvc;

    private RoleMst roleMst;

    private RoleMst insertedRoleMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleMst createEntity() {
        return new RoleMst()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .parentRoleId(DEFAULT_PARENT_ROLE_ID)
            .roleId(DEFAULT_ROLE_ID)
            .roleName(DEFAULT_ROLE_NAME)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleMst createUpdatedEntity() {
        return new RoleMst()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .parentRoleId(UPDATED_PARENT_ROLE_ID)
            .roleId(UPDATED_ROLE_ID)
            .roleName(UPDATED_ROLE_NAME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        roleMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRoleMst != null) {
            roleMstRepository.delete(insertedRoleMst);
            roleMstSearchRepository.delete(insertedRoleMst);
            insertedRoleMst = null;
        }
    }

    @Test
    @Transactional
    void createRoleMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        // Create the RoleMst
        var returnedRoleMst = om.readValue(
            restRoleMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoleMst.class
        );

        // Validate the RoleMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRoleMstUpdatableFieldsEquals(returnedRoleMst, getPersistedRoleMst(returnedRoleMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedRoleMst = returnedRoleMst;
    }

    @Test
    @Transactional
    void createRoleMstWithExistingId() throws Exception {
        // Create the RoleMst with an existing ID
        roleMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleMst)))
            .andExpect(status().isBadRequest());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllRoleMsts() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);

        // Get all the roleMstList
        restRoleMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].parentRoleId").value(hasItem(DEFAULT_PARENT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getRoleMst() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);

        // Get the roleMst
        restRoleMstMockMvc
            .perform(get(ENTITY_API_URL_ID, roleMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleMst.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.parentRoleId").value(DEFAULT_PARENT_ROLE_ID))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoleMst() throws Exception {
        // Get the roleMst
        restRoleMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoleMst() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleMstSearchRepository.save(roleMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());

        // Update the roleMst
        RoleMst updatedRoleMst = roleMstRepository.findById(roleMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoleMst are not directly saved in db
        em.detach(updatedRoleMst);
        updatedRoleMst
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .parentRoleId(UPDATED_PARENT_ROLE_ID)
            .roleId(UPDATED_ROLE_ID)
            .roleName(UPDATED_ROLE_NAME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restRoleMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoleMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRoleMst))
            )
            .andExpect(status().isOk());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoleMstToMatchAllProperties(updatedRoleMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<RoleMst> roleMstSearchList = Streamable.of(roleMstSearchRepository.findAll()).toList();
                RoleMst testRoleMstSearch = roleMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertRoleMstAllPropertiesEquals(testRoleMstSearch, updatedRoleMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingRoleMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        roleMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMstMockMvc
            .perform(put(ENTITY_API_URL_ID, roleMst.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleMst)))
            .andExpect(status().isBadRequest());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        roleMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roleMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        roleMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateRoleMstWithPatch() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleMst using partial update
        RoleMst partialUpdatedRoleMst = new RoleMst();
        partialUpdatedRoleMst.setId(roleMst.getId());

        partialUpdatedRoleMst.createdOn(UPDATED_CREATED_ON).updatedOn(UPDATED_UPDATED_ON);

        restRoleMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoleMst))
            )
            .andExpect(status().isOk());

        // Validate the RoleMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleMstUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoleMst, roleMst), getPersistedRoleMst(roleMst));
    }

    @Test
    @Transactional
    void fullUpdateRoleMstWithPatch() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleMst using partial update
        RoleMst partialUpdatedRoleMst = new RoleMst();
        partialUpdatedRoleMst.setId(roleMst.getId());

        partialUpdatedRoleMst
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .parentRoleId(UPDATED_PARENT_ROLE_ID)
            .roleId(UPDATED_ROLE_ID)
            .roleName(UPDATED_ROLE_NAME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restRoleMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoleMst))
            )
            .andExpect(status().isOk());

        // Validate the RoleMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleMstUpdatableFieldsEquals(partialUpdatedRoleMst, getPersistedRoleMst(partialUpdatedRoleMst));
    }

    @Test
    @Transactional
    void patchNonExistingRoleMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        roleMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleMst.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roleMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        roleMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roleMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        roleMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roleMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteRoleMst() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);
        roleMstRepository.save(roleMst);
        roleMstSearchRepository.save(roleMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the roleMst
        restRoleMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(roleMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchRoleMst() throws Exception {
        // Initialize the database
        insertedRoleMst = roleMstRepository.saveAndFlush(roleMst);
        roleMstSearchRepository.save(roleMst);

        // Search the roleMst
        restRoleMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + roleMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].parentRoleId").value(hasItem(DEFAULT_PARENT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return roleMstRepository.count();
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

    protected RoleMst getPersistedRoleMst(RoleMst roleMst) {
        return roleMstRepository.findById(roleMst.getId()).orElseThrow();
    }

    protected void assertPersistedRoleMstToMatchAllProperties(RoleMst expectedRoleMst) {
        assertRoleMstAllPropertiesEquals(expectedRoleMst, getPersistedRoleMst(expectedRoleMst));
    }

    protected void assertPersistedRoleMstToMatchUpdatableProperties(RoleMst expectedRoleMst) {
        assertRoleMstAllUpdatablePropertiesEquals(expectedRoleMst, getPersistedRoleMst(expectedRoleMst));
    }
}
