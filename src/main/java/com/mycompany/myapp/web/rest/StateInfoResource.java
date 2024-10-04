package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StateInfo;
import com.mycompany.myapp.repository.StateInfoRepository;
import com.mycompany.myapp.repository.search.StateInfoSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.StateInfo}.
 */
@RestController
@RequestMapping("/api/state-infos")
@Transactional
public class StateInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(StateInfoResource.class);

    private static final String ENTITY_NAME = "stateInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateInfoRepository stateInfoRepository;

    private final StateInfoSearchRepository stateInfoSearchRepository;

    public StateInfoResource(StateInfoRepository stateInfoRepository, StateInfoSearchRepository stateInfoSearchRepository) {
        this.stateInfoRepository = stateInfoRepository;
        this.stateInfoSearchRepository = stateInfoSearchRepository;
    }

    /**
     * {@code POST  /state-infos} : Create a new stateInfo.
     *
     * @param stateInfo the stateInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateInfo, or with status {@code 400 (Bad Request)} if the stateInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StateInfo> createStateInfo(@RequestBody StateInfo stateInfo) throws URISyntaxException {
        LOG.debug("REST request to save StateInfo : {}", stateInfo);
        if (stateInfo.getId() != null) {
            throw new BadRequestAlertException("A new stateInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stateInfo = stateInfoRepository.save(stateInfo);
        stateInfoSearchRepository.index(stateInfo);
        return ResponseEntity.created(new URI("/api/state-infos/" + stateInfo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stateInfo.getId().toString()))
            .body(stateInfo);
    }

    /**
     * {@code PUT  /state-infos/:id} : Updates an existing stateInfo.
     *
     * @param id the id of the stateInfo to save.
     * @param stateInfo the stateInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateInfo,
     * or with status {@code 400 (Bad Request)} if the stateInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StateInfo> updateStateInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateInfo stateInfo
    ) throws URISyntaxException {
        LOG.debug("REST request to update StateInfo : {}, {}", id, stateInfo);
        if (stateInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stateInfo = stateInfoRepository.save(stateInfo);
        stateInfoSearchRepository.index(stateInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateInfo.getId().toString()))
            .body(stateInfo);
    }

    /**
     * {@code PATCH  /state-infos/:id} : Partial updates given fields of an existing stateInfo, field will ignore if it is null
     *
     * @param id the id of the stateInfo to save.
     * @param stateInfo the stateInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateInfo,
     * or with status {@code 400 (Bad Request)} if the stateInfo is not valid,
     * or with status {@code 404 (Not Found)} if the stateInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the stateInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StateInfo> partialUpdateStateInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateInfo stateInfo
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StateInfo partially : {}, {}", id, stateInfo);
        if (stateInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StateInfo> result = stateInfoRepository
            .findById(stateInfo.getId())
            .map(existingStateInfo -> {
                if (stateInfo.getCreatedOn() != null) {
                    existingStateInfo.setCreatedOn(stateInfo.getCreatedOn());
                }
                if (stateInfo.getIsActive() != null) {
                    existingStateInfo.setIsActive(stateInfo.getIsActive());
                }
                if (stateInfo.getStateId() != null) {
                    existingStateInfo.setStateId(stateInfo.getStateId());
                }
                if (stateInfo.getStateName() != null) {
                    existingStateInfo.setStateName(stateInfo.getStateName());
                }

                return existingStateInfo;
            })
            .map(stateInfoRepository::save)
            .map(savedStateInfo -> {
                stateInfoSearchRepository.index(savedStateInfo);
                return savedStateInfo;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /state-infos} : get all the stateInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateInfos in body.
     */
    @GetMapping("")
    public List<StateInfo> getAllStateInfos() {
        LOG.debug("REST request to get all StateInfos");
        return stateInfoRepository.findAll();
    }

    /**
     * {@code GET  /state-infos/:id} : get the "id" stateInfo.
     *
     * @param id the id of the stateInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StateInfo> getStateInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StateInfo : {}", id);
        Optional<StateInfo> stateInfo = stateInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stateInfo);
    }

    /**
     * {@code DELETE  /state-infos/:id} : delete the "id" stateInfo.
     *
     * @param id the id of the stateInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStateInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StateInfo : {}", id);
        stateInfoRepository.deleteById(id);
        stateInfoSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /state-infos/_search?query=:query} : search for the stateInfo corresponding
     * to the query.
     *
     * @param query the query of the stateInfo search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<StateInfo> searchStateInfos(@RequestParam("query") String query) {
        LOG.debug("REST request to search StateInfos for query {}", query);
        try {
            return StreamSupport.stream(stateInfoSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
