package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ErrorLogAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ErrorLog;
import com.mycompany.myapp.repository.ErrorLogRepository;
import com.mycompany.myapp.repository.search.ErrorLogSearchRepository;
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
 * Integration tests for the {@link ErrorLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ErrorLogResourceIT {

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_ERROR_ID = 1;
    private static final Integer UPDATED_ERROR_ID = 2;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/error-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/error-logs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private ErrorLogSearchRepository errorLogSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restErrorLogMockMvc;

    private ErrorLog errorLog;

    private ErrorLog insertedErrorLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ErrorLog createEntity() {
        return new ErrorLog()
            .createdOn(DEFAULT_CREATED_ON)
            .errorId(DEFAULT_ERROR_ID)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .errorPath(DEFAULT_ERROR_PATH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ErrorLog createUpdatedEntity() {
        return new ErrorLog()
            .createdOn(UPDATED_CREATED_ON)
            .errorId(UPDATED_ERROR_ID)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .errorPath(UPDATED_ERROR_PATH);
    }

    @BeforeEach
    public void initTest() {
        errorLog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedErrorLog != null) {
            errorLogRepository.delete(insertedErrorLog);
            errorLogSearchRepository.delete(insertedErrorLog);
            insertedErrorLog = null;
        }
    }

    @Test
    @Transactional
    void createErrorLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        // Create the ErrorLog
        var returnedErrorLog = om.readValue(
            restErrorLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(errorLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ErrorLog.class
        );

        // Validate the ErrorLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertErrorLogUpdatableFieldsEquals(returnedErrorLog, getPersistedErrorLog(returnedErrorLog));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedErrorLog = returnedErrorLog;
    }

    @Test
    @Transactional
    void createErrorLogWithExistingId() throws Exception {
        // Create the ErrorLog with an existing ID
        errorLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restErrorLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(errorLog)))
            .andExpect(status().isBadRequest());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllErrorLogs() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);

        // Get all the errorLogList
        restErrorLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(errorLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].errorId").value(hasItem(DEFAULT_ERROR_ID)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].errorPath").value(hasItem(DEFAULT_ERROR_PATH.toString())));
    }

    @Test
    @Transactional
    void getErrorLog() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);

        // Get the errorLog
        restErrorLogMockMvc
            .perform(get(ENTITY_API_URL_ID, errorLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(errorLog.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.errorId").value(DEFAULT_ERROR_ID))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE.toString()))
            .andExpect(jsonPath("$.errorPath").value(DEFAULT_ERROR_PATH.toString()));
    }

    @Test
    @Transactional
    void getNonExistingErrorLog() throws Exception {
        // Get the errorLog
        restErrorLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingErrorLog() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        errorLogSearchRepository.save(errorLog);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());

        // Update the errorLog
        ErrorLog updatedErrorLog = errorLogRepository.findById(errorLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedErrorLog are not directly saved in db
        em.detach(updatedErrorLog);
        updatedErrorLog
            .createdOn(UPDATED_CREATED_ON)
            .errorId(UPDATED_ERROR_ID)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .errorPath(UPDATED_ERROR_PATH);

        restErrorLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedErrorLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedErrorLog))
            )
            .andExpect(status().isOk());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedErrorLogToMatchAllProperties(updatedErrorLog);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ErrorLog> errorLogSearchList = Streamable.of(errorLogSearchRepository.findAll()).toList();
                ErrorLog testErrorLogSearch = errorLogSearchList.get(searchDatabaseSizeAfter - 1);

                assertErrorLogAllPropertiesEquals(testErrorLogSearch, updatedErrorLog);
            });
    }

    @Test
    @Transactional
    void putNonExistingErrorLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        errorLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restErrorLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, errorLog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(errorLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchErrorLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        errorLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restErrorLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(errorLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamErrorLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        errorLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restErrorLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(errorLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateErrorLogWithPatch() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the errorLog using partial update
        ErrorLog partialUpdatedErrorLog = new ErrorLog();
        partialUpdatedErrorLog.setId(errorLog.getId());

        partialUpdatedErrorLog.createdOn(UPDATED_CREATED_ON).errorMessage(UPDATED_ERROR_MESSAGE).errorPath(UPDATED_ERROR_PATH);

        restErrorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedErrorLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedErrorLog))
            )
            .andExpect(status().isOk());

        // Validate the ErrorLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertErrorLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedErrorLog, errorLog), getPersistedErrorLog(errorLog));
    }

    @Test
    @Transactional
    void fullUpdateErrorLogWithPatch() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the errorLog using partial update
        ErrorLog partialUpdatedErrorLog = new ErrorLog();
        partialUpdatedErrorLog.setId(errorLog.getId());

        partialUpdatedErrorLog
            .createdOn(UPDATED_CREATED_ON)
            .errorId(UPDATED_ERROR_ID)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .errorPath(UPDATED_ERROR_PATH);

        restErrorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedErrorLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedErrorLog))
            )
            .andExpect(status().isOk());

        // Validate the ErrorLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertErrorLogUpdatableFieldsEquals(partialUpdatedErrorLog, getPersistedErrorLog(partialUpdatedErrorLog));
    }

    @Test
    @Transactional
    void patchNonExistingErrorLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        errorLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restErrorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, errorLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(errorLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchErrorLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        errorLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restErrorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(errorLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamErrorLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        errorLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restErrorLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(errorLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ErrorLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteErrorLog() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);
        errorLogRepository.save(errorLog);
        errorLogSearchRepository.save(errorLog);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the errorLog
        restErrorLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, errorLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(errorLogSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchErrorLog() throws Exception {
        // Initialize the database
        insertedErrorLog = errorLogRepository.saveAndFlush(errorLog);
        errorLogSearchRepository.save(errorLog);

        // Search the errorLog
        restErrorLogMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + errorLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(errorLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].errorId").value(hasItem(DEFAULT_ERROR_ID)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].errorPath").value(hasItem(DEFAULT_ERROR_PATH.toString())));
    }

    protected long getRepositoryCount() {
        return errorLogRepository.count();
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

    protected ErrorLog getPersistedErrorLog(ErrorLog errorLog) {
        return errorLogRepository.findById(errorLog.getId()).orElseThrow();
    }

    protected void assertPersistedErrorLogToMatchAllProperties(ErrorLog expectedErrorLog) {
        assertErrorLogAllPropertiesEquals(expectedErrorLog, getPersistedErrorLog(expectedErrorLog));
    }

    protected void assertPersistedErrorLogToMatchUpdatableProperties(ErrorLog expectedErrorLog) {
        assertErrorLogAllUpdatablePropertiesEquals(expectedErrorLog, getPersistedErrorLog(expectedErrorLog));
    }
}
