package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.IocCategoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.IocCategory;
import com.mycompany.myapp.repository.IocCategoryRepository;
import com.mycompany.myapp.repository.search.IocCategorySearchRepository;
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
 * Integration tests for the {@link IocCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IocCategoryResourceIT {

    private static final String DEFAULT_IOC_CATEGORY_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_IOC_CATEGORY_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_IOC_CATEGORY_ID = 1;
    private static final Integer UPDATED_IOC_CATEGORY_ID = 2;

    private static final String DEFAULT_IOC_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IOC_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ioc-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ioc-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IocCategoryRepository iocCategoryRepository;

    @Autowired
    private IocCategorySearchRepository iocCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIocCategoryMockMvc;

    private IocCategory iocCategory;

    private IocCategory insertedIocCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IocCategory createEntity() {
        return new IocCategory()
            .iocCategoryColor(DEFAULT_IOC_CATEGORY_COLOR)
            .iocCategoryId(DEFAULT_IOC_CATEGORY_ID)
            .iocCategoryName(DEFAULT_IOC_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IocCategory createUpdatedEntity() {
        return new IocCategory()
            .iocCategoryColor(UPDATED_IOC_CATEGORY_COLOR)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .iocCategoryName(UPDATED_IOC_CATEGORY_NAME);
    }

    @BeforeEach
    public void initTest() {
        iocCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedIocCategory != null) {
            iocCategoryRepository.delete(insertedIocCategory);
            iocCategorySearchRepository.delete(insertedIocCategory);
            insertedIocCategory = null;
        }
    }

    @Test
    @Transactional
    void createIocCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        // Create the IocCategory
        var returnedIocCategory = om.readValue(
            restIocCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iocCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IocCategory.class
        );

        // Validate the IocCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIocCategoryUpdatableFieldsEquals(returnedIocCategory, getPersistedIocCategory(returnedIocCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedIocCategory = returnedIocCategory;
    }

    @Test
    @Transactional
    void createIocCategoryWithExistingId() throws Exception {
        // Create the IocCategory with an existing ID
        iocCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restIocCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iocCategory)))
            .andExpect(status().isBadRequest());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllIocCategories() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);

        // Get all the iocCategoryList
        restIocCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iocCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].iocCategoryColor").value(hasItem(DEFAULT_IOC_CATEGORY_COLOR)))
            .andExpect(jsonPath("$.[*].iocCategoryId").value(hasItem(DEFAULT_IOC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].iocCategoryName").value(hasItem(DEFAULT_IOC_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getIocCategory() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);

        // Get the iocCategory
        restIocCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, iocCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(iocCategory.getId().intValue()))
            .andExpect(jsonPath("$.iocCategoryColor").value(DEFAULT_IOC_CATEGORY_COLOR))
            .andExpect(jsonPath("$.iocCategoryId").value(DEFAULT_IOC_CATEGORY_ID))
            .andExpect(jsonPath("$.iocCategoryName").value(DEFAULT_IOC_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIocCategory() throws Exception {
        // Get the iocCategory
        restIocCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIocCategory() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        iocCategorySearchRepository.save(iocCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());

        // Update the iocCategory
        IocCategory updatedIocCategory = iocCategoryRepository.findById(iocCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIocCategory are not directly saved in db
        em.detach(updatedIocCategory);
        updatedIocCategory
            .iocCategoryColor(UPDATED_IOC_CATEGORY_COLOR)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .iocCategoryName(UPDATED_IOC_CATEGORY_NAME);

        restIocCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIocCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIocCategory))
            )
            .andExpect(status().isOk());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIocCategoryToMatchAllProperties(updatedIocCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<IocCategory> iocCategorySearchList = Streamable.of(iocCategorySearchRepository.findAll()).toList();
                IocCategory testIocCategorySearch = iocCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertIocCategoryAllPropertiesEquals(testIocCategorySearch, updatedIocCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingIocCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        iocCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIocCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, iocCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(iocCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchIocCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        iocCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIocCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(iocCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIocCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        iocCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIocCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(iocCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateIocCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the iocCategory using partial update
        IocCategory partialUpdatedIocCategory = new IocCategory();
        partialUpdatedIocCategory.setId(iocCategory.getId());

        restIocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIocCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIocCategory))
            )
            .andExpect(status().isOk());

        // Validate the IocCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIocCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIocCategory, iocCategory),
            getPersistedIocCategory(iocCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateIocCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the iocCategory using partial update
        IocCategory partialUpdatedIocCategory = new IocCategory();
        partialUpdatedIocCategory.setId(iocCategory.getId());

        partialUpdatedIocCategory
            .iocCategoryColor(UPDATED_IOC_CATEGORY_COLOR)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .iocCategoryName(UPDATED_IOC_CATEGORY_NAME);

        restIocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIocCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIocCategory))
            )
            .andExpect(status().isOk());

        // Validate the IocCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIocCategoryUpdatableFieldsEquals(partialUpdatedIocCategory, getPersistedIocCategory(partialUpdatedIocCategory));
    }

    @Test
    @Transactional
    void patchNonExistingIocCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        iocCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, iocCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(iocCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIocCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        iocCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(iocCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIocCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        iocCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIocCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(iocCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IocCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteIocCategory() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);
        iocCategoryRepository.save(iocCategory);
        iocCategorySearchRepository.save(iocCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the iocCategory
        restIocCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, iocCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(iocCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchIocCategory() throws Exception {
        // Initialize the database
        insertedIocCategory = iocCategoryRepository.saveAndFlush(iocCategory);
        iocCategorySearchRepository.save(iocCategory);

        // Search the iocCategory
        restIocCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + iocCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iocCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].iocCategoryColor").value(hasItem(DEFAULT_IOC_CATEGORY_COLOR)))
            .andExpect(jsonPath("$.[*].iocCategoryId").value(hasItem(DEFAULT_IOC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].iocCategoryName").value(hasItem(DEFAULT_IOC_CATEGORY_NAME)));
    }

    protected long getRepositoryCount() {
        return iocCategoryRepository.count();
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

    protected IocCategory getPersistedIocCategory(IocCategory iocCategory) {
        return iocCategoryRepository.findById(iocCategory.getId()).orElseThrow();
    }

    protected void assertPersistedIocCategoryToMatchAllProperties(IocCategory expectedIocCategory) {
        assertIocCategoryAllPropertiesEquals(expectedIocCategory, getPersistedIocCategory(expectedIocCategory));
    }

    protected void assertPersistedIocCategoryToMatchUpdatableProperties(IocCategory expectedIocCategory) {
        assertIocCategoryAllUpdatablePropertiesEquals(expectedIocCategory, getPersistedIocCategory(expectedIocCategory));
    }
}
