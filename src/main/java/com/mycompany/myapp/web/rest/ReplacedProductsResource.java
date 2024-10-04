package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ReplacedProducts;
import com.mycompany.myapp.repository.ReplacedProductsRepository;
import com.mycompany.myapp.repository.search.ReplacedProductsSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReplacedProducts}.
 */
@RestController
@RequestMapping("/api/replaced-products")
@Transactional
public class ReplacedProductsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReplacedProductsResource.class);

    private static final String ENTITY_NAME = "replacedProducts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReplacedProductsRepository replacedProductsRepository;

    private final ReplacedProductsSearchRepository replacedProductsSearchRepository;

    public ReplacedProductsResource(
        ReplacedProductsRepository replacedProductsRepository,
        ReplacedProductsSearchRepository replacedProductsSearchRepository
    ) {
        this.replacedProductsRepository = replacedProductsRepository;
        this.replacedProductsSearchRepository = replacedProductsSearchRepository;
    }

    /**
     * {@code POST  /replaced-products} : Create a new replacedProducts.
     *
     * @param replacedProducts the replacedProducts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new replacedProducts, or with status {@code 400 (Bad Request)} if the replacedProducts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReplacedProducts> createReplacedProducts(@RequestBody ReplacedProducts replacedProducts)
        throws URISyntaxException {
        LOG.debug("REST request to save ReplacedProducts : {}", replacedProducts);
        if (replacedProducts.getId() != null) {
            throw new BadRequestAlertException("A new replacedProducts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        replacedProducts = replacedProductsRepository.save(replacedProducts);
        replacedProductsSearchRepository.index(replacedProducts);
        return ResponseEntity.created(new URI("/api/replaced-products/" + replacedProducts.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, replacedProducts.getId().toString()))
            .body(replacedProducts);
    }

    /**
     * {@code PUT  /replaced-products/:id} : Updates an existing replacedProducts.
     *
     * @param id the id of the replacedProducts to save.
     * @param replacedProducts the replacedProducts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated replacedProducts,
     * or with status {@code 400 (Bad Request)} if the replacedProducts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the replacedProducts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReplacedProducts> updateReplacedProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReplacedProducts replacedProducts
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReplacedProducts : {}, {}", id, replacedProducts);
        if (replacedProducts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, replacedProducts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!replacedProductsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        replacedProducts = replacedProductsRepository.save(replacedProducts);
        replacedProductsSearchRepository.index(replacedProducts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, replacedProducts.getId().toString()))
            .body(replacedProducts);
    }

    /**
     * {@code PATCH  /replaced-products/:id} : Partial updates given fields of an existing replacedProducts, field will ignore if it is null
     *
     * @param id the id of the replacedProducts to save.
     * @param replacedProducts the replacedProducts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated replacedProducts,
     * or with status {@code 400 (Bad Request)} if the replacedProducts is not valid,
     * or with status {@code 404 (Not Found)} if the replacedProducts is not found,
     * or with status {@code 500 (Internal Server Error)} if the replacedProducts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReplacedProducts> partialUpdateReplacedProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReplacedProducts replacedProducts
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReplacedProducts partially : {}, {}", id, replacedProducts);
        if (replacedProducts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, replacedProducts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!replacedProductsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReplacedProducts> result = replacedProductsRepository
            .findById(replacedProducts.getId())
            .map(existingReplacedProducts -> {
                if (replacedProducts.getIsActive() != null) {
                    existingReplacedProducts.setIsActive(replacedProducts.getIsActive());
                }
                if (replacedProducts.getProductId() != null) {
                    existingReplacedProducts.setProductId(replacedProducts.getProductId());
                }
                if (replacedProducts.getReplacedByUserId() != null) {
                    existingReplacedProducts.setReplacedByUserId(replacedProducts.getReplacedByUserId());
                }
                if (replacedProducts.getReplacedId() != null) {
                    existingReplacedProducts.setReplacedId(replacedProducts.getReplacedId());
                }
                if (replacedProducts.getReplacedProductId() != null) {
                    existingReplacedProducts.setReplacedProductId(replacedProducts.getReplacedProductId());
                }
                if (replacedProducts.getReplacementDate() != null) {
                    existingReplacedProducts.setReplacementDate(replacedProducts.getReplacementDate());
                }
                if (replacedProducts.getSchoolDistrictId() != null) {
                    existingReplacedProducts.setSchoolDistrictId(replacedProducts.getSchoolDistrictId());
                }

                return existingReplacedProducts;
            })
            .map(replacedProductsRepository::save)
            .map(savedReplacedProducts -> {
                replacedProductsSearchRepository.index(savedReplacedProducts);
                return savedReplacedProducts;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, replacedProducts.getId().toString())
        );
    }

    /**
     * {@code GET  /replaced-products} : get all the replacedProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of replacedProducts in body.
     */
    @GetMapping("")
    public List<ReplacedProducts> getAllReplacedProducts() {
        LOG.debug("REST request to get all ReplacedProducts");
        return replacedProductsRepository.findAll();
    }

    /**
     * {@code GET  /replaced-products/:id} : get the "id" replacedProducts.
     *
     * @param id the id of the replacedProducts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the replacedProducts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReplacedProducts> getReplacedProducts(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReplacedProducts : {}", id);
        Optional<ReplacedProducts> replacedProducts = replacedProductsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(replacedProducts);
    }

    /**
     * {@code DELETE  /replaced-products/:id} : delete the "id" replacedProducts.
     *
     * @param id the id of the replacedProducts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReplacedProducts(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReplacedProducts : {}", id);
        replacedProductsRepository.deleteById(id);
        replacedProductsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /replaced-products/_search?query=:query} : search for the replacedProducts corresponding
     * to the query.
     *
     * @param query the query of the replacedProducts search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ReplacedProducts> searchReplacedProducts(@RequestParam("query") String query) {
        LOG.debug("REST request to search ReplacedProducts for query {}", query);
        try {
            return StreamSupport.stream(replacedProductsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
