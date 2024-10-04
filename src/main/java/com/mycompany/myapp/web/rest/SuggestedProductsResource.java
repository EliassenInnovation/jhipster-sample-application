package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SuggestedProducts;
import com.mycompany.myapp.repository.SuggestedProductsRepository;
import com.mycompany.myapp.repository.search.SuggestedProductsSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SuggestedProducts}.
 */
@RestController
@RequestMapping("/api/suggested-products")
@Transactional
public class SuggestedProductsResource {

    private static final Logger LOG = LoggerFactory.getLogger(SuggestedProductsResource.class);

    private static final String ENTITY_NAME = "suggestedProducts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuggestedProductsRepository suggestedProductsRepository;

    private final SuggestedProductsSearchRepository suggestedProductsSearchRepository;

    public SuggestedProductsResource(
        SuggestedProductsRepository suggestedProductsRepository,
        SuggestedProductsSearchRepository suggestedProductsSearchRepository
    ) {
        this.suggestedProductsRepository = suggestedProductsRepository;
        this.suggestedProductsSearchRepository = suggestedProductsSearchRepository;
    }

    /**
     * {@code POST  /suggested-products} : Create a new suggestedProducts.
     *
     * @param suggestedProducts the suggestedProducts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suggestedProducts, or with status {@code 400 (Bad Request)} if the suggestedProducts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SuggestedProducts> createSuggestedProducts(@RequestBody SuggestedProducts suggestedProducts)
        throws URISyntaxException {
        LOG.debug("REST request to save SuggestedProducts : {}", suggestedProducts);
        if (suggestedProducts.getId() != null) {
            throw new BadRequestAlertException("A new suggestedProducts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        suggestedProducts = suggestedProductsRepository.save(suggestedProducts);
        suggestedProductsSearchRepository.index(suggestedProducts);
        return ResponseEntity.created(new URI("/api/suggested-products/" + suggestedProducts.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, suggestedProducts.getId().toString()))
            .body(suggestedProducts);
    }

    /**
     * {@code PUT  /suggested-products/:id} : Updates an existing suggestedProducts.
     *
     * @param id the id of the suggestedProducts to save.
     * @param suggestedProducts the suggestedProducts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suggestedProducts,
     * or with status {@code 400 (Bad Request)} if the suggestedProducts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suggestedProducts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuggestedProducts> updateSuggestedProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SuggestedProducts suggestedProducts
    ) throws URISyntaxException {
        LOG.debug("REST request to update SuggestedProducts : {}, {}", id, suggestedProducts);
        if (suggestedProducts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suggestedProducts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suggestedProductsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        suggestedProducts = suggestedProductsRepository.save(suggestedProducts);
        suggestedProductsSearchRepository.index(suggestedProducts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suggestedProducts.getId().toString()))
            .body(suggestedProducts);
    }

    /**
     * {@code PATCH  /suggested-products/:id} : Partial updates given fields of an existing suggestedProducts, field will ignore if it is null
     *
     * @param id the id of the suggestedProducts to save.
     * @param suggestedProducts the suggestedProducts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suggestedProducts,
     * or with status {@code 400 (Bad Request)} if the suggestedProducts is not valid,
     * or with status {@code 404 (Not Found)} if the suggestedProducts is not found,
     * or with status {@code 500 (Internal Server Error)} if the suggestedProducts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuggestedProducts> partialUpdateSuggestedProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SuggestedProducts suggestedProducts
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SuggestedProducts partially : {}, {}", id, suggestedProducts);
        if (suggestedProducts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suggestedProducts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suggestedProductsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuggestedProducts> result = suggestedProductsRepository
            .findById(suggestedProducts.getId())
            .map(existingSuggestedProducts -> {
                if (suggestedProducts.getIsActive() != null) {
                    existingSuggestedProducts.setIsActive(suggestedProducts.getIsActive());
                }
                if (suggestedProducts.getIsApprove() != null) {
                    existingSuggestedProducts.setIsApprove(suggestedProducts.getIsApprove());
                }
                if (suggestedProducts.getProductId() != null) {
                    existingSuggestedProducts.setProductId(suggestedProducts.getProductId());
                }
                if (suggestedProducts.getSuggestedByDistrict() != null) {
                    existingSuggestedProducts.setSuggestedByDistrict(suggestedProducts.getSuggestedByDistrict());
                }
                if (suggestedProducts.getSuggestedByUserId() != null) {
                    existingSuggestedProducts.setSuggestedByUserId(suggestedProducts.getSuggestedByUserId());
                }
                if (suggestedProducts.getSuggestedProductId() != null) {
                    existingSuggestedProducts.setSuggestedProductId(suggestedProducts.getSuggestedProductId());
                }
                if (suggestedProducts.getSuggestionDate() != null) {
                    existingSuggestedProducts.setSuggestionDate(suggestedProducts.getSuggestionDate());
                }
                if (suggestedProducts.getSuggestionId() != null) {
                    existingSuggestedProducts.setSuggestionId(suggestedProducts.getSuggestionId());
                }

                return existingSuggestedProducts;
            })
            .map(suggestedProductsRepository::save)
            .map(savedSuggestedProducts -> {
                suggestedProductsSearchRepository.index(savedSuggestedProducts);
                return savedSuggestedProducts;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suggestedProducts.getId().toString())
        );
    }

    /**
     * {@code GET  /suggested-products} : get all the suggestedProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suggestedProducts in body.
     */
    @GetMapping("")
    public List<SuggestedProducts> getAllSuggestedProducts() {
        LOG.debug("REST request to get all SuggestedProducts");
        return suggestedProductsRepository.findAll();
    }

    /**
     * {@code GET  /suggested-products/:id} : get the "id" suggestedProducts.
     *
     * @param id the id of the suggestedProducts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suggestedProducts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuggestedProducts> getSuggestedProducts(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SuggestedProducts : {}", id);
        Optional<SuggestedProducts> suggestedProducts = suggestedProductsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(suggestedProducts);
    }

    /**
     * {@code DELETE  /suggested-products/:id} : delete the "id" suggestedProducts.
     *
     * @param id the id of the suggestedProducts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuggestedProducts(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SuggestedProducts : {}", id);
        suggestedProductsRepository.deleteById(id);
        suggestedProductsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /suggested-products/_search?query=:query} : search for the suggestedProducts corresponding
     * to the query.
     *
     * @param query the query of the suggestedProducts search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SuggestedProducts> searchSuggestedProducts(@RequestParam("query") String query) {
        LOG.debug("REST request to search SuggestedProducts for query {}", query);
        try {
            return StreamSupport.stream(suggestedProductsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
