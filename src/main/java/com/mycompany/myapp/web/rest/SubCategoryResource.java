package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SubCategory;
import com.mycompany.myapp.repository.SubCategoryRepository;
import com.mycompany.myapp.repository.search.SubCategorySearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SubCategory}.
 */
@RestController
@RequestMapping("/api/sub-categories")
@Transactional
public class SubCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubCategoryResource.class);

    private static final String ENTITY_NAME = "subCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategorySearchRepository subCategorySearchRepository;

    public SubCategoryResource(SubCategoryRepository subCategoryRepository, SubCategorySearchRepository subCategorySearchRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategorySearchRepository = subCategorySearchRepository;
    }

    /**
     * {@code POST  /sub-categories} : Create a new subCategory.
     *
     * @param subCategory the subCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCategory, or with status {@code 400 (Bad Request)} if the subCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubCategory> createSubCategory(@RequestBody SubCategory subCategory) throws URISyntaxException {
        LOG.debug("REST request to save SubCategory : {}", subCategory);
        if (subCategory.getId() != null) {
            throw new BadRequestAlertException("A new subCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subCategory = subCategoryRepository.save(subCategory);
        subCategorySearchRepository.index(subCategory);
        return ResponseEntity.created(new URI("/api/sub-categories/" + subCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subCategory.getId().toString()))
            .body(subCategory);
    }

    /**
     * {@code PUT  /sub-categories/:id} : Updates an existing subCategory.
     *
     * @param id the id of the subCategory to save.
     * @param subCategory the subCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategory,
     * or with status {@code 400 (Bad Request)} if the subCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubCategory subCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubCategory : {}, {}", id, subCategory);
        if (subCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subCategory = subCategoryRepository.save(subCategory);
        subCategorySearchRepository.index(subCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCategory.getId().toString()))
            .body(subCategory);
    }

    /**
     * {@code PATCH  /sub-categories/:id} : Partial updates given fields of an existing subCategory, field will ignore if it is null
     *
     * @param id the id of the subCategory to save.
     * @param subCategory the subCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategory,
     * or with status {@code 400 (Bad Request)} if the subCategory is not valid,
     * or with status {@code 404 (Not Found)} if the subCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the subCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubCategory> partialUpdateSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubCategory subCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubCategory partially : {}, {}", id, subCategory);
        if (subCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubCategory> result = subCategoryRepository
            .findById(subCategory.getId())
            .map(existingSubCategory -> {
                if (subCategory.getCategoryId() != null) {
                    existingSubCategory.setCategoryId(subCategory.getCategoryId());
                }
                if (subCategory.getCreatedBy() != null) {
                    existingSubCategory.setCreatedBy(subCategory.getCreatedBy());
                }
                if (subCategory.getCreatedOn() != null) {
                    existingSubCategory.setCreatedOn(subCategory.getCreatedOn());
                }
                if (subCategory.getIsActive() != null) {
                    existingSubCategory.setIsActive(subCategory.getIsActive());
                }
                if (subCategory.getSubCategoryCode() != null) {
                    existingSubCategory.setSubCategoryCode(subCategory.getSubCategoryCode());
                }
                if (subCategory.getSubCategoryId() != null) {
                    existingSubCategory.setSubCategoryId(subCategory.getSubCategoryId());
                }
                if (subCategory.getSubCategoryName() != null) {
                    existingSubCategory.setSubCategoryName(subCategory.getSubCategoryName());
                }
                if (subCategory.getUpdatedBy() != null) {
                    existingSubCategory.setUpdatedBy(subCategory.getUpdatedBy());
                }
                if (subCategory.getUpdatedOn() != null) {
                    existingSubCategory.setUpdatedOn(subCategory.getUpdatedOn());
                }

                return existingSubCategory;
            })
            .map(subCategoryRepository::save)
            .map(savedSubCategory -> {
                subCategorySearchRepository.index(savedSubCategory);
                return savedSubCategory;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategories in body.
     */
    @GetMapping("")
    public List<SubCategory> getAllSubCategories() {
        LOG.debug("REST request to get all SubCategories");
        return subCategoryRepository.findAll();
    }

    /**
     * {@code GET  /sub-categories/:id} : get the "id" subCategory.
     *
     * @param id the id of the subCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubCategory : {}", id);
        Optional<SubCategory> subCategory = subCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subCategory);
    }

    /**
     * {@code DELETE  /sub-categories/:id} : delete the "id" subCategory.
     *
     * @param id the id of the subCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubCategory : {}", id);
        subCategoryRepository.deleteById(id);
        subCategorySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /sub-categories/_search?query=:query} : search for the subCategory corresponding
     * to the query.
     *
     * @param query the query of the subCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SubCategory> searchSubCategories(@RequestParam("query") String query) {
        LOG.debug("REST request to search SubCategories for query {}", query);
        try {
            return StreamSupport.stream(subCategorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
