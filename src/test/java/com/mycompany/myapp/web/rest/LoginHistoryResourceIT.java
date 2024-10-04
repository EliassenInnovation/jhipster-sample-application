package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LoginHistoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LoginHistory;
import com.mycompany.myapp.repository.LoginHistoryRepository;
import com.mycompany.myapp.repository.search.LoginHistorySearchRepository;
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
 * Integration tests for the {@link LoginHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoginHistoryResourceIT {

    private static final Integer DEFAULT_FORGOT_PIN = 1;
    private static final Integer UPDATED_FORGOT_PIN = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_LOGIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOGIN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LOGIN_LOG_ID = 1;
    private static final Integer UPDATED_LOGIN_LOG_ID = 2;

    private static final String DEFAULT_LOGIN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LOG_OUT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOG_OUT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final String ENTITY_API_URL = "/api/login-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/login-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private LoginHistorySearchRepository loginHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoginHistoryMockMvc;

    private LoginHistory loginHistory;

    private LoginHistory insertedLoginHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginHistory createEntity() {
        return new LoginHistory()
            .forgotPin(DEFAULT_FORGOT_PIN)
            .isActive(DEFAULT_IS_ACTIVE)
            .loginDate(DEFAULT_LOGIN_DATE)
            .loginLogId(DEFAULT_LOGIN_LOG_ID)
            .loginType(DEFAULT_LOGIN_TYPE)
            .logOutDate(DEFAULT_LOG_OUT_DATE)
            .userId(DEFAULT_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoginHistory createUpdatedEntity() {
        return new LoginHistory()
            .forgotPin(UPDATED_FORGOT_PIN)
            .isActive(UPDATED_IS_ACTIVE)
            .loginDate(UPDATED_LOGIN_DATE)
            .loginLogId(UPDATED_LOGIN_LOG_ID)
            .loginType(UPDATED_LOGIN_TYPE)
            .logOutDate(UPDATED_LOG_OUT_DATE)
            .userId(UPDATED_USER_ID);
    }

    @BeforeEach
    public void initTest() {
        loginHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLoginHistory != null) {
            loginHistoryRepository.delete(insertedLoginHistory);
            loginHistorySearchRepository.delete(insertedLoginHistory);
            insertedLoginHistory = null;
        }
    }

    @Test
    @Transactional
    void createLoginHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        // Create the LoginHistory
        var returnedLoginHistory = om.readValue(
            restLoginHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loginHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoginHistory.class
        );

        // Validate the LoginHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLoginHistoryUpdatableFieldsEquals(returnedLoginHistory, getPersistedLoginHistory(returnedLoginHistory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedLoginHistory = returnedLoginHistory;
    }

    @Test
    @Transactional
    void createLoginHistoryWithExistingId() throws Exception {
        // Create the LoginHistory with an existing ID
        loginHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoginHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loginHistory)))
            .andExpect(status().isBadRequest());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllLoginHistories() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);

        // Get all the loginHistoryList
        restLoginHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].forgotPin").value(hasItem(DEFAULT_FORGOT_PIN)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].loginDate").value(hasItem(DEFAULT_LOGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].loginLogId").value(hasItem(DEFAULT_LOGIN_LOG_ID)))
            .andExpect(jsonPath("$.[*].loginType").value(hasItem(DEFAULT_LOGIN_TYPE)))
            .andExpect(jsonPath("$.[*].logOutDate").value(hasItem(DEFAULT_LOG_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getLoginHistory() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);

        // Get the loginHistory
        restLoginHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, loginHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loginHistory.getId().intValue()))
            .andExpect(jsonPath("$.forgotPin").value(DEFAULT_FORGOT_PIN))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.loginDate").value(DEFAULT_LOGIN_DATE.toString()))
            .andExpect(jsonPath("$.loginLogId").value(DEFAULT_LOGIN_LOG_ID))
            .andExpect(jsonPath("$.loginType").value(DEFAULT_LOGIN_TYPE))
            .andExpect(jsonPath("$.logOutDate").value(DEFAULT_LOG_OUT_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getNonExistingLoginHistory() throws Exception {
        // Get the loginHistory
        restLoginHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoginHistory() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        loginHistorySearchRepository.save(loginHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());

        // Update the loginHistory
        LoginHistory updatedLoginHistory = loginHistoryRepository.findById(loginHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoginHistory are not directly saved in db
        em.detach(updatedLoginHistory);
        updatedLoginHistory
            .forgotPin(UPDATED_FORGOT_PIN)
            .isActive(UPDATED_IS_ACTIVE)
            .loginDate(UPDATED_LOGIN_DATE)
            .loginLogId(UPDATED_LOGIN_LOG_ID)
            .loginType(UPDATED_LOGIN_TYPE)
            .logOutDate(UPDATED_LOG_OUT_DATE)
            .userId(UPDATED_USER_ID);

        restLoginHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLoginHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLoginHistory))
            )
            .andExpect(status().isOk());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoginHistoryToMatchAllProperties(updatedLoginHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<LoginHistory> loginHistorySearchList = Streamable.of(loginHistorySearchRepository.findAll()).toList();
                LoginHistory testLoginHistorySearch = loginHistorySearchList.get(searchDatabaseSizeAfter - 1);

                assertLoginHistoryAllPropertiesEquals(testLoginHistorySearch, updatedLoginHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        loginHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loginHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loginHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        loginHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loginHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        loginHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loginHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateLoginHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loginHistory using partial update
        LoginHistory partialUpdatedLoginHistory = new LoginHistory();
        partialUpdatedLoginHistory.setId(loginHistory.getId());

        partialUpdatedLoginHistory.isActive(UPDATED_IS_ACTIVE).loginDate(UPDATED_LOGIN_DATE).logOutDate(UPDATED_LOG_OUT_DATE);

        restLoginHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoginHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoginHistory))
            )
            .andExpect(status().isOk());

        // Validate the LoginHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoginHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLoginHistory, loginHistory),
            getPersistedLoginHistory(loginHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateLoginHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loginHistory using partial update
        LoginHistory partialUpdatedLoginHistory = new LoginHistory();
        partialUpdatedLoginHistory.setId(loginHistory.getId());

        partialUpdatedLoginHistory
            .forgotPin(UPDATED_FORGOT_PIN)
            .isActive(UPDATED_IS_ACTIVE)
            .loginDate(UPDATED_LOGIN_DATE)
            .loginLogId(UPDATED_LOGIN_LOG_ID)
            .loginType(UPDATED_LOGIN_TYPE)
            .logOutDate(UPDATED_LOG_OUT_DATE)
            .userId(UPDATED_USER_ID);

        restLoginHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoginHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoginHistory))
            )
            .andExpect(status().isOk());

        // Validate the LoginHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoginHistoryUpdatableFieldsEquals(partialUpdatedLoginHistory, getPersistedLoginHistory(partialUpdatedLoginHistory));
    }

    @Test
    @Transactional
    void patchNonExistingLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        loginHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loginHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loginHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        loginHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loginHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoginHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        loginHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoginHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loginHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoginHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteLoginHistory() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);
        loginHistoryRepository.save(loginHistory);
        loginHistorySearchRepository.save(loginHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the loginHistory
        restLoginHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, loginHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(loginHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchLoginHistory() throws Exception {
        // Initialize the database
        insertedLoginHistory = loginHistoryRepository.saveAndFlush(loginHistory);
        loginHistorySearchRepository.save(loginHistory);

        // Search the loginHistory
        restLoginHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loginHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].forgotPin").value(hasItem(DEFAULT_FORGOT_PIN)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].loginDate").value(hasItem(DEFAULT_LOGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].loginLogId").value(hasItem(DEFAULT_LOGIN_LOG_ID)))
            .andExpect(jsonPath("$.[*].loginType").value(hasItem(DEFAULT_LOGIN_TYPE)))
            .andExpect(jsonPath("$.[*].logOutDate").value(hasItem(DEFAULT_LOG_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    protected long getRepositoryCount() {
        return loginHistoryRepository.count();
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

    protected LoginHistory getPersistedLoginHistory(LoginHistory loginHistory) {
        return loginHistoryRepository.findById(loginHistory.getId()).orElseThrow();
    }

    protected void assertPersistedLoginHistoryToMatchAllProperties(LoginHistory expectedLoginHistory) {
        assertLoginHistoryAllPropertiesEquals(expectedLoginHistory, getPersistedLoginHistory(expectedLoginHistory));
    }

    protected void assertPersistedLoginHistoryToMatchUpdatableProperties(LoginHistory expectedLoginHistory) {
        assertLoginHistoryAllUpdatablePropertiesEquals(expectedLoginHistory, getPersistedLoginHistory(expectedLoginHistory));
    }
}
