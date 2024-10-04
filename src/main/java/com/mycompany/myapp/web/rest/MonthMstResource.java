package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MonthMst;
import com.mycompany.myapp.repository.MonthMstRepository;
import com.mycompany.myapp.repository.search.MonthMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MonthMst}.
 */
@RestController
@RequestMapping("/api/month-msts")
@Transactional
public class MonthMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonthMstResource.class);

    private static final String ENTITY_NAME = "monthMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthMstRepository monthMstRepository;

    private final MonthMstSearchRepository monthMstSearchRepository;

    public MonthMstResource(MonthMstRepository monthMstRepository, MonthMstSearchRepository monthMstSearchRepository) {
        this.monthMstRepository = monthMstRepository;
        this.monthMstSearchRepository = monthMstSearchRepository;
    }

    /**
     * {@code POST  /month-msts} : Create a new monthMst.
     *
     * @param monthMst the monthMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monthMst, or with status {@code 400 (Bad Request)} if the monthMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonthMst> createMonthMst(@RequestBody MonthMst monthMst) throws URISyntaxException {
        LOG.debug("REST request to save MonthMst : {}", monthMst);
        if (monthMst.getId() != null) {
            throw new BadRequestAlertException("A new monthMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monthMst = monthMstRepository.save(monthMst);
        monthMstSearchRepository.index(monthMst);
        return ResponseEntity.created(new URI("/api/month-msts/" + monthMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monthMst.getId().toString()))
            .body(monthMst);
    }

    /**
     * {@code PUT  /month-msts/:id} : Updates an existing monthMst.
     *
     * @param id the id of the monthMst to save.
     * @param monthMst the monthMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthMst,
     * or with status {@code 400 (Bad Request)} if the monthMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monthMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonthMst> updateMonthMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthMst monthMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonthMst : {}, {}", id, monthMst);
        if (monthMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monthMst = monthMstRepository.save(monthMst);
        monthMstSearchRepository.index(monthMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monthMst.getId().toString()))
            .body(monthMst);
    }

    /**
     * {@code PATCH  /month-msts/:id} : Partial updates given fields of an existing monthMst, field will ignore if it is null
     *
     * @param id the id of the monthMst to save.
     * @param monthMst the monthMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthMst,
     * or with status {@code 400 (Bad Request)} if the monthMst is not valid,
     * or with status {@code 404 (Not Found)} if the monthMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the monthMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonthMst> partialUpdateMonthMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthMst monthMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonthMst partially : {}, {}", id, monthMst);
        if (monthMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonthMst> result = monthMstRepository
            .findById(monthMst.getId())
            .map(existingMonthMst -> {
                if (monthMst.getIsActive() != null) {
                    existingMonthMst.setIsActive(monthMst.getIsActive());
                }
                if (monthMst.getMonthID() != null) {
                    existingMonthMst.setMonthID(monthMst.getMonthID());
                }
                if (monthMst.getMonthName() != null) {
                    existingMonthMst.setMonthName(monthMst.getMonthName());
                }
                if (monthMst.getYear() != null) {
                    existingMonthMst.setYear(monthMst.getYear());
                }

                return existingMonthMst;
            })
            .map(monthMstRepository::save)
            .map(savedMonthMst -> {
                monthMstSearchRepository.index(savedMonthMst);
                return savedMonthMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monthMst.getId().toString())
        );
    }

    /**
     * {@code GET  /month-msts} : get all the monthMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monthMsts in body.
     */
    @GetMapping("")
    public List<MonthMst> getAllMonthMsts() {
        LOG.debug("REST request to get all MonthMsts");
        return monthMstRepository.findAll();
    }

    /**
     * {@code GET  /month-msts/:id} : get the "id" monthMst.
     *
     * @param id the id of the monthMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonthMst> getMonthMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonthMst : {}", id);
        Optional<MonthMst> monthMst = monthMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(monthMst);
    }

    /**
     * {@code DELETE  /month-msts/:id} : delete the "id" monthMst.
     *
     * @param id the id of the monthMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonthMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonthMst : {}", id);
        monthMstRepository.deleteById(id);
        monthMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /month-msts/_search?query=:query} : search for the monthMst corresponding
     * to the query.
     *
     * @param query the query of the monthMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<MonthMst> searchMonthMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search MonthMsts for query {}", query);
        try {
            return StreamSupport.stream(monthMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
