package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductAllergen;
import com.mycompany.myapp.repository.ProductAllergenRepository;
import com.mycompany.myapp.repository.search.ProductAllergenSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductAllergen}.
 */
@RestController
@RequestMapping("/api/product-allergens")
@Transactional
public class ProductAllergenResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAllergenResource.class);

    private static final String ENTITY_NAME = "productAllergen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductAllergenRepository productAllergenRepository;

    private final ProductAllergenSearchRepository productAllergenSearchRepository;

    public ProductAllergenResource(
        ProductAllergenRepository productAllergenRepository,
        ProductAllergenSearchRepository productAllergenSearchRepository
    ) {
        this.productAllergenRepository = productAllergenRepository;
        this.productAllergenSearchRepository = productAllergenSearchRepository;
    }

    /**
     * {@code POST  /product-allergens} : Create a new productAllergen.
     *
     * @param productAllergen the productAllergen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAllergen, or with status {@code 400 (Bad Request)} if the productAllergen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductAllergen> createProductAllergen(@RequestBody ProductAllergen productAllergen) throws URISyntaxException {
        LOG.debug("REST request to save ProductAllergen : {}", productAllergen);
        if (productAllergen.getId() != null) {
            throw new BadRequestAlertException("A new productAllergen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productAllergen = productAllergenRepository.save(productAllergen);
        productAllergenSearchRepository.index(productAllergen);
        return ResponseEntity.created(new URI("/api/product-allergens/" + productAllergen.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productAllergen.getId().toString()))
            .body(productAllergen);
    }

    /**
     * {@code PUT  /product-allergens/:id} : Updates an existing productAllergen.
     *
     * @param id the id of the productAllergen to save.
     * @param productAllergen the productAllergen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAllergen,
     * or with status {@code 400 (Bad Request)} if the productAllergen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAllergen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAllergen> updateProductAllergen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductAllergen productAllergen
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductAllergen : {}, {}", id, productAllergen);
        if (productAllergen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAllergen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAllergenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productAllergen = productAllergenRepository.save(productAllergen);
        productAllergenSearchRepository.index(productAllergen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAllergen.getId().toString()))
            .body(productAllergen);
    }

    /**
     * {@code PATCH  /product-allergens/:id} : Partial updates given fields of an existing productAllergen, field will ignore if it is null
     *
     * @param id the id of the productAllergen to save.
     * @param productAllergen the productAllergen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAllergen,
     * or with status {@code 400 (Bad Request)} if the productAllergen is not valid,
     * or with status {@code 404 (Not Found)} if the productAllergen is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAllergen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductAllergen> partialUpdateProductAllergen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductAllergen productAllergen
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductAllergen partially : {}, {}", id, productAllergen);
        if (productAllergen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAllergen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAllergenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAllergen> result = productAllergenRepository
            .findById(productAllergen.getId())
            .map(existingProductAllergen -> {
                if (productAllergen.getAllergenId() != null) {
                    existingProductAllergen.setAllergenId(productAllergen.getAllergenId());
                }
                if (productAllergen.getAllergenGroup() != null) {
                    existingProductAllergen.setAllergenGroup(productAllergen.getAllergenGroup());
                }
                if (productAllergen.getAllergenName() != null) {
                    existingProductAllergen.setAllergenName(productAllergen.getAllergenName());
                }
                if (productAllergen.getDescription() != null) {
                    existingProductAllergen.setDescription(productAllergen.getDescription());
                }
                if (productAllergen.getgTIN() != null) {
                    existingProductAllergen.setgTIN(productAllergen.getgTIN());
                }
                if (productAllergen.getgTINUPC() != null) {
                    existingProductAllergen.setgTINUPC(productAllergen.getgTINUPC());
                }
                if (productAllergen.getProductAllergenId() != null) {
                    existingProductAllergen.setProductAllergenId(productAllergen.getProductAllergenId());
                }
                if (productAllergen.getProductId() != null) {
                    existingProductAllergen.setProductId(productAllergen.getProductId());
                }
                if (productAllergen.getuPC() != null) {
                    existingProductAllergen.setuPC(productAllergen.getuPC());
                }

                return existingProductAllergen;
            })
            .map(productAllergenRepository::save)
            .map(savedProductAllergen -> {
                productAllergenSearchRepository.index(savedProductAllergen);
                return savedProductAllergen;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAllergen.getId().toString())
        );
    }

    /**
     * {@code GET  /product-allergens} : get all the productAllergens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productAllergens in body.
     */
    @GetMapping("")
    public List<ProductAllergen> getAllProductAllergens() {
        LOG.debug("REST request to get all ProductAllergens");
        return productAllergenRepository.findAll();
    }

    /**
     * {@code GET  /product-allergens/:id} : get the "id" productAllergen.
     *
     * @param id the id of the productAllergen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAllergen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAllergen> getProductAllergen(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductAllergen : {}", id);
        Optional<ProductAllergen> productAllergen = productAllergenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productAllergen);
    }

    /**
     * {@code DELETE  /product-allergens/:id} : delete the "id" productAllergen.
     *
     * @param id the id of the productAllergen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAllergen(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductAllergen : {}", id);
        productAllergenRepository.deleteById(id);
        productAllergenSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-allergens/_search?query=:query} : search for the productAllergen corresponding
     * to the query.
     *
     * @param query the query of the productAllergen search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductAllergen> searchProductAllergens(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductAllergens for query {}", query);
        try {
            return StreamSupport.stream(productAllergenSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
