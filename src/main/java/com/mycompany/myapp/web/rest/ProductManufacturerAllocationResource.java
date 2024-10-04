package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductManufacturerAllocation;
import com.mycompany.myapp.repository.ProductManufacturerAllocationRepository;
import com.mycompany.myapp.repository.search.ProductManufacturerAllocationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductManufacturerAllocation}.
 */
@RestController
@RequestMapping("/api/product-manufacturer-allocations")
@Transactional
public class ProductManufacturerAllocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductManufacturerAllocationResource.class);

    private static final String ENTITY_NAME = "productManufacturerAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductManufacturerAllocationRepository productManufacturerAllocationRepository;

    private final ProductManufacturerAllocationSearchRepository productManufacturerAllocationSearchRepository;

    public ProductManufacturerAllocationResource(
        ProductManufacturerAllocationRepository productManufacturerAllocationRepository,
        ProductManufacturerAllocationSearchRepository productManufacturerAllocationSearchRepository
    ) {
        this.productManufacturerAllocationRepository = productManufacturerAllocationRepository;
        this.productManufacturerAllocationSearchRepository = productManufacturerAllocationSearchRepository;
    }

    /**
     * {@code POST  /product-manufacturer-allocations} : Create a new productManufacturerAllocation.
     *
     * @param productManufacturerAllocation the productManufacturerAllocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productManufacturerAllocation, or with status {@code 400 (Bad Request)} if the productManufacturerAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductManufacturerAllocation> createProductManufacturerAllocation(
        @RequestBody ProductManufacturerAllocation productManufacturerAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductManufacturerAllocation : {}", productManufacturerAllocation);
        if (productManufacturerAllocation.getId() != null) {
            throw new BadRequestAlertException("A new productManufacturerAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productManufacturerAllocation = productManufacturerAllocationRepository.save(productManufacturerAllocation);
        productManufacturerAllocationSearchRepository.index(productManufacturerAllocation);
        return ResponseEntity.created(new URI("/api/product-manufacturer-allocations/" + productManufacturerAllocation.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productManufacturerAllocation.getId().toString())
            )
            .body(productManufacturerAllocation);
    }

    /**
     * {@code PUT  /product-manufacturer-allocations/:id} : Updates an existing productManufacturerAllocation.
     *
     * @param id the id of the productManufacturerAllocation to save.
     * @param productManufacturerAllocation the productManufacturerAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productManufacturerAllocation,
     * or with status {@code 400 (Bad Request)} if the productManufacturerAllocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productManufacturerAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductManufacturerAllocation> updateProductManufacturerAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductManufacturerAllocation productManufacturerAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductManufacturerAllocation : {}, {}", id, productManufacturerAllocation);
        if (productManufacturerAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productManufacturerAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productManufacturerAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productManufacturerAllocation = productManufacturerAllocationRepository.save(productManufacturerAllocation);
        productManufacturerAllocationSearchRepository.index(productManufacturerAllocation);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productManufacturerAllocation.getId().toString())
            )
            .body(productManufacturerAllocation);
    }

    /**
     * {@code PATCH  /product-manufacturer-allocations/:id} : Partial updates given fields of an existing productManufacturerAllocation, field will ignore if it is null
     *
     * @param id the id of the productManufacturerAllocation to save.
     * @param productManufacturerAllocation the productManufacturerAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productManufacturerAllocation,
     * or with status {@code 400 (Bad Request)} if the productManufacturerAllocation is not valid,
     * or with status {@code 404 (Not Found)} if the productManufacturerAllocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the productManufacturerAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductManufacturerAllocation> partialUpdateProductManufacturerAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductManufacturerAllocation productManufacturerAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductManufacturerAllocation partially : {}, {}", id, productManufacturerAllocation);
        if (productManufacturerAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productManufacturerAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productManufacturerAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductManufacturerAllocation> result = productManufacturerAllocationRepository
            .findById(productManufacturerAllocation.getId())
            .map(existingProductManufacturerAllocation -> {
                if (productManufacturerAllocation.getCreatedBy() != null) {
                    existingProductManufacturerAllocation.setCreatedBy(productManufacturerAllocation.getCreatedBy());
                }
                if (productManufacturerAllocation.getCreatedOn() != null) {
                    existingProductManufacturerAllocation.setCreatedOn(productManufacturerAllocation.getCreatedOn());
                }
                if (productManufacturerAllocation.getIsAllocated() != null) {
                    existingProductManufacturerAllocation.setIsAllocated(productManufacturerAllocation.getIsAllocated());
                }
                if (productManufacturerAllocation.getManufactureId() != null) {
                    existingProductManufacturerAllocation.setManufactureId(productManufacturerAllocation.getManufactureId());
                }
                if (productManufacturerAllocation.getProductId() != null) {
                    existingProductManufacturerAllocation.setProductId(productManufacturerAllocation.getProductId());
                }
                if (productManufacturerAllocation.getProductManufacturerAllocationId() != null) {
                    existingProductManufacturerAllocation.setProductManufacturerAllocationId(
                        productManufacturerAllocation.getProductManufacturerAllocationId()
                    );
                }
                if (productManufacturerAllocation.getUpdatedBy() != null) {
                    existingProductManufacturerAllocation.setUpdatedBy(productManufacturerAllocation.getUpdatedBy());
                }
                if (productManufacturerAllocation.getUpdatedOn() != null) {
                    existingProductManufacturerAllocation.setUpdatedOn(productManufacturerAllocation.getUpdatedOn());
                }

                return existingProductManufacturerAllocation;
            })
            .map(productManufacturerAllocationRepository::save)
            .map(savedProductManufacturerAllocation -> {
                productManufacturerAllocationSearchRepository.index(savedProductManufacturerAllocation);
                return savedProductManufacturerAllocation;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productManufacturerAllocation.getId().toString())
        );
    }

    /**
     * {@code GET  /product-manufacturer-allocations} : get all the productManufacturerAllocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productManufacturerAllocations in body.
     */
    @GetMapping("")
    public List<ProductManufacturerAllocation> getAllProductManufacturerAllocations() {
        LOG.debug("REST request to get all ProductManufacturerAllocations");
        return productManufacturerAllocationRepository.findAll();
    }

    /**
     * {@code GET  /product-manufacturer-allocations/:id} : get the "id" productManufacturerAllocation.
     *
     * @param id the id of the productManufacturerAllocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productManufacturerAllocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductManufacturerAllocation> getProductManufacturerAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductManufacturerAllocation : {}", id);
        Optional<ProductManufacturerAllocation> productManufacturerAllocation = productManufacturerAllocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productManufacturerAllocation);
    }

    /**
     * {@code DELETE  /product-manufacturer-allocations/:id} : delete the "id" productManufacturerAllocation.
     *
     * @param id the id of the productManufacturerAllocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductManufacturerAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductManufacturerAllocation : {}", id);
        productManufacturerAllocationRepository.deleteById(id);
        productManufacturerAllocationSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-manufacturer-allocations/_search?query=:query} : search for the productManufacturerAllocation corresponding
     * to the query.
     *
     * @param query the query of the productManufacturerAllocation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductManufacturerAllocation> searchProductManufacturerAllocations(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductManufacturerAllocations for query {}", query);
        try {
            return StreamSupport.stream(productManufacturerAllocationSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
