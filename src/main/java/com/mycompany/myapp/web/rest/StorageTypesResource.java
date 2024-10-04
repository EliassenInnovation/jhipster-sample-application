package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StorageTypes;
import com.mycompany.myapp.repository.StorageTypesRepository;
import com.mycompany.myapp.repository.search.StorageTypesSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StorageTypes}.
 */
@RestController
@RequestMapping("/api/storage-types")
@Transactional
public class StorageTypesResource {

    private static final Logger LOG = LoggerFactory.getLogger(StorageTypesResource.class);

    private static final String ENTITY_NAME = "storageTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageTypesRepository storageTypesRepository;

    private final StorageTypesSearchRepository storageTypesSearchRepository;

    public StorageTypesResource(StorageTypesRepository storageTypesRepository, StorageTypesSearchRepository storageTypesSearchRepository) {
        this.storageTypesRepository = storageTypesRepository;
        this.storageTypesSearchRepository = storageTypesSearchRepository;
    }

    /**
     * {@code POST  /storage-types} : Create a new storageTypes.
     *
     * @param storageTypes the storageTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageTypes, or with status {@code 400 (Bad Request)} if the storageTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StorageTypes> createStorageTypes(@RequestBody StorageTypes storageTypes) throws URISyntaxException {
        LOG.debug("REST request to save StorageTypes : {}", storageTypes);
        if (storageTypes.getId() != null) {
            throw new BadRequestAlertException("A new storageTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        storageTypes = storageTypesRepository.save(storageTypes);
        storageTypesSearchRepository.index(storageTypes);
        return ResponseEntity.created(new URI("/api/storage-types/" + storageTypes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, storageTypes.getId().toString()))
            .body(storageTypes);
    }

    /**
     * {@code PUT  /storage-types/:id} : Updates an existing storageTypes.
     *
     * @param id the id of the storageTypes to save.
     * @param storageTypes the storageTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageTypes,
     * or with status {@code 400 (Bad Request)} if the storageTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StorageTypes> updateStorageTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageTypes storageTypes
    ) throws URISyntaxException {
        LOG.debug("REST request to update StorageTypes : {}, {}", id, storageTypes);
        if (storageTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        storageTypes = storageTypesRepository.save(storageTypes);
        storageTypesSearchRepository.index(storageTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageTypes.getId().toString()))
            .body(storageTypes);
    }

    /**
     * {@code PATCH  /storage-types/:id} : Partial updates given fields of an existing storageTypes, field will ignore if it is null
     *
     * @param id the id of the storageTypes to save.
     * @param storageTypes the storageTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageTypes,
     * or with status {@code 400 (Bad Request)} if the storageTypes is not valid,
     * or with status {@code 404 (Not Found)} if the storageTypes is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageTypes> partialUpdateStorageTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StorageTypes storageTypes
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StorageTypes partially : {}, {}", id, storageTypes);
        if (storageTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageTypes> result = storageTypesRepository
            .findById(storageTypes.getId())
            .map(existingStorageTypes -> {
                if (storageTypes.getStorageTypeId() != null) {
                    existingStorageTypes.setStorageTypeId(storageTypes.getStorageTypeId());
                }
                if (storageTypes.getStorageTypeName() != null) {
                    existingStorageTypes.setStorageTypeName(storageTypes.getStorageTypeName());
                }

                return existingStorageTypes;
            })
            .map(storageTypesRepository::save)
            .map(savedStorageTypes -> {
                storageTypesSearchRepository.index(savedStorageTypes);
                return savedStorageTypes;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageTypes.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-types} : get all the storageTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageTypes in body.
     */
    @GetMapping("")
    public List<StorageTypes> getAllStorageTypes() {
        LOG.debug("REST request to get all StorageTypes");
        return storageTypesRepository.findAll();
    }

    /**
     * {@code GET  /storage-types/:id} : get the "id" storageTypes.
     *
     * @param id the id of the storageTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StorageTypes> getStorageTypes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StorageTypes : {}", id);
        Optional<StorageTypes> storageTypes = storageTypesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(storageTypes);
    }

    /**
     * {@code DELETE  /storage-types/:id} : delete the "id" storageTypes.
     *
     * @param id the id of the storageTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageTypes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StorageTypes : {}", id);
        storageTypesRepository.deleteById(id);
        storageTypesSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /storage-types/_search?query=:query} : search for the storageTypes corresponding
     * to the query.
     *
     * @param query the query of the storageTypes search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<StorageTypes> searchStorageTypes(@RequestParam("query") String query) {
        LOG.debug("REST request to search StorageTypes for query {}", query);
        try {
            return StreamSupport.stream(storageTypesSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
