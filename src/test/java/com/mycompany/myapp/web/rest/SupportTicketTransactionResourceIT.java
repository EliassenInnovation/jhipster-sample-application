package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SupportTicketTransactionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SupportTicketTransaction;
import com.mycompany.myapp.repository.SupportTicketTransactionRepository;
import com.mycompany.myapp.repository.search.SupportTicketTransactionSearchRepository;
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
 * Integration tests for the {@link SupportTicketTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupportTicketTransactionResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_SENT_BY_FIG_SUPPORT = false;
    private static final Boolean UPDATED_IS_SENT_BY_FIG_SUPPORT = true;

    private static final Integer DEFAULT_LAST_UPDATED_BY = 1;
    private static final Integer UPDATED_LAST_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_LAST_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TICKET_ID = 1;
    private static final Integer UPDATED_TICKET_ID = 2;

    private static final Integer DEFAULT_TICKET_TRANSACTION_ID = 1;
    private static final Integer UPDATED_TICKET_TRANSACTION_ID = 2;

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/support-ticket-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/support-ticket-transactions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupportTicketTransactionRepository supportTicketTransactionRepository;

    @Autowired
    private SupportTicketTransactionSearchRepository supportTicketTransactionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupportTicketTransactionMockMvc;

    private SupportTicketTransaction supportTicketTransaction;

    private SupportTicketTransaction insertedSupportTicketTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportTicketTransaction createEntity() {
        return new SupportTicketTransaction()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .description(DEFAULT_DESCRIPTION)
            .fileExtension(DEFAULT_FILE_EXTENSION)
            .fileName(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .fileSize(DEFAULT_FILE_SIZE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isSentByFigSupport(DEFAULT_IS_SENT_BY_FIG_SUPPORT)
            .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
            .lastUpdatedOn(DEFAULT_LAST_UPDATED_ON)
            .ticketId(DEFAULT_TICKET_ID)
            .ticketTransactionId(DEFAULT_TICKET_TRANSACTION_ID)
            .userId(DEFAULT_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupportTicketTransaction createUpdatedEntity() {
        return new SupportTicketTransaction()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .fileExtension(UPDATED_FILE_EXTENSION)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .isActive(UPDATED_IS_ACTIVE)
            .isSentByFigSupport(UPDATED_IS_SENT_BY_FIG_SUPPORT)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .ticketId(UPDATED_TICKET_ID)
            .ticketTransactionId(UPDATED_TICKET_TRANSACTION_ID)
            .userId(UPDATED_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        supportTicketTransaction = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupportTicketTransaction != null) {
            supportTicketTransactionRepository.delete(insertedSupportTicketTransaction);
            supportTicketTransactionSearchRepository.delete(insertedSupportTicketTransaction);
            insertedSupportTicketTransaction = null;
        }
    }

    @Test
    @Transactional
    void createSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        // Create the SupportTicketTransaction
        var returnedSupportTicketTransaction = om.readValue(
            restSupportTicketTransactionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketTransaction))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupportTicketTransaction.class
        );

        // Validate the SupportTicketTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupportTicketTransactionUpdatableFieldsEquals(
            returnedSupportTicketTransaction,
            getPersistedSupportTicketTransaction(returnedSupportTicketTransaction)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSupportTicketTransaction = returnedSupportTicketTransaction;
    }

    @Test
    @Transactional
    void createSupportTicketTransactionWithExistingId() throws Exception {
        // Create the SupportTicketTransaction with an existing ID
        supportTicketTransaction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportTicketTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSupportTicketTransactions() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);

        // Get all the supportTicketTransactionList
        restSupportTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportTicketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileExtension").value(hasItem(DEFAULT_FILE_EXTENSION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isSentByFigSupport").value(hasItem(DEFAULT_IS_SENT_BY_FIG_SUPPORT.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID)))
            .andExpect(jsonPath("$.[*].ticketTransactionId").value(hasItem(DEFAULT_TICKET_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getSupportTicketTransaction() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);

        // Get the supportTicketTransaction
        restSupportTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, supportTicketTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supportTicketTransaction.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fileExtension").value(DEFAULT_FILE_EXTENSION))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isSentByFigSupport").value(DEFAULT_IS_SENT_BY_FIG_SUPPORT.booleanValue()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY))
            .andExpect(jsonPath("$.lastUpdatedOn").value(DEFAULT_LAST_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.ticketId").value(DEFAULT_TICKET_ID))
            .andExpect(jsonPath("$.ticketTransactionId").value(DEFAULT_TICKET_TRANSACTION_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingSupportTicketTransaction() throws Exception {
        // Get the supportTicketTransaction
        restSupportTicketTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupportTicketTransaction() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        supportTicketTransactionSearchRepository.save(supportTicketTransaction);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());

        // Update the supportTicketTransaction
        SupportTicketTransaction updatedSupportTicketTransaction = supportTicketTransactionRepository
            .findById(supportTicketTransaction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSupportTicketTransaction are not directly saved in db
        em.detach(updatedSupportTicketTransaction);
        updatedSupportTicketTransaction
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .fileExtension(UPDATED_FILE_EXTENSION)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .isActive(UPDATED_IS_ACTIVE)
            .isSentByFigSupport(UPDATED_IS_SENT_BY_FIG_SUPPORT)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .ticketId(UPDATED_TICKET_ID)
            .ticketTransactionId(UPDATED_TICKET_TRANSACTION_ID)
            .userId(UPDATED_USER_ID);

        restSupportTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupportTicketTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupportTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupportTicketTransactionToMatchAllProperties(updatedSupportTicketTransaction);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SupportTicketTransaction> supportTicketTransactionSearchList = Streamable.of(
                    supportTicketTransactionSearchRepository.findAll()
                ).toList();
                SupportTicketTransaction testSupportTicketTransactionSearch = supportTicketTransactionSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertSupportTicketTransactionAllPropertiesEquals(testSupportTicketTransactionSearch, updatedSupportTicketTransaction);
            });
    }

    @Test
    @Transactional
    void putNonExistingSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        supportTicketTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supportTicketTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        supportTicketTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supportTicketTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        supportTicketTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supportTicketTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSupportTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicketTransaction using partial update
        SupportTicketTransaction partialUpdatedSupportTicketTransaction = new SupportTicketTransaction();
        partialUpdatedSupportTicketTransaction.setId(supportTicketTransaction.getId());

        partialUpdatedSupportTicketTransaction
            .createdBy(UPDATED_CREATED_BY)
            .isActive(UPDATED_IS_ACTIVE)
            .isSentByFigSupport(UPDATED_IS_SENT_BY_FIG_SUPPORT)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON);

        restSupportTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicketTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportTicketTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupportTicketTransaction, supportTicketTransaction),
            getPersistedSupportTicketTransaction(supportTicketTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupportTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supportTicketTransaction using partial update
        SupportTicketTransaction partialUpdatedSupportTicketTransaction = new SupportTicketTransaction();
        partialUpdatedSupportTicketTransaction.setId(supportTicketTransaction.getId());

        partialUpdatedSupportTicketTransaction
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .fileExtension(UPDATED_FILE_EXTENSION)
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .isActive(UPDATED_IS_ACTIVE)
            .isSentByFigSupport(UPDATED_IS_SENT_BY_FIG_SUPPORT)
            .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
            .lastUpdatedOn(UPDATED_LAST_UPDATED_ON)
            .ticketId(UPDATED_TICKET_ID)
            .ticketTransactionId(UPDATED_TICKET_TRANSACTION_ID)
            .userId(UPDATED_USER_ID);

        restSupportTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupportTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupportTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the SupportTicketTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupportTicketTransactionUpdatableFieldsEquals(
            partialUpdatedSupportTicketTransaction,
            getPersistedSupportTicketTransaction(partialUpdatedSupportTicketTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        supportTicketTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supportTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportTicketTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        supportTicketTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supportTicketTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupportTicketTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        supportTicketTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupportTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supportTicketTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupportTicketTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSupportTicketTransaction() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);
        supportTicketTransactionRepository.save(supportTicketTransaction);
        supportTicketTransactionSearchRepository.save(supportTicketTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the supportTicketTransaction
        restSupportTicketTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, supportTicketTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(supportTicketTransactionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSupportTicketTransaction() throws Exception {
        // Initialize the database
        insertedSupportTicketTransaction = supportTicketTransactionRepository.saveAndFlush(supportTicketTransaction);
        supportTicketTransactionSearchRepository.save(supportTicketTransaction);

        // Search the supportTicketTransaction
        restSupportTicketTransactionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + supportTicketTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supportTicketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileExtension").value(hasItem(DEFAULT_FILE_EXTENSION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isSentByFigSupport").value(hasItem(DEFAULT_IS_SENT_BY_FIG_SUPPORT.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].lastUpdatedOn").value(hasItem(DEFAULT_LAST_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].ticketId").value(hasItem(DEFAULT_TICKET_ID)))
            .andExpect(jsonPath("$.[*].ticketTransactionId").value(hasItem(DEFAULT_TICKET_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    protected long getRepositoryCount() {
        return supportTicketTransactionRepository.count();
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

    protected SupportTicketTransaction getPersistedSupportTicketTransaction(SupportTicketTransaction supportTicketTransaction) {
        return supportTicketTransactionRepository.findById(supportTicketTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedSupportTicketTransactionToMatchAllProperties(SupportTicketTransaction expectedSupportTicketTransaction) {
        assertSupportTicketTransactionAllPropertiesEquals(
            expectedSupportTicketTransaction,
            getPersistedSupportTicketTransaction(expectedSupportTicketTransaction)
        );
    }

    protected void assertPersistedSupportTicketTransactionToMatchUpdatableProperties(
        SupportTicketTransaction expectedSupportTicketTransaction
    ) {
        assertSupportTicketTransactionAllUpdatablePropertiesEquals(
            expectedSupportTicketTransaction,
            getPersistedSupportTicketTransaction(expectedSupportTicketTransaction)
        );
    }
}
