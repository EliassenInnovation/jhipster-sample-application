package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DistributorAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Distributor;
import com.mycompany.myapp.repository.DistributorRepository;
import com.mycompany.myapp.repository.search.DistributorSearchRepository;
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
 * Integration tests for the {@link DistributorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DistributorResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DISTRIBUTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISTRIBUTOR_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISTRIBUTOR_ID = 1;
    private static final Integer UPDATED_DISTRIBUTOR_ID = 2;

    private static final String DEFAULT_DISTRIBUTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISTRIBUTOR_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/distributors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/distributors/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private DistributorSearchRepository distributorSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistributorMockMvc;

    private Distributor distributor;

    private Distributor insertedDistributor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distributor createEntity() {
        return new Distributor()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .distributorCode(DEFAULT_DISTRIBUTOR_CODE)
            .distributorID(DEFAULT_DISTRIBUTOR_ID)
            .distributorName(DEFAULT_DISTRIBUTOR_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distributor createUpdatedEntity() {
        return new Distributor()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorCode(UPDATED_DISTRIBUTOR_CODE)
            .distributorID(UPDATED_DISTRIBUTOR_ID)
            .distributorName(UPDATED_DISTRIBUTOR_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);
    }

    @BeforeEach
    public void initTest() {
        distributor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDistributor != null) {
            distributorRepository.delete(insertedDistributor);
            distributorSearchRepository.delete(insertedDistributor);
            insertedDistributor = null;
        }
    }

    @Test
    @Transactional
    void createDistributor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        // Create the Distributor
        var returnedDistributor = om.readValue(
            restDistributorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distributor)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Distributor.class
        );

        // Validate the Distributor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDistributorUpdatableFieldsEquals(returnedDistributor, getPersistedDistributor(returnedDistributor));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDistributor = returnedDistributor;
    }

    @Test
    @Transactional
    void createDistributorWithExistingId() throws Exception {
        // Create the Distributor with an existing ID
        distributor.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistributorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distributor)))
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDistributors() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);

        // Get all the distributorList
        restDistributorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distributor.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].distributorCode").value(hasItem(DEFAULT_DISTRIBUTOR_CODE)))
            .andExpect(jsonPath("$.[*].distributorID").value(hasItem(DEFAULT_DISTRIBUTOR_ID)))
            .andExpect(jsonPath("$.[*].distributorName").value(hasItem(DEFAULT_DISTRIBUTOR_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getDistributor() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);

        // Get the distributor
        restDistributorMockMvc
            .perform(get(ENTITY_API_URL_ID, distributor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(distributor.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.distributorCode").value(DEFAULT_DISTRIBUTOR_CODE))
            .andExpect(jsonPath("$.distributorID").value(DEFAULT_DISTRIBUTOR_ID))
            .andExpect(jsonPath("$.distributorName").value(DEFAULT_DISTRIBUTOR_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDistributor() throws Exception {
        // Get the distributor
        restDistributorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDistributor() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        distributorSearchRepository.save(distributor);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());

        // Update the distributor
        Distributor updatedDistributor = distributorRepository.findById(distributor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDistributor are not directly saved in db
        em.detach(updatedDistributor);
        updatedDistributor
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorCode(UPDATED_DISTRIBUTOR_CODE)
            .distributorID(UPDATED_DISTRIBUTOR_ID)
            .distributorName(UPDATED_DISTRIBUTOR_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restDistributorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDistributor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDistributor))
            )
            .andExpect(status().isOk());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDistributorToMatchAllProperties(updatedDistributor);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Distributor> distributorSearchList = Streamable.of(distributorSearchRepository.findAll()).toList();
                Distributor testDistributorSearch = distributorSearchList.get(searchDatabaseSizeAfter - 1);

                assertDistributorAllPropertiesEquals(testDistributorSearch, updatedDistributor);
            });
    }

    @Test
    @Transactional
    void putNonExistingDistributor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        distributor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistributorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, distributor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistributor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        distributor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistributorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistributor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        distributor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistributorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distributor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDistributorWithPatch() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distributor using partial update
        Distributor partialUpdatedDistributor = new Distributor();
        partialUpdatedDistributor.setId(distributor.getId());

        partialUpdatedDistributor
            .createdOn(UPDATED_CREATED_ON)
            .isActive(UPDATED_IS_ACTIVE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restDistributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistributor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistributor))
            )
            .andExpect(status().isOk());

        // Validate the Distributor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistributorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDistributor, distributor),
            getPersistedDistributor(distributor)
        );
    }

    @Test
    @Transactional
    void fullUpdateDistributorWithPatch() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distributor using partial update
        Distributor partialUpdatedDistributor = new Distributor();
        partialUpdatedDistributor.setId(distributor.getId());

        partialUpdatedDistributor
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .distributorCode(UPDATED_DISTRIBUTOR_CODE)
            .distributorID(UPDATED_DISTRIBUTOR_ID)
            .distributorName(UPDATED_DISTRIBUTOR_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON);

        restDistributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistributor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistributor))
            )
            .andExpect(status().isOk());

        // Validate the Distributor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistributorUpdatableFieldsEquals(partialUpdatedDistributor, getPersistedDistributor(partialUpdatedDistributor));
    }

    @Test
    @Transactional
    void patchNonExistingDistributor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        distributor.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, distributor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistributor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        distributor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistributor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        distributor.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistributorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(distributor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distributor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDistributor() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);
        distributorRepository.save(distributor);
        distributorSearchRepository.save(distributor);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the distributor
        restDistributorMockMvc
            .perform(delete(ENTITY_API_URL_ID, distributor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(distributorSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDistributor() throws Exception {
        // Initialize the database
        insertedDistributor = distributorRepository.saveAndFlush(distributor);
        distributorSearchRepository.save(distributor);

        // Search the distributor
        restDistributorMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + distributor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distributor.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].distributorCode").value(hasItem(DEFAULT_DISTRIBUTOR_CODE)))
            .andExpect(jsonPath("$.[*].distributorID").value(hasItem(DEFAULT_DISTRIBUTOR_ID)))
            .andExpect(jsonPath("$.[*].distributorName").value(hasItem(DEFAULT_DISTRIBUTOR_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    protected long getRepositoryCount() {
        return distributorRepository.count();
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

    protected Distributor getPersistedDistributor(Distributor distributor) {
        return distributorRepository.findById(distributor.getId()).orElseThrow();
    }

    protected void assertPersistedDistributorToMatchAllProperties(Distributor expectedDistributor) {
        assertDistributorAllPropertiesEquals(expectedDistributor, getPersistedDistributor(expectedDistributor));
    }

    protected void assertPersistedDistributorToMatchUpdatableProperties(Distributor expectedDistributor) {
        assertDistributorAllUpdatablePropertiesEquals(expectedDistributor, getPersistedDistributor(expectedDistributor));
    }
}
