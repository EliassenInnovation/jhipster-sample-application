package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CommunityLikeMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommunityLikeMst;
import com.mycompany.myapp.repository.CommunityLikeMstRepository;
import com.mycompany.myapp.repository.search.CommunityLikeMstSearchRepository;
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
 * Integration tests for the {@link CommunityLikeMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityLikeMstResourceIT {

    private static final UUID DEFAULT_COMMUNITY_LIKE_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_LIKE_ID = UUID.randomUUID();

    private static final UUID DEFAULT_COMMUNITY_POST_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_POST_ID = UUID.randomUUID();

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_LIKED = false;
    private static final Boolean UPDATED_IS_LIKED = true;

    private static final Integer DEFAULT_LIKED_BY_USER_ID = 1;
    private static final Integer UPDATED_LIKED_BY_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/community-like-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/community-like-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommunityLikeMstRepository communityLikeMstRepository;

    @Autowired
    private CommunityLikeMstSearchRepository communityLikeMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityLikeMstMockMvc;

    private CommunityLikeMst communityLikeMst;

    private CommunityLikeMst insertedCommunityLikeMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityLikeMst createEntity() {
        return new CommunityLikeMst()
            .communityLikeId(DEFAULT_COMMUNITY_LIKE_ID)
            .communityPostId(DEFAULT_COMMUNITY_POST_ID)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .isLiked(DEFAULT_IS_LIKED)
            .likedByUserId(DEFAULT_LIKED_BY_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityLikeMst createUpdatedEntity() {
        return new CommunityLikeMst()
            .communityLikeId(UPDATED_COMMUNITY_LIKE_ID)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .isLiked(UPDATED_IS_LIKED)
            .likedByUserId(UPDATED_LIKED_BY_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        communityLikeMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCommunityLikeMst != null) {
            communityLikeMstRepository.delete(insertedCommunityLikeMst);
            communityLikeMstSearchRepository.delete(insertedCommunityLikeMst);
            insertedCommunityLikeMst = null;
        }
    }

    @Test
    @Transactional
    void createCommunityLikeMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        // Create the CommunityLikeMst
        var returnedCommunityLikeMst = om.readValue(
            restCommunityLikeMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityLikeMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommunityLikeMst.class
        );

        // Validate the CommunityLikeMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommunityLikeMstUpdatableFieldsEquals(returnedCommunityLikeMst, getPersistedCommunityLikeMst(returnedCommunityLikeMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCommunityLikeMst = returnedCommunityLikeMst;
    }

    @Test
    @Transactional
    void createCommunityLikeMstWithExistingId() throws Exception {
        // Create the CommunityLikeMst with an existing ID
        communityLikeMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityLikeMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityLikeMst)))
            .andExpect(status().isBadRequest());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCommunityLikeMsts() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);

        // Get all the communityLikeMstList
        restCommunityLikeMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityLikeMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].communityLikeId").value(hasItem(DEFAULT_COMMUNITY_LIKE_ID.toString())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isLiked").value(hasItem(DEFAULT_IS_LIKED.booleanValue())))
            .andExpect(jsonPath("$.[*].likedByUserId").value(hasItem(DEFAULT_LIKED_BY_USER_ID)));
    }

    @Test
    @Transactional
    void getCommunityLikeMst() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);

        // Get the communityLikeMst
        restCommunityLikeMstMockMvc
            .perform(get(ENTITY_API_URL_ID, communityLikeMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityLikeMst.getId().intValue()))
            .andExpect(jsonPath("$.communityLikeId").value(DEFAULT_COMMUNITY_LIKE_ID.toString()))
            .andExpect(jsonPath("$.communityPostId").value(DEFAULT_COMMUNITY_POST_ID.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isLiked").value(DEFAULT_IS_LIKED.booleanValue()))
            .andExpect(jsonPath("$.likedByUserId").value(DEFAULT_LIKED_BY_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingCommunityLikeMst() throws Exception {
        // Get the communityLikeMst
        restCommunityLikeMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommunityLikeMst() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        communityLikeMstSearchRepository.save(communityLikeMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());

        // Update the communityLikeMst
        CommunityLikeMst updatedCommunityLikeMst = communityLikeMstRepository.findById(communityLikeMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommunityLikeMst are not directly saved in db
        em.detach(updatedCommunityLikeMst);
        updatedCommunityLikeMst
            .communityLikeId(UPDATED_COMMUNITY_LIKE_ID)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .isLiked(UPDATED_IS_LIKED)
            .likedByUserId(UPDATED_LIKED_BY_USER_ID);

        restCommunityLikeMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityLikeMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommunityLikeMst))
            )
            .andExpect(status().isOk());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommunityLikeMstToMatchAllProperties(updatedCommunityLikeMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CommunityLikeMst> communityLikeMstSearchList = Streamable.of(communityLikeMstSearchRepository.findAll()).toList();
                CommunityLikeMst testCommunityLikeMstSearch = communityLikeMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertCommunityLikeMstAllPropertiesEquals(testCommunityLikeMstSearch, updatedCommunityLikeMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingCommunityLikeMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        communityLikeMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityLikeMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityLikeMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityLikeMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityLikeMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        communityLikeMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLikeMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityLikeMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityLikeMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        communityLikeMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLikeMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityLikeMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCommunityLikeMstWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityLikeMst using partial update
        CommunityLikeMst partialUpdatedCommunityLikeMst = new CommunityLikeMst();
        partialUpdatedCommunityLikeMst.setId(communityLikeMst.getId());

        partialUpdatedCommunityLikeMst.createdOn(UPDATED_CREATED_ON).likedByUserId(UPDATED_LIKED_BY_USER_ID);

        restCommunityLikeMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityLikeMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityLikeMst))
            )
            .andExpect(status().isOk());

        // Validate the CommunityLikeMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityLikeMstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommunityLikeMst, communityLikeMst),
            getPersistedCommunityLikeMst(communityLikeMst)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommunityLikeMstWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityLikeMst using partial update
        CommunityLikeMst partialUpdatedCommunityLikeMst = new CommunityLikeMst();
        partialUpdatedCommunityLikeMst.setId(communityLikeMst.getId());

        partialUpdatedCommunityLikeMst
            .communityLikeId(UPDATED_COMMUNITY_LIKE_ID)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .isLiked(UPDATED_IS_LIKED)
            .likedByUserId(UPDATED_LIKED_BY_USER_ID);

        restCommunityLikeMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityLikeMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityLikeMst))
            )
            .andExpect(status().isOk());

        // Validate the CommunityLikeMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityLikeMstUpdatableFieldsEquals(
            partialUpdatedCommunityLikeMst,
            getPersistedCommunityLikeMst(partialUpdatedCommunityLikeMst)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCommunityLikeMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        communityLikeMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityLikeMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityLikeMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityLikeMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityLikeMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        communityLikeMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLikeMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityLikeMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityLikeMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        communityLikeMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLikeMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(communityLikeMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityLikeMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCommunityLikeMst() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);
        communityLikeMstRepository.save(communityLikeMst);
        communityLikeMstSearchRepository.save(communityLikeMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the communityLikeMst
        restCommunityLikeMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityLikeMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityLikeMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCommunityLikeMst() throws Exception {
        // Initialize the database
        insertedCommunityLikeMst = communityLikeMstRepository.saveAndFlush(communityLikeMst);
        communityLikeMstSearchRepository.save(communityLikeMst);

        // Search the communityLikeMst
        restCommunityLikeMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + communityLikeMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityLikeMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].communityLikeId").value(hasItem(DEFAULT_COMMUNITY_LIKE_ID.toString())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isLiked").value(hasItem(DEFAULT_IS_LIKED.booleanValue())))
            .andExpect(jsonPath("$.[*].likedByUserId").value(hasItem(DEFAULT_LIKED_BY_USER_ID)));
    }

    protected long getRepositoryCount() {
        return communityLikeMstRepository.count();
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

    protected CommunityLikeMst getPersistedCommunityLikeMst(CommunityLikeMst communityLikeMst) {
        return communityLikeMstRepository.findById(communityLikeMst.getId()).orElseThrow();
    }

    protected void assertPersistedCommunityLikeMstToMatchAllProperties(CommunityLikeMst expectedCommunityLikeMst) {
        assertCommunityLikeMstAllPropertiesEquals(expectedCommunityLikeMst, getPersistedCommunityLikeMst(expectedCommunityLikeMst));
    }

    protected void assertPersistedCommunityLikeMstToMatchUpdatableProperties(CommunityLikeMst expectedCommunityLikeMst) {
        assertCommunityLikeMstAllUpdatablePropertiesEquals(
            expectedCommunityLikeMst,
            getPersistedCommunityLikeMst(expectedCommunityLikeMst)
        );
    }
}
