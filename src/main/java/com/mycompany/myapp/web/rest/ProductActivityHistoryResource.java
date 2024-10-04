package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductActivityHistory;
import com.mycompany.myapp.repository.ProductActivityHistoryRepository;
import com.mycompany.myapp.repository.search.ProductActivityHistorySearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductActivityHistory}.
 */
@RestController
@RequestMapping("/api/product-activity-histories")
@Transactional
public class ProductActivityHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductActivityHistoryResource.class);

    private static final String ENTITY_NAME = "productActivityHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductActivityHistoryRepository productActivityHistoryRepository;

    private final ProductActivityHistorySearchRepository productActivityHistorySearchRepository;

    public ProductActivityHistoryResource(
        ProductActivityHistoryRepository productActivityHistoryRepository,
        ProductActivityHistorySearchRepository productActivityHistorySearchRepository
    ) {
        this.productActivityHistoryRepository = productActivityHistoryRepository;
        this.productActivityHistorySearchRepository = productActivityHistorySearchRepository;
    }

    /**
     * {@code POST  /product-activity-histories} : Create a new productActivityHistory.
     *
     * @param productActivityHistory the productActivityHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productActivityHistory, or with status {@code 400 (Bad Request)} if the productActivityHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductActivityHistory> createProductActivityHistory(@RequestBody ProductActivityHistory productActivityHistory)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductActivityHistory : {}", productActivityHistory);
        if (productActivityHistory.getId() != null) {
            throw new BadRequestAlertException("A new productActivityHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productActivityHistory = productActivityHistoryRepository.save(productActivityHistory);
        productActivityHistorySearchRepository.index(productActivityHistory);
        return ResponseEntity.created(new URI("/api/product-activity-histories/" + productActivityHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productActivityHistory.getId().toString()))
            .body(productActivityHistory);
    }

    /**
     * {@code PUT  /product-activity-histories/:id} : Updates an existing productActivityHistory.
     *
     * @param id the id of the productActivityHistory to save.
     * @param productActivityHistory the productActivityHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productActivityHistory,
     * or with status {@code 400 (Bad Request)} if the productActivityHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productActivityHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductActivityHistory> updateProductActivityHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductActivityHistory productActivityHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductActivityHistory : {}, {}", id, productActivityHistory);
        if (productActivityHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productActivityHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productActivityHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productActivityHistory = productActivityHistoryRepository.save(productActivityHistory);
        productActivityHistorySearchRepository.index(productActivityHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productActivityHistory.getId().toString()))
            .body(productActivityHistory);
    }

    /**
     * {@code PATCH  /product-activity-histories/:id} : Partial updates given fields of an existing productActivityHistory, field will ignore if it is null
     *
     * @param id the id of the productActivityHistory to save.
     * @param productActivityHistory the productActivityHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productActivityHistory,
     * or with status {@code 400 (Bad Request)} if the productActivityHistory is not valid,
     * or with status {@code 404 (Not Found)} if the productActivityHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productActivityHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductActivityHistory> partialUpdateProductActivityHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductActivityHistory productActivityHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductActivityHistory partially : {}, {}", id, productActivityHistory);
        if (productActivityHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productActivityHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productActivityHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductActivityHistory> result = productActivityHistoryRepository
            .findById(productActivityHistory.getId())
            .map(existingProductActivityHistory -> {
                if (productActivityHistory.getActivityId() != null) {
                    existingProductActivityHistory.setActivityId(productActivityHistory.getActivityId());
                }
                if (productActivityHistory.getActivityType() != null) {
                    existingProductActivityHistory.setActivityType(productActivityHistory.getActivityType());
                }
                if (productActivityHistory.getCreatedBy() != null) {
                    existingProductActivityHistory.setCreatedBy(productActivityHistory.getCreatedBy());
                }
                if (productActivityHistory.getCreatedOn() != null) {
                    existingProductActivityHistory.setCreatedOn(productActivityHistory.getCreatedOn());
                }
                if (productActivityHistory.getDate() != null) {
                    existingProductActivityHistory.setDate(productActivityHistory.getDate());
                }
                if (productActivityHistory.getIsActive() != null) {
                    existingProductActivityHistory.setIsActive(productActivityHistory.getIsActive());
                }
                if (productActivityHistory.getProductId() != null) {
                    existingProductActivityHistory.setProductId(productActivityHistory.getProductId());
                }
                if (productActivityHistory.getSuggestedProductId() != null) {
                    existingProductActivityHistory.setSuggestedProductId(productActivityHistory.getSuggestedProductId());
                }
                if (productActivityHistory.getUpdatedBy() != null) {
                    existingProductActivityHistory.setUpdatedBy(productActivityHistory.getUpdatedBy());
                }
                if (productActivityHistory.getUpdatedOn() != null) {
                    existingProductActivityHistory.setUpdatedOn(productActivityHistory.getUpdatedOn());
                }

                return existingProductActivityHistory;
            })
            .map(productActivityHistoryRepository::save)
            .map(savedProductActivityHistory -> {
                productActivityHistorySearchRepository.index(savedProductActivityHistory);
                return savedProductActivityHistory;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productActivityHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /product-activity-histories} : get all the productActivityHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productActivityHistories in body.
     */
    @GetMapping("")
    public List<ProductActivityHistory> getAllProductActivityHistories() {
        LOG.debug("REST request to get all ProductActivityHistories");
        return productActivityHistoryRepository.findAll();
    }

    /**
     * {@code GET  /product-activity-histories/:id} : get the "id" productActivityHistory.
     *
     * @param id the id of the productActivityHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productActivityHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductActivityHistory> getProductActivityHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductActivityHistory : {}", id);
        Optional<ProductActivityHistory> productActivityHistory = productActivityHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productActivityHistory);
    }

    /**
     * {@code DELETE  /product-activity-histories/:id} : delete the "id" productActivityHistory.
     *
     * @param id the id of the productActivityHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductActivityHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductActivityHistory : {}", id);
        productActivityHistoryRepository.deleteById(id);
        productActivityHistorySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-activity-histories/_search?query=:query} : search for the productActivityHistory corresponding
     * to the query.
     *
     * @param query the query of the productActivityHistory search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductActivityHistory> searchProductActivityHistories(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductActivityHistories for query {}", query);
        try {
            return StreamSupport.stream(productActivityHistorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
