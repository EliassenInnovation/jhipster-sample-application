package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductsToUpdate;
import com.mycompany.myapp.repository.ProductsToUpdateRepository;
import com.mycompany.myapp.repository.search.ProductsToUpdateSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductsToUpdate}.
 */
@RestController
@RequestMapping("/api/products-to-updates")
@Transactional
public class ProductsToUpdateResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsToUpdateResource.class);

    private static final String ENTITY_NAME = "productsToUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductsToUpdateRepository productsToUpdateRepository;

    private final ProductsToUpdateSearchRepository productsToUpdateSearchRepository;

    public ProductsToUpdateResource(
        ProductsToUpdateRepository productsToUpdateRepository,
        ProductsToUpdateSearchRepository productsToUpdateSearchRepository
    ) {
        this.productsToUpdateRepository = productsToUpdateRepository;
        this.productsToUpdateSearchRepository = productsToUpdateSearchRepository;
    }

    /**
     * {@code POST  /products-to-updates} : Create a new productsToUpdate.
     *
     * @param productsToUpdate the productsToUpdate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productsToUpdate, or with status {@code 400 (Bad Request)} if the productsToUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductsToUpdate> createProductsToUpdate(@RequestBody ProductsToUpdate productsToUpdate)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductsToUpdate : {}", productsToUpdate);
        if (productsToUpdate.getId() != null) {
            throw new BadRequestAlertException("A new productsToUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productsToUpdate = productsToUpdateRepository.save(productsToUpdate);
        productsToUpdateSearchRepository.index(productsToUpdate);
        return ResponseEntity.created(new URI("/api/products-to-updates/" + productsToUpdate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productsToUpdate.getId().toString()))
            .body(productsToUpdate);
    }

    /**
     * {@code PUT  /products-to-updates/:id} : Updates an existing productsToUpdate.
     *
     * @param id the id of the productsToUpdate to save.
     * @param productsToUpdate the productsToUpdate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsToUpdate,
     * or with status {@code 400 (Bad Request)} if the productsToUpdate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productsToUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductsToUpdate> updateProductsToUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsToUpdate productsToUpdate
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductsToUpdate : {}, {}", id, productsToUpdate);
        if (productsToUpdate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsToUpdate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsToUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productsToUpdate = productsToUpdateRepository.save(productsToUpdate);
        productsToUpdateSearchRepository.index(productsToUpdate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsToUpdate.getId().toString()))
            .body(productsToUpdate);
    }

    /**
     * {@code PATCH  /products-to-updates/:id} : Partial updates given fields of an existing productsToUpdate, field will ignore if it is null
     *
     * @param id the id of the productsToUpdate to save.
     * @param productsToUpdate the productsToUpdate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsToUpdate,
     * or with status {@code 400 (Bad Request)} if the productsToUpdate is not valid,
     * or with status {@code 404 (Not Found)} if the productsToUpdate is not found,
     * or with status {@code 500 (Internal Server Error)} if the productsToUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductsToUpdate> partialUpdateProductsToUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsToUpdate productsToUpdate
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductsToUpdate partially : {}, {}", id, productsToUpdate);
        if (productsToUpdate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsToUpdate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsToUpdateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductsToUpdate> result = productsToUpdateRepository
            .findById(productsToUpdate.getId())
            .map(existingProductsToUpdate -> {
                if (productsToUpdate.getMaxGLNCode() != null) {
                    existingProductsToUpdate.setMaxGLNCode(productsToUpdate.getMaxGLNCode());
                }
                if (productsToUpdate.getMaxManufacturerID() != null) {
                    existingProductsToUpdate.setMaxManufacturerID(productsToUpdate.getMaxManufacturerID());
                }
                if (productsToUpdate.getProductId() != null) {
                    existingProductsToUpdate.setProductId(productsToUpdate.getProductId());
                }

                return existingProductsToUpdate;
            })
            .map(productsToUpdateRepository::save)
            .map(savedProductsToUpdate -> {
                productsToUpdateSearchRepository.index(savedProductsToUpdate);
                return savedProductsToUpdate;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsToUpdate.getId().toString())
        );
    }

    /**
     * {@code GET  /products-to-updates} : get all the productsToUpdates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productsToUpdates in body.
     */
    @GetMapping("")
    public List<ProductsToUpdate> getAllProductsToUpdates() {
        LOG.debug("REST request to get all ProductsToUpdates");
        return productsToUpdateRepository.findAll();
    }

    /**
     * {@code GET  /products-to-updates/:id} : get the "id" productsToUpdate.
     *
     * @param id the id of the productsToUpdate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productsToUpdate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductsToUpdate> getProductsToUpdate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductsToUpdate : {}", id);
        Optional<ProductsToUpdate> productsToUpdate = productsToUpdateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productsToUpdate);
    }

    /**
     * {@code DELETE  /products-to-updates/:id} : delete the "id" productsToUpdate.
     *
     * @param id the id of the productsToUpdate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductsToUpdate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductsToUpdate : {}", id);
        productsToUpdateRepository.deleteById(id);
        productsToUpdateSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /products-to-updates/_search?query=:query} : search for the productsToUpdate corresponding
     * to the query.
     *
     * @param query the query of the productsToUpdate search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductsToUpdate> searchProductsToUpdates(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductsToUpdates for query {}", query);
        try {
            return StreamSupport.stream(productsToUpdateSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
