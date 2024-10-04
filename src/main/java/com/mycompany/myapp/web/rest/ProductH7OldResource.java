package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductH7Old;
import com.mycompany.myapp.repository.ProductH7OldRepository;
import com.mycompany.myapp.repository.search.ProductH7OldSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductH7Old}.
 */
@RestController
@RequestMapping("/api/product-h-7-olds")
@Transactional
public class ProductH7OldResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductH7OldResource.class);

    private static final String ENTITY_NAME = "productH7Old";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductH7OldRepository productH7OldRepository;

    private final ProductH7OldSearchRepository productH7OldSearchRepository;

    public ProductH7OldResource(ProductH7OldRepository productH7OldRepository, ProductH7OldSearchRepository productH7OldSearchRepository) {
        this.productH7OldRepository = productH7OldRepository;
        this.productH7OldSearchRepository = productH7OldSearchRepository;
    }

    /**
     * {@code POST  /product-h-7-olds} : Create a new productH7Old.
     *
     * @param productH7Old the productH7Old to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productH7Old, or with status {@code 400 (Bad Request)} if the productH7Old has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductH7Old> createProductH7Old(@RequestBody ProductH7Old productH7Old) throws URISyntaxException {
        LOG.debug("REST request to save ProductH7Old : {}", productH7Old);
        if (productH7Old.getId() != null) {
            throw new BadRequestAlertException("A new productH7Old cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productH7Old = productH7OldRepository.save(productH7Old);
        productH7OldSearchRepository.index(productH7Old);
        return ResponseEntity.created(new URI("/api/product-h-7-olds/" + productH7Old.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productH7Old.getId().toString()))
            .body(productH7Old);
    }

    /**
     * {@code PUT  /product-h-7-olds/:id} : Updates an existing productH7Old.
     *
     * @param id the id of the productH7Old to save.
     * @param productH7Old the productH7Old to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productH7Old,
     * or with status {@code 400 (Bad Request)} if the productH7Old is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productH7Old couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductH7Old> updateProductH7Old(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductH7Old productH7Old
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductH7Old : {}, {}", id, productH7Old);
        if (productH7Old.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productH7Old.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productH7OldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productH7Old = productH7OldRepository.save(productH7Old);
        productH7OldSearchRepository.index(productH7Old);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productH7Old.getId().toString()))
            .body(productH7Old);
    }

    /**
     * {@code PATCH  /product-h-7-olds/:id} : Partial updates given fields of an existing productH7Old, field will ignore if it is null
     *
     * @param id the id of the productH7Old to save.
     * @param productH7Old the productH7Old to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productH7Old,
     * or with status {@code 400 (Bad Request)} if the productH7Old is not valid,
     * or with status {@code 404 (Not Found)} if the productH7Old is not found,
     * or with status {@code 500 (Internal Server Error)} if the productH7Old couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductH7Old> partialUpdateProductH7Old(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductH7Old productH7Old
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductH7Old partially : {}, {}", id, productH7Old);
        if (productH7Old.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productH7Old.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productH7OldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductH7Old> result = productH7OldRepository
            .findById(productH7Old.getId())
            .map(existingProductH7Old -> {
                if (productH7Old.getGtinUpc() != null) {
                    existingProductH7Old.setGtinUpc(productH7Old.getGtinUpc());
                }
                if (productH7Old.geth7KeywordId() != null) {
                    existingProductH7Old.seth7KeywordId(productH7Old.geth7KeywordId());
                }
                if (productH7Old.getiOCGroup() != null) {
                    existingProductH7Old.setiOCGroup(productH7Old.getiOCGroup());
                }
                if (productH7Old.getProductH7Id() != null) {
                    existingProductH7Old.setProductH7Id(productH7Old.getProductH7Id());
                }
                if (productH7Old.getProductId() != null) {
                    existingProductH7Old.setProductId(productH7Old.getProductId());
                }
                if (productH7Old.getProductName() != null) {
                    existingProductH7Old.setProductName(productH7Old.getProductName());
                }

                return existingProductH7Old;
            })
            .map(productH7OldRepository::save)
            .map(savedProductH7Old -> {
                productH7OldSearchRepository.index(savedProductH7Old);
                return savedProductH7Old;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productH7Old.getId().toString())
        );
    }

    /**
     * {@code GET  /product-h-7-olds} : get all the productH7Olds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productH7Olds in body.
     */
    @GetMapping("")
    public List<ProductH7Old> getAllProductH7Olds() {
        LOG.debug("REST request to get all ProductH7Olds");
        return productH7OldRepository.findAll();
    }

    /**
     * {@code GET  /product-h-7-olds/:id} : get the "id" productH7Old.
     *
     * @param id the id of the productH7Old to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productH7Old, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductH7Old> getProductH7Old(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductH7Old : {}", id);
        Optional<ProductH7Old> productH7Old = productH7OldRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productH7Old);
    }

    /**
     * {@code DELETE  /product-h-7-olds/:id} : delete the "id" productH7Old.
     *
     * @param id the id of the productH7Old to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductH7Old(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductH7Old : {}", id);
        productH7OldRepository.deleteById(id);
        productH7OldSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-h-7-olds/_search?query=:query} : search for the productH7Old corresponding
     * to the query.
     *
     * @param query the query of the productH7Old search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductH7Old> searchProductH7Olds(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductH7Olds for query {}", query);
        try {
            return StreamSupport.stream(productH7OldSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
