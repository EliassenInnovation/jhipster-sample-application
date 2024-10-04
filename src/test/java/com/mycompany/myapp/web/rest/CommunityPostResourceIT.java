package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CommunityPostAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommunityPost;
import com.mycompany.myapp.repository.CommunityPostRepository;
import com.mycompany.myapp.repository.search.CommunityPostSearchRepository;
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
 * Integration tests for the {@link CommunityPostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityPostResourceIT {

    private static final UUID DEFAULT_COMMUNITY_POST_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_POST_ID = UUID.randomUUID();

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_LAST_UPDATED_BY = 1;
    private static final Integer UPDATED_LAST_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_POST_TYPE_ID = 1;
    private static final Integer UPDATED_POST_TYPE_ID = 2;

    private static final Integer DEFAULT_PRIVACY_TYPE_ID = 1;
    private static final Integer UPDATED_PRIVACY_TYPE_ID = 2;

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/community-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/community-posts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @Autowired
    private CommunityPostSearchRepository communityPostSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityPostMockMvc;

    private CommunityPost communityPost;

    private CommunityPost insertedCommunityPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityPost createEntity() {
        return new CommunityPost()
            .communityPostId(DEFAULT_COMMUNITY_POST_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON)
            .postTypeId(DEFAULT_POST_TYPE_ID)
            .privacyTypeId(DEFAULT_PRIVACY_TYPE_ID)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .userId(DEFAULT_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityPost createUpdatedEntity() {
        return new CommunityPost()
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .postTypeId(UPDATED_POST_TYPE_ID)
            .privacyTypeId(UPDATED_PRIVACY_TYPE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .userId(UPDATED_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        communityPost = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCommunityPost != null) {
            communityPostRepository.delete(insertedCommunityPost);
            communityPostSearchRepository.delete(insertedCommunityPost);
            insertedCommunityPost = null;
        }
    }

    @Test
    @Transactional
    void createCommunityPost() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        // Create the CommunityPost
        var returnedCommunityPost = om.readValue(
            restCommunityPostMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityPost)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommunityPost.class
        );

        // Validate the CommunityPost in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommunityPostUpdatableFieldsEquals(returnedCommunityPost, getPersistedCommunityPost(returnedCommunityPost));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCommunityPost = returnedCommunityPost;
    }

    @Test
    @Transactional
    void createCommunityPostWithExistingId() throws Exception {
        // Create the CommunityPost with an existing ID
        communityPost.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityPost)))
            .andExpect(status().isBadRequest());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCommunityPosts() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);

        // Get all the communityPostList
        restCommunityPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].postTypeId").value(hasItem(DEFAULT_POST_TYPE_ID)))
            .andExpect(jsonPath("$.[*].privacyTypeId").value(hasItem(DEFAULT_PRIVACY_TYPE_ID)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getCommunityPost() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);

        // Get the communityPost
        restCommunityPostMockMvc
            .perform(get(ENTITY_API_URL_ID, communityPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityPost.getId().intValue()))
            .andExpect(jsonPath("$.communityPostId").value(DEFAULT_COMMUNITY_POST_ID.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.postTypeId").value(DEFAULT_POST_TYPE_ID))
            .andExpect(jsonPath("$.privacyTypeId").value(DEFAULT_PRIVACY_TYPE_ID))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingCommunityPost() throws Exception {
        // Get the communityPost
        restCommunityPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommunityPost() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        communityPostSearchRepository.save(communityPost);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());

        // Update the communityPost
        CommunityPost updatedCommunityPost = communityPostRepository.findById(communityPost.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommunityPost are not directly saved in db
        em.detach(updatedCommunityPost);
        updatedCommunityPost
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .postTypeId(UPDATED_POST_TYPE_ID)
            .privacyTypeId(UPDATED_PRIVACY_TYPE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .userId(UPDATED_USER_ID);

        restCommunityPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommunityPost))
            )
            .andExpect(status().isOk());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommunityPostToMatchAllProperties(updatedCommunityPost);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CommunityPost> communityPostSearchList = Streamable.of(communityPostSearchRepository.findAll()).toList();
                CommunityPost testCommunityPostSearch = communityPostSearchList.get(searchDatabaseSizeAfter - 1);

                assertCommunityPostAllPropertiesEquals(testCommunityPostSearch, updatedCommunityPost);
            });
    }

    @Test
    @Transactional
    void putNonExistingCommunityPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        communityPost.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        communityPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        communityPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCommunityPostWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityPost using partial update
        CommunityPost partialUpdatedCommunityPost = new CommunityPost();
        partialUpdatedCommunityPost.setId(communityPost.getId());

        partialUpdatedCommunityPost
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .description(UPDATED_DESCRIPTION)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .privacyTypeId(UPDATED_PRIVACY_TYPE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID);

        restCommunityPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityPost))
            )
            .andExpect(status().isOk());

        // Validate the CommunityPost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityPostUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommunityPost, communityPost),
            getPersistedCommunityPost(communityPost)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommunityPostWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityPost using partial update
        CommunityPost partialUpdatedCommunityPost = new CommunityPost();
        partialUpdatedCommunityPost.setId(communityPost.getId());

        partialUpdatedCommunityPost
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .postTypeId(UPDATED_POST_TYPE_ID)
            .privacyTypeId(UPDATED_PRIVACY_TYPE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .userId(UPDATED_USER_ID);

        restCommunityPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityPost))
            )
            .andExpect(status().isOk());

        // Validate the CommunityPost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityPostUpdatableFieldsEquals(partialUpdatedCommunityPost, getPersistedCommunityPost(partialUpdatedCommunityPost));
    }

    @Test
    @Transactional
    void patchNonExistingCommunityPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        communityPost.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        communityPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        communityPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(communityPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCommunityPost() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);
        communityPostRepository.save(communityPost);
        communityPostSearchRepository.save(communityPost);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the communityPost
        restCommunityPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCommunityPost() throws Exception {
        // Initialize the database
        insertedCommunityPost = communityPostRepository.saveAndFlush(communityPost);
        communityPostSearchRepository.save(communityPost);

        // Search the communityPost
        restCommunityPostMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + communityPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].postTypeId").value(hasItem(DEFAULT_POST_TYPE_ID)))
            .andExpect(jsonPath("$.[*].privacyTypeId").value(hasItem(DEFAULT_PRIVACY_TYPE_ID)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    protected long getRepositoryCount() {
        return communityPostRepository.count();
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

    protected CommunityPost getPersistedCommunityPost(CommunityPost communityPost) {
        return communityPostRepository.findById(communityPost.getId()).orElseThrow();
    }

    protected void assertPersistedCommunityPostToMatchAllProperties(CommunityPost expectedCommunityPost) {
        assertCommunityPostAllPropertiesEquals(expectedCommunityPost, getPersistedCommunityPost(expectedCommunityPost));
    }

    protected void assertPersistedCommunityPostToMatchUpdatableProperties(CommunityPost expectedCommunityPost) {
        assertCommunityPostAllUpdatablePropertiesEquals(expectedCommunityPost, getPersistedCommunityPost(expectedCommunityPost));
    }
}
