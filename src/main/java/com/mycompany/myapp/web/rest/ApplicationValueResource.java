package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ApplicationValue;
import com.mycompany.myapp.repository.ApplicationValueRepository;
import com.mycompany.myapp.repository.search.ApplicationValueSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ApplicationValue}.
 */
@RestController
@RequestMapping("/api/application-values")
@Transactional
public class ApplicationValueResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationValueResource.class);

    private static final String ENTITY_NAME = "applicationValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationValueRepository applicationValueRepository;

    private final ApplicationValueSearchRepository applicationValueSearchRepository;

    public ApplicationValueResource(
        ApplicationValueRepository applicationValueRepository,
        ApplicationValueSearchRepository applicationValueSearchRepository
    ) {
        this.applicationValueRepository = applicationValueRepository;
        this.applicationValueSearchRepository = applicationValueSearchRepository;
    }

    /**
     * {@code POST  /application-values} : Create a new applicationValue.
     *
     * @param applicationValue the applicationValue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationValue, or with status {@code 400 (Bad Request)} if the applicationValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationValue> createApplicationValue(@RequestBody ApplicationValue applicationValue)
        throws URISyntaxException {
        LOG.debug("REST request to save ApplicationValue : {}", applicationValue);
        if (applicationValue.getId() != null) {
            throw new BadRequestAlertException("A new applicationValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        applicationValue = applicationValueRepository.save(applicationValue);
        applicationValueSearchRepository.index(applicationValue);
        return ResponseEntity.created(new URI("/api/application-values/" + applicationValue.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, applicationValue.getId().toString()))
            .body(applicationValue);
    }

    /**
     * {@code PUT  /application-values/:id} : Updates an existing applicationValue.
     *
     * @param id the id of the applicationValue to save.
     * @param applicationValue the applicationValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationValue,
     * or with status {@code 400 (Bad Request)} if the applicationValue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationValue> updateApplicationValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationValue applicationValue
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApplicationValue : {}, {}", id, applicationValue);
        if (applicationValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationValue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        applicationValue = applicationValueRepository.save(applicationValue);
        applicationValueSearchRepository.index(applicationValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationValue.getId().toString()))
            .body(applicationValue);
    }

    /**
     * {@code PATCH  /application-values/:id} : Partial updates given fields of an existing applicationValue, field will ignore if it is null
     *
     * @param id the id of the applicationValue to save.
     * @param applicationValue the applicationValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationValue,
     * or with status {@code 400 (Bad Request)} if the applicationValue is not valid,
     * or with status {@code 404 (Not Found)} if the applicationValue is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationValue> partialUpdateApplicationValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationValue applicationValue
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApplicationValue partially : {}, {}", id, applicationValue);
        if (applicationValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationValue.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationValue> result = applicationValueRepository
            .findById(applicationValue.getId())
            .map(existingApplicationValue -> {
                if (applicationValue.getApplicationValueId() != null) {
                    existingApplicationValue.setApplicationValueId(applicationValue.getApplicationValueId());
                }
                if (applicationValue.getKey() != null) {
                    existingApplicationValue.setKey(applicationValue.getKey());
                }
                if (applicationValue.getValueDate() != null) {
                    existingApplicationValue.setValueDate(applicationValue.getValueDate());
                }
                if (applicationValue.getValueInt() != null) {
                    existingApplicationValue.setValueInt(applicationValue.getValueInt());
                }
                if (applicationValue.getValueLong() != null) {
                    existingApplicationValue.setValueLong(applicationValue.getValueLong());
                }
                if (applicationValue.getValueString() != null) {
                    existingApplicationValue.setValueString(applicationValue.getValueString());
                }

                return existingApplicationValue;
            })
            .map(applicationValueRepository::save)
            .map(savedApplicationValue -> {
                applicationValueSearchRepository.index(savedApplicationValue);
                return savedApplicationValue;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationValue.getId().toString())
        );
    }

    /**
     * {@code GET  /application-values} : get all the applicationValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationValues in body.
     */
    @GetMapping("")
    public List<ApplicationValue> getAllApplicationValues() {
        LOG.debug("REST request to get all ApplicationValues");
        return applicationValueRepository.findAll();
    }

    /**
     * {@code GET  /application-values/:id} : get the "id" applicationValue.
     *
     * @param id the id of the applicationValue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationValue> getApplicationValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApplicationValue : {}", id);
        Optional<ApplicationValue> applicationValue = applicationValueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicationValue);
    }

    /**
     * {@code DELETE  /application-values/:id} : delete the "id" applicationValue.
     *
     * @param id the id of the applicationValue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApplicationValue : {}", id);
        applicationValueRepository.deleteById(id);
        applicationValueSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /application-values/_search?query=:query} : search for the applicationValue corresponding
     * to the query.
     *
     * @param query the query of the applicationValue search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ApplicationValue> searchApplicationValues(@RequestParam("query") String query) {
        LOG.debug("REST request to search ApplicationValues for query {}", query);
        try {
            return StreamSupport.stream(applicationValueSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
