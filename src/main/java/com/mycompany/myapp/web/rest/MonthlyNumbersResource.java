package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MonthlyNumbers;
import com.mycompany.myapp.repository.MonthlyNumbersRepository;
import com.mycompany.myapp.repository.search.MonthlyNumbersSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MonthlyNumbers}.
 */
@RestController
@RequestMapping("/api/monthly-numbers")
@Transactional
public class MonthlyNumbersResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonthlyNumbersResource.class);

    private static final String ENTITY_NAME = "monthlyNumbers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthlyNumbersRepository monthlyNumbersRepository;

    private final MonthlyNumbersSearchRepository monthlyNumbersSearchRepository;

    public MonthlyNumbersResource(
        MonthlyNumbersRepository monthlyNumbersRepository,
        MonthlyNumbersSearchRepository monthlyNumbersSearchRepository
    ) {
        this.monthlyNumbersRepository = monthlyNumbersRepository;
        this.monthlyNumbersSearchRepository = monthlyNumbersSearchRepository;
    }

    /**
     * {@code POST  /monthly-numbers} : Create a new monthlyNumbers.
     *
     * @param monthlyNumbers the monthlyNumbers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monthlyNumbers, or with status {@code 400 (Bad Request)} if the monthlyNumbers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonthlyNumbers> createMonthlyNumbers(@RequestBody MonthlyNumbers monthlyNumbers) throws URISyntaxException {
        LOG.debug("REST request to save MonthlyNumbers : {}", monthlyNumbers);
        if (monthlyNumbers.getId() != null) {
            throw new BadRequestAlertException("A new monthlyNumbers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monthlyNumbers = monthlyNumbersRepository.save(monthlyNumbers);
        monthlyNumbersSearchRepository.index(monthlyNumbers);
        return ResponseEntity.created(new URI("/api/monthly-numbers/" + monthlyNumbers.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monthlyNumbers.getId().toString()))
            .body(monthlyNumbers);
    }

    /**
     * {@code PUT  /monthly-numbers/:id} : Updates an existing monthlyNumbers.
     *
     * @param id the id of the monthlyNumbers to save.
     * @param monthlyNumbers the monthlyNumbers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthlyNumbers,
     * or with status {@code 400 (Bad Request)} if the monthlyNumbers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monthlyNumbers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonthlyNumbers> updateMonthlyNumbers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthlyNumbers monthlyNumbers
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonthlyNumbers : {}, {}", id, monthlyNumbers);
        if (monthlyNumbers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthlyNumbers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthlyNumbersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monthlyNumbers = monthlyNumbersRepository.save(monthlyNumbers);
        monthlyNumbersSearchRepository.index(monthlyNumbers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monthlyNumbers.getId().toString()))
            .body(monthlyNumbers);
    }

    /**
     * {@code PATCH  /monthly-numbers/:id} : Partial updates given fields of an existing monthlyNumbers, field will ignore if it is null
     *
     * @param id the id of the monthlyNumbers to save.
     * @param monthlyNumbers the monthlyNumbers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthlyNumbers,
     * or with status {@code 400 (Bad Request)} if the monthlyNumbers is not valid,
     * or with status {@code 404 (Not Found)} if the monthlyNumbers is not found,
     * or with status {@code 500 (Internal Server Error)} if the monthlyNumbers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonthlyNumbers> partialUpdateMonthlyNumbers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthlyNumbers monthlyNumbers
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonthlyNumbers partially : {}, {}", id, monthlyNumbers);
        if (monthlyNumbers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthlyNumbers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthlyNumbersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonthlyNumbers> result = monthlyNumbersRepository
            .findById(monthlyNumbers.getId())
            .map(existingMonthlyNumbers -> {
                if (monthlyNumbers.getActualMonthId() != null) {
                    existingMonthlyNumbers.setActualMonthId(monthlyNumbers.getActualMonthId());
                }
                if (monthlyNumbers.getCreatedOn() != null) {
                    existingMonthlyNumbers.setCreatedOn(monthlyNumbers.getCreatedOn());
                }
                if (monthlyNumbers.getEnrollment() != null) {
                    existingMonthlyNumbers.setEnrollment(monthlyNumbers.getEnrollment());
                }
                if (monthlyNumbers.getFreeAndReducedPercent() != null) {
                    existingMonthlyNumbers.setFreeAndReducedPercent(monthlyNumbers.getFreeAndReducedPercent());
                }
                if (monthlyNumbers.getiD() != null) {
                    existingMonthlyNumbers.setiD(monthlyNumbers.getiD());
                }
                if (monthlyNumbers.getIsActive() != null) {
                    existingMonthlyNumbers.setIsActive(monthlyNumbers.getIsActive());
                }
                if (monthlyNumbers.getMealsServed() != null) {
                    existingMonthlyNumbers.setMealsServed(monthlyNumbers.getMealsServed());
                }
                if (monthlyNumbers.getModifiedOn() != null) {
                    existingMonthlyNumbers.setModifiedOn(monthlyNumbers.getModifiedOn());
                }
                if (monthlyNumbers.getMonthId() != null) {
                    existingMonthlyNumbers.setMonthId(monthlyNumbers.getMonthId());
                }
                if (monthlyNumbers.getNumberOfDistricts() != null) {
                    existingMonthlyNumbers.setNumberOfDistricts(monthlyNumbers.getNumberOfDistricts());
                }
                if (monthlyNumbers.getNumberOfSites() != null) {
                    existingMonthlyNumbers.setNumberOfSites(monthlyNumbers.getNumberOfSites());
                }
                if (monthlyNumbers.getRegDate() != null) {
                    existingMonthlyNumbers.setRegDate(monthlyNumbers.getRegDate());
                }
                if (monthlyNumbers.getSchoolDistrictId() != null) {
                    existingMonthlyNumbers.setSchoolDistrictId(monthlyNumbers.getSchoolDistrictId());
                }
                if (monthlyNumbers.getYear() != null) {
                    existingMonthlyNumbers.setYear(monthlyNumbers.getYear());
                }

                return existingMonthlyNumbers;
            })
            .map(monthlyNumbersRepository::save)
            .map(savedMonthlyNumbers -> {
                monthlyNumbersSearchRepository.index(savedMonthlyNumbers);
                return savedMonthlyNumbers;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monthlyNumbers.getId().toString())
        );
    }

    /**
     * {@code GET  /monthly-numbers} : get all the monthlyNumbers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monthlyNumbers in body.
     */
    @GetMapping("")
    public List<MonthlyNumbers> getAllMonthlyNumbers() {
        LOG.debug("REST request to get all MonthlyNumbers");
        return monthlyNumbersRepository.findAll();
    }

    /**
     * {@code GET  /monthly-numbers/:id} : get the "id" monthlyNumbers.
     *
     * @param id the id of the monthlyNumbers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthlyNumbers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonthlyNumbers> getMonthlyNumbers(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonthlyNumbers : {}", id);
        Optional<MonthlyNumbers> monthlyNumbers = monthlyNumbersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(monthlyNumbers);
    }

    /**
     * {@code DELETE  /monthly-numbers/:id} : delete the "id" monthlyNumbers.
     *
     * @param id the id of the monthlyNumbers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonthlyNumbers(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonthlyNumbers : {}", id);
        monthlyNumbersRepository.deleteById(id);
        monthlyNumbersSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /monthly-numbers/_search?query=:query} : search for the monthlyNumbers corresponding
     * to the query.
     *
     * @param query the query of the monthlyNumbers search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<MonthlyNumbers> searchMonthlyNumbers(@RequestParam("query") String query) {
        LOG.debug("REST request to search MonthlyNumbers for query {}", query);
        try {
            return StreamSupport.stream(monthlyNumbersSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
