package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserMst;
import com.mycompany.myapp.repository.UserMstRepository;
import com.mycompany.myapp.repository.search.UserMstSearchRepository;
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
 * Integration tests for the {@link UserMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserMstResourceIT {

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATE_BY = 1;
    private static final Integer UPDATED_CREATE_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MANUFACTURER_ID = 1;
    private static final Integer UPDATED_MANUFACTURER_ID = 2;

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECT_ID = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROLE_ID = 1;
    private static final Integer UPDATED_ROLE_ID = 2;

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserMstRepository userMstRepository;

    @Autowired
    private UserMstSearchRepository userMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMstMockMvc;

    private UserMst userMst;

    private UserMst insertedUserMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMst createEntity() {
        return new UserMst()
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .coverImage(DEFAULT_COVER_IMAGE)
            .createBy(DEFAULT_CREATE_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastName(DEFAULT_LAST_NAME)
            .manufacturerId(DEFAULT_MANUFACTURER_ID)
            .mobile(DEFAULT_MOBILE)
            .objectId(DEFAULT_OBJECT_ID)
            .password(DEFAULT_PASSWORD)
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .roleId(DEFAULT_ROLE_ID)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .state(DEFAULT_STATE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .userId(DEFAULT_USER_ID)
            .zipCode(DEFAULT_ZIP_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMst createUpdatedEntity() {
        return new UserMst()
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .coverImage(UPDATED_COVER_IMAGE)
            .createBy(UPDATED_CREATE_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .lastName(UPDATED_LAST_NAME)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .mobile(UPDATED_MOBILE)
            .objectId(UPDATED_OBJECT_ID)
            .password(UPDATED_PASSWORD)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .roleId(UPDATED_ROLE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .state(UPDATED_STATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userId(UPDATED_USER_ID)
            .zipCode(UPDATED_ZIP_CODE);
    }

    @BeforeEach
    public void initTest() {
        userMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserMst != null) {
            userMstRepository.delete(insertedUserMst);
            userMstSearchRepository.delete(insertedUserMst);
            insertedUserMst = null;
        }
    }

    @Test
    @Transactional
    void createUserMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        // Create the UserMst
        var returnedUserMst = om.readValue(
            restUserMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserMst.class
        );

        // Validate the UserMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserMstUpdatableFieldsEquals(returnedUserMst, getPersistedUserMst(returnedUserMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUserMst = returnedUserMst;
    }

    @Test
    @Transactional
    void createUserMstWithExistingId() throws Exception {
        // Create the UserMst with an existing ID
        userMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMst)))
            .andExpect(status().isBadRequest());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUserMsts() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);

        // Get all the userMstList
        restUserMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)));
    }

    @Test
    @Transactional
    void getUserMst() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);

        // Get the userMst
        restUserMstMockMvc
            .perform(get(ENTITY_API_URL_ID, userMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userMst.getId().intValue()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.manufacturerId").value(DEFAULT_MANUFACTURER_ID))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.profileImage").value(DEFAULT_PROFILE_IMAGE.toString()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE));
    }

    @Test
    @Transactional
    void getNonExistingUserMst() throws Exception {
        // Get the userMst
        restUserMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserMst() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        userMstSearchRepository.save(userMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());

        // Update the userMst
        UserMst updatedUserMst = userMstRepository.findById(userMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserMst are not directly saved in db
        em.detach(updatedUserMst);
        updatedUserMst
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .coverImage(UPDATED_COVER_IMAGE)
            .createBy(UPDATED_CREATE_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .lastName(UPDATED_LAST_NAME)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .mobile(UPDATED_MOBILE)
            .objectId(UPDATED_OBJECT_ID)
            .password(UPDATED_PASSWORD)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .roleId(UPDATED_ROLE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .state(UPDATED_STATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userId(UPDATED_USER_ID)
            .zipCode(UPDATED_ZIP_CODE);

        restUserMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserMst))
            )
            .andExpect(status().isOk());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserMstToMatchAllProperties(updatedUserMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserMst> userMstSearchList = Streamable.of(userMstSearchRepository.findAll()).toList();
                UserMst testUserMstSearch = userMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertUserMstAllPropertiesEquals(testUserMstSearch, updatedUserMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingUserMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        userMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMstMockMvc
            .perform(put(ENTITY_API_URL_ID, userMst.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMst)))
            .andExpect(status().isBadRequest());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        userMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        userMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUserMstWithPatch() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userMst using partial update
        UserMst partialUpdatedUserMst = new UserMst();
        partialUpdatedUserMst.setId(userMst.getId());

        partialUpdatedUserMst
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .createBy(UPDATED_CREATE_BY)
            .firstName(UPDATED_FIRST_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .mobile(UPDATED_MOBILE)
            .password(UPDATED_PASSWORD)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .roleId(UPDATED_ROLE_ID)
            .updatedBy(UPDATED_UPDATED_BY)
            .userId(UPDATED_USER_ID);

        restUserMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserMst))
            )
            .andExpect(status().isOk());

        // Validate the UserMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserMstUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUserMst, userMst), getPersistedUserMst(userMst));
    }

    @Test
    @Transactional
    void fullUpdateUserMstWithPatch() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userMst using partial update
        UserMst partialUpdatedUserMst = new UserMst();
        partialUpdatedUserMst.setId(userMst.getId());

        partialUpdatedUserMst
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .coverImage(UPDATED_COVER_IMAGE)
            .createBy(UPDATED_CREATE_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .lastName(UPDATED_LAST_NAME)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .mobile(UPDATED_MOBILE)
            .objectId(UPDATED_OBJECT_ID)
            .password(UPDATED_PASSWORD)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .roleId(UPDATED_ROLE_ID)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .state(UPDATED_STATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .userId(UPDATED_USER_ID)
            .zipCode(UPDATED_ZIP_CODE);

        restUserMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserMst))
            )
            .andExpect(status().isOk());

        // Validate the UserMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserMstUpdatableFieldsEquals(partialUpdatedUserMst, getPersistedUserMst(partialUpdatedUserMst));
    }

    @Test
    @Transactional
    void patchNonExistingUserMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        userMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userMst.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        userMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        userMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUserMst() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);
        userMstRepository.save(userMst);
        userMstSearchRepository.save(userMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userMst
        restUserMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, userMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUserMst() throws Exception {
        // Initialize the database
        insertedUserMst = userMstRepository.saveAndFlush(userMst);
        userMstSearchRepository.save(userMst);

        // Search the userMst
        restUserMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + userMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)));
    }

    protected long getRepositoryCount() {
        return userMstRepository.count();
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

    protected UserMst getPersistedUserMst(UserMst userMst) {
        return userMstRepository.findById(userMst.getId()).orElseThrow();
    }

    protected void assertPersistedUserMstToMatchAllProperties(UserMst expectedUserMst) {
        assertUserMstAllPropertiesEquals(expectedUserMst, getPersistedUserMst(expectedUserMst));
    }

    protected void assertPersistedUserMstToMatchUpdatableProperties(UserMst expectedUserMst) {
        assertUserMstAllUpdatablePropertiesEquals(expectedUserMst, getPersistedUserMst(expectedUserMst));
    }
}
