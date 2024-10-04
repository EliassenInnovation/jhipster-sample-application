package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SupportCategoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SupportCategory;
import com.mycompany.myapp.repository.SupportCategoryRepository;
import com.mycompany.myapp.repository.search.SupportCategorySearchRepository;
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
 * Integration tests for the {@link SupportCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupportCategoryResourceIT {

    private static final Integer DEFAULT_SUPPORT_CATEGORY_ID = 1;
    private static final Integer UPDATED_SUPPORT_CATEGORY_ID = 2;

    private static final String DEFAULT_SUPPORT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/support-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/support-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupportCategoryRepository supportCategoryRepository;

    @Autowired
    private SupportCategorySearchRepository supportCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupportCategoryMockMvc;

    private SupportCategory supportCategory;

    private SupportCategory insertedSupportCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportCategory createEntity() {
        return new SupportCategory().supportCategoryId(DEFAULT_SUPPORT_CATEGORY_ID).supportCategoryName(DEFAULT_SUPPORT_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportCategory createUpdatedEntity() {
        return new SupportCategory().supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID).supportCategoryName(UPDATED_SUPPORT_CATEGORY_NAME);
    }

    @BeforeEach
    public void initTest() {
        supportCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupportCategory != null) {
            supportCategoryRepository.delete(insertedSupportCategory);
            supportCategorySearchRepository.delete(insertedSupportCategory);
            insertedSupportCategory = null;
        }
    }

    @Test
    @Transactional
    void createSupportCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        // Create the SupportCategory
        var returnedSupportCategory = om.readValue(
            restSupportCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupportCategory.class
        );

        // Validate the SupportCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupportCategoryUpdatableFieldsEquals(returnedSupportCategory, getPersistedSupportCategory(returnedSupportCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSupportCategory = returnedSupportCategory;
    }

    @Test
    @Transactional
    void createSupportCategoryWithExistingId() throws Exception {
        // Create the SupportCategory with an existing ID
        supportCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportCategory)))
            .andExpect(status().isBadRequest());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSupportCategories() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);

        // Get all the supportCategoryList
        restSupportCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].supportCategoryId").value(hasItem(DEFAULT_SUPPORT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].supportCategoryName").value(hasItem(DEFAULT_SUPPORT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getSupportCategory() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);

        // Get the supportCategory
        restSupportCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, supportCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supportCategory.getId().intValue()))
            .andExpect(jsonPath("$.supportCategoryId").value(DEFAULT_SUPPORT_CATEGORY_ID))
            .andExpect(jsonPath("$.supportCategoryName").value(DEFAULT_SUPPORT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSupportCategory() throws Exception {
        // Get the supportCategory
        restSupportCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupportCategory() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportCategorySearchRepository.save(supportCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());

        // Update the supportCategory
        SupportCategory updatedSupportCategory = supportCategoryRepository.findById(supportCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupportCategory are not directly saved in db
        em.detach(updatedSupportCategory);
        updatedSupportCategory.supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID).supportCategoryName(UPDATED_SUPPORT_CATEGORY_NAME);

        restSupportCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupportCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupportCategory))
            )
            .andExpect(status().isOk());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupportCategoryToMatchAllProperties(updatedSupportCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SupportCategory> supportCategorySearchList = Streamable.of(supportCategorySearchRepository.findAll()).toList();
                SupportCategory testSupportCategorySearch = supportCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertSupportCategoryAllPropertiesEquals(testSupportCategorySearch, updatedSupportCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingSupportCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        supportCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supportCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupportCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        supportCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupportCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        supportCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSupportCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportCategory using partial update
        SupportCategory partialUpdatedSupportCategory = new SupportCategory();
        partialUpdatedSupportCategory.setId(supportCategory.getId());

        partialUpdatedSupportCategory.supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID);

        restSupportCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportCategory))
            )
            .andExpect(status().isOk());

        // Validate the SupportCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupportCategory, supportCategory),
            getPersistedSupportCategory(supportCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupportCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportCategory using partial update
        SupportCategory partialUpdatedSupportCategory = new SupportCategory();
        partialUpdatedSupportCategory.setId(supportCategory.getId());

        partialUpdatedSupportCategory.supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID).supportCategoryName(UPDATED_SUPPORT_CATEGORY_NAME);

        restSupportCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportCategory))
            )
            .andExpect(status().isOk());

        // Validate the SupportCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportCategoryUpdatableFieldsEquals(
            partialUpdatedSupportCategory,
            getPersistedSupportCategory(partialUpdatedSupportCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSupportCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        supportCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supportCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupportCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        supportCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupportCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        supportCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supportCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSupportCategory() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);
        supportCategoryRepository.save(supportCategory);
        supportCategorySearchRepository.save(supportCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the supportCategory
        restSupportCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, supportCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSupportCategory() throws Exception {
        // Initialize the database
        insertedSupportCategory = supportCategoryRepository.saveAndFlush(supportCategory);
        supportCategorySearchRepository.save(supportCategory);

        // Search the supportCategory
        restSupportCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + supportCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].supportCategoryId").value(hasItem(DEFAULT_SUPPORT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].supportCategoryName").value(hasItem(DEFAULT_SUPPORT_CATEGORY_NAME)));
    }

    protected long getRepositoryCount() {
        return supportCategoryRepository.count();
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

    protected SupportCategory getPersistedSupportCategory(SupportCategory supportCategory) {
        return supportCategoryRepository.findById(supportCategory.getId()).orElseThrow();
    }

    protected void assertPersistedSupportCategoryToMatchAllProperties(SupportCategory expectedSupportCategory) {
        assertSupportCategoryAllPropertiesEquals(expectedSupportCategory, getPersistedSupportCategory(expectedSupportCategory));
    }

    protected void assertPersistedSupportCategoryToMatchUpdatableProperties(SupportCategory expectedSupportCategory) {
        assertSupportCategoryAllUpdatablePropertiesEquals(expectedSupportCategory, getPersistedSupportCategory(expectedSupportCategory));
    }
}
