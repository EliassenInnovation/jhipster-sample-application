package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductImageBeforeApprove;
import com.mycompany.myapp.repository.ProductImageBeforeApproveRepository;
import com.mycompany.myapp.repository.search.ProductImageBeforeApproveSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductImageBeforeApprove}.
 */
@RestController
@RequestMapping("/api/product-image-before-approves")
@Transactional
public class ProductImageBeforeApproveResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductImageBeforeApproveResource.class);

    private static final String ENTITY_NAME = "productImageBeforeApprove";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductImageBeforeApproveRepository productImageBeforeApproveRepository;

    private final ProductImageBeforeApproveSearchRepository productImageBeforeApproveSearchRepository;

    public ProductImageBeforeApproveResource(
        ProductImageBeforeApproveRepository productImageBeforeApproveRepository,
        ProductImageBeforeApproveSearchRepository productImageBeforeApproveSearchRepository
    ) {
        this.productImageBeforeApproveRepository = productImageBeforeApproveRepository;
        this.productImageBeforeApproveSearchRepository = productImageBeforeApproveSearchRepository;
    }

    /**
     * {@code POST  /product-image-before-approves} : Create a new productImageBeforeApprove.
     *
     * @param productImageBeforeApprove the productImageBeforeApprove to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productImageBeforeApprove, or with status {@code 400 (Bad Request)} if the productImageBeforeApprove has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductImageBeforeApprove> createProductImageBeforeApprove(
        @RequestBody ProductImageBeforeApprove productImageBeforeApprove
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProductImageBeforeApprove : {}", productImageBeforeApprove);
        if (productImageBeforeApprove.getId() != null) {
            throw new BadRequestAlertException("A new productImageBeforeApprove cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productImageBeforeApprove = productImageBeforeApproveRepository.save(productImageBeforeApprove);
        productImageBeforeApproveSearchRepository.index(productImageBeforeApprove);
        return ResponseEntity.created(new URI("/api/product-image-before-approves/" + productImageBeforeApprove.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productImageBeforeApprove.getId().toString()))
            .body(productImageBeforeApprove);
    }

    /**
     * {@code PUT  /product-image-before-approves/:id} : Updates an existing productImageBeforeApprove.
     *
     * @param id the id of the productImageBeforeApprove to save.
     * @param productImageBeforeApprove the productImageBeforeApprove to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImageBeforeApprove,
     * or with status {@code 400 (Bad Request)} if the productImageBeforeApprove is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productImageBeforeApprove couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductImageBeforeApprove> updateProductImageBeforeApprove(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImageBeforeApprove productImageBeforeApprove
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductImageBeforeApprove : {}, {}", id, productImageBeforeApprove);
        if (productImageBeforeApprove.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImageBeforeApprove.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageBeforeApproveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productImageBeforeApprove = productImageBeforeApproveRepository.save(productImageBeforeApprove);
        productImageBeforeApproveSearchRepository.index(productImageBeforeApprove);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImageBeforeApprove.getId().toString()))
            .body(productImageBeforeApprove);
    }

    /**
     * {@code PATCH  /product-image-before-approves/:id} : Partial updates given fields of an existing productImageBeforeApprove, field will ignore if it is null
     *
     * @param id the id of the productImageBeforeApprove to save.
     * @param productImageBeforeApprove the productImageBeforeApprove to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImageBeforeApprove,
     * or with status {@code 400 (Bad Request)} if the productImageBeforeApprove is not valid,
     * or with status {@code 404 (Not Found)} if the productImageBeforeApprove is not found,
     * or with status {@code 500 (Internal Server Error)} if the productImageBeforeApprove couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductImageBeforeApprove> partialUpdateProductImageBeforeApprove(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImageBeforeApprove productImageBeforeApprove
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductImageBeforeApprove partially : {}, {}", id, productImageBeforeApprove);
        if (productImageBeforeApprove.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImageBeforeApprove.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageBeforeApproveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductImageBeforeApprove> result = productImageBeforeApproveRepository
            .findById(productImageBeforeApprove.getId())
            .map(existingProductImageBeforeApprove -> {
                if (productImageBeforeApprove.getCreatedBy() != null) {
                    existingProductImageBeforeApprove.setCreatedBy(productImageBeforeApprove.getCreatedBy());
                }
                if (productImageBeforeApprove.getCreatedOn() != null) {
                    existingProductImageBeforeApprove.setCreatedOn(productImageBeforeApprove.getCreatedOn());
                }
                if (productImageBeforeApprove.getImageURL() != null) {
                    existingProductImageBeforeApprove.setImageURL(productImageBeforeApprove.getImageURL());
                }
                if (productImageBeforeApprove.getIsActive() != null) {
                    existingProductImageBeforeApprove.setIsActive(productImageBeforeApprove.getIsActive());
                }
                if (productImageBeforeApprove.getProductId() != null) {
                    existingProductImageBeforeApprove.setProductId(productImageBeforeApprove.getProductId());
                }
                if (productImageBeforeApprove.getProductImageId() != null) {
                    existingProductImageBeforeApprove.setProductImageId(productImageBeforeApprove.getProductImageId());
                }

                return existingProductImageBeforeApprove;
            })
            .map(productImageBeforeApproveRepository::save)
            .map(savedProductImageBeforeApprove -> {
                productImageBeforeApproveSearchRepository.index(savedProductImageBeforeApprove);
                return savedProductImageBeforeApprove;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImageBeforeApprove.getId().toString())
        );
    }

    /**
     * {@code GET  /product-image-before-approves} : get all the productImageBeforeApproves.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productImageBeforeApproves in body.
     */
    @GetMapping("")
    public List<ProductImageBeforeApprove> getAllProductImageBeforeApproves() {
        LOG.debug("REST request to get all ProductImageBeforeApproves");
        return productImageBeforeApproveRepository.findAll();
    }

    /**
     * {@code GET  /product-image-before-approves/:id} : get the "id" productImageBeforeApprove.
     *
     * @param id the id of the productImageBeforeApprove to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productImageBeforeApprove, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductImageBeforeApprove> getProductImageBeforeApprove(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductImageBeforeApprove : {}", id);
        Optional<ProductImageBeforeApprove> productImageBeforeApprove = productImageBeforeApproveRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productImageBeforeApprove);
    }

    /**
     * {@code DELETE  /product-image-before-approves/:id} : delete the "id" productImageBeforeApprove.
     *
     * @param id the id of the productImageBeforeApprove to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductImageBeforeApprove(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductImageBeforeApprove : {}", id);
        productImageBeforeApproveRepository.deleteById(id);
        productImageBeforeApproveSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-image-before-approves/_search?query=:query} : search for the productImageBeforeApprove corresponding
     * to the query.
     *
     * @param query the query of the productImageBeforeApprove search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductImageBeforeApprove> searchProductImageBeforeApproves(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductImageBeforeApproves for query {}", query);
        try {
            return StreamSupport.stream(productImageBeforeApproveSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
