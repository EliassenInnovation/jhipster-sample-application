package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductDistrictAllocation;
import com.mycompany.myapp.repository.ProductDistrictAllocationRepository;
import com.mycompany.myapp.repository.search.ProductDistrictAllocationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductDistrictAllocation}.
 */
@RestController
@RequestMapping("/api/product-district-allocations")
@Transactional
public class ProductDistrictAllocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductDistrictAllocationResource.class);

    private static final String ENTITY_NAME = "productDistrictAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductDistrictAllocationRepository productDistrictAllocationRepository;

    private final ProductDistrictAllocationSearchRepository productDistrictAllocationSearchRepository;

    public ProductDistrictAllocationResource(
        ProductDistrictAllocationRepository productDistrictAllocationRepository,
        ProductDistrictAllocationSearchRepository productDistrictAllocationSearchRepository
    ) {
        this.productDistrictAllocationRepository = productDistrictAllocationRepository;
        this.productDistrictAllocationSearchRepository = productDistrictAllocationSearchRepository;
    }

    /**
     * {@code POST  /product-district-allocations} : Create a new productDistrictAllocation.
     *
     * @param productDistrictAllocation the productDistrictAllocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDistrictAllocation, or with status {@code 400 (Bad Request)} if the productDistrictAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductDistrictAllocation> createProductDistrictAllocation(
        @RequestBody ProductDistrictAllocation productDistrictAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductDistrictAllocation : {}", productDistrictAllocation);
        if (productDistrictAllocation.getId() != null) {
            throw new BadRequestAlertException("A new productDistrictAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productDistrictAllocation = productDistrictAllocationRepository.save(productDistrictAllocation);
        productDistrictAllocationSearchRepository.index(productDistrictAllocation);
        return ResponseEntity.created(new URI("/api/product-district-allocations/" + productDistrictAllocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productDistrictAllocation.getId().toString()))
            .body(productDistrictAllocation);
    }

    /**
     * {@code PUT  /product-district-allocations/:id} : Updates an existing productDistrictAllocation.
     *
     * @param id the id of the productDistrictAllocation to save.
     * @param productDistrictAllocation the productDistrictAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDistrictAllocation,
     * or with status {@code 400 (Bad Request)} if the productDistrictAllocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDistrictAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDistrictAllocation> updateProductDistrictAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDistrictAllocation productDistrictAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductDistrictAllocation : {}, {}", id, productDistrictAllocation);
        if (productDistrictAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDistrictAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productDistrictAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productDistrictAllocation = productDistrictAllocationRepository.save(productDistrictAllocation);
        productDistrictAllocationSearchRepository.index(productDistrictAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDistrictAllocation.getId().toString()))
            .body(productDistrictAllocation);
    }

    /**
     * {@code PATCH  /product-district-allocations/:id} : Partial updates given fields of an existing productDistrictAllocation, field will ignore if it is null
     *
     * @param id the id of the productDistrictAllocation to save.
     * @param productDistrictAllocation the productDistrictAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDistrictAllocation,
     * or with status {@code 400 (Bad Request)} if the productDistrictAllocation is not valid,
     * or with status {@code 404 (Not Found)} if the productDistrictAllocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the productDistrictAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductDistrictAllocation> partialUpdateProductDistrictAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDistrictAllocation productDistrictAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductDistrictAllocation partially : {}, {}", id, productDistrictAllocation);
        if (productDistrictAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDistrictAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productDistrictAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductDistrictAllocation> result = productDistrictAllocationRepository
            .findById(productDistrictAllocation.getId())
            .map(existingProductDistrictAllocation -> {
                if (productDistrictAllocation.getCreatedBy() != null) {
                    existingProductDistrictAllocation.setCreatedBy(productDistrictAllocation.getCreatedBy());
                }
                if (productDistrictAllocation.getCreatedOn() != null) {
                    existingProductDistrictAllocation.setCreatedOn(productDistrictAllocation.getCreatedOn());
                }
                if (productDistrictAllocation.getIsAllocated() != null) {
                    existingProductDistrictAllocation.setIsAllocated(productDistrictAllocation.getIsAllocated());
                }
                if (productDistrictAllocation.getProductDistrictAllocationId() != null) {
                    existingProductDistrictAllocation.setProductDistrictAllocationId(
                        productDistrictAllocation.getProductDistrictAllocationId()
                    );
                }
                if (productDistrictAllocation.getProductId() != null) {
                    existingProductDistrictAllocation.setProductId(productDistrictAllocation.getProductId());
                }
                if (productDistrictAllocation.getSchoolDistrictId() != null) {
                    existingProductDistrictAllocation.setSchoolDistrictId(productDistrictAllocation.getSchoolDistrictId());
                }
                if (productDistrictAllocation.getUpdatedBy() != null) {
                    existingProductDistrictAllocation.setUpdatedBy(productDistrictAllocation.getUpdatedBy());
                }
                if (productDistrictAllocation.getUpdatedOn() != null) {
                    existingProductDistrictAllocation.setUpdatedOn(productDistrictAllocation.getUpdatedOn());
                }

                return existingProductDistrictAllocation;
            })
            .map(productDistrictAllocationRepository::save)
            .map(savedProductDistrictAllocation -> {
                productDistrictAllocationSearchRepository.index(savedProductDistrictAllocation);
                return savedProductDistrictAllocation;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDistrictAllocation.getId().toString())
        );
    }

    /**
     * {@code GET  /product-district-allocations} : get all the productDistrictAllocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productDistrictAllocations in body.
     */
    @GetMapping("")
    public List<ProductDistrictAllocation> getAllProductDistrictAllocations() {
        LOG.debug("REST request to get all ProductDistrictAllocations");
        return productDistrictAllocationRepository.findAll();
    }

    /**
     * {@code GET  /product-district-allocations/:id} : get the "id" productDistrictAllocation.
     *
     * @param id the id of the productDistrictAllocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDistrictAllocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDistrictAllocation> getProductDistrictAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductDistrictAllocation : {}", id);
        Optional<ProductDistrictAllocation> productDistrictAllocation = productDistrictAllocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productDistrictAllocation);
    }

    /**
     * {@code DELETE  /product-district-allocations/:id} : delete the "id" productDistrictAllocation.
     *
     * @param id the id of the productDistrictAllocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductDistrictAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductDistrictAllocation : {}", id);
        productDistrictAllocationRepository.deleteById(id);
        productDistrictAllocationSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-district-allocations/_search?query=:query} : search for the productDistrictAllocation corresponding
     * to the query.
     *
     * @param query the query of the productDistrictAllocation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductDistrictAllocation> searchProductDistrictAllocations(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductDistrictAllocations for query {}", query);
        try {
            return StreamSupport.stream(productDistrictAllocationSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
