package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductImage;
import com.mycompany.myapp.repository.ProductImageRepository;
import com.mycompany.myapp.repository.search.ProductImageSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductImage}.
 */
@RestController
@RequestMapping("/api/product-images")
@Transactional
public class ProductImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductImageResource.class);

    private static final String ENTITY_NAME = "productImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductImageRepository productImageRepository;

    private final ProductImageSearchRepository productImageSearchRepository;

    public ProductImageResource(ProductImageRepository productImageRepository, ProductImageSearchRepository productImageSearchRepository) {
        this.productImageRepository = productImageRepository;
        this.productImageSearchRepository = productImageSearchRepository;
    }

    /**
     * {@code POST  /product-images} : Create a new productImage.
     *
     * @param productImage the productImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productImage, or with status {@code 400 (Bad Request)} if the productImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductImage> createProductImage(@RequestBody ProductImage productImage) throws URISyntaxException {
        LOG.debug("REST request to save ProductImage : {}", productImage);
        if (productImage.getId() != null) {
            throw new BadRequestAlertException("A new productImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productImage = productImageRepository.save(productImage);
        productImageSearchRepository.index(productImage);
        return ResponseEntity.created(new URI("/api/product-images/" + productImage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productImage.getId().toString()))
            .body(productImage);
    }

    /**
     * {@code PUT  /product-images/:id} : Updates an existing productImage.
     *
     * @param id the id of the productImage to save.
     * @param productImage the productImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImage,
     * or with status {@code 400 (Bad Request)} if the productImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductImage> updateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImage productImage
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductImage : {}, {}", id, productImage);
        if (productImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productImage = productImageRepository.save(productImage);
        productImageSearchRepository.index(productImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImage.getId().toString()))
            .body(productImage);
    }

    /**
     * {@code PATCH  /product-images/:id} : Partial updates given fields of an existing productImage, field will ignore if it is null
     *
     * @param id the id of the productImage to save.
     * @param productImage the productImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImage,
     * or with status {@code 400 (Bad Request)} if the productImage is not valid,
     * or with status {@code 404 (Not Found)} if the productImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the productImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductImage> partialUpdateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductImage productImage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductImage partially : {}, {}", id, productImage);
        if (productImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductImage> result = productImageRepository
            .findById(productImage.getId())
            .map(existingProductImage -> {
                if (productImage.getCreatedBy() != null) {
                    existingProductImage.setCreatedBy(productImage.getCreatedBy());
                }
                if (productImage.getCreatedOn() != null) {
                    existingProductImage.setCreatedOn(productImage.getCreatedOn());
                }
                if (productImage.getImageURL() != null) {
                    existingProductImage.setImageURL(productImage.getImageURL());
                }
                if (productImage.getIsActive() != null) {
                    existingProductImage.setIsActive(productImage.getIsActive());
                }
                if (productImage.getProductId() != null) {
                    existingProductImage.setProductId(productImage.getProductId());
                }
                if (productImage.getProductImageId() != null) {
                    existingProductImage.setProductImageId(productImage.getProductImageId());
                }

                return existingProductImage;
            })
            .map(productImageRepository::save)
            .map(savedProductImage -> {
                productImageSearchRepository.index(savedProductImage);
                return savedProductImage;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productImage.getId().toString())
        );
    }

    /**
     * {@code GET  /product-images} : get all the productImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productImages in body.
     */
    @GetMapping("")
    public List<ProductImage> getAllProductImages() {
        LOG.debug("REST request to get all ProductImages");
        return productImageRepository.findAll();
    }

    /**
     * {@code GET  /product-images/:id} : get the "id" productImage.
     *
     * @param id the id of the productImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductImage> getProductImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductImage : {}", id);
        Optional<ProductImage> productImage = productImageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productImage);
    }

    /**
     * {@code DELETE  /product-images/:id} : delete the "id" productImage.
     *
     * @param id the id of the productImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductImage : {}", id);
        productImageRepository.deleteById(id);
        productImageSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-images/_search?query=:query} : search for the productImage corresponding
     * to the query.
     *
     * @param query the query of the productImage search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductImage> searchProductImages(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductImages for query {}", query);
        try {
            return StreamSupport.stream(productImageSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
