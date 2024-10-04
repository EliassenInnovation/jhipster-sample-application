package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PrivacyType;
import com.mycompany.myapp.repository.PrivacyTypeRepository;
import com.mycompany.myapp.repository.search.PrivacyTypeSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PrivacyType}.
 */
@RestController
@RequestMapping("/api/privacy-types")
@Transactional
public class PrivacyTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PrivacyTypeResource.class);

    private static final String ENTITY_NAME = "privacyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrivacyTypeRepository privacyTypeRepository;

    private final PrivacyTypeSearchRepository privacyTypeSearchRepository;

    public PrivacyTypeResource(PrivacyTypeRepository privacyTypeRepository, PrivacyTypeSearchRepository privacyTypeSearchRepository) {
        this.privacyTypeRepository = privacyTypeRepository;
        this.privacyTypeSearchRepository = privacyTypeSearchRepository;
    }

    /**
     * {@code POST  /privacy-types} : Create a new privacyType.
     *
     * @param privacyType the privacyType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new privacyType, or with status {@code 400 (Bad Request)} if the privacyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PrivacyType> createPrivacyType(@RequestBody PrivacyType privacyType) throws URISyntaxException {
        LOG.debug("REST request to save PrivacyType : {}", privacyType);
        if (privacyType.getId() != null) {
            throw new BadRequestAlertException("A new privacyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        privacyType = privacyTypeRepository.save(privacyType);
        privacyTypeSearchRepository.index(privacyType);
        return ResponseEntity.created(new URI("/api/privacy-types/" + privacyType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, privacyType.getId().toString()))
            .body(privacyType);
    }

    /**
     * {@code PUT  /privacy-types/:id} : Updates an existing privacyType.
     *
     * @param id the id of the privacyType to save.
     * @param privacyType the privacyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated privacyType,
     * or with status {@code 400 (Bad Request)} if the privacyType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the privacyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrivacyType> updatePrivacyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrivacyType privacyType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PrivacyType : {}, {}", id, privacyType);
        if (privacyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, privacyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!privacyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        privacyType = privacyTypeRepository.save(privacyType);
        privacyTypeSearchRepository.index(privacyType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, privacyType.getId().toString()))
            .body(privacyType);
    }

    /**
     * {@code PATCH  /privacy-types/:id} : Partial updates given fields of an existing privacyType, field will ignore if it is null
     *
     * @param id the id of the privacyType to save.
     * @param privacyType the privacyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated privacyType,
     * or with status {@code 400 (Bad Request)} if the privacyType is not valid,
     * or with status {@code 404 (Not Found)} if the privacyType is not found,
     * or with status {@code 500 (Internal Server Error)} if the privacyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrivacyType> partialUpdatePrivacyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrivacyType privacyType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PrivacyType partially : {}, {}", id, privacyType);
        if (privacyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, privacyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!privacyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrivacyType> result = privacyTypeRepository
            .findById(privacyType.getId())
            .map(existingPrivacyType -> {
                if (privacyType.getPrivacyTypeId() != null) {
                    existingPrivacyType.setPrivacyTypeId(privacyType.getPrivacyTypeId());
                }
                if (privacyType.getPrivacyTypeName() != null) {
                    existingPrivacyType.setPrivacyTypeName(privacyType.getPrivacyTypeName());
                }

                return existingPrivacyType;
            })
            .map(privacyTypeRepository::save)
            .map(savedPrivacyType -> {
                privacyTypeSearchRepository.index(savedPrivacyType);
                return savedPrivacyType;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, privacyType.getId().toString())
        );
    }

    /**
     * {@code GET  /privacy-types} : get all the privacyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of privacyTypes in body.
     */
    @GetMapping("")
    public List<PrivacyType> getAllPrivacyTypes() {
        LOG.debug("REST request to get all PrivacyTypes");
        return privacyTypeRepository.findAll();
    }

    /**
     * {@code GET  /privacy-types/:id} : get the "id" privacyType.
     *
     * @param id the id of the privacyType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the privacyType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrivacyType> getPrivacyType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PrivacyType : {}", id);
        Optional<PrivacyType> privacyType = privacyTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(privacyType);
    }

    /**
     * {@code DELETE  /privacy-types/:id} : delete the "id" privacyType.
     *
     * @param id the id of the privacyType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivacyType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PrivacyType : {}", id);
        privacyTypeRepository.deleteById(id);
        privacyTypeSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /privacy-types/_search?query=:query} : search for the privacyType corresponding
     * to the query.
     *
     * @param query the query of the privacyType search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<PrivacyType> searchPrivacyTypes(@RequestParam("query") String query) {
        LOG.debug("REST request to search PrivacyTypes for query {}", query);
        try {
            return StreamSupport.stream(privacyTypeSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
