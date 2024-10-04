package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AllergenMst;
import com.mycompany.myapp.repository.AllergenMstRepository;
import com.mycompany.myapp.repository.search.AllergenMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AllergenMst}.
 */
@RestController
@RequestMapping("/api/allergen-msts")
@Transactional
public class AllergenMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllergenMstResource.class);

    private static final String ENTITY_NAME = "allergenMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllergenMstRepository allergenMstRepository;

    private final AllergenMstSearchRepository allergenMstSearchRepository;

    public AllergenMstResource(AllergenMstRepository allergenMstRepository, AllergenMstSearchRepository allergenMstSearchRepository) {
        this.allergenMstRepository = allergenMstRepository;
        this.allergenMstSearchRepository = allergenMstSearchRepository;
    }

    /**
     * {@code POST  /allergen-msts} : Create a new allergenMst.
     *
     * @param allergenMst the allergenMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allergenMst, or with status {@code 400 (Bad Request)} if the allergenMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllergenMst> createAllergenMst(@RequestBody AllergenMst allergenMst) throws URISyntaxException {
        LOG.debug("REST request to save AllergenMst : {}", allergenMst);
        if (allergenMst.getId() != null) {
            throw new BadRequestAlertException("A new allergenMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allergenMst = allergenMstRepository.save(allergenMst);
        allergenMstSearchRepository.index(allergenMst);
        return ResponseEntity.created(new URI("/api/allergen-msts/" + allergenMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allergenMst.getId().toString()))
            .body(allergenMst);
    }

    /**
     * {@code PUT  /allergen-msts/:id} : Updates an existing allergenMst.
     *
     * @param id the id of the allergenMst to save.
     * @param allergenMst the allergenMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergenMst,
     * or with status {@code 400 (Bad Request)} if the allergenMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allergenMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllergenMst> updateAllergenMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AllergenMst allergenMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update AllergenMst : {}, {}", id, allergenMst);
        if (allergenMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergenMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergenMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allergenMst = allergenMstRepository.save(allergenMst);
        allergenMstSearchRepository.index(allergenMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergenMst.getId().toString()))
            .body(allergenMst);
    }

    /**
     * {@code PATCH  /allergen-msts/:id} : Partial updates given fields of an existing allergenMst, field will ignore if it is null
     *
     * @param id the id of the allergenMst to save.
     * @param allergenMst the allergenMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergenMst,
     * or with status {@code 400 (Bad Request)} if the allergenMst is not valid,
     * or with status {@code 404 (Not Found)} if the allergenMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the allergenMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllergenMst> partialUpdateAllergenMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AllergenMst allergenMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AllergenMst partially : {}, {}", id, allergenMst);
        if (allergenMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergenMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergenMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllergenMst> result = allergenMstRepository
            .findById(allergenMst.getId())
            .map(existingAllergenMst -> {
                if (allergenMst.getAllergenGroup() != null) {
                    existingAllergenMst.setAllergenGroup(allergenMst.getAllergenGroup());
                }
                if (allergenMst.getAllergenId() != null) {
                    existingAllergenMst.setAllergenId(allergenMst.getAllergenId());
                }
                if (allergenMst.getAllergenName() != null) {
                    existingAllergenMst.setAllergenName(allergenMst.getAllergenName());
                }

                return existingAllergenMst;
            })
            .map(allergenMstRepository::save)
            .map(savedAllergenMst -> {
                allergenMstSearchRepository.index(savedAllergenMst);
                return savedAllergenMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergenMst.getId().toString())
        );
    }

    /**
     * {@code GET  /allergen-msts} : get all the allergenMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allergenMsts in body.
     */
    @GetMapping("")
    public List<AllergenMst> getAllAllergenMsts() {
        LOG.debug("REST request to get all AllergenMsts");
        return allergenMstRepository.findAll();
    }

    /**
     * {@code GET  /allergen-msts/:id} : get the "id" allergenMst.
     *
     * @param id the id of the allergenMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allergenMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllergenMst> getAllergenMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AllergenMst : {}", id);
        Optional<AllergenMst> allergenMst = allergenMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(allergenMst);
    }

    /**
     * {@code DELETE  /allergen-msts/:id} : delete the "id" allergenMst.
     *
     * @param id the id of the allergenMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergenMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AllergenMst : {}", id);
        allergenMstRepository.deleteById(id);
        allergenMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /allergen-msts/_search?query=:query} : search for the allergenMst corresponding
     * to the query.
     *
     * @param query the query of the allergenMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<AllergenMst> searchAllergenMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search AllergenMsts for query {}", query);
        try {
            return StreamSupport.stream(allergenMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
