package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductUpcAllocation;
import com.mycompany.myapp.repository.ProductUpcAllocationRepository;
import com.mycompany.myapp.repository.search.ProductUpcAllocationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductUpcAllocation}.
 */
@RestController
@RequestMapping("/api/product-upc-allocations")
@Transactional
public class ProductUpcAllocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductUpcAllocationResource.class);

    private static final String ENTITY_NAME = "productUpcAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductUpcAllocationRepository productUpcAllocationRepository;

    private final ProductUpcAllocationSearchRepository productUpcAllocationSearchRepository;

    public ProductUpcAllocationResource(
        ProductUpcAllocationRepository productUpcAllocationRepository,
        ProductUpcAllocationSearchRepository productUpcAllocationSearchRepository
    ) {
        this.productUpcAllocationRepository = productUpcAllocationRepository;
        this.productUpcAllocationSearchRepository = productUpcAllocationSearchRepository;
    }

    /**
     * {@code POST  /product-upc-allocations} : Create a new productUpcAllocation.
     *
     * @param productUpcAllocation the productUpcAllocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productUpcAllocation, or with status {@code 400 (Bad Request)} if the productUpcAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductUpcAllocation> createProductUpcAllocation(@RequestBody ProductUpcAllocation productUpcAllocation)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductUpcAllocation : {}", productUpcAllocation);
        if (productUpcAllocation.getId() != null) {
            throw new BadRequestAlertException("A new productUpcAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productUpcAllocation = productUpcAllocationRepository.save(productUpcAllocation);
        productUpcAllocationSearchRepository.index(productUpcAllocation);
        return ResponseEntity.created(new URI("/api/product-upc-allocations/" + productUpcAllocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productUpcAllocation.getId().toString()))
            .body(productUpcAllocation);
    }

    /**
     * {@code PUT  /product-upc-allocations/:id} : Updates an existing productUpcAllocation.
     *
     * @param id the id of the productUpcAllocation to save.
     * @param productUpcAllocation the productUpcAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productUpcAllocation,
     * or with status {@code 400 (Bad Request)} if the productUpcAllocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productUpcAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductUpcAllocation> updateProductUpcAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductUpcAllocation productUpcAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductUpcAllocation : {}, {}", id, productUpcAllocation);
        if (productUpcAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productUpcAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productUpcAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productUpcAllocation = productUpcAllocationRepository.save(productUpcAllocation);
        productUpcAllocationSearchRepository.index(productUpcAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productUpcAllocation.getId().toString()))
            .body(productUpcAllocation);
    }

    /**
     * {@code PATCH  /product-upc-allocations/:id} : Partial updates given fields of an existing productUpcAllocation, field will ignore if it is null
     *
     * @param id the id of the productUpcAllocation to save.
     * @param productUpcAllocation the productUpcAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productUpcAllocation,
     * or with status {@code 400 (Bad Request)} if the productUpcAllocation is not valid,
     * or with status {@code 404 (Not Found)} if the productUpcAllocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the productUpcAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductUpcAllocation> partialUpdateProductUpcAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductUpcAllocation productUpcAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductUpcAllocation partially : {}, {}", id, productUpcAllocation);
        if (productUpcAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productUpcAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productUpcAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductUpcAllocation> result = productUpcAllocationRepository
            .findById(productUpcAllocation.getId())
            .map(existingProductUpcAllocation -> {
                if (productUpcAllocation.getCreatedBy() != null) {
                    existingProductUpcAllocation.setCreatedBy(productUpcAllocation.getCreatedBy());
                }
                if (productUpcAllocation.getCreatedOn() != null) {
                    existingProductUpcAllocation.setCreatedOn(productUpcAllocation.getCreatedOn());
                }
                if (productUpcAllocation.getIsActive() != null) {
                    existingProductUpcAllocation.setIsActive(productUpcAllocation.getIsActive());
                }
                if (productUpcAllocation.getProductId() != null) {
                    existingProductUpcAllocation.setProductId(productUpcAllocation.getProductId());
                }
                if (productUpcAllocation.getProductUpcId() != null) {
                    existingProductUpcAllocation.setProductUpcId(productUpcAllocation.getProductUpcId());
                }
                if (productUpcAllocation.getuPC() != null) {
                    existingProductUpcAllocation.setuPC(productUpcAllocation.getuPC());
                }
                if (productUpcAllocation.getUpdatedBy() != null) {
                    existingProductUpcAllocation.setUpdatedBy(productUpcAllocation.getUpdatedBy());
                }
                if (productUpcAllocation.getUpdatedOn() != null) {
                    existingProductUpcAllocation.setUpdatedOn(productUpcAllocation.getUpdatedOn());
                }

                return existingProductUpcAllocation;
            })
            .map(productUpcAllocationRepository::save)
            .map(savedProductUpcAllocation -> {
                productUpcAllocationSearchRepository.index(savedProductUpcAllocation);
                return savedProductUpcAllocation;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productUpcAllocation.getId().toString())
        );
    }

    /**
     * {@code GET  /product-upc-allocations} : get all the productUpcAllocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productUpcAllocations in body.
     */
    @GetMapping("")
    public List<ProductUpcAllocation> getAllProductUpcAllocations() {
        LOG.debug("REST request to get all ProductUpcAllocations");
        return productUpcAllocationRepository.findAll();
    }

    /**
     * {@code GET  /product-upc-allocations/:id} : get the "id" productUpcAllocation.
     *
     * @param id the id of the productUpcAllocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productUpcAllocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductUpcAllocation> getProductUpcAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductUpcAllocation : {}", id);
        Optional<ProductUpcAllocation> productUpcAllocation = productUpcAllocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productUpcAllocation);
    }

    /**
     * {@code DELETE  /product-upc-allocations/:id} : delete the "id" productUpcAllocation.
     *
     * @param id the id of the productUpcAllocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductUpcAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductUpcAllocation : {}", id);
        productUpcAllocationRepository.deleteById(id);
        productUpcAllocationSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-upc-allocations/_search?query=:query} : search for the productUpcAllocation corresponding
     * to the query.
     *
     * @param query the query of the productUpcAllocation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductUpcAllocation> searchProductUpcAllocations(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductUpcAllocations for query {}", query);
        try {
            return StreamSupport.stream(productUpcAllocationSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
