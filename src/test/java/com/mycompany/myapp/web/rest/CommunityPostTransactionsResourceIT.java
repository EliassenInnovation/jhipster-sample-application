package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CommunityPostTransactionsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommunityPostTransactions;
import com.mycompany.myapp.repository.CommunityPostTransactionsRepository;
import com.mycompany.myapp.repository.search.CommunityPostTransactionsSearchRepository;
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
 * Integration tests for the {@link CommunityPostTransactionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityPostTransactionsResourceIT {

    private static final String DEFAULT_ATTACHMENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_URL = "BBBBBBBBBB";

    private static final UUID DEFAULT_COMMUNITY_POST_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_POST_ID = UUID.randomUUID();

    private static final UUID DEFAULT_COMMUNITY_POST_TRANSACTION_ID = UUID.randomUUID();
    private static final UUID UPDATED_COMMUNITY_POST_TRANSACTION_ID = UUID.randomUUID();

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_LAST_UPDATED_BY = 1;
    private static final Integer UPDATED_LAST_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/community-post-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/community-post-transactions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommunityPostTransactionsRepository communityPostTransactionsRepository;

    @Autowired
    private CommunityPostTransactionsSearchRepository communityPostTransactionsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityPostTransactionsMockMvc;

    private CommunityPostTransactions communityPostTransactions;

    private CommunityPostTransactions insertedCommunityPostTransactions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityPostTransactions createEntity() {
        return new CommunityPostTransactions()
            .attachmentUrl(DEFAULT_ATTACHMENT_URL)
            .communityPostId(DEFAULT_COMMUNITY_POST_ID)
            .communityPostTransactionId(DEFAULT_COMMUNITY_POST_TRANSACTION_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityPostTransactions createUpdatedEntity() {
        return new CommunityPostTransactions()
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .communityPostTransactionId(UPDATED_COMMUNITY_POST_TRANSACTION_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        communityPostTransactions = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCommunityPostTransactions != null) {
            communityPostTransactionsRepository.delete(insertedCommunityPostTransactions);
            communityPostTransactionsSearchRepository.delete(insertedCommunityPostTransactions);
            insertedCommunityPostTransactions = null;
        }
    }

    @Test
    @Transactional
    void createCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        // Create the CommunityPostTransactions
        var returnedCommunityPostTransactions = om.readValue(
            restCommunityPostTransactionsMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityPostTransactions))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommunityPostTransactions.class
        );

        // Validate the CommunityPostTransactions in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommunityPostTransactionsUpdatableFieldsEquals(
            returnedCommunityPostTransactions,
            getPersistedCommunityPostTransactions(returnedCommunityPostTransactions)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCommunityPostTransactions = returnedCommunityPostTransactions;
    }

    @Test
    @Transactional
    void createCommunityPostTransactionsWithExistingId() throws Exception {
        // Create the CommunityPostTransactions with an existing ID
        communityPostTransactions.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityPostTransactionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityPostTransactions)))
            .andExpect(status().isBadRequest());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCommunityPostTransactions() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);

        // Get all the communityPostTransactionsList
        restCommunityPostTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityPostTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentUrl").value(hasItem(DEFAULT_ATTACHMENT_URL.toString())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].communityPostTransactionId").value(hasItem(DEFAULT_COMMUNITY_POST_TRANSACTION_ID.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getCommunityPostTransactions() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);

        // Get the communityPostTransactions
        restCommunityPostTransactionsMockMvc
            .perform(get(ENTITY_API_URL_ID, communityPostTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityPostTransactions.getId().intValue()))
            .andExpect(jsonPath("$.attachmentUrl").value(DEFAULT_ATTACHMENT_URL.toString()))
            .andExpect(jsonPath("$.communityPostId").value(DEFAULT_COMMUNITY_POST_ID.toString()))
            .andExpect(jsonPath("$.communityPostTransactionId").value(DEFAULT_COMMUNITY_POST_TRANSACTION_ID.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommunityPostTransactions() throws Exception {
        // Get the communityPostTransactions
        restCommunityPostTransactionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommunityPostTransactions() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        communityPostTransactionsSearchRepository.save(communityPostTransactions);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());

        // Update the communityPostTransactions
        CommunityPostTransactions updatedCommunityPostTransactions = communityPostTransactionsRepository
            .findById(communityPostTransactions.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCommunityPostTransactions are not directly saved in db
        em.detach(updatedCommunityPostTransactions);
        updatedCommunityPostTransactions
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .communityPostTransactionId(UPDATED_COMMUNITY_POST_TRANSACTION_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restCommunityPostTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityPostTransactions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommunityPostTransactions))
            )
            .andExpect(status().isOk());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommunityPostTransactionsToMatchAllProperties(updatedCommunityPostTransactions);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CommunityPostTransactions> communityPostTransactionsSearchList = Streamable.of(
                    communityPostTransactionsSearchRepository.findAll()
                ).toList();
                CommunityPostTransactions testCommunityPostTransactionsSearch = communityPostTransactionsSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertCommunityPostTransactionsAllPropertiesEquals(testCommunityPostTransactionsSearch, updatedCommunityPostTransactions);
            });
    }

    @Test
    @Transactional
    void putNonExistingCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        communityPostTransactions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityPostTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityPostTransactions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityPostTransactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        communityPostTransactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(communityPostTransactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        communityPostTransactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostTransactionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(communityPostTransactions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCommunityPostTransactionsWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityPostTransactions using partial update
        CommunityPostTransactions partialUpdatedCommunityPostTransactions = new CommunityPostTransactions();
        partialUpdatedCommunityPostTransactions.setId(communityPostTransactions.getId());

        partialUpdatedCommunityPostTransactions.createdOn(UPDATED_CREATED_ON).lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restCommunityPostTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityPostTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityPostTransactions))
            )
            .andExpect(status().isOk());

        // Validate the CommunityPostTransactions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityPostTransactionsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommunityPostTransactions, communityPostTransactions),
            getPersistedCommunityPostTransactions(communityPostTransactions)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommunityPostTransactionsWithPatch() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the communityPostTransactions using partial update
        CommunityPostTransactions partialUpdatedCommunityPostTransactions = new CommunityPostTransactions();
        partialUpdatedCommunityPostTransactions.setId(communityPostTransactions.getId());

        partialUpdatedCommunityPostTransactions
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .communityPostId(UPDATED_COMMUNITY_POST_ID)
            .communityPostTransactionId(UPDATED_COMMUNITY_POST_TRANSACTION_ID)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restCommunityPostTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityPostTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommunityPostTransactions))
            )
            .andExpect(status().isOk());

        // Validate the CommunityPostTransactions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommunityPostTransactionsUpdatableFieldsEquals(
            partialUpdatedCommunityPostTransactions,
            getPersistedCommunityPostTransactions(partialUpdatedCommunityPostTransactions)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        communityPostTransactions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityPostTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityPostTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityPostTransactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        communityPostTransactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(communityPostTransactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityPostTransactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        communityPostTransactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityPostTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(communityPostTransactions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityPostTransactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCommunityPostTransactions() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);
        communityPostTransactionsRepository.save(communityPostTransactions);
        communityPostTransactionsSearchRepository.save(communityPostTransactions);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the communityPostTransactions
        restCommunityPostTransactionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityPostTransactions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(communityPostTransactionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCommunityPostTransactions() throws Exception {
        // Initialize the database
        insertedCommunityPostTransactions = communityPostTransactionsRepository.saveAndFlush(communityPostTransactions);
        communityPostTransactionsSearchRepository.save(communityPostTransactions);

        // Search the communityPostTransactions
        restCommunityPostTransactionsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + communityPostTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityPostTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentUrl").value(hasItem(DEFAULT_ATTACHMENT_URL.toString())))
            .andExpect(jsonPath("$.[*].communityPostId").value(hasItem(DEFAULT_COMMUNITY_POST_ID.toString())))
            .andExpect(jsonPath("$.[*].communityPostTransactionId").value(hasItem(DEFAULT_COMMUNITY_POST_TRANSACTION_ID.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return communityPostTransactionsRepository.count();
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

    protected CommunityPostTransactions getPersistedCommunityPostTransactions(CommunityPostTransactions communityPostTransactions) {
        return communityPostTransactionsRepository.findById(communityPostTransactions.getId()).orElseThrow();
    }

    protected void assertPersistedCommunityPostTransactionsToMatchAllProperties(
        CommunityPostTransactions expectedCommunityPostTransactions
    ) {
        assertCommunityPostTransactionsAllPropertiesEquals(
            expectedCommunityPostTransactions,
            getPersistedCommunityPostTransactions(expectedCommunityPostTransactions)
        );
    }

    protected void assertPersistedCommunityPostTransactionsToMatchUpdatableProperties(
        CommunityPostTransactions expectedCommunityPostTransactions
    ) {
        assertCommunityPostTransactionsAllUpdatablePropertiesEquals(
            expectedCommunityPostTransactions,
            getPersistedCommunityPostTransactions(expectedCommunityPostTransactions)
        );
    }
}
