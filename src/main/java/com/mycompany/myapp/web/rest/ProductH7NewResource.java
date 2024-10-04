package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductH7New;
import com.mycompany.myapp.repository.ProductH7NewRepository;
import com.mycompany.myapp.repository.search.ProductH7NewSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductH7New}.
 */
@RestController
@RequestMapping("/api/product-h-7-news")
@Transactional
public class ProductH7NewResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductH7NewResource.class);

    private static final String ENTITY_NAME = "productH7New";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductH7NewRepository productH7NewRepository;

    private final ProductH7NewSearchRepository productH7NewSearchRepository;

    public ProductH7NewResource(ProductH7NewRepository productH7NewRepository, ProductH7NewSearchRepository productH7NewSearchRepository) {
        this.productH7NewRepository = productH7NewRepository;
        this.productH7NewSearchRepository = productH7NewSearchRepository;
    }

    /**
     * {@code POST  /product-h-7-news} : Create a new productH7New.
     *
     * @param productH7New the productH7New to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productH7New, or with status {@code 400 (Bad Request)} if the productH7New has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductH7New> createProductH7New(@RequestBody ProductH7New productH7New) throws URISyntaxException {
        LOG.debug("REST request to save ProductH7New : {}", productH7New);
        if (productH7New.getId() != null) {
            throw new BadRequestAlertException("A new productH7New cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productH7New = productH7NewRepository.save(productH7New);
        productH7NewSearchRepository.index(productH7New);
        return ResponseEntity.created(new URI("/api/product-h-7-news/" + productH7New.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productH7New.getId().toString()))
            .body(productH7New);
    }

    /**
     * {@code PUT  /product-h-7-news/:id} : Updates an existing productH7New.
     *
     * @param id the id of the productH7New to save.
     * @param productH7New the productH7New to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productH7New,
     * or with status {@code 400 (Bad Request)} if the productH7New is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productH7New couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductH7New> updateProductH7New(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductH7New productH7New
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductH7New : {}, {}", id, productH7New);
        if (productH7New.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productH7New.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productH7NewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productH7New = productH7NewRepository.save(productH7New);
        productH7NewSearchRepository.index(productH7New);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productH7New.getId().toString()))
            .body(productH7New);
    }

    /**
     * {@code PATCH  /product-h-7-news/:id} : Partial updates given fields of an existing productH7New, field will ignore if it is null
     *
     * @param id the id of the productH7New to save.
     * @param productH7New the productH7New to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productH7New,
     * or with status {@code 400 (Bad Request)} if the productH7New is not valid,
     * or with status {@code 404 (Not Found)} if the productH7New is not found,
     * or with status {@code 500 (Internal Server Error)} if the productH7New couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductH7New> partialUpdateProductH7New(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductH7New productH7New
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductH7New partially : {}, {}", id, productH7New);
        if (productH7New.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productH7New.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productH7NewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductH7New> result = productH7NewRepository
            .findById(productH7New.getId())
            .map(existingProductH7New -> {
                if (productH7New.getGtinUpc() != null) {
                    existingProductH7New.setGtinUpc(productH7New.getGtinUpc());
                }
                if (productH7New.geth7KeywordId() != null) {
                    existingProductH7New.seth7KeywordId(productH7New.geth7KeywordId());
                }
                if (productH7New.getiOCGroup() != null) {
                    existingProductH7New.setiOCGroup(productH7New.getiOCGroup());
                }
                if (productH7New.getProductH7Id() != null) {
                    existingProductH7New.setProductH7Id(productH7New.getProductH7Id());
                }
                if (productH7New.getProductId() != null) {
                    existingProductH7New.setProductId(productH7New.getProductId());
                }
                if (productH7New.getProductName() != null) {
                    existingProductH7New.setProductName(productH7New.getProductName());
                }

                return existingProductH7New;
            })
            .map(productH7NewRepository::save)
            .map(savedProductH7New -> {
                productH7NewSearchRepository.index(savedProductH7New);
                return savedProductH7New;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productH7New.getId().toString())
        );
    }

    /**
     * {@code GET  /product-h-7-news} : get all the productH7News.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productH7News in body.
     */
    @GetMapping("")
    public List<ProductH7New> getAllProductH7News() {
        LOG.debug("REST request to get all ProductH7News");
        return productH7NewRepository.findAll();
    }

    /**
     * {@code GET  /product-h-7-news/:id} : get the "id" productH7New.
     *
     * @param id the id of the productH7New to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productH7New, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductH7New> getProductH7New(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductH7New : {}", id);
        Optional<ProductH7New> productH7New = productH7NewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productH7New);
    }

    /**
     * {@code DELETE  /product-h-7-news/:id} : delete the "id" productH7New.
     *
     * @param id the id of the productH7New to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductH7New(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductH7New : {}", id);
        productH7NewRepository.deleteById(id);
        productH7NewSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-h-7-news/_search?query=:query} : search for the productH7New corresponding
     * to the query.
     *
     * @param query the query of the productH7New search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductH7New> searchProductH7News(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductH7News for query {}", query);
        try {
            return StreamSupport.stream(productH7NewSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
