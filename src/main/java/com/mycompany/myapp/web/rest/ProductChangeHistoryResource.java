package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductChangeHistory;
import com.mycompany.myapp.repository.ProductChangeHistoryRepository;
import com.mycompany.myapp.repository.search.ProductChangeHistorySearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductChangeHistory}.
 */
@RestController
@RequestMapping("/api/product-change-histories")
@Transactional
public class ProductChangeHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductChangeHistoryResource.class);

    private static final String ENTITY_NAME = "productChangeHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductChangeHistoryRepository productChangeHistoryRepository;

    private final ProductChangeHistorySearchRepository productChangeHistorySearchRepository;

    public ProductChangeHistoryResource(
        ProductChangeHistoryRepository productChangeHistoryRepository,
        ProductChangeHistorySearchRepository productChangeHistorySearchRepository
    ) {
        this.productChangeHistoryRepository = productChangeHistoryRepository;
        this.productChangeHistorySearchRepository = productChangeHistorySearchRepository;
    }

    /**
     * {@code POST  /product-change-histories} : Create a new productChangeHistory.
     *
     * @param productChangeHistory the productChangeHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productChangeHistory, or with status {@code 400 (Bad Request)} if the productChangeHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductChangeHistory> createProductChangeHistory(@RequestBody ProductChangeHistory productChangeHistory)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductChangeHistory : {}", productChangeHistory);
        if (productChangeHistory.getId() != null) {
            throw new BadRequestAlertException("A new productChangeHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productChangeHistory = productChangeHistoryRepository.save(productChangeHistory);
        productChangeHistorySearchRepository.index(productChangeHistory);
        return ResponseEntity.created(new URI("/api/product-change-histories/" + productChangeHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productChangeHistory.getId().toString()))
            .body(productChangeHistory);
    }

    /**
     * {@code PUT  /product-change-histories/:id} : Updates an existing productChangeHistory.
     *
     * @param id the id of the productChangeHistory to save.
     * @param productChangeHistory the productChangeHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productChangeHistory,
     * or with status {@code 400 (Bad Request)} if the productChangeHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productChangeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductChangeHistory> updateProductChangeHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductChangeHistory productChangeHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductChangeHistory : {}, {}", id, productChangeHistory);
        if (productChangeHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productChangeHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productChangeHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productChangeHistory = productChangeHistoryRepository.save(productChangeHistory);
        productChangeHistorySearchRepository.index(productChangeHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productChangeHistory.getId().toString()))
            .body(productChangeHistory);
    }

    /**
     * {@code PATCH  /product-change-histories/:id} : Partial updates given fields of an existing productChangeHistory, field will ignore if it is null
     *
     * @param id the id of the productChangeHistory to save.
     * @param productChangeHistory the productChangeHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productChangeHistory,
     * or with status {@code 400 (Bad Request)} if the productChangeHistory is not valid,
     * or with status {@code 404 (Not Found)} if the productChangeHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productChangeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductChangeHistory> partialUpdateProductChangeHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductChangeHistory productChangeHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductChangeHistory partially : {}, {}", id, productChangeHistory);
        if (productChangeHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productChangeHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productChangeHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductChangeHistory> result = productChangeHistoryRepository
            .findById(productChangeHistory.getId())
            .map(existingProductChangeHistory -> {
                if (productChangeHistory.getCreatedBy() != null) {
                    existingProductChangeHistory.setCreatedBy(productChangeHistory.getCreatedBy());
                }
                if (productChangeHistory.getDateCreated() != null) {
                    existingProductChangeHistory.setDateCreated(productChangeHistory.getDateCreated());
                }
                if (productChangeHistory.getHistoryId() != null) {
                    existingProductChangeHistory.setHistoryId(productChangeHistory.getHistoryId());
                }
                if (productChangeHistory.getIocCategoryId() != null) {
                    existingProductChangeHistory.setIocCategoryId(productChangeHistory.getIocCategoryId());
                }
                if (productChangeHistory.getIsActive() != null) {
                    existingProductChangeHistory.setIsActive(productChangeHistory.getIsActive());
                }
                if (productChangeHistory.getProductId() != null) {
                    existingProductChangeHistory.setProductId(productChangeHistory.getProductId());
                }
                if (productChangeHistory.getSchoolDistrictId() != null) {
                    existingProductChangeHistory.setSchoolDistrictId(productChangeHistory.getSchoolDistrictId());
                }
                if (productChangeHistory.getSelectionType() != null) {
                    existingProductChangeHistory.setSelectionType(productChangeHistory.getSelectionType());
                }

                return existingProductChangeHistory;
            })
            .map(productChangeHistoryRepository::save)
            .map(savedProductChangeHistory -> {
                productChangeHistorySearchRepository.index(savedProductChangeHistory);
                return savedProductChangeHistory;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productChangeHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /product-change-histories} : get all the productChangeHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productChangeHistories in body.
     */
    @GetMapping("")
    public List<ProductChangeHistory> getAllProductChangeHistories() {
        LOG.debug("REST request to get all ProductChangeHistories");
        return productChangeHistoryRepository.findAll();
    }

    /**
     * {@code GET  /product-change-histories/:id} : get the "id" productChangeHistory.
     *
     * @param id the id of the productChangeHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productChangeHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductChangeHistory> getProductChangeHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductChangeHistory : {}", id);
        Optional<ProductChangeHistory> productChangeHistory = productChangeHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productChangeHistory);
    }

    /**
     * {@code DELETE  /product-change-histories/:id} : delete the "id" productChangeHistory.
     *
     * @param id the id of the productChangeHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductChangeHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductChangeHistory : {}", id);
        productChangeHistoryRepository.deleteById(id);
        productChangeHistorySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-change-histories/_search?query=:query} : search for the productChangeHistory corresponding
     * to the query.
     *
     * @param query the query of the productChangeHistory search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductChangeHistory> searchProductChangeHistories(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductChangeHistories for query {}", query);
        try {
            return StreamSupport.stream(productChangeHistorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
