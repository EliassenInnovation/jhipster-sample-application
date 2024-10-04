package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SupportCategory;
import com.mycompany.myapp.repository.SupportCategoryRepository;
import com.mycompany.myapp.repository.search.SupportCategorySearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SupportCategory}.
 */
@RestController
@RequestMapping("/api/support-categories")
@Transactional
public class SupportCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupportCategoryResource.class);

    private static final String ENTITY_NAME = "supportCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupportCategoryRepository supportCategoryRepository;

    private final SupportCategorySearchRepository supportCategorySearchRepository;

    public SupportCategoryResource(
        SupportCategoryRepository supportCategoryRepository,
        SupportCategorySearchRepository supportCategorySearchRepository
    ) {
        this.supportCategoryRepository = supportCategoryRepository;
        this.supportCategorySearchRepository = supportCategorySearchRepository;
    }

    /**
     * {@code POST  /support-categories} : Create a new supportCategory.
     *
     * @param supportCategory the supportCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supportCategory, or with status {@code 400 (Bad Request)} if the supportCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupportCategory> createSupportCategory(@RequestBody SupportCategory supportCategory) throws URISyntaxException {
        LOG.debug("REST request to save SupportCategory : {}", supportCategory);
        if (supportCategory.getId() != null) {
            throw new BadRequestAlertException("A new supportCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supportCategory = supportCategoryRepository.save(supportCategory);
        supportCategorySearchRepository.index(supportCategory);
        return ResponseEntity.created(new URI("/api/support-categories/" + supportCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supportCategory.getId().toString()))
            .body(supportCategory);
    }

    /**
     * {@code PUT  /support-categories/:id} : Updates an existing supportCategory.
     *
     * @param id the id of the supportCategory to save.
     * @param supportCategory the supportCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportCategory,
     * or with status {@code 400 (Bad Request)} if the supportCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supportCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupportCategory> updateSupportCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupportCategory supportCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupportCategory : {}, {}", id, supportCategory);
        if (supportCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supportCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supportCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supportCategory = supportCategoryRepository.save(supportCategory);
        supportCategorySearchRepository.index(supportCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportCategory.getId().toString()))
            .body(supportCategory);
    }

    /**
     * {@code PATCH  /support-categories/:id} : Partial updates given fields of an existing supportCategory, field will ignore if it is null
     *
     * @param id the id of the supportCategory to save.
     * @param supportCategory the supportCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportCategory,
     * or with status {@code 400 (Bad Request)} if the supportCategory is not valid,
     * or with status {@code 404 (Not Found)} if the supportCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the supportCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupportCategory> partialUpdateSupportCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupportCategory supportCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupportCategory partially : {}, {}", id, supportCategory);
        if (supportCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supportCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supportCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupportCategory> result = supportCategoryRepository
            .findById(supportCategory.getId())
            .map(existingSupportCategory -> {
                if (supportCategory.getSupportCategoryId() != null) {
                    existingSupportCategory.setSupportCategoryId(supportCategory.getSupportCategoryId());
                }
                if (supportCategory.getSupportCategoryName() != null) {
                    existingSupportCategory.setSupportCategoryName(supportCategory.getSupportCategoryName());
                }

                return existingSupportCategory;
            })
            .map(supportCategoryRepository::save)
            .map(savedSupportCategory -> {
                supportCategorySearchRepository.index(savedSupportCategory);
                return savedSupportCategory;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /support-categories} : get all the supportCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supportCategories in body.
     */
    @GetMapping("")
    public List<SupportCategory> getAllSupportCategories() {
        LOG.debug("REST request to get all SupportCategories");
        return supportCategoryRepository.findAll();
    }

    /**
     * {@code GET  /support-categories/:id} : get the "id" supportCategory.
     *
     * @param id the id of the supportCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supportCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupportCategory> getSupportCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupportCategory : {}", id);
        Optional<SupportCategory> supportCategory = supportCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(supportCategory);
    }

    /**
     * {@code DELETE  /support-categories/:id} : delete the "id" supportCategory.
     *
     * @param id the id of the supportCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupportCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupportCategory : {}", id);
        supportCategoryRepository.deleteById(id);
        supportCategorySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /support-categories/_search?query=:query} : search for the supportCategory corresponding
     * to the query.
     *
     * @param query the query of the supportCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SupportCategory> searchSupportCategories(@RequestParam("query") String query) {
        LOG.debug("REST request to search SupportCategories for query {}", query);
        try {
            return StreamSupport.stream(supportCategorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
