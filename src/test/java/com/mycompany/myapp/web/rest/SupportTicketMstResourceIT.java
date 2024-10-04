package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SupportTicketMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SupportTicketMst;
import com.mycompany.myapp.repository.SupportTicketMstRepository;
import com.mycompany.myapp.repository.search.SupportTicketMstSearchRepository;
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
 * Integration tests for the {@link SupportTicketMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupportTicketMstResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_WITH_OUT_LOGIN = false;
    private static final Boolean UPDATED_IS_WITH_OUT_LOGIN = true;

    private static final Integer DEFAULT_LAST_UPDATED_BY = 1;
    private static final Integer UPDATED_LAST_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_SCHOOL_DISTRICT_ID = 1;
    private static final Integer UPDATED_SCHOOL_DISTRICT_ID = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUPPORT_CATEGORY_ID = 1;
    private static final Integer UPDATED_SUPPORT_CATEGORY_ID = 2;

    private static final Integer DEFAULT_TICKET_ID = 1;
    private static final Integer UPDATED_TICKET_ID = 2;

    private static final Integer DEFAULT_TICKET_REFERENCE_NUMBER = 1;
    private static final Integer UPDATED_TICKET_REFERENCE_NUMBER = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/support-ticket-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/support-ticket-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupportTicketMstRepository supportTicketMstRepository;

    @Autowired
    private SupportTicketMstSearchRepository supportTicketMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupportTicketMstMockMvc;

    private SupportTicketMst supportTicketMst;

    private SupportTicketMst insertedSupportTicketMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportTicketMst createEntity() {
        return new SupportTicketMst()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .isWithOutLogin(DEFAULT_IS_WITH_OUT_LOGIN)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON)
            .priority(DEFAULT_PRIORITY)
            .schoolDistrictId(DEFAULT_SCHOOL_DISTRICT_ID)
            .status(DEFAULT_STATUS)
            .subject(DEFAULT_SUBJECT)
            .supportCategoryId(DEFAULT_SUPPORT_CATEGORY_ID)
            .ticketId(DEFAULT_TICKET_ID)
            .ticketReferenceNumber(DEFAULT_TICKET_REFERENCE_NUMBER)
            .userId(DEFAULT_USER_ID)
            .userName(DEFAULT_USER_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportTicketMst createUpdatedEntity() {
        return new SupportTicketMst()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .isWithOutLogin(UPDATED_IS_WITH_OUT_LOGIN)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .priority(UPDATED_PRIORITY)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .status(UPDATED_STATUS)
            .subject(UPDATED_SUBJECT)
            .supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID)
            .ticketId(UPDATED_TICKET_ID)
            .ticketReferenceNumber(UPDATED_TICKET_REFERENCE_NUMBER)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME);
    }

    @BeforeEach
    public void initTest() {
        supportTicketMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupportTicketMst != null) {
            supportTicketMstRepository.delete(insertedSupportTicketMst);
            supportTicketMstSearchRepository.delete(insertedSupportTicketMst);
            insertedSupportTicketMst = null;
        }
    }

    @Test
    @Transactional
    void createSupportTicketMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        // Create the SupportTicketMst
        var returnedSupportTicketMst = om.readValue(
            restSupportTicketMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupportTicketMst.class
        );

        // Validate the SupportTicketMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupportTicketMstUpdatableFieldsEquals(returnedSupportTicketMst, getPersistedSupportTicketMst(returnedSupportTicketMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSupportTicketMst = returnedSupportTicketMst;
    }

    @Test
    @Transactional
    void createSupportTicketMstWithExistingId() throws Exception {
        // Create the SupportTicketMst with an existing ID
        supportTicketMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportTicketMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketMst)))
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSupportTicketMsts() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);

        // Get all the supportTicketMstList
        restSupportTicketMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportTicketMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isWithOutLogin").value(hasItem(DEFAULT_IS_WITH_OUT_LOGIN.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].supportCategoryId").value(hasItem(DEFAULT_SUPPORT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID)))
            .andExpect(jsonPath("$.[*].ticketReferenceNumber").value(hasItem(DEFAULT_TICKET_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));
    }

    @Test
    @Transactional
    void getSupportTicketMst() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);

        // Get the supportTicketMst
        restSupportTicketMstMockMvc
            .perform(get(ENTITY_API_URL_ID, supportTicketMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supportTicketMst.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isWithOutLogin").value(DEFAULT_IS_WITH_OUT_LOGIN.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.schoolDistrictId").value(DEFAULT_SCHOOL_DISTRICT_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.supportCategoryId").value(DEFAULT_SUPPORT_CATEGORY_ID))
            .andExpect(jsonPath("$.ticketId").value(DEFAULT_TICKET_ID))
            .andExpect(jsonPath("$.ticketReferenceNumber").value(DEFAULT_TICKET_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSupportTicketMst() throws Exception {
        // Get the supportTicketMst
        restSupportTicketMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupportTicketMst() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicketMstSearchRepository.save(supportTicketMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());

        // Update the supportTicketMst
        SupportTicketMst updatedSupportTicketMst = supportTicketMstRepository.findById(supportTicketMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupportTicketMst are not directly saved in db
        em.detach(updatedSupportTicketMst);
        updatedSupportTicketMst
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .isWithOutLogin(UPDATED_IS_WITH_OUT_LOGIN)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .priority(UPDATED_PRIORITY)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .status(UPDATED_STATUS)
            .subject(UPDATED_SUBJECT)
            .supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID)
            .ticketId(UPDATED_TICKET_ID)
            .ticketReferenceNumber(UPDATED_TICKET_REFERENCE_NUMBER)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME);

        restSupportTicketMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupportTicketMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupportTicketMst))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupportTicketMstToMatchAllProperties(updatedSupportTicketMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SupportTicketMst> supportTicketMstSearchList = Streamable.of(supportTicketMstSearchRepository.findAll()).toList();
                SupportTicketMst testSupportTicketMstSearch = supportTicketMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertSupportTicketMstAllPropertiesEquals(testSupportTicketMstSearch, updatedSupportTicketMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingSupportTicketMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        supportTicketMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportTicketMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supportTicketMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupportTicketMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        supportTicketMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupportTicketMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        supportTicketMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSupportTicketMstWithPatch() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicketMst using partial update
        SupportTicketMst partialUpdatedSupportTicketMst = new SupportTicketMst();
        partialUpdatedSupportTicketMst.setId(supportTicketMst.getId());

        partialUpdatedSupportTicketMst
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .isWithOutLogin(UPDATED_IS_WITH_OUT_LOGIN)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID)
            .userName(UPDATED_USER_NAME);

        restSupportTicketMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportTicketMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportTicketMst))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicketMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportTicketMstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupportTicketMst, supportTicketMst),
            getPersistedSupportTicketMst(supportTicketMst)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupportTicketMstWithPatch() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicketMst using partial update
        SupportTicketMst partialUpdatedSupportTicketMst = new SupportTicketMst();
        partialUpdatedSupportTicketMst.setId(supportTicketMst.getId());

        partialUpdatedSupportTicketMst
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .isWithOutLogin(UPDATED_IS_WITH_OUT_LOGIN)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .priority(UPDATED_PRIORITY)
            .schoolDistrictId(UPDATED_SCHOOL_DISTRICT_ID)
            .status(UPDATED_STATUS)
            .subject(UPDATED_SUBJECT)
            .supportCategoryId(UPDATED_SUPPORT_CATEGORY_ID)
            .ticketId(UPDATED_TICKET_ID)
            .ticketReferenceNumber(UPDATED_TICKET_REFERENCE_NUMBER)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME);

        restSupportTicketMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportTicketMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportTicketMst))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicketMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportTicketMstUpdatableFieldsEquals(
            partialUpdatedSupportTicketMst,
            getPersistedSupportTicketMst(partialUpdatedSupportTicketMst)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSupportTicketMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        supportTicketMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportTicketMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supportTicketMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportTicketMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupportTicketMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        supportTicketMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportTicketMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupportTicketMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        supportTicketMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supportTicketMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportTicketMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSupportTicketMst() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);
        supportTicketMstRepository.save(supportTicketMst);
        supportTicketMstSearchRepository.save(supportTicketMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the supportTicketMst
        restSupportTicketMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, supportTicketMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSupportTicketMst() throws Exception {
        // Initialize the database
        insertedSupportTicketMst = supportTicketMstRepository.saveAndFlush(supportTicketMst);
        supportTicketMstSearchRepository.save(supportTicketMst);

        // Search the supportTicketMst
        restSupportTicketMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + supportTicketMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportTicketMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isWithOutLogin").value(hasItem(DEFAULT_IS_WITH_OUT_LOGIN.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].schoolDistrictId").value(hasItem(DEFAULT_SCHOOL_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].supportCategoryId").value(hasItem(DEFAULT_SUPPORT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID)))
            .andExpect(jsonPath("$.[*].ticketReferenceNumber").value(hasItem(DEFAULT_TICKET_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));
    }

    protected long getRepositoryCount() {
        return supportTicketMstRepository.count();
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

    protected SupportTicketMst getPersistedSupportTicketMst(SupportTicketMst supportTicketMst) {
        return supportTicketMstRepository.findById(supportTicketMst.getId()).orElseThrow();
    }

    protected void assertPersistedSupportTicketMstToMatchAllProperties(SupportTicketMst expectedSupportTicketMst) {
        assertSupportTicketMstAllPropertiesEquals(expectedSupportTicketMst, getPersistedSupportTicketMst(expectedSupportTicketMst));
    }

    protected void assertPersistedSupportTicketMstToMatchUpdatableProperties(SupportTicketMst expectedSupportTicketMst) {
        assertSupportTicketMstAllUpdatablePropertiesEquals(
            expectedSupportTicketMst,
            getPersistedSupportTicketMst(expectedSupportTicketMst)
        );
    }
}
