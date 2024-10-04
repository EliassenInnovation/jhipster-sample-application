package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MonthlyNumbersAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MonthlyNumbers;
import com.mycompany.myapp.repository.MonthlyNumbersRepository;
import com.mycompany.myapp.repository.search.MonthlyNumbersSearchRepository;
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
 * Integration tests for the {@link MonthlyNumbersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonthlyNumbersResourceIT {

    private static final Integer DEFAULT_ACTUAL_MONTH_ID = 1;
    private static final Integer UPDATED_ACTUAL_MONTH_ID = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_ENROLLMENT = 1;
    private static final Integer UPDATED_ENROLLMENT = 2;

    private static final Integer DEFAULT_FREE_AND_REDUCED_PERCENT = 1;
    private static final Integer UPDATED_FREE_AND_REDUCED_PERCENT = 2;

    private static final Integer DEFAULT_I_D = 1;
    private static final Integer UPDATED_I_D = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_MEALS_SERVED = 1;
    private static final Integer UPDATED_MEALS_SERVED = 2;

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_MONTH_ID = 1;
    private static final Integer UPDATED_MONTH_ID = 2;

    private static final Integer DEFAULT_NUMBER_OF_DISTRICTS = 1;
    private static final Integer UPDATED_NUMBER_OF_DISTRICTS = 2;

    private static final Integer DEFAULT_NUMBER_OF_SITES = 1;
    private static final Integer UPDATED_NUMBER_OF_SITES = 2;

    private static final LocalDate DEFAULT_REG_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REG_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/monthly-numbers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/monthly-numbers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonthlyNumbersRepository monthlyNumbersRepository;

    @Autowired
    private MonthlyNumbersSearchRepository monthlyNumbersSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonthlyNumbersMockMvc;

    private MonthlyNumbers monthlyNumbers;

    private MonthlyNumbers insertedMonthlyNumbers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyNumbers createEntity() {
        return new MonthlyNumbers()
            .actualMonthId(DEFAULT_ACTUAL_MONTH_ID)
            .createdOn(DEFAULT_CREATED_ON)
            .enrollment(DEFAULT_ENROLLMENT)
            .freeAndReducedPercent(DEFAULT_FREE_AND_REDUCED_PERCENT)
            .iD(DEFAULT_I_D)
            .isActive(DEFAULT_IS_ACTIVE)
            .mealsServed(DEFAULT_MEALS_SERVED)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .monthId(DEFAULT_MONTH_ID)
            .numberOfDistricts(DEFAULT_NUMBER_OF_DISTRICTS)
            .numberOfSites(DEFAULT_NUMBER_OF_SITES)
            .regDate(DEFAULT_REG_DATE)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .year(DEFAULT_YEAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonthlyNumbers createUpdatedEntity() {
        return new MonthlyNumbers()
            .actualMonthId(UPDATED_ACTUAL_MONTH_ID)
            .createdOn(UPDATED_CREATED_ON)
            .enrollment(UPDATED_ENROLLMENT)
            .freeAndReducedPercent(UPDATED_FREE_AND_REDUCED_PERCENT)
            .iD(UPDATED_I_D)
            .isActive(UPDATED_IS_ACTIVE)
            .mealsServed(UPDATED_MEALS_SERVED)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .monthId(UPDATED_MONTH_ID)
            .numberOfDistricts(UPDATED_NUMBER_OF_DISTRICTS)
            .numberOfSites(UPDATED_NUMBER_OF_SITES)
            .regDate(UPDATED_REG_DATE)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .year(UPDATED_YEAR);
    }

    @BeforeEach
    public void initTest() {
        monthlyNumbers = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMonthlyNumbers != null) {
            monthlyNumbersRepository.delete(insertedMonthlyNumbers);
            monthlyNumbersSearchRepository.delete(insertedMonthlyNumbers);
            insertedMonthlyNumbers = null;
        }
    }

    @Test
    @Transactional
    void createMonthlyNumbers() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        // Create the MonthlyNumbers
        var returnedMonthlyNumbers = om.readValue(
            restMonthlyNumbersMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthlyNumbers)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonthlyNumbers.class
        );

        // Validate the MonthlyNumbers in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMonthlyNumbersUpdatableFieldsEquals(returnedMonthlyNumbers, getPersistedMonthlyNumbers(returnedMonthlyNumbers));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMonthlyNumbers = returnedMonthlyNumbers;
    }

    @Test
    @Transactional
    void createMonthlyNumbersWithExistingId() throws Exception {
        // Create the MonthlyNumbers with an existing ID
        monthlyNumbers.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonthlyNumbersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthlyNumbers)))
            .andExpect(status().isBadRequest());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMonthlyNumbers() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);

        // Get all the monthlyNumbersList
        restMonthlyNumbersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyNumbers.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualMonthId").value(hasItem(DEFAULT_ACTUAL_MONTH_ID)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].enrollment").value(hasItem(DEFAULT_ENROLLMENT)))
            .andExpect(jsonPath("$.[*].freeAndReducedPercent").value(hasItem(DEFAULT_FREE_AND_REDUCED_PERCENT)))
            .andExpect(jsonPath("$.[*].iD").value(hasItem(DEFAULT_I_D)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mealsServed").value(hasItem(DEFAULT_MEALS_SERVED)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].monthId").value(hasItem(DEFAULT_MONTH_ID)))
            .andExpect(jsonPath("$.[*].numberOfDistricts").value(hasItem(DEFAULT_NUMBER_OF_DISTRICTS)))
            .andExpect(jsonPath("$.[*].numberOfSites").value(hasItem(DEFAULT_NUMBER_OF_SITES)))
            .andExpect(jsonPath("$.[*].regDate").value(hasItem(DEFAULT_REG_DATE.toString())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getMonthlyNumbers() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);

        // Get the monthlyNumbers
        restMonthlyNumbersMockMvc
            .perform(get(ENTITY_API_URL_ID, monthlyNumbers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monthlyNumbers.getId().intValue()))
            .andExpect(jsonPath("$.actualMonthId").value(DEFAULT_ACTUAL_MONTH_ID))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.enrollment").value(DEFAULT_ENROLLMENT))
            .andExpect(jsonPath("$.freeAndReducedPercent").value(DEFAULT_FREE_AND_REDUCED_PERCENT))
            .andExpect(jsonPath("$.iD").value(DEFAULT_I_D))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.mealsServed").value(DEFAULT_MEALS_SERVED))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.monthId").value(DEFAULT_MONTH_ID))
            .andExpect(jsonPath("$.numberOfDistricts").value(DEFAULT_NUMBER_OF_DISTRICTS))
            .andExpect(jsonPath("$.numberOfSites").value(DEFAULT_NUMBER_OF_SITES))
            .andExpect(jsonPath("$.regDate").value(DEFAULT_REG_DATE.toString()))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingMonthlyNumbers() throws Exception {
        // Get the monthlyNumbers
        restMonthlyNumbersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonthlyNumbers() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        monthlyNumbersSearchRepository.save(monthlyNumbers);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());

        // Update the monthlyNumbers
        MonthlyNumbers updatedMonthlyNumbers = monthlyNumbersRepository.findById(monthlyNumbers.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMonthlyNumbers are not directly saved in db
        em.detach(updatedMonthlyNumbers);
        updatedMonthlyNumbers
            .actualMonthId(UPDATED_ACTUAL_MONTH_ID)
            .createdOn(UPDATED_CREATED_ON)
            .enrollment(UPDATED_ENROLLMENT)
            .freeAndReducedPercent(UPDATED_FREE_AND_REDUCED_PERCENT)
            .iD(UPDATED_I_D)
            .isActive(UPDATED_IS_ACTIVE)
            .mealsServed(UPDATED_MEALS_SERVED)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .monthId(UPDATED_MONTH_ID)
            .numberOfDistricts(UPDATED_NUMBER_OF_DISTRICTS)
            .numberOfSites(UPDATED_NUMBER_OF_SITES)
            .regDate(UPDATED_REG_DATE)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .year(UPDATED_YEAR);

        restMonthlyNumbersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMonthlyNumbers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMonthlyNumbers))
            )
            .andExpect(status().isOk());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonthlyNumbersToMatchAllProperties(updatedMonthlyNumbers);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MonthlyNumbers> monthlyNumbersSearchList = Streamable.of(monthlyNumbersSearchRepository.findAll()).toList();
                MonthlyNumbers testMonthlyNumbersSearch = monthlyNumbersSearchList.get(searchDatabaseSizeAfter - 1);

                assertMonthlyNumbersAllPropertiesEquals(testMonthlyNumbersSearch, updatedMonthlyNumbers);
            });
    }

    @Test
    @Transactional
    void putNonExistingMonthlyNumbers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        monthlyNumbers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthlyNumbersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monthlyNumbers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monthlyNumbers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonthlyNumbers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        monthlyNumbers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyNumbersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monthlyNumbers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonthlyNumbers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        monthlyNumbers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyNumbersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monthlyNumbers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMonthlyNumbersWithPatch() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monthlyNumbers using partial update
        MonthlyNumbers partialUpdatedMonthlyNumbers = new MonthlyNumbers();
        partialUpdatedMonthlyNumbers.setId(monthlyNumbers.getId());

        partialUpdatedMonthlyNumbers
            .enrollment(UPDATED_ENROLLMENT)
            .iD(UPDATED_I_D)
            .mealsServed(UPDATED_MEALS_SERVED)
            .monthId(UPDATED_MONTH_ID)
            .numberOfDistricts(UPDATED_NUMBER_OF_DISTRICTS)
            .year(UPDATED_YEAR);

        restMonthlyNumbersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonthlyNumbers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonthlyNumbers))
            )
            .andExpect(status().isOk());

        // Validate the MonthlyNumbers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonthlyNumbersUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonthlyNumbers, monthlyNumbers),
            getPersistedMonthlyNumbers(monthlyNumbers)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonthlyNumbersWithPatch() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monthlyNumbers using partial update
        MonthlyNumbers partialUpdatedMonthlyNumbers = new MonthlyNumbers();
        partialUpdatedMonthlyNumbers.setId(monthlyNumbers.getId());

        partialUpdatedMonthlyNumbers
            .actualMonthId(UPDATED_ACTUAL_MONTH_ID)
            .createdOn(UPDATED_CREATED_ON)
            .enrollment(UPDATED_ENROLLMENT)
            .freeAndReducedPercent(UPDATED_FREE_AND_REDUCED_PERCENT)
            .iD(UPDATED_I_D)
            .isActive(UPDATED_IS_ACTIVE)
            .mealsServed(UPDATED_MEALS_SERVED)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .monthId(UPDATED_MONTH_ID)
            .numberOfDistricts(UPDATED_NUMBER_OF_DISTRICTS)
            .numberOfSites(UPDATED_NUMBER_OF_SITES)
            .regDate(UPDATED_REG_DATE)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .year(UPDATED_YEAR);

        restMonthlyNumbersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonthlyNumbers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonthlyNumbers))
            )
            .andExpect(status().isOk());

        // Validate the MonthlyNumbers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonthlyNumbersUpdatableFieldsEquals(partialUpdatedMonthlyNumbers, getPersistedMonthlyNumbers(partialUpdatedMonthlyNumbers));
    }

    @Test
    @Transactional
    void patchNonExistingMonthlyNumbers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        monthlyNumbers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonthlyNumbersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monthlyNumbers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monthlyNumbers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonthlyNumbers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        monthlyNumbers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyNumbersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monthlyNumbers))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonthlyNumbers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        monthlyNumbers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonthlyNumbersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monthlyNumbers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonthlyNumbers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMonthlyNumbers() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);
        monthlyNumbersRepository.save(monthlyNumbers);
        monthlyNumbersSearchRepository.save(monthlyNumbers);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the monthlyNumbers
        restMonthlyNumbersMockMvc
            .perform(delete(ENTITY_API_URL_ID, monthlyNumbers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(monthlyNumbersSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMonthlyNumbers() throws Exception {
        // Initialize the database
        insertedMonthlyNumbers = monthlyNumbersRepository.saveAndFlush(monthlyNumbers);
        monthlyNumbersSearchRepository.save(monthlyNumbers);

        // Search the monthlyNumbers
        restMonthlyNumbersMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + monthlyNumbers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monthlyNumbers.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualMonthId").value(hasItem(DEFAULT_ACTUAL_MONTH_ID)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].enrollment").value(hasItem(DEFAULT_ENROLLMENT)))
            .andExpect(jsonPath("$.[*].freeAndReducedPercent").value(hasItem(DEFAULT_FREE_AND_REDUCED_PERCENT)))
            .andExpect(jsonPath("$.[*].iD").value(hasItem(DEFAULT_I_D)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mealsServed").value(hasItem(DEFAULT_MEALS_SERVED)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].monthId").value(hasItem(DEFAULT_MONTH_ID)))
            .andExpect(jsonPath("$.[*].numberOfDistricts").value(hasItem(DEFAULT_NUMBER_OF_DISTRICTS)))
            .andExpect(jsonPath("$.[*].numberOfSites").value(hasItem(DEFAULT_NUMBER_OF_SITES)))
            .andExpect(jsonPath("$.[*].regDate").value(hasItem(DEFAULT_REG_DATE.toString())))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    protected long getRepositoryCount() {
        return monthlyNumbersRepository.count();
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

    protected MonthlyNumbers getPersistedMonthlyNumbers(MonthlyNumbers monthlyNumbers) {
        return monthlyNumbersRepository.findById(monthlyNumbers.getId()).orElseThrow();
    }

    protected void assertPersistedMonthlyNumbersToMatchAllProperties(MonthlyNumbers expectedMonthlyNumbers) {
        assertMonthlyNumbersAllPropertiesEquals(expectedMonthlyNumbers, getPersistedMonthlyNumbers(expectedMonthlyNumbers));
    }

    protected void assertPersistedMonthlyNumbersToMatchUpdatableProperties(MonthlyNumbers expectedMonthlyNumbers) {
        assertMonthlyNumbersAllUpdatablePropertiesEquals(expectedMonthlyNumbers, getPersistedMonthlyNumbers(expectedMonthlyNumbers));
    }
}
