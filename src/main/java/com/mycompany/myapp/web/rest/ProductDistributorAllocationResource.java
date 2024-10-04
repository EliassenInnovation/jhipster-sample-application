package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductDistributorAllocation;
import com.mycompany.myapp.repository.ProductDistributorAllocationRepository;
import com.mycompany.myapp.repository.search.ProductDistributorAllocationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductDistributorAllocation}.
 */
@RestController
@RequestMapping("/api/product-distributor-allocations")
@Transactional
public class ProductDistributorAllocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDistributorAllocationResource.class);

    private static final String ENTITY_NAME = "productDistributorAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductDistributorAllocationRepository productDistributorAllocationRepository;

    private final ProductDistributorAllocationSearchRepository productDistributorAllocationSearchRepository;

    public ProductDistributorAllocationResource(
        ProductDistributorAllocationRepository productDistributorAllocationRepository,
        ProductDistributorAllocationSearchRepository productDistributorAllocationSearchRepository
    ) {
        this.productDistributorAllocationRepository = productDistributorAllocationRepository;
        this.productDistributorAllocationSearchRepository = productDistributorAllocationSearchRepository;
    }

    /**
     * {@code POST  /product-distributor-allocations} : Create a new productDistributorAllocation.
     *
     * @param productDistributorAllocation the productDistributorAllocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDistributorAllocation, or with status {@code 400 (Bad Request)} if the productDistributorAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductDistributorAllocation> createProductDistributorAllocation(
        @RequestBody ProductDistributorAllocation productDistributorAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductDistributorAllocation : {}", productDistributorAllocation);
        if (productDistributorAllocation.getId() != null) {
            throw new BadRequestAlertException("A new productDistributorAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productDistributorAllocation = productDistributorAllocationRepository.save(productDistributorAllocation);
        productDistributorAllocationSearchRepository.index(productDistributorAllocation);
        return ResponseEntity.created(new URI("/api/product-distributor-allocations/" + productDistributorAllocation.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productDistributorAllocation.getId().toString())
            )
            .body(productDistributorAllocation);
    }

    /**
     * {@code PUT  /product-distributor-allocations/:id} : Updates an existing productDistributorAllocation.
     *
     * @param id the id of the productDistributorAllocation to save.
     * @param productDistributorAllocation the productDistributorAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDistributorAllocation,
     * or with status {@code 400 (Bad Request)} if the productDistributorAllocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDistributorAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDistributorAllocation> updateProductDistributorAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDistributorAllocation productDistributorAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductDistributorAllocation : {}, {}", id, productDistributorAllocation);
        if (productDistributorAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDistributorAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productDistributorAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productDistributorAllocation = productDistributorAllocationRepository.save(productDistributorAllocation);
        productDistributorAllocationSearchRepository.index(productDistributorAllocation);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDistributorAllocation.getId().toString())
            )
            .body(productDistributorAllocation);
    }

    /**
     * {@code PATCH  /product-distributor-allocations/:id} : Partial updates given fields of an existing productDistributorAllocation, field will ignore if it is null
     *
     * @param id the id of the productDistributorAllocation to save.
     * @param productDistributorAllocation the productDistributorAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDistributorAllocation,
     * or with status {@code 400 (Bad Request)} if the productDistributorAllocation is not valid,
     * or with status {@code 404 (Not Found)} if the productDistributorAllocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the productDistributorAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductDistributorAllocation> partialUpdateProductDistributorAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDistributorAllocation productDistributorAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductDistributorAllocation partially : {}, {}", id, productDistributorAllocation);
        if (productDistributorAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDistributorAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productDistributorAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductDistributorAllocation> result = productDistributorAllocationRepository
            .findById(productDistributorAllocation.getId())
            .map(existingProductDistributorAllocation -> {
                if (productDistributorAllocation.getCreatedBy() != null) {
                    existingProductDistributorAllocation.setCreatedBy(productDistributorAllocation.getCreatedBy());
                }
                if (productDistributorAllocation.getCreatedOn() != null) {
                    existingProductDistributorAllocation.setCreatedOn(productDistributorAllocation.getCreatedOn());
                }
                if (productDistributorAllocation.getDistributorId() != null) {
                    existingProductDistributorAllocation.setDistributorId(productDistributorAllocation.getDistributorId());
                }
                if (productDistributorAllocation.getIsAllocated() != null) {
                    existingProductDistributorAllocation.setIsAllocated(productDistributorAllocation.getIsAllocated());
                }
                if (productDistributorAllocation.getProductDistributorAllocationId() != null) {
                    existingProductDistributorAllocation.setProductDistributorAllocationId(
                        productDistributorAllocation.getProductDistributorAllocationId()
                    );
                }
                if (productDistributorAllocation.getProductId() != null) {
                    existingProductDistributorAllocation.setProductId(productDistributorAllocation.getProductId());
                }
                if (productDistributorAllocation.getUpdatedBy() != null) {
                    existingProductDistributorAllocation.setUpdatedBy(productDistributorAllocation.getUpdatedBy());
                }
                if (productDistributorAllocation.getUpdatedOn() != null) {
                    existingProductDistributorAllocation.setUpdatedOn(productDistributorAllocation.getUpdatedOn());
                }

                return existingProductDistributorAllocation;
            })
            .map(productDistributorAllocationRepository::save)
            .map(savedProductDistributorAllocation -> {
                productDistributorAllocationSearchRepository.index(savedProductDistributorAllocation);
                return savedProductDistributorAllocation;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDistributorAllocation.getId().toString())
        );
    }

    /**
     * {@code GET  /product-distributor-allocations} : get all the productDistributorAllocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productDistributorAllocations in body.
     */
    @GetMapping("")
    public List<ProductDistributorAllocation> getAllProductDistributorAllocations() {
        LOG.debug("REST request to get all ProductDistributorAllocations");
        return productDistributorAllocationRepository.findAll();
    }

    /**
     * {@code GET  /product-distributor-allocations/:id} : get the "id" productDistributorAllocation.
     *
     * @param id the id of the productDistributorAllocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDistributorAllocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDistributorAllocation> getProductDistributorAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductDistributorAllocation : {}", id);
        Optional<ProductDistributorAllocation> productDistributorAllocation = productDistributorAllocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productDistributorAllocation);
    }

    /**
     * {@code DELETE  /product-distributor-allocations/:id} : delete the "id" productDistributorAllocation.
     *
     * @param id the id of the productDistributorAllocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDistributorAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductDistributorAllocation : {}", id);
        productDistributorAllocationRepository.deleteById(id);
        productDistributorAllocationSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-distributor-allocations/_search?query=:query} : search for the productDistributorAllocation corresponding
     * to the query.
     *
     * @param query the query of the productDistributorAllocation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductDistributorAllocation> searchProductDistributorAllocations(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductDistributorAllocations for query {}", query);
        try {
            return StreamSupport.stream(productDistributorAllocationSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
