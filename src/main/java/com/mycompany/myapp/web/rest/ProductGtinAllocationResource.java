package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductGtinAllocation;
import com.mycompany.myapp.repository.ProductGtinAllocationRepository;
import com.mycompany.myapp.repository.search.ProductGtinAllocationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductGtinAllocation}.
 */
@RestController
@RequestMapping("/api/product-gtin-allocations")
@Transactional
public class ProductGtinAllocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductGtinAllocationResource.class);

    private static final String ENTITY_NAME = "productGtinAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductGtinAllocationRepository productGtinAllocationRepository;

    private final ProductGtinAllocationSearchRepository productGtinAllocationSearchRepository;

    public ProductGtinAllocationResource(
        ProductGtinAllocationRepository productGtinAllocationRepository,
        ProductGtinAllocationSearchRepository productGtinAllocationSearchRepository
    ) {
        this.productGtinAllocationRepository = productGtinAllocationRepository;
        this.productGtinAllocationSearchRepository = productGtinAllocationSearchRepository;
    }

    /**
     * {@code POST  /product-gtin-allocations} : Create a new productGtinAllocation.
     *
     * @param productGtinAllocation the productGtinAllocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productGtinAllocation, or with status {@code 400 (Bad Request)} if the productGtinAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductGtinAllocation> createProductGtinAllocation(@RequestBody ProductGtinAllocation productGtinAllocation)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductGtinAllocation : {}", productGtinAllocation);
        if (productGtinAllocation.getId() != null) {
            throw new BadRequestAlertException("A new productGtinAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productGtinAllocation = productGtinAllocationRepository.save(productGtinAllocation);
        productGtinAllocationSearchRepository.index(productGtinAllocation);
        return ResponseEntity.created(new URI("/api/product-gtin-allocations/" + productGtinAllocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productGtinAllocation.getId().toString()))
            .body(productGtinAllocation);
    }

    /**
     * {@code PUT  /product-gtin-allocations/:id} : Updates an existing productGtinAllocation.
     *
     * @param id the id of the productGtinAllocation to save.
     * @param productGtinAllocation the productGtinAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productGtinAllocation,
     * or with status {@code 400 (Bad Request)} if the productGtinAllocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productGtinAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductGtinAllocation> updateProductGtinAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductGtinAllocation productGtinAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductGtinAllocation : {}, {}", id, productGtinAllocation);
        if (productGtinAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productGtinAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productGtinAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productGtinAllocation = productGtinAllocationRepository.save(productGtinAllocation);
        productGtinAllocationSearchRepository.index(productGtinAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productGtinAllocation.getId().toString()))
            .body(productGtinAllocation);
    }

    /**
     * {@code PATCH  /product-gtin-allocations/:id} : Partial updates given fields of an existing productGtinAllocation, field will ignore if it is null
     *
     * @param id the id of the productGtinAllocation to save.
     * @param productGtinAllocation the productGtinAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productGtinAllocation,
     * or with status {@code 400 (Bad Request)} if the productGtinAllocation is not valid,
     * or with status {@code 404 (Not Found)} if the productGtinAllocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the productGtinAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductGtinAllocation> partialUpdateProductGtinAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductGtinAllocation productGtinAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductGtinAllocation partially : {}, {}", id, productGtinAllocation);
        if (productGtinAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productGtinAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productGtinAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductGtinAllocation> result = productGtinAllocationRepository
            .findById(productGtinAllocation.getId())
            .map(existingProductGtinAllocation -> {
                if (productGtinAllocation.getCreatedBy() != null) {
                    existingProductGtinAllocation.setCreatedBy(productGtinAllocation.getCreatedBy());
                }
                if (productGtinAllocation.getCreatedOn() != null) {
                    existingProductGtinAllocation.setCreatedOn(productGtinAllocation.getCreatedOn());
                }
                if (productGtinAllocation.getgTIN() != null) {
                    existingProductGtinAllocation.setgTIN(productGtinAllocation.getgTIN());
                }
                if (productGtinAllocation.getIsActive() != null) {
                    existingProductGtinAllocation.setIsActive(productGtinAllocation.getIsActive());
                }
                if (productGtinAllocation.getProductGtinId() != null) {
                    existingProductGtinAllocation.setProductGtinId(productGtinAllocation.getProductGtinId());
                }
                if (productGtinAllocation.getProductId() != null) {
                    existingProductGtinAllocation.setProductId(productGtinAllocation.getProductId());
                }
                if (productGtinAllocation.getUpdatedBy() != null) {
                    existingProductGtinAllocation.setUpdatedBy(productGtinAllocation.getUpdatedBy());
                }
                if (productGtinAllocation.getUpdatedOn() != null) {
                    existingProductGtinAllocation.setUpdatedOn(productGtinAllocation.getUpdatedOn());
                }

                return existingProductGtinAllocation;
            })
            .map(productGtinAllocationRepository::save)
            .map(savedProductGtinAllocation -> {
                productGtinAllocationSearchRepository.index(savedProductGtinAllocation);
                return savedProductGtinAllocation;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productGtinAllocation.getId().toString())
        );
    }

    /**
     * {@code GET  /product-gtin-allocations} : get all the productGtinAllocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productGtinAllocations in body.
     */
    @GetMapping("")
    public List<ProductGtinAllocation> getAllProductGtinAllocations() {
        LOG.debug("REST request to get all ProductGtinAllocations");
        return productGtinAllocationRepository.findAll();
    }

    /**
     * {@code GET  /product-gtin-allocations/:id} : get the "id" productGtinAllocation.
     *
     * @param id the id of the productGtinAllocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productGtinAllocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductGtinAllocation> getProductGtinAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductGtinAllocation : {}", id);
        Optional<ProductGtinAllocation> productGtinAllocation = productGtinAllocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productGtinAllocation);
    }

    /**
     * {@code DELETE  /product-gtin-allocations/:id} : delete the "id" productGtinAllocation.
     *
     * @param id the id of the productGtinAllocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductGtinAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductGtinAllocation : {}", id);
        productGtinAllocationRepository.deleteById(id);
        productGtinAllocationSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-gtin-allocations/_search?query=:query} : search for the productGtinAllocation corresponding
     * to the query.
     *
     * @param query the query of the productGtinAllocation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductGtinAllocation> searchProductGtinAllocations(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductGtinAllocations for query {}", query);
        try {
            return StreamSupport.stream(productGtinAllocationSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
