package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SchoolDistrictAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SchoolDistrict;
import com.mycompany.myapp.repository.SchoolDistrictRepository;
import com.mycompany.myapp.repository.search.SchoolDistrictSearchRepository;
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
 * Integration tests for the {@link SchoolDistrictResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchoolDistrictResourceIT {

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DISTRICT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_SERVICE_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_SERVICE_OPTIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_LAST_UPDATED_BY = 1;
    private static final Integer UPDATED_LAST_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final String DEFAULT_SITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SITE_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String ENTITY_API_URL = "/api/school-districts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/school-districts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SchoolDistrictRepository schoolDistrictRepository;

    @Autowired
    private SchoolDistrictSearchRepository schoolDistrictSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchoolDistrictMockMvc;

    private SchoolDistrict schoolDistrict;

    private SchoolDistrict insertedSchoolDistrict;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolDistrict createEntity() {
        return new SchoolDistrict()
            .city(DEFAULT_CITY)
            .contractCompany(DEFAULT_CONTRACT_COMPANY)
            .country(DEFAULT_COUNTRY)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .districtLogo(DEFAULT_DISTRICT_LOGO)
            .districtName(DEFAULT_DISTRICT_NAME)
            .email(DEFAULT_EMAIL)
            .foodServiceOptions(DEFAULT_FOOD_SERVICE_OPTIONS)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .siteCode(DEFAULT_SITE_CODE)
            .state(DEFAULT_STATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchoolDistrict createUpdatedEntity() {
        return new SchoolDistrict()
            .city(UPDATED_CITY)
            .contractCompany(UPDATED_CONTRACT_COMPANY)
            .country(UPDATED_COUNTRY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .districtLogo(UPDATED_DISTRICT_LOGO)
            .districtName(UPDATED_DISTRICT_NAME)
            .email(UPDATED_EMAIL)
            .foodServiceOptions(UPDATED_FOOD_SERVICE_OPTIONS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .siteCode(UPDATED_SITE_CODE)
            .state(UPDATED_STATE);
    }

    @BeforeEach
    public void initTest() {
        schoolDistrict = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSchoolDistrict != null) {
            schoolDistrictRepository.delete(insertedSchoolDistrict);
            schoolDistrictSearchRepository.delete(insertedSchoolDistrict);
            insertedSchoolDistrict = null;
        }
    }

    @Test
    @Transactional
    void createSchoolDistrict() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        // Create the SchoolDistrict
        var returnedSchoolDistrict = om.readValue(
            restSchoolDistrictMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDistrict)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SchoolDistrict.class
        );

        // Validate the SchoolDistrict in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSchoolDistrictUpdatableFieldsEquals(returnedSchoolDistrict, getPersistedSchoolDistrict(returnedSchoolDistrict));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSchoolDistrict = returnedSchoolDistrict;
    }

    @Test
    @Transactional
    void createSchoolDistrictWithExistingId() throws Exception {
        // Create the SchoolDistrict with an existing ID
        schoolDistrict.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchoolDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDistrict)))
            .andExpect(status().isBadRequest());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSchoolDistricts() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);

        // Get all the schoolDistrictList
        restSchoolDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolDistrict.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].contractCompany").value(hasItem(DEFAULT_CONTRACT_COMPANY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].districtLogo").value(hasItem(DEFAULT_DISTRICT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].districtName").value(hasItem(DEFAULT_DISTRICT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].foodServiceOptions").value(hasItem(DEFAULT_FOOD_SERVICE_OPTIONS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)));
    }

    @Test
    @Transactional
    void getSchoolDistrict() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);

        // Get the schoolDistrict
        restSchoolDistrictMockMvc
            .perform(get(ENTITY_API_URL_ID, schoolDistrict.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schoolDistrict.getId().intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.contractCompany").value(DEFAULT_CONTRACT_COMPANY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.districtLogo").value(DEFAULT_DISTRICT_LOGO.toString()))
            .andExpect(jsonPath("$.districtName").value(DEFAULT_DISTRICT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.foodServiceOptions").value(DEFAULT_FOOD_SERVICE_OPTIONS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.siteCode").value(DEFAULT_SITE_CODE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE));
    }

    @Test
    @Transactional
    void getNonExistingSchoolDistrict() throws Exception {
        // Get the schoolDistrict
        restSchoolDistrictMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchoolDistrict() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        schoolDistrictSearchRepository.save(schoolDistrict);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());

        // Update the schoolDistrict
        SchoolDistrict updatedSchoolDistrict = schoolDistrictRepository.findById(schoolDistrict.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchoolDistrict are not directly saved in db
        em.detach(updatedSchoolDistrict);
        updatedSchoolDistrict
            .city(UPDATED_CITY)
            .contractCompany(UPDATED_CONTRACT_COMPANY)
            .country(UPDATED_COUNTRY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .districtLogo(UPDATED_DISTRICT_LOGO)
            .districtName(UPDATED_DISTRICT_NAME)
            .email(UPDATED_EMAIL)
            .foodServiceOptions(UPDATED_FOOD_SERVICE_OPTIONS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .siteCode(UPDATED_SITE_CODE)
            .state(UPDATED_STATE);

        restSchoolDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSchoolDistrict.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSchoolDistrict))
            )
            .andExpect(status().isOk());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSchoolDistrictToMatchAllProperties(updatedSchoolDistrict);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SchoolDistrict> schoolDistrictSearchList = Streamable.of(schoolDistrictSearchRepository.findAll()).toList();
                SchoolDistrict testSchoolDistrictSearch = schoolDistrictSearchList.get(searchDatabaseSizeAfter - 1);

                assertSchoolDistrictAllPropertiesEquals(testSchoolDistrictSearch, updatedSchoolDistrict);
            });
    }

    @Test
    @Transactional
    void putNonExistingSchoolDistrict() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        schoolDistrict.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schoolDistrict.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schoolDistrict))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchoolDistrict() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        schoolDistrict.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(schoolDistrict))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchoolDistrict() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        schoolDistrict.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDistrictMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(schoolDistrict)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSchoolDistrictWithPatch() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schoolDistrict using partial update
        SchoolDistrict partialUpdatedSchoolDistrict = new SchoolDistrict();
        partialUpdatedSchoolDistrict.setId(schoolDistrict.getId());

        partialUpdatedSchoolDistrict
            .city(UPDATED_CITY)
            .contractCompany(UPDATED_CONTRACT_COMPANY)
            .country(UPDATED_COUNTRY)
            .createdOn(UPDATED_CREATED_ON)
            .districtLogo(UPDATED_DISTRICT_LOGO)
            .foodServiceOptions(UPDATED_FOOD_SERVICE_OPTIONS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .state(UPDATED_STATE);

        restSchoolDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchoolDistrict))
            )
            .andExpect(status().isOk());

        // Validate the SchoolDistrict in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSchoolDistrictUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSchoolDistrict, schoolDistrict),
            getPersistedSchoolDistrict(schoolDistrict)
        );
    }

    @Test
    @Transactional
    void fullUpdateSchoolDistrictWithPatch() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schoolDistrict using partial update
        SchoolDistrict partialUpdatedSchoolDistrict = new SchoolDistrict();
        partialUpdatedSchoolDistrict.setId(schoolDistrict.getId());

        partialUpdatedSchoolDistrict
            .city(UPDATED_CITY)
            .contractCompany(UPDATED_CONTRACT_COMPANY)
            .country(UPDATED_COUNTRY)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .districtLogo(UPDATED_DISTRICT_LOGO)
            .districtName(UPDATED_DISTRICT_NAME)
            .email(UPDATED_EMAIL)
            .foodServiceOptions(UPDATED_FOOD_SERVICE_OPTIONS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .siteCode(UPDATED_SITE_CODE)
            .state(UPDATED_STATE);

        restSchoolDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchoolDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchoolDistrict))
            )
            .andExpect(status().isOk());

        // Validate the SchoolDistrict in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSchoolDistrictUpdatableFieldsEquals(partialUpdatedSchoolDistrict, getPersistedSchoolDistrict(partialUpdatedSchoolDistrict));
    }

    @Test
    @Transactional
    void patchNonExistingSchoolDistrict() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        schoolDistrict.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchoolDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schoolDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schoolDistrict))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchoolDistrict() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        schoolDistrict.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(schoolDistrict))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchoolDistrict() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        schoolDistrict.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchoolDistrictMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(schoolDistrict)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchoolDistrict in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSchoolDistrict() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);
        schoolDistrictRepository.save(schoolDistrict);
        schoolDistrictSearchRepository.save(schoolDistrict);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the schoolDistrict
        restSchoolDistrictMockMvc
            .perform(delete(ENTITY_API_URL_ID, schoolDistrict.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(schoolDistrictSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSchoolDistrict() throws Exception {
        // Initialize the database
        insertedSchoolDistrict = schoolDistrictRepository.saveAndFlush(schoolDistrict);
        schoolDistrictSearchRepository.save(schoolDistrict);

        // Search the schoolDistrict
        restSchoolDistrictMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + schoolDistrict.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schoolDistrict.getId().intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].contractCompany").value(hasItem(DEFAULT_CONTRACT_COMPANY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].districtLogo").value(hasItem(DEFAULT_DISTRICT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].districtName").value(hasItem(DEFAULT_DISTRICT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].foodServiceOptions").value(hasItem(DEFAULT_FOOD_SERVICE_OPTIONS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)));
    }

    protected long getRepositoryCount() {
        return schoolDistrictRepository.count();
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

    protected SchoolDistrict getPersistedSchoolDistrict(SchoolDistrict schoolDistrict) {
        return schoolDistrictRepository.findById(schoolDistrict.getId()).orElseThrow();
    }

    protected void assertPersistedSchoolDistrictToMatchAllProperties(SchoolDistrict expectedSchoolDistrict) {
        assertSchoolDistrictAllPropertiesEquals(expectedSchoolDistrict, getPersistedSchoolDistrict(expectedSchoolDistrict));
    }

    protected void assertPersistedSchoolDistrictToMatchUpdatableProperties(SchoolDistrict expectedSchoolDistrict) {
        assertSchoolDistrictAllUpdatablePropertiesEquals(expectedSchoolDistrict, getPersistedSchoolDistrict(expectedSchoolDistrict));
    }
}
