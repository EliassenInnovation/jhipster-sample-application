package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SubCategoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SubCategory;
import com.mycompany.myapp.repository.SubCategoryRepository;
import com.mycompany.myapp.repository.search.SubCategorySearchRepository;
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
 * Integration tests for the {@link SubCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubCategoryResourceIT {

    private static final Integer DEFAULT_CATEGORY_ID = 1;
    private static final Integer UPDATED_CATEGORY_ID = 2;

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_SUB_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUB_CATEGORY_ID = 1;
    private static final Integer UPDATED_SUB_CATEGORY_ID = 2;

    private static final String DEFAULT_SUB_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/sub-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/sub-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private SubCategorySearchRepository subCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoryMockMvc;

    private SubCategory subCategory;

    private SubCategory insertedSubCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createEntity() {
        return new SubCategory()
            .categoryId(DEFAULT_CATEGORY_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .subCategoryCode(DEFAULT_SUB_CATEGORY_CODE)
            .subCategoryId(DEFAULT_SUB_CATEGORY_ID)
            .subCategoryName(DEFAULT_SUB_CATEGORY_NAME)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createUpdatedEntity() {
        return new SubCategory()
            .categoryId(UPDATED_CATEGORY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryId(UPDATED_SUB_CATEGORY_ID)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        subCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubCategory != null) {
            subCategoryRepository.delete(insertedSubCategory);
            subCategorySearchRepository.delete(insertedSubCategory);
            insertedSubCategory = null;
        }
    }

    @Test
    @Transactional
    void createSubCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        // Create the SubCategory
        var returnedSubCategory = om.readValue(
            restSubCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubCategory.class
        );

        // Validate the SubCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubCategoryUpdatableFieldsEquals(returnedSubCategory, getPersistedSubCategory(returnedSubCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSubCategory = returnedSubCategory;
    }

    @Test
    @Transactional
    void createSubCategoryWithExistingId() throws Exception {
        // Create the SubCategory with an existing ID
        subCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subCategory)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSubCategories() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].subCategoryCode").value(hasItem(DEFAULT_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryId").value(hasItem(DEFAULT_SUB_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].subCategoryName").value(hasItem(DEFAULT_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getSubCategory() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategory
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.subCategoryCode").value(DEFAULT_SUB_CATEGORY_CODE))
            .andExpect(jsonPath("$.subCategoryId").value(DEFAULT_SUB_CATEGORY_ID))
            .andExpect(jsonPath("$.subCategoryName").value(DEFAULT_SUB_CATEGORY_NAME))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubCategory() throws Exception {
        // Get the subCategory
        restSubCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubCategory() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        subCategorySearchRepository.save(subCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());

        // Update the subCategory
        SubCategory updatedSubCategory = subCategoryRepository.findById(subCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubCategory are not directly saved in db
        em.detach(updatedSubCategory);
        updatedSubCategory
            .categoryId(UPDATED_CATEGORY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryId(UPDATED_SUB_CATEGORY_ID)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubCategoryToMatchAllProperties(updatedSubCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SubCategory> subCategorySearchList = Streamable.of(subCategorySearchRepository.findAll()).toList();
                SubCategory testSubCategorySearch = subCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertSubCategoryAllPropertiesEquals(testSubCategorySearch, updatedSubCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSubCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subCategory using partial update
        SubCategory partialUpdatedSubCategory = new SubCategory();
        partialUpdatedSubCategory.setId(subCategory.getId());

        partialUpdatedSubCategory
            .categoryId(UPDATED_CATEGORY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .updatedOn(UPDATED_UPDATED_ON);

        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubCategory, subCategory),
            getPersistedSubCategory(subCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subCategory using partial update
        SubCategory partialUpdatedSubCategory = new SubCategory();
        partialUpdatedSubCategory.setId(subCategory.getId());

        partialUpdatedSubCategory
            .categoryId(UPDATED_CATEGORY_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .subCategoryCode(UPDATED_SUB_CATEGORY_CODE)
            .subCategoryId(UPDATED_SUB_CATEGORY_ID)
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubCategoryUpdatableFieldsEquals(partialUpdatedSubCategory, getPersistedSubCategory(partialUpdatedSubCategory));
    }

    @Test
    @Transactional
    void patchNonExistingSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSubCategory() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);
        subCategoryRepository.save(subCategory);
        subCategorySearchRepository.save(subCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the subCategory
        restSubCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, subCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSubCategory() throws Exception {
        // Initialize the database
        insertedSubCategory = subCategoryRepository.saveAndFlush(subCategory);
        subCategorySearchRepository.save(subCategory);

        // Search the subCategory
        restSubCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].subCategoryCode").value(hasItem(DEFAULT_SUB_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].subCategoryId").value(hasItem(DEFAULT_SUB_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].subCategoryName").value(hasItem(DEFAULT_SUB_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return subCategoryRepository.count();
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

    protected SubCategory getPersistedSubCategory(SubCategory subCategory) {
        return subCategoryRepository.findById(subCategory.getId()).orElseThrow();
    }

    protected void assertPersistedSubCategoryToMatchAllProperties(SubCategory expectedSubCategory) {
        assertSubCategoryAllPropertiesEquals(expectedSubCategory, getPersistedSubCategory(expectedSubCategory));
    }

    protected void assertPersistedSubCategoryToMatchUpdatableProperties(SubCategory expectedSubCategory) {
        assertSubCategoryAllUpdatablePropertiesEquals(expectedSubCategory, getPersistedSubCategory(expectedSubCategory));
    }
}
