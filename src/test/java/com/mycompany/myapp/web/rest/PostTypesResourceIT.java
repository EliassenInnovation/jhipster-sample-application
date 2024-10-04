package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PostTypesAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PostTypes;
import com.mycompany.myapp.repository.PostTypesRepository;
import com.mycompany.myapp.repository.search.PostTypesSearchRepository;
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
 * Integration tests for the {@link PostTypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostTypesResourceIT {

    private static final UUID DEFAULT_CREATED_BY = UUID.randomUUID();
    private static final UUID UPDATED_CREATED_BY = UUID.randomUUID();

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final UUID DEFAULT_LAST_UPDATED_BY = UUID.randomUUID();
    private static final UUID UPDATED_LAST_UPDATED_BY = UUID.randomUUID();

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_POST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_POST_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POST_TYPE_ID = 1;
    private static final Integer UPDATED_POST_TYPE_ID = 2;

    private static final String ENTITY_API_URL = "/api/post-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/post-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostTypesRepository postTypesRepository;

    @Autowired
    private PostTypesSearchRepository postTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostTypesMockMvc;

    private PostTypes postTypes;

    private PostTypes insertedPostTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostTypes createEntity() {
        return new PostTypes()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON)
            .postType(DEFAULT_POST_TYPE)
            .postTypeId(DEFAULT_POST_TYPE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostTypes createUpdatedEntity() {
        return new PostTypes()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .postType(UPDATED_POST_TYPE)
            .postTypeId(UPDATED_POST_TYPE_ID);
    }

    @BeforeEach
    public void initTest() {
        postTypes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPostTypes != null) {
            postTypesRepository.delete(insertedPostTypes);
            postTypesSearchRepository.delete(insertedPostTypes);
            insertedPostTypes = null;
        }
    }

    @Test
    @Transactional
    void createPostTypes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        // Create the PostTypes
        var returnedPostTypes = om.readValue(
            restPostTypesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postTypes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostTypes.class
        );

        // Validate the PostTypes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPostTypesUpdatableFieldsEquals(returnedPostTypes, getPersistedPostTypes(returnedPostTypes));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPostTypes = returnedPostTypes;
    }

    @Test
    @Transactional
    void createPostTypesWithExistingId() throws Exception {
        // Create the PostTypes with an existing ID
        postTypes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postTypes)))
            .andExpect(status().isBadRequest());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPostTypes() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);

        // Get all the postTypesList
        restPostTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE)))
            .andExpect(jsonPath("$.[*].postTypeId").value(hasItem(DEFAULT_POST_TYPE_ID)));
    }

    @Test
    @Transactional
    void getPostTypes() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);

        // Get the postTypes
        restPostTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, postTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postTypes.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.toString()))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.postType").value(DEFAULT_POST_TYPE))
            .andExpect(jsonPath("$.postTypeId").value(DEFAULT_POST_TYPE_ID));
    }

    @Test
    @Transactional
    void getNonExistingPostTypes() throws Exception {
        // Get the postTypes
        restPostTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostTypes() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        postTypesSearchRepository.save(postTypes);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());

        // Update the postTypes
        PostTypes updatedPostTypes = postTypesRepository.findById(postTypes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostTypes are not directly saved in db
        em.detach(updatedPostTypes);
        updatedPostTypes
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .postType(UPDATED_POST_TYPE)
            .postTypeId(UPDATED_POST_TYPE_ID);

        restPostTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPostTypes))
            )
            .andExpect(status().isOk());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostTypesToMatchAllProperties(updatedPostTypes);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PostTypes> postTypesSearchList = Streamable.of(postTypesSearchRepository.findAll()).toList();
                PostTypes testPostTypesSearch = postTypesSearchList.get(searchDatabaseSizeAfter - 1);

                assertPostTypesAllPropertiesEquals(testPostTypesSearch, updatedPostTypes);
            });
    }

    @Test
    @Transactional
    void putNonExistingPostTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        postTypes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postTypes.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        postTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        postTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostTypesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePostTypesWithPatch() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postTypes using partial update
        PostTypes partialUpdatedPostTypes = new PostTypes();
        partialUpdatedPostTypes.setId(postTypes.getId());

        partialUpdatedPostTypes.isActive(UPDATED_IS_ACTIVE).lastUpdatedBy(UPDATED_LAST_UPDATED_BY);

        restPostTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostTypes))
            )
            .andExpect(status().isOk());

        // Validate the PostTypes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostTypesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostTypes, postTypes),
            getPersistedPostTypes(postTypes)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostTypesWithPatch() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postTypes using partial update
        PostTypes partialUpdatedPostTypes = new PostTypes();
        partialUpdatedPostTypes.setId(postTypes.getId());

        partialUpdatedPostTypes
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .postType(UPDATED_POST_TYPE)
            .postTypeId(UPDATED_POST_TYPE_ID);

        restPostTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostTypes))
            )
            .andExpect(status().isOk());

        // Validate the PostTypes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostTypesUpdatableFieldsEquals(partialUpdatedPostTypes, getPersistedPostTypes(partialUpdatedPostTypes));
    }

    @Test
    @Transactional
    void patchNonExistingPostTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        postTypes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        postTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostTypes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        postTypes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostTypesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostTypes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePostTypes() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);
        postTypesRepository.save(postTypes);
        postTypesSearchRepository.save(postTypes);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the postTypes
        restPostTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, postTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postTypesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPostTypes() throws Exception {
        // Initialize the database
        insertedPostTypes = postTypesRepository.saveAndFlush(postTypes);
        postTypesSearchRepository.save(postTypes);

        // Search the postTypes
        restPostTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + postTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE)))
            .andExpect(jsonPath("$.[*].postTypeId").value(hasItem(DEFAULT_POST_TYPE_ID)));
    }

    protected long getRepositoryCount() {
        return postTypesRepository.count();
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

    protected PostTypes getPersistedPostTypes(PostTypes postTypes) {
        return postTypesRepository.findById(postTypes.getId()).orElseThrow();
    }

    protected void assertPersistedPostTypesToMatchAllProperties(PostTypes expectedPostTypes) {
        assertPostTypesAllPropertiesEquals(expectedPostTypes, getPersistedPostTypes(expectedPostTypes));
    }

    protected void assertPersistedPostTypesToMatchUpdatableProperties(PostTypes expectedPostTypes) {
        assertPostTypesAllUpdatablePropertiesEquals(expectedPostTypes, getPersistedPostTypes(expectedPostTypes));
    }
}
