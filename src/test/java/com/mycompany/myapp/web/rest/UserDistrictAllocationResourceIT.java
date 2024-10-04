package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserDistrictAllocationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserDistrictAllocation;
import com.mycompany.myapp.repository.UserDistrictAllocationRepository;
import com.mycompany.myapp.repository.search.UserDistrictAllocationSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link UserDistrictAllocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDistrictAllocationResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ALLOCATED = false;
    private static final Boolean UPDATED_IS_ALLOCATED = true;

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final UUID DEFAULT_USER_DISTRICT_ALLOCATION_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_DISTRICT_ALLOCATION_ID = UUID.randomUUID();

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/user-district-allocations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-district-allocations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserDistrictAllocationRepository userDistrictAllocationRepository;

    @Autowired
    private UserDistrictAllocationSearchRepository userDistrictAllocationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDistrictAllocationMockMvc;

    private UserDistrictAllocation userDistrictAllocation;

    private UserDistrictAllocation insertedUserDistrictAllocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDistrictAllocation createEntity() {
        return new UserDistrictAllocation()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isAllocated(DEFAULT_IS_ALLOCATED)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .userDistrictAllocationId(DEFAULT_USER_DISTRICT_ALLOCATION_ID)
            .userId(DEFAULT_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDistrictAllocation createUpdatedEntity() {
        return new UserDistrictAllocation()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userDistrictAllocationId(UPDATED_USER_DISTRICT_ALLOCATION_ID)
            .userId(UPDATED_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        userDistrictAllocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserDistrictAllocation != null) {
            userDistrictAllocationRepository.delete(insertedUserDistrictAllocation);
            userDistrictAllocationSearchRepository.delete(insertedUserDistrictAllocation);
            insertedUserDistrictAllocation = null;
        }
    }

    @Test
    @Transactional
    void createUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        // Create the UserDistrictAllocation
        var returnedUserDistrictAllocation = om.readValue(
            restUserDistrictAllocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDistrictAllocation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDistrictAllocation.class
        );

        // Validate the UserDistrictAllocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserDistrictAllocationUpdatableFieldsEquals(
            returnedUserDistrictAllocation,
            getPersistedUserDistrictAllocation(returnedUserDistrictAllocation)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUserDistrictAllocation = returnedUserDistrictAllocation;
    }

    @Test
    @Transactional
    void createUserDistrictAllocationWithExistingId() throws Exception {
        // Create the UserDistrictAllocation with an existing ID
        userDistrictAllocation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDistrictAllocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDistrictAllocation)))
            .andExpect(status().isBadRequest());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUserDistrictAllocations() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);

        // Get all the userDistrictAllocationList
        restUserDistrictAllocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDistrictAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].userDistrictAllocationId").value(hasItem(DEFAULT_USER_DISTRICT_ALLOCATION_ID.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getUserDistrictAllocation() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);

        // Get the userDistrictAllocation
        restUserDistrictAllocationMockMvc
            .perform(get(ENTITY_API_URL_ID, userDistrictAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDistrictAllocation.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isAllocated").value(DEFAULT_IS_ALLOCATED.booleanValue()))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.userDistrictAllocationId").value(DEFAULT_USER_DISTRICT_ALLOCATION_ID.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingUserDistrictAllocation() throws Exception {
        // Get the userDistrictAllocation
        restUserDistrictAllocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserDistrictAllocation() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDistrictAllocationSearchRepository.save(userDistrictAllocation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());

        // Update the userDistrictAllocation
        UserDistrictAllocation updatedUserDistrictAllocation = userDistrictAllocationRepository
            .findById(userDistrictAllocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserDistrictAllocation are not directly saved in db
        em.detach(updatedUserDistrictAllocation);
        updatedUserDistrictAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userDistrictAllocationId(UPDATED_USER_DISTRICT_ALLOCATION_ID)
            .userId(UPDATED_USER_ID);

        restUserDistrictAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserDistrictAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserDistrictAllocation))
            )
            .andExpect(status().isOk());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserDistrictAllocationToMatchAllProperties(updatedUserDistrictAllocation);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserDistrictAllocation> userDistrictAllocationSearchList = Streamable.of(
                    userDistrictAllocationSearchRepository.findAll()
                ).toList();
                UserDistrictAllocation testUserDistrictAllocationSearch = userDistrictAllocationSearchList.get(searchDatabaseSizeAfter - 1);

                assertUserDistrictAllocationAllPropertiesEquals(testUserDistrictAllocationSearch, updatedUserDistrictAllocation);
            });
    }

    @Test
    @Transactional
    void putNonExistingUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        userDistrictAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDistrictAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDistrictAllocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        userDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDistrictAllocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        userDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDistrictAllocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDistrictAllocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUserDistrictAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDistrictAllocation using partial update
        UserDistrictAllocation partialUpdatedUserDistrictAllocation = new UserDistrictAllocation();
        partialUpdatedUserDistrictAllocation.setId(userDistrictAllocation.getId());

        partialUpdatedUserDistrictAllocation
            .isAllocated(UPDATED_IS_ALLOCATED)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userId(UPDATED_USER_ID);

        restUserDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDistrictAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDistrictAllocation))
            )
            .andExpect(status().isOk());

        // Validate the UserDistrictAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDistrictAllocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserDistrictAllocation, userDistrictAllocation),
            getPersistedUserDistrictAllocation(userDistrictAllocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserDistrictAllocationWithPatch() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDistrictAllocation using partial update
        UserDistrictAllocation partialUpdatedUserDistrictAllocation = new UserDistrictAllocation();
        partialUpdatedUserDistrictAllocation.setId(userDistrictAllocation.getId());

        partialUpdatedUserDistrictAllocation
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isAllocated(UPDATED_IS_ALLOCATED)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userDistrictAllocationId(UPDATED_USER_DISTRICT_ALLOCATION_ID)
            .userId(UPDATED_USER_ID);

        restUserDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDistrictAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDistrictAllocation))
            )
            .andExpect(status().isOk());

        // Validate the UserDistrictAllocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDistrictAllocationUpdatableFieldsEquals(
            partialUpdatedUserDistrictAllocation,
            getPersistedUserDistrictAllocation(partialUpdatedUserDistrictAllocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        userDistrictAllocation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDistrictAllocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        userDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDistrictAllocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDistrictAllocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        userDistrictAllocation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDistrictAllocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDistrictAllocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDistrictAllocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUserDistrictAllocation() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);
        userDistrictAllocationRepository.save(userDistrictAllocation);
        userDistrictAllocationSearchRepository.save(userDistrictAllocation);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userDistrictAllocation
        restUserDistrictAllocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDistrictAllocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userDistrictAllocationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUserDistrictAllocation() throws Exception {
        // Initialize the database
        insertedUserDistrictAllocation = userDistrictAllocationRepository.saveAndFlush(userDistrictAllocation);
        userDistrictAllocationSearchRepository.save(userDistrictAllocation);

        // Search the userDistrictAllocation
        restUserDistrictAllocationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + userDistrictAllocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDistrictAllocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isAllocated").value(hasItem(DEFAULT_IS_ALLOCATED.booleanValue())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].userDistrictAllocationId").value(hasItem(DEFAULT_USER_DISTRICT_ALLOCATION_ID.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    protected long getRepositoryCount() {
        return userDistrictAllocationRepository.count();
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

    protected UserDistrictAllocation getPersistedUserDistrictAllocation(UserDistrictAllocation userDistrictAllocation) {
        return userDistrictAllocationRepository.findById(userDistrictAllocation.getId()).orElseThrow();
    }

    protected void assertPersistedUserDistrictAllocationToMatchAllProperties(UserDistrictAllocation expectedUserDistrictAllocation) {
        assertUserDistrictAllocationAllPropertiesEquals(
            expectedUserDistrictAllocation,
            getPersistedUserDistrictAllocation(expectedUserDistrictAllocation)
        );
    }

    protected void assertPersistedUserDistrictAllocationToMatchUpdatableProperties(UserDistrictAllocation expectedUserDistrictAllocation) {
        assertUserDistrictAllocationAllUpdatablePropertiesEquals(
            expectedUserDistrictAllocation,
            getPersistedUserDistrictAllocation(expectedUserDistrictAllocation)
        );
    }
}
