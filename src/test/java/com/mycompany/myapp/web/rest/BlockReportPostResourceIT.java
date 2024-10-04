package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.BlockReportPostAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BlockReportPost;
import com.mycompany.myapp.repository.BlockReportPostRepository;
import com.mycompany.myapp.repository.search.BlockReportPostSearchRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BlockReportPostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlockReportPostResourceIT {

    private static final String DEFAULT_BLOCK_CATEGORIES = "AAAAAAAAAA";
    private static final String UPDATED_BLOCK_CATEGORIES = "BBBBBBBBBB";

    private static final String DEFAULT_BLOCKING_REASON = "AAAAAAAAAA";
    private static final String UPDATED_BLOCKING_REASON = "BBBBBBBBBB";

    private static final UUID DEFAULT_POST_BLOCK_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_POST_BLOCK_REPORT_ID = UUID.randomUUID();

    private static final UUID DEFAULT_POST_ID = UUID.randomUUID();
    private static final UUID UPDATED_POST_ID = UUID.randomUUID();

    private static final String DEFAULT_POST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_POST_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_REQUESTED_BY = 1;
    private static final Integer UPDATED_REQUESTED_BY = 2;

    private static final String ENTITY_API_URL = "/api/block-report-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/block-report-posts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BlockReportPostRepository blockReportPostRepository;

    @Autowired
    private BlockReportPostSearchRepository blockReportPostSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockReportPostMockMvc;

    private BlockReportPost blockReportPost;

    private BlockReportPost insertedBlockReportPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlockReportPost createEntity() {
        return new BlockReportPost()
            .blockCategories(DEFAULT_BLOCK_CATEGORIES)
            .blockingReason(DEFAULT_BLOCKING_REASON)
            .postBlockReportId(DEFAULT_POST_BLOCK_REPORT_ID)
            .postId(DEFAULT_POST_ID)
            .postType(DEFAULT_POST_TYPE)
            .requestedBy(DEFAULT_REQUESTED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlockReportPost createUpdatedEntity() {
        return new BlockReportPost()
            .blockCategories(UPDATED_BLOCK_CATEGORIES)
            .blockingReason(UPDATED_BLOCKING_REASON)
            .postBlockReportId(UPDATED_POST_BLOCK_REPORT_ID)
            .postId(UPDATED_POST_ID)
            .postType(UPDATED_POST_TYPE)
            .requestedBy(UPDATED_REQUESTED_BY);
    }

    @BeforeEach
    public void initTest() {
        blockReportPost = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBlockReportPost != null) {
            blockReportPostRepository.delete(insertedBlockReportPost);
            blockReportPostSearchRepository.delete(insertedBlockReportPost);
            insertedBlockReportPost = null;
        }
    }

    @Test
    @Transactional
    void createBlockReportPost() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        // Create the BlockReportPost
        var returnedBlockReportPost = om.readValue(
            restBlockReportPostMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockReportPost)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BlockReportPost.class
        );

        // Validate the BlockReportPost in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBlockReportPostUpdatableFieldsEquals(returnedBlockReportPost, getPersistedBlockReportPost(returnedBlockReportPost));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBlockReportPost = returnedBlockReportPost;
    }

    @Test
    @Transactional
    void createBlockReportPostWithExistingId() throws Exception {
        // Create the BlockReportPost with an existing ID
        blockReportPost.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockReportPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockReportPost)))
            .andExpect(status().isBadRequest());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBlockReportPosts() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);

        // Get all the blockReportPostList
        restBlockReportPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockReportPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].blockCategories").value(hasItem(DEFAULT_BLOCK_CATEGORIES.toString())))
            .andExpect(jsonPath("$.[*].blockingReason").value(hasItem(DEFAULT_BLOCKING_REASON.toString())))
            .andExpect(jsonPath("$.[*].postBlockReportId").value(hasItem(DEFAULT_POST_BLOCK_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE)))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)));
    }

    @Test
    @Transactional
    void getBlockReportPost() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);

        // Get the blockReportPost
        restBlockReportPostMockMvc
            .perform(get(ENTITY_API_URL_ID, blockReportPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blockReportPost.getId().intValue()))
            .andExpect(jsonPath("$.blockCategories").value(DEFAULT_BLOCK_CATEGORIES.toString()))
            .andExpect(jsonPath("$.blockingReason").value(DEFAULT_BLOCKING_REASON.toString()))
            .andExpect(jsonPath("$.postBlockReportId").value(DEFAULT_POST_BLOCK_REPORT_ID.toString()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.toString()))
            .andExpect(jsonPath("$.postType").value(DEFAULT_POST_TYPE))
            .andExpect(jsonPath("$.requestedBy").value(DEFAULT_REQUESTED_BY));
    }

    @Test
    @Transactional
    void getNonExistingBlockReportPost() throws Exception {
        // Get the blockReportPost
        restBlockReportPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBlockReportPost() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        blockReportPostSearchRepository.save(blockReportPost);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());

        // Update the blockReportPost
        BlockReportPost updatedBlockReportPost = blockReportPostRepository.findById(blockReportPost.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBlockReportPost are not directly saved in db
        em.detach(updatedBlockReportPost);
        updatedBlockReportPost
            .blockCategories(UPDATED_BLOCK_CATEGORIES)
            .blockingReason(UPDATED_BLOCKING_REASON)
            .postBlockReportId(UPDATED_POST_BLOCK_REPORT_ID)
            .postId(UPDATED_POST_ID)
            .postType(UPDATED_POST_TYPE)
            .requestedBy(UPDATED_REQUESTED_BY);

        restBlockReportPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlockReportPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBlockReportPost))
            )
            .andExpect(status().isOk());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBlockReportPostToMatchAllProperties(updatedBlockReportPost);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BlockReportPost> blockReportPostSearchList = Streamable.of(blockReportPostSearchRepository.findAll()).toList();
                BlockReportPost testBlockReportPostSearch = blockReportPostSearchList.get(searchDatabaseSizeAfter - 1);

                assertBlockReportPostAllPropertiesEquals(testBlockReportPostSearch, updatedBlockReportPost);
            });
    }

    @Test
    @Transactional
    void putNonExistingBlockReportPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        blockReportPost.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockReportPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blockReportPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blockReportPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlockReportPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        blockReportPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockReportPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blockReportPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlockReportPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        blockReportPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockReportPostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blockReportPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBlockReportPostWithPatch() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blockReportPost using partial update
        BlockReportPost partialUpdatedBlockReportPost = new BlockReportPost();
        partialUpdatedBlockReportPost.setId(blockReportPost.getId());

        partialUpdatedBlockReportPost.blockingReason(UPDATED_BLOCKING_REASON).requestedBy(UPDATED_REQUESTED_BY);

        restBlockReportPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlockReportPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlockReportPost))
            )
            .andExpect(status().isOk());

        // Validate the BlockReportPost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlockReportPostUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBlockReportPost, blockReportPost),
            getPersistedBlockReportPost(blockReportPost)
        );
    }

    @Test
    @Transactional
    void fullUpdateBlockReportPostWithPatch() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blockReportPost using partial update
        BlockReportPost partialUpdatedBlockReportPost = new BlockReportPost();
        partialUpdatedBlockReportPost.setId(blockReportPost.getId());

        partialUpdatedBlockReportPost
            .blockCategories(UPDATED_BLOCK_CATEGORIES)
            .blockingReason(UPDATED_BLOCKING_REASON)
            .postBlockReportId(UPDATED_POST_BLOCK_REPORT_ID)
            .postId(UPDATED_POST_ID)
            .postType(UPDATED_POST_TYPE)
            .requestedBy(UPDATED_REQUESTED_BY);

        restBlockReportPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlockReportPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlockReportPost))
            )
            .andExpect(status().isOk());

        // Validate the BlockReportPost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlockReportPostUpdatableFieldsEquals(
            partialUpdatedBlockReportPost,
            getPersistedBlockReportPost(partialUpdatedBlockReportPost)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBlockReportPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        blockReportPost.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockReportPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blockReportPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blockReportPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlockReportPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        blockReportPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockReportPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blockReportPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlockReportPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        blockReportPost.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockReportPostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blockReportPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlockReportPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBlockReportPost() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);
        blockReportPostRepository.save(blockReportPost);
        blockReportPostSearchRepository.save(blockReportPost);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the blockReportPost
        restBlockReportPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, blockReportPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blockReportPostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBlockReportPost() throws Exception {
        // Initialize the database
        insertedBlockReportPost = blockReportPostRepository.saveAndFlush(blockReportPost);
        blockReportPostSearchRepository.save(blockReportPost);

        // Search the blockReportPost
        restBlockReportPostMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + blockReportPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockReportPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].blockCategories").value(hasItem(DEFAULT_BLOCK_CATEGORIES.toString())))
            .andExpect(jsonPath("$.[*].blockingReason").value(hasItem(DEFAULT_BLOCKING_REASON.toString())))
            .andExpect(jsonPath("$.[*].postBlockReportId").value(hasItem(DEFAULT_POST_BLOCK_REPORT_ID.toString())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE)))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)));
    }

    protected long getRepositoryCount() {
        return blockReportPostRepository.count();
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

    protected BlockReportPost getPersistedBlockReportPost(BlockReportPost blockReportPost) {
        return blockReportPostRepository.findById(blockReportPost.getId()).orElseThrow();
    }

    protected void assertPersistedBlockReportPostToMatchAllProperties(BlockReportPost expectedBlockReportPost) {
        assertBlockReportPostAllPropertiesEquals(expectedBlockReportPost, getPersistedBlockReportPost(expectedBlockReportPost));
    }

    protected void assertPersistedBlockReportPostToMatchUpdatableProperties(BlockReportPost expectedBlockReportPost) {
        assertBlockReportPostAllUpdatablePropertiesEquals(expectedBlockReportPost, getPersistedBlockReportPost(expectedBlockReportPost));
    }
}
