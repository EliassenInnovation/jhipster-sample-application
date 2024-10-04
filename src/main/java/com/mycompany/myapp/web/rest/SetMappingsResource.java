package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SetMappings;
import com.mycompany.myapp.repository.SetMappingsRepository;
import com.mycompany.myapp.repository.search.SetMappingsSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SetMappings}.
 */
@RestController
@RequestMapping("/api/set-mappings")
@Transactional
public class SetMappingsResource {

    private static final Logger LOG = LoggerFactory.getLogger(SetMappingsResource.class);

    private static final String ENTITY_NAME = "setMappings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SetMappingsRepository setMappingsRepository;

    private final SetMappingsSearchRepository setMappingsSearchRepository;

    public SetMappingsResource(SetMappingsRepository setMappingsRepository, SetMappingsSearchRepository setMappingsSearchRepository) {
        this.setMappingsRepository = setMappingsRepository;
        this.setMappingsSearchRepository = setMappingsSearchRepository;
    }

    /**
     * {@code POST  /set-mappings} : Create a new setMappings.
     *
     * @param setMappings the setMappings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new setMappings, or with status {@code 400 (Bad Request)} if the setMappings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SetMappings> createSetMappings(@RequestBody SetMappings setMappings) throws URISyntaxException {
        LOG.debug("REST request to save SetMappings : {}", setMappings);
        if (setMappings.getId() != null) {
            throw new BadRequestAlertException("A new setMappings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        setMappings = setMappingsRepository.save(setMappings);
        setMappingsSearchRepository.index(setMappings);
        return ResponseEntity.created(new URI("/api/set-mappings/" + setMappings.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, setMappings.getId().toString()))
            .body(setMappings);
    }

    /**
     * {@code PUT  /set-mappings/:id} : Updates an existing setMappings.
     *
     * @param id the id of the setMappings to save.
     * @param setMappings the setMappings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setMappings,
     * or with status {@code 400 (Bad Request)} if the setMappings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the setMappings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SetMappings> updateSetMappings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SetMappings setMappings
    ) throws URISyntaxException {
        LOG.debug("REST request to update SetMappings : {}, {}", id, setMappings);
        if (setMappings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setMappings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setMappingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        setMappings = setMappingsRepository.save(setMappings);
        setMappingsSearchRepository.index(setMappings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setMappings.getId().toString()))
            .body(setMappings);
    }

    /**
     * {@code PATCH  /set-mappings/:id} : Partial updates given fields of an existing setMappings, field will ignore if it is null
     *
     * @param id the id of the setMappings to save.
     * @param setMappings the setMappings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setMappings,
     * or with status {@code 400 (Bad Request)} if the setMappings is not valid,
     * or with status {@code 404 (Not Found)} if the setMappings is not found,
     * or with status {@code 500 (Internal Server Error)} if the setMappings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SetMappings> partialUpdateSetMappings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SetMappings setMappings
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SetMappings partially : {}, {}", id, setMappings);
        if (setMappings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setMappings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setMappingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SetMappings> result = setMappingsRepository
            .findById(setMappings.getId())
            .map(existingSetMappings -> {
                if (setMappings.getiD() != null) {
                    existingSetMappings.setiD(setMappings.getiD());
                }
                if (setMappings.getOneWorldValue() != null) {
                    existingSetMappings.setOneWorldValue(setMappings.getOneWorldValue());
                }
                if (setMappings.getProductValue() != null) {
                    existingSetMappings.setProductValue(setMappings.getProductValue());
                }
                if (setMappings.getSetName() != null) {
                    existingSetMappings.setSetName(setMappings.getSetName());
                }

                return existingSetMappings;
            })
            .map(setMappingsRepository::save)
            .map(savedSetMappings -> {
                setMappingsSearchRepository.index(savedSetMappings);
                return savedSetMappings;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setMappings.getId().toString())
        );
    }

    /**
     * {@code GET  /set-mappings} : get all the setMappings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of setMappings in body.
     */
    @GetMapping("")
    public List<SetMappings> getAllSetMappings() {
        LOG.debug("REST request to get all SetMappings");
        return setMappingsRepository.findAll();
    }

    /**
     * {@code GET  /set-mappings/:id} : get the "id" setMappings.
     *
     * @param id the id of the setMappings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the setMappings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SetMappings> getSetMappings(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SetMappings : {}", id);
        Optional<SetMappings> setMappings = setMappingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(setMappings);
    }

    /**
     * {@code DELETE  /set-mappings/:id} : delete the "id" setMappings.
     *
     * @param id the id of the setMappings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetMappings(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SetMappings : {}", id);
        setMappingsRepository.deleteById(id);
        setMappingsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /set-mappings/_search?query=:query} : search for the setMappings corresponding
     * to the query.
     *
     * @param query the query of the setMappings search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SetMappings> searchSetMappings(@RequestParam("query") String query) {
        LOG.debug("REST request to search SetMappings for query {}", query);
        try {
            return StreamSupport.stream(setMappingsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
