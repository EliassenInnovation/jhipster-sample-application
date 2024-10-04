package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductH7;
import com.mycompany.myapp.repository.ProductH7Repository;
import com.mycompany.myapp.repository.search.ProductH7SearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductH7}.
 */
@RestController
@RequestMapping("/api/product-h-7-s")
@Transactional
public class ProductH7Resource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductH7Resource.class);

    private static final String ENTITY_NAME = "productH7";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductH7Repository productH7Repository;

    private final ProductH7SearchRepository productH7SearchRepository;

    public ProductH7Resource(ProductH7Repository productH7Repository, ProductH7SearchRepository productH7SearchRepository) {
        this.productH7Repository = productH7Repository;
        this.productH7SearchRepository = productH7SearchRepository;
    }

    /**
     * {@code POST  /product-h-7-s} : Create a new productH7.
     *
     * @param productH7 the productH7 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productH7, or with status {@code 400 (Bad Request)} if the productH7 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductH7> createProductH7(@RequestBody ProductH7 productH7) throws URISyntaxException {
        LOG.debug("REST request to save ProductH7 : {}", productH7);
        if (productH7.getId() != null) {
            throw new BadRequestAlertException("A new productH7 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productH7 = productH7Repository.save(productH7);
        productH7SearchRepository.index(productH7);
        return ResponseEntity.created(new URI("/api/product-h-7-s/" + productH7.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productH7.getId().toString()))
            .body(productH7);
    }

    /**
     * {@code PUT  /product-h-7-s/:id} : Updates an existing productH7.
     *
     * @param id the id of the productH7 to save.
     * @param productH7 the productH7 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productH7,
     * or with status {@code 400 (Bad Request)} if the productH7 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productH7 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductH7> updateProductH7(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductH7 productH7
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductH7 : {}, {}", id, productH7);
        if (productH7.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productH7.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productH7Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productH7 = productH7Repository.save(productH7);
        productH7SearchRepository.index(productH7);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productH7.getId().toString()))
            .body(productH7);
    }

    /**
     * {@code PATCH  /product-h-7-s/:id} : Partial updates given fields of an existing productH7, field will ignore if it is null
     *
     * @param id the id of the productH7 to save.
     * @param productH7 the productH7 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productH7,
     * or with status {@code 400 (Bad Request)} if the productH7 is not valid,
     * or with status {@code 404 (Not Found)} if the productH7 is not found,
     * or with status {@code 500 (Internal Server Error)} if the productH7 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductH7> partialUpdateProductH7(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductH7 productH7
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductH7 partially : {}, {}", id, productH7);
        if (productH7.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productH7.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productH7Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductH7> result = productH7Repository
            .findById(productH7.getId())
            .map(existingProductH7 -> {
                if (productH7.getGtinUpc() != null) {
                    existingProductH7.setGtinUpc(productH7.getGtinUpc());
                }
                if (productH7.geth7KeywordId() != null) {
                    existingProductH7.seth7KeywordId(productH7.geth7KeywordId());
                }
                if (productH7.getiOCGroup() != null) {
                    existingProductH7.setiOCGroup(productH7.getiOCGroup());
                }
                if (productH7.getProductH7Id() != null) {
                    existingProductH7.setProductH7Id(productH7.getProductH7Id());
                }
                if (productH7.getProductId() != null) {
                    existingProductH7.setProductId(productH7.getProductId());
                }
                if (productH7.getProductName() != null) {
                    existingProductH7.setProductName(productH7.getProductName());
                }

                return existingProductH7;
            })
            .map(productH7Repository::save)
            .map(savedProductH7 -> {
                productH7SearchRepository.index(savedProductH7);
                return savedProductH7;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productH7.getId().toString())
        );
    }

    /**
     * {@code GET  /product-h-7-s} : get all the productH7s.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productH7s in body.
     */
    @GetMapping("")
    public List<ProductH7> getAllProductH7s() {
        LOG.debug("REST request to get all ProductH7s");
        return productH7Repository.findAll();
    }

    /**
     * {@code GET  /product-h-7-s/:id} : get the "id" productH7.
     *
     * @param id the id of the productH7 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productH7, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductH7> getProductH7(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductH7 : {}", id);
        Optional<ProductH7> productH7 = productH7Repository.findById(id);
        return ResponseUtil.wrapOrNotFound(productH7);
    }

    /**
     * {@code DELETE  /product-h-7-s/:id} : delete the "id" productH7.
     *
     * @param id the id of the productH7 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductH7(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductH7 : {}", id);
        productH7Repository.deleteById(id);
        productH7SearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-h-7-s/_search?query=:query} : search for the productH7 corresponding
     * to the query.
     *
     * @param query the query of the productH7 search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductH7> searchProductH7s(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductH7s for query {}", query);
        try {
            return StreamSupport.stream(productH7SearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
