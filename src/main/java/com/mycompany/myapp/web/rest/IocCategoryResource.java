package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.IocCategory;
import com.mycompany.myapp.repository.IocCategoryRepository;
import com.mycompany.myapp.repository.search.IocCategorySearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.IocCategory}.
 */
@RestController
@RequestMapping("/api/ioc-categories")
@Transactional
public class IocCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(IocCategoryResource.class);

    private static final String ENTITY_NAME = "iocCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IocCategoryRepository iocCategoryRepository;

    private final IocCategorySearchRepository iocCategorySearchRepository;

    public IocCategoryResource(IocCategoryRepository iocCategoryRepository, IocCategorySearchRepository iocCategorySearchRepository) {
        this.iocCategoryRepository = iocCategoryRepository;
        this.iocCategorySearchRepository = iocCategorySearchRepository;
    }

    /**
     * {@code POST  /ioc-categories} : Create a new iocCategory.
     *
     * @param iocCategory the iocCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iocCategory, or with status {@code 400 (Bad Request)} if the iocCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IocCategory> createIocCategory(@RequestBody IocCategory iocCategory) throws URISyntaxException {
        LOG.debug("REST request to save IocCategory : {}", iocCategory);
        if (iocCategory.getId() != null) {
            throw new BadRequestAlertException("A new iocCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        iocCategory = iocCategoryRepository.save(iocCategory);
        iocCategorySearchRepository.index(iocCategory);
        return ResponseEntity.created(new URI("/api/ioc-categories/" + iocCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, iocCategory.getId().toString()))
            .body(iocCategory);
    }

    /**
     * {@code PUT  /ioc-categories/:id} : Updates an existing iocCategory.
     *
     * @param id the id of the iocCategory to save.
     * @param iocCategory the iocCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iocCategory,
     * or with status {@code 400 (Bad Request)} if the iocCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iocCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IocCategory> updateIocCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IocCategory iocCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update IocCategory : {}, {}", id, iocCategory);
        if (iocCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iocCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iocCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        iocCategory = iocCategoryRepository.save(iocCategory);
        iocCategorySearchRepository.index(iocCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iocCategory.getId().toString()))
            .body(iocCategory);
    }

    /**
     * {@code PATCH  /ioc-categories/:id} : Partial updates given fields of an existing iocCategory, field will ignore if it is null
     *
     * @param id the id of the iocCategory to save.
     * @param iocCategory the iocCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iocCategory,
     * or with status {@code 400 (Bad Request)} if the iocCategory is not valid,
     * or with status {@code 404 (Not Found)} if the iocCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the iocCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IocCategory> partialUpdateIocCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IocCategory iocCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IocCategory partially : {}, {}", id, iocCategory);
        if (iocCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iocCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iocCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IocCategory> result = iocCategoryRepository
            .findById(iocCategory.getId())
            .map(existingIocCategory -> {
                if (iocCategory.getIocCategoryColor() != null) {
                    existingIocCategory.setIocCategoryColor(iocCategory.getIocCategoryColor());
                }
                if (iocCategory.getIocCategoryId() != null) {
                    existingIocCategory.setIocCategoryId(iocCategory.getIocCategoryId());
                }
                if (iocCategory.getIocCategoryName() != null) {
                    existingIocCategory.setIocCategoryName(iocCategory.getIocCategoryName());
                }

                return existingIocCategory;
            })
            .map(iocCategoryRepository::save)
            .map(savedIocCategory -> {
                iocCategorySearchRepository.index(savedIocCategory);
                return savedIocCategory;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iocCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /ioc-categories} : get all the iocCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iocCategories in body.
     */
    @GetMapping("")
    public List<IocCategory> getAllIocCategories() {
        LOG.debug("REST request to get all IocCategories");
        return iocCategoryRepository.findAll();
    }

    /**
     * {@code GET  /ioc-categories/:id} : get the "id" iocCategory.
     *
     * @param id the id of the iocCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iocCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IocCategory> getIocCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IocCategory : {}", id);
        Optional<IocCategory> iocCategory = iocCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iocCategory);
    }

    /**
     * {@code DELETE  /ioc-categories/:id} : delete the "id" iocCategory.
     *
     * @param id the id of the iocCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIocCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IocCategory : {}", id);
        iocCategoryRepository.deleteById(id);
        iocCategorySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ioc-categories/_search?query=:query} : search for the iocCategory corresponding
     * to the query.
     *
     * @param query the query of the iocCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<IocCategory> searchIocCategories(@RequestParam("query") String query) {
        LOG.debug("REST request to search IocCategories for query {}", query);
        try {
            return StreamSupport.stream(iocCategorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
