package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.H7KeywordMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.H7KeywordMst;
import com.mycompany.myapp.repository.H7KeywordMstRepository;
import com.mycompany.myapp.repository.search.H7KeywordMstSearchRepository;
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
 * Integration tests for the {@link H7KeywordMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class H7KeywordMstResourceIT {

    private static final String DEFAULT_H_7_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_H_7_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_H_7_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_H_7_KEYWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_H_7_KEYWORD_ID = 1;
    private static final Integer UPDATED_H_7_KEYWORD_ID = 2;

    private static final String DEFAULT_IOC_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_IOC_GROUP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/h-7-keyword-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/h-7-keyword-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private H7KeywordMstRepository h7KeywordMstRepository;

    @Autowired
    private H7KeywordMstSearchRepository h7KeywordMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restH7KeywordMstMockMvc;

    private H7KeywordMst h7KeywordMst;

    private H7KeywordMst insertedH7KeywordMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static H7KeywordMst createEntity() {
        return new H7KeywordMst()
            .h7Group(DEFAULT_H_7_GROUP)
            .h7Keyword(DEFAULT_H_7_KEYWORD)
            .h7keywordId(DEFAULT_H_7_KEYWORD_ID)
            .iocGroup(DEFAULT_IOC_GROUP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static H7KeywordMst createUpdatedEntity() {
        return new H7KeywordMst()
            .h7Group(UPDATED_H_7_GROUP)
            .h7Keyword(UPDATED_H_7_KEYWORD)
            .h7keywordId(UPDATED_H_7_KEYWORD_ID)
            .iocGroup(UPDATED_IOC_GROUP);
    }

    @BeforeEach
    public void initTest() {
        h7KeywordMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedH7KeywordMst != null) {
            h7KeywordMstRepository.delete(insertedH7KeywordMst);
            h7KeywordMstSearchRepository.delete(insertedH7KeywordMst);
            insertedH7KeywordMst = null;
        }
    }

    @Test
    @Transactional
    void createH7KeywordMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        // Create the H7KeywordMst
        var returnedH7KeywordMst = om.readValue(
            restH7KeywordMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(h7KeywordMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            H7KeywordMst.class
        );

        // Validate the H7KeywordMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertH7KeywordMstUpdatableFieldsEquals(returnedH7KeywordMst, getPersistedH7KeywordMst(returnedH7KeywordMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedH7KeywordMst = returnedH7KeywordMst;
    }

    @Test
    @Transactional
    void createH7KeywordMstWithExistingId() throws Exception {
        // Create the H7KeywordMst with an existing ID
        h7KeywordMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restH7KeywordMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(h7KeywordMst)))
            .andExpect(status().isBadRequest());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllH7KeywordMsts() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);

        // Get all the h7KeywordMstList
        restH7KeywordMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(h7KeywordMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].h7Group").value(hasItem(DEFAULT_H_7_GROUP)))
            .andExpect(jsonPath("$.[*].h7Keyword").value(hasItem(DEFAULT_H_7_KEYWORD)))
            .andExpect(jsonPath("$.[*].h7keywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iocGroup").value(hasItem(DEFAULT_IOC_GROUP)));
    }

    @Test
    @Transactional
    void getH7KeywordMst() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);

        // Get the h7KeywordMst
        restH7KeywordMstMockMvc
            .perform(get(ENTITY_API_URL_ID, h7KeywordMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(h7KeywordMst.getId().intValue()))
            .andExpect(jsonPath("$.h7Group").value(DEFAULT_H_7_GROUP))
            .andExpect(jsonPath("$.h7Keyword").value(DEFAULT_H_7_KEYWORD))
            .andExpect(jsonPath("$.h7keywordId").value(DEFAULT_H_7_KEYWORD_ID))
            .andExpect(jsonPath("$.iocGroup").value(DEFAULT_IOC_GROUP));
    }

    @Test
    @Transactional
    void getNonExistingH7KeywordMst() throws Exception {
        // Get the h7KeywordMst
        restH7KeywordMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingH7KeywordMst() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        h7KeywordMstSearchRepository.save(h7KeywordMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());

        // Update the h7KeywordMst
        H7KeywordMst updatedH7KeywordMst = h7KeywordMstRepository.findById(h7KeywordMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedH7KeywordMst are not directly saved in db
        em.detach(updatedH7KeywordMst);
        updatedH7KeywordMst
            .h7Group(UPDATED_H_7_GROUP)
            .h7Keyword(UPDATED_H_7_KEYWORD)
            .h7keywordId(UPDATED_H_7_KEYWORD_ID)
            .iocGroup(UPDATED_IOC_GROUP);

        restH7KeywordMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedH7KeywordMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedH7KeywordMst))
            )
            .andExpect(status().isOk());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedH7KeywordMstToMatchAllProperties(updatedH7KeywordMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<H7KeywordMst> h7KeywordMstSearchList = Streamable.of(h7KeywordMstSearchRepository.findAll()).toList();
                H7KeywordMst testH7KeywordMstSearch = h7KeywordMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertH7KeywordMstAllPropertiesEquals(testH7KeywordMstSearch, updatedH7KeywordMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingH7KeywordMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        h7KeywordMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restH7KeywordMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, h7KeywordMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(h7KeywordMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchH7KeywordMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        h7KeywordMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restH7KeywordMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(h7KeywordMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamH7KeywordMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        h7KeywordMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restH7KeywordMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(h7KeywordMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateH7KeywordMstWithPatch() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the h7KeywordMst using partial update
        H7KeywordMst partialUpdatedH7KeywordMst = new H7KeywordMst();
        partialUpdatedH7KeywordMst.setId(h7KeywordMst.getId());

        partialUpdatedH7KeywordMst.h7Keyword(UPDATED_H_7_KEYWORD);

        restH7KeywordMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedH7KeywordMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedH7KeywordMst))
            )
            .andExpect(status().isOk());

        // Validate the H7KeywordMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertH7KeywordMstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedH7KeywordMst, h7KeywordMst),
            getPersistedH7KeywordMst(h7KeywordMst)
        );
    }

    @Test
    @Transactional
    void fullUpdateH7KeywordMstWithPatch() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the h7KeywordMst using partial update
        H7KeywordMst partialUpdatedH7KeywordMst = new H7KeywordMst();
        partialUpdatedH7KeywordMst.setId(h7KeywordMst.getId());

        partialUpdatedH7KeywordMst
            .h7Group(UPDATED_H_7_GROUP)
            .h7Keyword(UPDATED_H_7_KEYWORD)
            .h7keywordId(UPDATED_H_7_KEYWORD_ID)
            .iocGroup(UPDATED_IOC_GROUP);

        restH7KeywordMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedH7KeywordMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedH7KeywordMst))
            )
            .andExpect(status().isOk());

        // Validate the H7KeywordMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertH7KeywordMstUpdatableFieldsEquals(partialUpdatedH7KeywordMst, getPersistedH7KeywordMst(partialUpdatedH7KeywordMst));
    }

    @Test
    @Transactional
    void patchNonExistingH7KeywordMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        h7KeywordMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restH7KeywordMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, h7KeywordMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(h7KeywordMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchH7KeywordMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        h7KeywordMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restH7KeywordMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(h7KeywordMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamH7KeywordMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        h7KeywordMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restH7KeywordMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(h7KeywordMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the H7KeywordMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteH7KeywordMst() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);
        h7KeywordMstRepository.save(h7KeywordMst);
        h7KeywordMstSearchRepository.save(h7KeywordMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the h7KeywordMst
        restH7KeywordMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, h7KeywordMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(h7KeywordMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchH7KeywordMst() throws Exception {
        // Initialize the database
        insertedH7KeywordMst = h7KeywordMstRepository.saveAndFlush(h7KeywordMst);
        h7KeywordMstSearchRepository.save(h7KeywordMst);

        // Search the h7KeywordMst
        restH7KeywordMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + h7KeywordMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(h7KeywordMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].h7Group").value(hasItem(DEFAULT_H_7_GROUP)))
            .andExpect(jsonPath("$.[*].h7Keyword").value(hasItem(DEFAULT_H_7_KEYWORD)))
            .andExpect(jsonPath("$.[*].h7keywordId").value(hasItem(DEFAULT_H_7_KEYWORD_ID)))
            .andExpect(jsonPath("$.[*].iocGroup").value(hasItem(DEFAULT_IOC_GROUP)));
    }

    protected long getRepositoryCount() {
        return h7KeywordMstRepository.count();
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

    protected H7KeywordMst getPersistedH7KeywordMst(H7KeywordMst h7KeywordMst) {
        return h7KeywordMstRepository.findById(h7KeywordMst.getId()).orElseThrow();
    }

    protected void assertPersistedH7KeywordMstToMatchAllProperties(H7KeywordMst expectedH7KeywordMst) {
        assertH7KeywordMstAllPropertiesEquals(expectedH7KeywordMst, getPersistedH7KeywordMst(expectedH7KeywordMst));
    }

    protected void assertPersistedH7KeywordMstToMatchUpdatableProperties(H7KeywordMst expectedH7KeywordMst) {
        assertH7KeywordMstAllUpdatablePropertiesEquals(expectedH7KeywordMst, getPersistedH7KeywordMst(expectedH7KeywordMst));
    }
}
