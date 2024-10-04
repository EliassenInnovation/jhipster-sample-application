package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ApplicationValueAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ApplicationValue;
import com.mycompany.myapp.repository.ApplicationValueRepository;
import com.mycompany.myapp.repository.search.ApplicationValueSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link ApplicationValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationValueResourceIT {

    private static final Integer DEFAULT_APPLICATION_VALUE_ID = 1;
    private static final Integer UPDATED_APPLICATION_VALUE_ID = 2;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_VALUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_VALUE_INT = 1;
    private static final Integer UPDATED_VALUE_INT = 2;

    private static final Long DEFAULT_VALUE_LONG = 1L;
    private static final Long UPDATED_VALUE_LONG = 2L;

    private static final String DEFAULT_VALUE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_STRING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/application-values/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationValueRepository applicationValueRepository;

    @Autowired
    private ApplicationValueSearchRepository applicationValueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationValueMockMvc;

    private ApplicationValue applicationValue;

    private ApplicationValue insertedApplicationValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationValue createEntity() {
        return new ApplicationValue()
            .applicationValueId(DEFAULT_APPLICATION_VALUE_ID)
            .key(DEFAULT_KEY)
            .valueDate(DEFAULT_VALUE_DATE)
            .valueInt(DEFAULT_VALUE_INT)
            .valueLong(DEFAULT_VALUE_LONG)
            .valueString(DEFAULT_VALUE_STRING);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationValue createUpdatedEntity() {
        return new ApplicationValue()
            .applicationValueId(UPDATED_APPLICATION_VALUE_ID)
            .key(UPDATED_KEY)
            .valueDate(UPDATED_VALUE_DATE)
            .valueInt(UPDATED_VALUE_INT)
            .valueLong(UPDATED_VALUE_LONG)
            .valueString(UPDATED_VALUE_STRING);
    }

    @BeforeEach
    public void initTest() {
        applicationValue = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedApplicationValue != null) {
            applicationValueRepository.delete(insertedApplicationValue);
            applicationValueSearchRepository.delete(insertedApplicationValue);
            insertedApplicationValue = null;
        }
    }

    @Test
    @Transactional
    void createApplicationValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        // Create the ApplicationValue
        var returnedApplicationValue = om.readValue(
            restApplicationValueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationValue)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApplicationValue.class
        );

        // Validate the ApplicationValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertApplicationValueUpdatableFieldsEquals(returnedApplicationValue, getPersistedApplicationValue(returnedApplicationValue));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedApplicationValue = returnedApplicationValue;
    }

    @Test
    @Transactional
    void createApplicationValueWithExistingId() throws Exception {
        // Create the ApplicationValue with an existing ID
        applicationValue.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationValue)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllApplicationValues() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);

        // Get all the applicationValueList
        restApplicationValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationValueId").value(hasItem(DEFAULT_APPLICATION_VALUE_ID)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueDate").value(hasItem(sameInstant(DEFAULT_VALUE_DATE))))
            .andExpect(jsonPath("$.[*].valueInt").value(hasItem(DEFAULT_VALUE_INT)))
            .andExpect(jsonPath("$.[*].valueLong").value(hasItem(DEFAULT_VALUE_LONG.intValue())))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING)));
    }

    @Test
    @Transactional
    void getApplicationValue() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);

        // Get the applicationValue
        restApplicationValueMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationValue.getId().intValue()))
            .andExpect(jsonPath("$.applicationValueId").value(DEFAULT_APPLICATION_VALUE_ID))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.valueDate").value(sameInstant(DEFAULT_VALUE_DATE)))
            .andExpect(jsonPath("$.valueInt").value(DEFAULT_VALUE_INT))
            .andExpect(jsonPath("$.valueLong").value(DEFAULT_VALUE_LONG.intValue()))
            .andExpect(jsonPath("$.valueString").value(DEFAULT_VALUE_STRING));
    }

    @Test
    @Transactional
    void getNonExistingApplicationValue() throws Exception {
        // Get the applicationValue
        restApplicationValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationValue() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        applicationValueSearchRepository.save(applicationValue);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());

        // Update the applicationValue
        ApplicationValue updatedApplicationValue = applicationValueRepository.findById(applicationValue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationValue are not directly saved in db
        em.detach(updatedApplicationValue);
        updatedApplicationValue
            .applicationValueId(UPDATED_APPLICATION_VALUE_ID)
            .key(UPDATED_KEY)
            .valueDate(UPDATED_VALUE_DATE)
            .valueInt(UPDATED_VALUE_INT)
            .valueLong(UPDATED_VALUE_LONG)
            .valueString(UPDATED_VALUE_STRING);

        restApplicationValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedApplicationValue))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationValueToMatchAllProperties(updatedApplicationValue);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ApplicationValue> applicationValueSearchList = Streamable.of(applicationValueSearchRepository.findAll()).toList();
                ApplicationValue testApplicationValueSearch = applicationValueSearchList.get(searchDatabaseSizeAfter - 1);

                assertApplicationValueAllPropertiesEquals(testApplicationValueSearch, updatedApplicationValue);
            });
    }

    @Test
    @Transactional
    void putNonExistingApplicationValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        applicationValue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        applicationValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(applicationValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        applicationValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(applicationValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateApplicationValueWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationValue using partial update
        ApplicationValue partialUpdatedApplicationValue = new ApplicationValue();
        partialUpdatedApplicationValue.setId(applicationValue.getId());

        partialUpdatedApplicationValue
            .key(UPDATED_KEY)
            .valueDate(UPDATED_VALUE_DATE)
            .valueInt(UPDATED_VALUE_INT)
            .valueLong(UPDATED_VALUE_LONG)
            .valueString(UPDATED_VALUE_STRING);

        restApplicationValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationValue))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationValueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplicationValue, applicationValue),
            getPersistedApplicationValue(applicationValue)
        );
    }

    @Test
    @Transactional
    void fullUpdateApplicationValueWithPatch() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the applicationValue using partial update
        ApplicationValue partialUpdatedApplicationValue = new ApplicationValue();
        partialUpdatedApplicationValue.setId(applicationValue.getId());

        partialUpdatedApplicationValue
            .applicationValueId(UPDATED_APPLICATION_VALUE_ID)
            .key(UPDATED_KEY)
            .valueDate(UPDATED_VALUE_DATE)
            .valueInt(UPDATED_VALUE_INT)
            .valueLong(UPDATED_VALUE_LONG)
            .valueString(UPDATED_VALUE_STRING);

        restApplicationValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApplicationValue))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationValueUpdatableFieldsEquals(
            partialUpdatedApplicationValue,
            getPersistedApplicationValue(partialUpdatedApplicationValue)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApplicationValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        applicationValue.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        applicationValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(applicationValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        applicationValue.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationValueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(applicationValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteApplicationValue() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);
        applicationValueRepository.save(applicationValue);
        applicationValueSearchRepository.save(applicationValue);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the applicationValue
        restApplicationValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(applicationValueSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchApplicationValue() throws Exception {
        // Initialize the database
        insertedApplicationValue = applicationValueRepository.saveAndFlush(applicationValue);
        applicationValueSearchRepository.save(applicationValue);

        // Search the applicationValue
        restApplicationValueMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + applicationValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationValueId").value(hasItem(DEFAULT_APPLICATION_VALUE_ID)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].valueDate").value(hasItem(sameInstant(DEFAULT_VALUE_DATE))))
            .andExpect(jsonPath("$.[*].valueInt").value(hasItem(DEFAULT_VALUE_INT)))
            .andExpect(jsonPath("$.[*].valueLong").value(hasItem(DEFAULT_VALUE_LONG.intValue())))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING)));
    }

    protected long getRepositoryCount() {
        return applicationValueRepository.count();
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

    protected ApplicationValue getPersistedApplicationValue(ApplicationValue applicationValue) {
        return applicationValueRepository.findById(applicationValue.getId()).orElseThrow();
    }

    protected void assertPersistedApplicationValueToMatchAllProperties(ApplicationValue expectedApplicationValue) {
        assertApplicationValueAllPropertiesEquals(expectedApplicationValue, getPersistedApplicationValue(expectedApplicationValue));
    }

    protected void assertPersistedApplicationValueToMatchUpdatableProperties(ApplicationValue expectedApplicationValue) {
        assertApplicationValueAllUpdatablePropertiesEquals(
            expectedApplicationValue,
            getPersistedApplicationValue(expectedApplicationValue)
        );
    }
}
