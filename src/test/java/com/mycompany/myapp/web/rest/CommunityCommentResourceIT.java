package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CommunityCommentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommunityComment;
import com.mycompany.myapp.repository.CommunityCommentRepository;
import com.mycompany.myapp.repository.search.CommunityCommentSearchRepository;
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
 * Integration tests for the {@link CommunityCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityCommentResourceIT {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMMENT_BY_USER_ID = 1;
    private static final Integer UPDATED_COMMENT_BY_USER_ID = 2;

    private static final UUID DEFAULT_COMMUNITY_COMMENT_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_COMMENT_ID = UUID.randomUUID();

    private static final UUID DEFAULT_COMMUNITY_POST_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_POST_ID = UUID.randomUUID();

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/community-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/community-comments/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommunityCommentRepository communityCommentRepository;

    @Autowired
    private CommunityCommentSearchRepository communityCommentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityCommentMockMvc;

    private CommunityComment communityComment;

    private CommunityComment insertedCommunityComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityComment createEntity() {
        return new CommunityComment()
            .comment(DEFAULT_COMMENT)
            .commentByUserId(DEFAULT_COMMENT_BY_USER_ID)
            .communityCommentId(DEFAULT_COMMUNITY_COMMENT_ID)
            .communityPostId(DEFAULT_COMMUNITY_POST_ID)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityComment createUpdatedEntity() {
        return new CommunityComment()
            .comment(UPDATED_COMMENT)
            .commentByUserId(UPDATED_COMMENT_BY_USER_ID)
            .communityCommentId(UPDATED_COMMUNITY_COMMENT_ID)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        communityComment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCommunityComment != null) {
            communityCommentRepository.delete(insertedCommunityComment);
            communityCommentSearchRepository.delete(insertedCommunityComment);
            insertedCommunityComment = null;
        }
    }

    @Test
    @Transactional
    void createCommunityComment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        // Create the CommunityComment
        var returnedCommunityComment = om.readValue(
            restCommunityCommentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityComment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommunityComment.class
        );

        // Validate the CommunityComment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommunityCommentUpdatableFieldsEquals(returnedCommunityComment, getPersistedCommunityComment(returnedCommunityComment));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCommunityComment = returnedCommunityComment;
    }

    @Test
    @Transactional
    void createCommunityCommentWithExistingId() throws Exception {
        // Create the CommunityComment with an existing ID
        communityComment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityComment)))
            .andExpect(status().isBadRequest());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCommunityComments() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);

        // Get all the communityCommentList
        restCommunityCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].commentByUserId").value(hasItem(DEFAULT_COMMENT_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].communityCommentId").value(hasItem(DEFAULT_COMMUNITY_COMMENT_ID.toString())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getCommunityComment() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);

        // Get the communityComment
        restCommunityCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, communityComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityComment.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.commentByUserId").value(DEFAULT_COMMENT_BY_USER_ID))
            .andExpect(jsonPath("$.communityCommentId").value(DEFAULT_COMMUNITY_COMMENT_ID.toString()))
            .andExpect(jsonPath("$.communityPostId").value(DEFAULT_COMMUNITY_POST_ID.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommunityComment() throws Exception {
        // Get the communityComment
        restCommunityCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommunityComment() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        communityCommentSearchRepository.save(communityComment);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());

        // Update the communityComment
        CommunityComment updatedCommunityComment = communityCommentRepository.findById(communityComment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommunityComment are not directly saved in db
        em.detach(updatedCommunityComment);
        updatedCommunityComment
            .comment(UPDATED_COMMENT)
            .commentByUserId(UPDATED_COMMENT_BY_USER_ID)
            .communityCommentId(UPDATED_COMMUNITY_COMMENT_ID)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restCommunityCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommunityComment))
            )
            .andExpect(status().isOk());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommunityCommentToMatchAllProperties(updatedCommunityComment);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CommunityComment> communityCommentSearchList = Streamable.of(communityCommentSearchRepository.findAll()).toList();
                CommunityComment testCommunityCommentSearch = communityCommentSearchList.get(searchDatabaseSizeAfter - 1);

                assertCommunityCommentAllPropertiesEquals(testCommunityCommentSearch, updatedCommunityComment);
            });
    }

    @Test
    @Transactional
    void putNonExistingCommunityComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        communityComment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        communityComment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        communityComment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityComment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCommunityCommentWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityComment using partial update
        CommunityComment partialUpdatedCommunityComment = new CommunityComment();
        partialUpdatedCommunityComment.setId(communityComment.getId());

        partialUpdatedCommunityComment
            .commentByUserId(UPDATED_COMMENT_BY_USER_ID)
            .communityCommentId(UPDATED_COMMUNITY_COMMENT_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restCommunityCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityComment))
            )
            .andExpect(status().isOk());

        // Validate the CommunityComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityCommentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommunityComment, communityComment),
            getPersistedCommunityComment(communityComment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommunityCommentWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityComment using partial update
        CommunityComment partialUpdatedCommunityComment = new CommunityComment();
        partialUpdatedCommunityComment.setId(communityComment.getId());

        partialUpdatedCommunityComment
            .comment(UPDATED_COMMENT)
            .commentByUserId(UPDATED_COMMENT_BY_USER_ID)
            .communityCommentId(UPDATED_COMMUNITY_COMMENT_ID)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restCommunityCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityComment))
            )
            .andExpect(status().isOk());

        // Validate the CommunityComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityCommentUpdatableFieldsEquals(
            partialUpdatedCommunityComment,
            getPersistedCommunityComment(partialUpdatedCommunityComment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCommunityComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        communityComment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        communityComment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        communityComment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityCommentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(communityComment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCommunityComment() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);
        communityCommentRepository.save(communityComment);
        communityCommentSearchRepository.save(communityComment);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the communityComment
        restCommunityCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityCommentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCommunityComment() throws Exception {
        // Initialize the database
        insertedCommunityComment = communityCommentRepository.saveAndFlush(communityComment);
        communityCommentSearchRepository.save(communityComment);

        // Search the communityComment
        restCommunityCommentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + communityComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].commentByUserId").value(hasItem(DEFAULT_COMMENT_BY_USER_ID)))
            .andExpect(jsonPath("$.[*].communityCommentId").value(hasItem(DEFAULT_COMMUNITY_COMMENT_ID.toString())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return communityCommentRepository.count();
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

    protected CommunityComment getPersistedCommunityComment(CommunityComment communityComment) {
        return communityCommentRepository.findById(communityComment.getId()).orElseThrow();
    }

    protected void assertPersistedCommunityCommentToMatchAllProperties(CommunityComment expectedCommunityComment) {
        assertCommunityCommentAllPropertiesEquals(expectedCommunityComment, getPersistedCommunityComment(expectedCommunityComment));
    }

    protected void assertPersistedCommunityCommentToMatchUpdatableProperties(CommunityComment expectedCommunityComment) {
        assertCommunityCommentAllUpdatablePropertiesEquals(
            expectedCommunityComment,
            getPersistedCommunityComment(expectedCommunityComment)
        );
    }
}
