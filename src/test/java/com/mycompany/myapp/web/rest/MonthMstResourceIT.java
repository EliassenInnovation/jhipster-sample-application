package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MonthMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MonthMst;
import com.mycompany.myapp.repository.MonthMstRepository;
import com.mycompany.myapp.repository.search.MonthMstSearchRepository;
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
 * Integration tests for the {@link MonthMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonthMstResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_MONTH_ID = 1;
    private static final Integer UPDATED_MONTH_ID = 2;

    private static final String DEFAULT_MONTH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MONTH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/month-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/month-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonthMstRepository monthMstRepository;

    @Autowired
    private MonthMstSearchRepository monthMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonthMstMockMvc;

    private MonthMst monthMst;

    private MonthMst insertedMonthMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthMst createEntity() {
        return new MonthMst().isActive(DEFAULT_IS_ACTIVE).monthID(DEFAULT_MONTH_ID).monthName(DEFAULT_MONTH_NAME).year(DEFAULT_YEAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthMst createUpdatedEntity() {
        return new MonthMst().isActive(UPDATED_IS_ACTIVE).monthID(UPDATED_MONTH_ID).monthName(UPDATED_MONTH_NAME).year(UPDATED_YEAR);
    }

    @BeforeEach
    public void initTest() {
        monthMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMonthMst != null) {
            monthMstRepository.delete(insertedMonthMst);
            monthMstSearchRepository.delete(insertedMonthMst);
            insertedMonthMst = null;
        }
    }

    @Test
    @Transactional
    void createMonthMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        // Create the MonthMst
        var returnedMonthMst = om.readValue(
            restMonthMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonthMst.class
        );

        // Validate the MonthMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMonthMstUpdatableFieldsEquals(returnedMonthMst, getPersistedMonthMst(returnedMonthMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMonthMst = returnedMonthMst;
    }

    @Test
    @Transactional
    void createMonthMstWithExistingId() throws Exception {
        // Create the MonthMst with an existing ID
        monthMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonthMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthMst)))
            .andExpect(status().isBadRequest());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMonthMsts() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);

        // Get all the monthMstList
        restMonthMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].monthID").value(hasItem(DEFAULT_MONTH_ID)))
            .andExpect(jsonPath("$.[*].monthName").value(hasItem(DEFAULT_MONTH_NAME)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getMonthMst() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);

        // Get the monthMst
        restMonthMstMockMvc
            .perform(get(ENTITY_API_URL_ID, monthMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monthMst.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.monthID").value(DEFAULT_MONTH_ID))
            .andExpect(jsonPath("$.monthName").value(DEFAULT_MONTH_NAME))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingMonthMst() throws Exception {
        // Get the monthMst
        restMonthMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonthMst() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        monthMstSearchRepository.save(monthMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());

        // Update the monthMst
        MonthMst updatedMonthMst = monthMstRepository.findById(monthMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMonthMst are not directly saved in db
        em.detach(updatedMonthMst);
        updatedMonthMst.isActive(UPDATED_IS_ACTIVE).monthID(UPDATED_MONTH_ID).monthName(UPDATED_MONTH_NAME).year(UPDATED_YEAR);

        restMonthMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMonthMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMonthMst))
            )
            .andExpect(status().isOk());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonthMstToMatchAllProperties(updatedMonthMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MonthMst> monthMstSearchList = Streamable.of(monthMstSearchRepository.findAll()).toList();
                MonthMst testMonthMstSearch = monthMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertMonthMstAllPropertiesEquals(testMonthMstSearch, updatedMonthMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingMonthMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        monthMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monthMst.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonthMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        monthMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monthMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonthMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        monthMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMonthMstWithPatch() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monthMst using partial update
        MonthMst partialUpdatedMonthMst = new MonthMst();
        partialUpdatedMonthMst.setId(monthMst.getId());

        partialUpdatedMonthMst.monthName(UPDATED_MONTH_NAME);

        restMonthMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonthMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonthMst))
            )
            .andExpect(status().isOk());

        // Validate the MonthMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonthMstUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMonthMst, monthMst), getPersistedMonthMst(monthMst));
    }

    @Test
    @Transactional
    void fullUpdateMonthMstWithPatch() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monthMst using partial update
        MonthMst partialUpdatedMonthMst = new MonthMst();
        partialUpdatedMonthMst.setId(monthMst.getId());

        partialUpdatedMonthMst.isActive(UPDATED_IS_ACTIVE).monthID(UPDATED_MONTH_ID).monthName(UPDATED_MONTH_NAME).year(UPDATED_YEAR);

        restMonthMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonthMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonthMst))
            )
            .andExpect(status().isOk());

        // Validate the MonthMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonthMstUpdatableFieldsEquals(partialUpdatedMonthMst, getPersistedMonthMst(partialUpdatedMonthMst));
    }

    @Test
    @Transactional
    void patchNonExistingMonthMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        monthMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monthMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monthMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonthMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        monthMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monthMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonthMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        monthMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monthMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonthMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMonthMst() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);
        monthMstRepository.save(monthMst);
        monthMstSearchRepository.save(monthMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the monthMst
        restMonthMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, monthMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMonthMst() throws Exception {
        // Initialize the database
        insertedMonthMst = monthMstRepository.saveAndFlush(monthMst);
        monthMstSearchRepository.save(monthMst);

        // Search the monthMst
        restMonthMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + monthMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].monthID").value(hasItem(DEFAULT_MONTH_ID)))
            .andExpect(jsonPath("$.[*].monthName").value(hasItem(DEFAULT_MONTH_NAME)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    protected long getRepositoryCount() {
        return monthMstRepository.count();
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

    protected MonthMst getPersistedMonthMst(MonthMst monthMst) {
        return monthMstRepository.findById(monthMst.getId()).orElseThrow();
    }

    protected void assertPersistedMonthMstToMatchAllProperties(MonthMst expectedMonthMst) {
        assertMonthMstAllPropertiesEquals(expectedMonthMst, getPersistedMonthMst(expectedMonthMst));
    }

    protected void assertPersistedMonthMstToMatchUpdatableProperties(MonthMst expectedMonthMst) {
        assertMonthMstAllUpdatablePropertiesEquals(expectedMonthMst, getPersistedMonthMst(expectedMonthMst));
    }
}
