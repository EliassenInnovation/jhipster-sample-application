package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ManufacturerAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Manufacturer;
import com.mycompany.myapp.repository.ManufacturerRepository;
import com.mycompany.myapp.repository.search.ManufacturerSearchRepository;
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
 * Integration tests for the {@link ManufacturerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManufacturerResourceIT {

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GLN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GLN_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final Integer DEFAULT_MANUFACTURER_ID = 1;
    private static final Integer UPDATED_MANUFACTURER_ID = 2;

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manufacturers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/manufacturers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ManufacturerSearchRepository manufacturerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManufacturerMockMvc;

    private Manufacturer manufacturer;

    private Manufacturer insertedManufacturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacturer createEntity() {
        return new Manufacturer()
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .glnNumber(DEFAULT_GLN_NUMBER)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastName(DEFAULT_LAST_NAME)
            .manufacturer(DEFAULT_MANUFACTURER)
            .manufacturerId(DEFAULT_MANUFACTURER_ID)
            .password(DEFAULT_PASSWORD)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacturer createUpdatedEntity() {
        return new Manufacturer()
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .glnNumber(UPDATED_GLN_NUMBER)
            .isActive(UPDATED_IS_ACTIVE)
            .lastName(UPDATED_LAST_NAME)
            .manufacturer(UPDATED_MANUFACTURER)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .password(UPDATED_PASSWORD)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        manufacturer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedManufacturer != null) {
            manufacturerRepository.delete(insertedManufacturer);
            manufacturerSearchRepository.delete(insertedManufacturer);
            insertedManufacturer = null;
        }
    }

    @Test
    @Transactional
    void createManufacturer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        // Create the Manufacturer
        var returnedManufacturer = om.readValue(
            restManufacturerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manufacturer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Manufacturer.class
        );

        // Validate the Manufacturer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertManufacturerUpdatableFieldsEquals(returnedManufacturer, getPersistedManufacturer(returnedManufacturer));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedManufacturer = returnedManufacturer;
    }

    @Test
    @Transactional
    void createManufacturerWithExistingId() throws Exception {
        // Create the Manufacturer with an existing ID
        manufacturer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restManufacturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manufacturer)))
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllManufacturers() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].glnNumber").value(hasItem(DEFAULT_GLN_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getManufacturer() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);

        // Get the manufacturer
        restManufacturerMockMvc
            .perform(get(ENTITY_API_URL_ID, manufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manufacturer.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.glnNumber").value(DEFAULT_GLN_NUMBER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER))
            .andExpect(jsonPath("$.manufacturerId").value(DEFAULT_MANUFACTURER_ID))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingManufacturer() throws Exception {
        // Get the manufacturer
        restManufacturerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManufacturer() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        manufacturerSearchRepository.save(manufacturer);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());

        // Update the manufacturer
        Manufacturer updatedManufacturer = manufacturerRepository.findById(manufacturer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedManufacturer are not directly saved in db
        em.detach(updatedManufacturer);
        updatedManufacturer
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .glnNumber(UPDATED_GLN_NUMBER)
            .isActive(UPDATED_IS_ACTIVE)
            .lastName(UPDATED_LAST_NAME)
            .manufacturer(UPDATED_MANUFACTURER)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .password(UPDATED_PASSWORD)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedManufacturer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedManufacturerToMatchAllProperties(updatedManufacturer);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Manufacturer> manufacturerSearchList = Streamable.of(manufacturerSearchRepository.findAll()).toList();
                Manufacturer testManufacturerSearch = manufacturerSearchList.get(searchDatabaseSizeAfter - 1);

                assertManufacturerAllPropertiesEquals(testManufacturerSearch, updatedManufacturer);
            });
    }

    @Test
    @Transactional
    void putNonExistingManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        manufacturer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manufacturer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manufacturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        manufacturer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manufacturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        manufacturer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manufacturer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateManufacturerWithPatch() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manufacturer using partial update
        Manufacturer partialUpdatedManufacturer = new Manufacturer();
        partialUpdatedManufacturer.setId(manufacturer.getId());

        partialUpdatedManufacturer
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .password(UPDATED_PASSWORD);

        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManufacturerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedManufacturer, manufacturer),
            getPersistedManufacturer(manufacturer)
        );
    }

    @Test
    @Transactional
    void fullUpdateManufacturerWithPatch() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manufacturer using partial update
        Manufacturer partialUpdatedManufacturer = new Manufacturer();
        partialUpdatedManufacturer.setId(manufacturer.getId());

        partialUpdatedManufacturer
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .glnNumber(UPDATED_GLN_NUMBER)
            .isActive(UPDATED_IS_ACTIVE)
            .lastName(UPDATED_LAST_NAME)
            .manufacturer(UPDATED_MANUFACTURER)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .password(UPDATED_PASSWORD)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManufacturer))
            )
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManufacturerUpdatableFieldsEquals(partialUpdatedManufacturer, getPersistedManufacturer(partialUpdatedManufacturer));
    }

    @Test
    @Transactional
    void patchNonExistingManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        manufacturer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manufacturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manufacturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        manufacturer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manufacturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManufacturer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        manufacturer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufacturerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(manufacturer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufacturer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteManufacturer() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);
        manufacturerRepository.save(manufacturer);
        manufacturerSearchRepository.save(manufacturer);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the manufacturer
        restManufacturerMockMvc
            .perform(delete(ENTITY_API_URL_ID, manufacturer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manufacturerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchManufacturer() throws Exception {
        // Initialize the database
        insertedManufacturer = manufacturerRepository.saveAndFlush(manufacturer);
        manufacturerSearchRepository.save(manufacturer);

        // Search the manufacturer
        restManufacturerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + manufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].glnNumber").value(hasItem(DEFAULT_GLN_NUMBER)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    protected long getRepositoryCount() {
        return manufacturerRepository.count();
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

    protected Manufacturer getPersistedManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.findById(manufacturer.getId()).orElseThrow();
    }

    protected void assertPersistedManufacturerToMatchAllProperties(Manufacturer expectedManufacturer) {
        assertManufacturerAllPropertiesEquals(expectedManufacturer, getPersistedManufacturer(expectedManufacturer));
    }

    protected void assertPersistedManufacturerToMatchUpdatableProperties(Manufacturer expectedManufacturer) {
        assertManufacturerAllUpdatablePropertiesEquals(expectedManufacturer, getPersistedManufacturer(expectedManufacturer));
    }
}
