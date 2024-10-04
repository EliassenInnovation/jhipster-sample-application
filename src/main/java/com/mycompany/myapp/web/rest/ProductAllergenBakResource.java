package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductAllergenBak;
import com.mycompany.myapp.repository.ProductAllergenBakRepository;
import com.mycompany.myapp.repository.search.ProductAllergenBakSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductAllergenBak}.
 */
@RestController
@RequestMapping("/api/product-allergen-baks")
@Transactional
public class ProductAllergenBakResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAllergenBakResource.class);

    private static final String ENTITY_NAME = "productAllergenBak";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductAllergenBakRepository productAllergenBakRepository;

    private final ProductAllergenBakSearchRepository productAllergenBakSearchRepository;

    public ProductAllergenBakResource(
        ProductAllergenBakRepository productAllergenBakRepository,
        ProductAllergenBakSearchRepository productAllergenBakSearchRepository
    ) {
        this.productAllergenBakRepository = productAllergenBakRepository;
        this.productAllergenBakSearchRepository = productAllergenBakSearchRepository;
    }

    /**
     * {@code POST  /product-allergen-baks} : Create a new productAllergenBak.
     *
     * @param productAllergenBak the productAllergenBak to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAllergenBak, or with status {@code 400 (Bad Request)} if the productAllergenBak has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductAllergenBak> createProductAllergenBak(@RequestBody ProductAllergenBak productAllergenBak)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductAllergenBak : {}", productAllergenBak);
        if (productAllergenBak.getId() != null) {
            throw new BadRequestAlertException("A new productAllergenBak cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productAllergenBak = productAllergenBakRepository.save(productAllergenBak);
        productAllergenBakSearchRepository.index(productAllergenBak);
        return ResponseEntity.created(new URI("/api/product-allergen-baks/" + productAllergenBak.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productAllergenBak.getId().toString()))
            .body(productAllergenBak);
    }

    /**
     * {@code PUT  /product-allergen-baks/:id} : Updates an existing productAllergenBak.
     *
     * @param id the id of the productAllergenBak to save.
     * @param productAllergenBak the productAllergenBak to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAllergenBak,
     * or with status {@code 400 (Bad Request)} if the productAllergenBak is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAllergenBak couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAllergenBak> updateProductAllergenBak(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductAllergenBak productAllergenBak
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductAllergenBak : {}, {}", id, productAllergenBak);
        if (productAllergenBak.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAllergenBak.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAllergenBakRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productAllergenBak = productAllergenBakRepository.save(productAllergenBak);
        productAllergenBakSearchRepository.index(productAllergenBak);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAllergenBak.getId().toString()))
            .body(productAllergenBak);
    }

    /**
     * {@code PATCH  /product-allergen-baks/:id} : Partial updates given fields of an existing productAllergenBak, field will ignore if it is null
     *
     * @param id the id of the productAllergenBak to save.
     * @param productAllergenBak the productAllergenBak to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAllergenBak,
     * or with status {@code 400 (Bad Request)} if the productAllergenBak is not valid,
     * or with status {@code 404 (Not Found)} if the productAllergenBak is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAllergenBak couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductAllergenBak> partialUpdateProductAllergenBak(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductAllergenBak productAllergenBak
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductAllergenBak partially : {}, {}", id, productAllergenBak);
        if (productAllergenBak.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAllergenBak.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAllergenBakRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAllergenBak> result = productAllergenBakRepository
            .findById(productAllergenBak.getId())
            .map(existingProductAllergenBak -> {
                if (productAllergenBak.getAllergenId() != null) {
                    existingProductAllergenBak.setAllergenId(productAllergenBak.getAllergenId());
                }
                if (productAllergenBak.getAllergenGroup() != null) {
                    existingProductAllergenBak.setAllergenGroup(productAllergenBak.getAllergenGroup());
                }
                if (productAllergenBak.getAllergenName() != null) {
                    existingProductAllergenBak.setAllergenName(productAllergenBak.getAllergenName());
                }
                if (productAllergenBak.getDescription() != null) {
                    existingProductAllergenBak.setDescription(productAllergenBak.getDescription());
                }
                if (productAllergenBak.getgTIN() != null) {
                    existingProductAllergenBak.setgTIN(productAllergenBak.getgTIN());
                }
                if (productAllergenBak.getgTINUPC() != null) {
                    existingProductAllergenBak.setgTINUPC(productAllergenBak.getgTINUPC());
                }
                if (productAllergenBak.getProductAllergenId() != null) {
                    existingProductAllergenBak.setProductAllergenId(productAllergenBak.getProductAllergenId());
                }
                if (productAllergenBak.getProductId() != null) {
                    existingProductAllergenBak.setProductId(productAllergenBak.getProductId());
                }
                if (productAllergenBak.getuPC() != null) {
                    existingProductAllergenBak.setuPC(productAllergenBak.getuPC());
                }

                return existingProductAllergenBak;
            })
            .map(productAllergenBakRepository::save)
            .map(savedProductAllergenBak -> {
                productAllergenBakSearchRepository.index(savedProductAllergenBak);
                return savedProductAllergenBak;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAllergenBak.getId().toString())
        );
    }

    /**
     * {@code GET  /product-allergen-baks} : get all the productAllergenBaks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productAllergenBaks in body.
     */
    @GetMapping("")
    public List<ProductAllergenBak> getAllProductAllergenBaks() {
        LOG.debug("REST request to get all ProductAllergenBaks");
        return productAllergenBakRepository.findAll();
    }

    /**
     * {@code GET  /product-allergen-baks/:id} : get the "id" productAllergenBak.
     *
     * @param id the id of the productAllergenBak to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAllergenBak, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAllergenBak> getProductAllergenBak(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductAllergenBak : {}", id);
        Optional<ProductAllergenBak> productAllergenBak = productAllergenBakRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productAllergenBak);
    }

    /**
     * {@code DELETE  /product-allergen-baks/:id} : delete the "id" productAllergenBak.
     *
     * @param id the id of the productAllergenBak to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAllergenBak(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductAllergenBak : {}", id);
        productAllergenBakRepository.deleteById(id);
        productAllergenBakSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-allergen-baks/_search?query=:query} : search for the productAllergenBak corresponding
     * to the query.
     *
     * @param query the query of the productAllergenBak search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductAllergenBak> searchProductAllergenBaks(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductAllergenBaks for query {}", query);
        try {
            return StreamSupport.stream(productAllergenBakSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
