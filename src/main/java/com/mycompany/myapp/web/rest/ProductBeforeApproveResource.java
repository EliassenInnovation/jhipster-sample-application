package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductBeforeApprove;
import com.mycompany.myapp.repository.ProductBeforeApproveRepository;
import com.mycompany.myapp.repository.search.ProductBeforeApproveSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductBeforeApprove}.
 */
@RestController
@RequestMapping("/api/product-before-approves")
@Transactional
public class ProductBeforeApproveResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductBeforeApproveResource.class);

    private static final String ENTITY_NAME = "productBeforeApprove";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductBeforeApproveRepository productBeforeApproveRepository;

    private final ProductBeforeApproveSearchRepository productBeforeApproveSearchRepository;

    public ProductBeforeApproveResource(
        ProductBeforeApproveRepository productBeforeApproveRepository,
        ProductBeforeApproveSearchRepository productBeforeApproveSearchRepository
    ) {
        this.productBeforeApproveRepository = productBeforeApproveRepository;
        this.productBeforeApproveSearchRepository = productBeforeApproveSearchRepository;
    }

    /**
     * {@code POST  /product-before-approves} : Create a new productBeforeApprove.
     *
     * @param productBeforeApprove the productBeforeApprove to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productBeforeApprove, or with status {@code 400 (Bad Request)} if the productBeforeApprove has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductBeforeApprove> createProductBeforeApprove(@RequestBody ProductBeforeApprove productBeforeApprove)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductBeforeApprove : {}", productBeforeApprove);
        if (productBeforeApprove.getId() != null) {
            throw new BadRequestAlertException("A new productBeforeApprove cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productBeforeApprove = productBeforeApproveRepository.save(productBeforeApprove);
        productBeforeApproveSearchRepository.index(productBeforeApprove);
        return ResponseEntity.created(new URI("/api/product-before-approves/" + productBeforeApprove.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productBeforeApprove.getId().toString()))
            .body(productBeforeApprove);
    }

    /**
     * {@code PUT  /product-before-approves/:id} : Updates an existing productBeforeApprove.
     *
     * @param id the id of the productBeforeApprove to save.
     * @param productBeforeApprove the productBeforeApprove to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBeforeApprove,
     * or with status {@code 400 (Bad Request)} if the productBeforeApprove is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productBeforeApprove couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductBeforeApprove> updateProductBeforeApprove(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductBeforeApprove productBeforeApprove
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductBeforeApprove : {}, {}", id, productBeforeApprove);
        if (productBeforeApprove.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productBeforeApprove.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productBeforeApproveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productBeforeApprove = productBeforeApproveRepository.save(productBeforeApprove);
        productBeforeApproveSearchRepository.index(productBeforeApprove);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBeforeApprove.getId().toString()))
            .body(productBeforeApprove);
    }

    /**
     * {@code PATCH  /product-before-approves/:id} : Partial updates given fields of an existing productBeforeApprove, field will ignore if it is null
     *
     * @param id the id of the productBeforeApprove to save.
     * @param productBeforeApprove the productBeforeApprove to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBeforeApprove,
     * or with status {@code 400 (Bad Request)} if the productBeforeApprove is not valid,
     * or with status {@code 404 (Not Found)} if the productBeforeApprove is not found,
     * or with status {@code 500 (Internal Server Error)} if the productBeforeApprove couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductBeforeApprove> partialUpdateProductBeforeApprove(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductBeforeApprove productBeforeApprove
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductBeforeApprove partially : {}, {}", id, productBeforeApprove);
        if (productBeforeApprove.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productBeforeApprove.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productBeforeApproveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductBeforeApprove> result = productBeforeApproveRepository
            .findById(productBeforeApprove.getId())
            .map(existingProductBeforeApprove -> {
                if (productBeforeApprove.getAddedSugar() != null) {
                    existingProductBeforeApprove.setAddedSugar(productBeforeApprove.getAddedSugar());
                }
                if (productBeforeApprove.getAddedSugarUom() != null) {
                    existingProductBeforeApprove.setAddedSugarUom(productBeforeApprove.getAddedSugarUom());
                }
                if (productBeforeApprove.getAllergenKeywords() != null) {
                    existingProductBeforeApprove.setAllergenKeywords(productBeforeApprove.getAllergenKeywords());
                }
                if (productBeforeApprove.getBrandName() != null) {
                    existingProductBeforeApprove.setBrandName(productBeforeApprove.getBrandName());
                }
                if (productBeforeApprove.getCalories() != null) {
                    existingProductBeforeApprove.setCalories(productBeforeApprove.getCalories());
                }
                if (productBeforeApprove.getCaloriesUom() != null) {
                    existingProductBeforeApprove.setCaloriesUom(productBeforeApprove.getCaloriesUom());
                }
                if (productBeforeApprove.getCarbohydrates() != null) {
                    existingProductBeforeApprove.setCarbohydrates(productBeforeApprove.getCarbohydrates());
                }
                if (productBeforeApprove.getCarbohydratesUom() != null) {
                    existingProductBeforeApprove.setCarbohydratesUom(productBeforeApprove.getCarbohydratesUom());
                }
                if (productBeforeApprove.getCategoryId() != null) {
                    existingProductBeforeApprove.setCategoryId(productBeforeApprove.getCategoryId());
                }
                if (productBeforeApprove.getCholesterol() != null) {
                    existingProductBeforeApprove.setCholesterol(productBeforeApprove.getCholesterol());
                }
                if (productBeforeApprove.getCholesterolUOM() != null) {
                    existingProductBeforeApprove.setCholesterolUOM(productBeforeApprove.getCholesterolUOM());
                }
                if (productBeforeApprove.getCreatedBy() != null) {
                    existingProductBeforeApprove.setCreatedBy(productBeforeApprove.getCreatedBy());
                }
                if (productBeforeApprove.getCreatedOn() != null) {
                    existingProductBeforeApprove.setCreatedOn(productBeforeApprove.getCreatedOn());
                }
                if (productBeforeApprove.getDescription() != null) {
                    existingProductBeforeApprove.setDescription(productBeforeApprove.getDescription());
                }
                if (productBeforeApprove.getDietaryFiber() != null) {
                    existingProductBeforeApprove.setDietaryFiber(productBeforeApprove.getDietaryFiber());
                }
                if (productBeforeApprove.getDietaryFiberUom() != null) {
                    existingProductBeforeApprove.setDietaryFiberUom(productBeforeApprove.getDietaryFiberUom());
                }
                if (productBeforeApprove.getDistributorId() != null) {
                    existingProductBeforeApprove.setDistributorId(productBeforeApprove.getDistributorId());
                }
                if (productBeforeApprove.getgTIN() != null) {
                    existingProductBeforeApprove.setgTIN(productBeforeApprove.getgTIN());
                }
                if (productBeforeApprove.getIngredients() != null) {
                    existingProductBeforeApprove.setIngredients(productBeforeApprove.getIngredients());
                }
                if (productBeforeApprove.getIocCategoryId() != null) {
                    existingProductBeforeApprove.setIocCategoryId(productBeforeApprove.getIocCategoryId());
                }
                if (productBeforeApprove.getIsActive() != null) {
                    existingProductBeforeApprove.setIsActive(productBeforeApprove.getIsActive());
                }
                if (productBeforeApprove.getIsMerge() != null) {
                    existingProductBeforeApprove.setIsMerge(productBeforeApprove.getIsMerge());
                }
                if (productBeforeApprove.getManufacturerId() != null) {
                    existingProductBeforeApprove.setManufacturerId(productBeforeApprove.getManufacturerId());
                }
                if (productBeforeApprove.getManufacturerProductCode() != null) {
                    existingProductBeforeApprove.setManufacturerProductCode(productBeforeApprove.getManufacturerProductCode());
                }
                if (productBeforeApprove.getMergeDate() != null) {
                    existingProductBeforeApprove.setMergeDate(productBeforeApprove.getMergeDate());
                }
                if (productBeforeApprove.getProductId() != null) {
                    existingProductBeforeApprove.setProductId(productBeforeApprove.getProductId());
                }
                if (productBeforeApprove.getProductLabelPdfUrl() != null) {
                    existingProductBeforeApprove.setProductLabelPdfUrl(productBeforeApprove.getProductLabelPdfUrl());
                }
                if (productBeforeApprove.getProductName() != null) {
                    existingProductBeforeApprove.setProductName(productBeforeApprove.getProductName());
                }
                if (productBeforeApprove.getProtein() != null) {
                    existingProductBeforeApprove.setProtein(productBeforeApprove.getProtein());
                }
                if (productBeforeApprove.getProteinUom() != null) {
                    existingProductBeforeApprove.setProteinUom(productBeforeApprove.getProteinUom());
                }
                if (productBeforeApprove.getSaturatedFat() != null) {
                    existingProductBeforeApprove.setSaturatedFat(productBeforeApprove.getSaturatedFat());
                }
                if (productBeforeApprove.getServing() != null) {
                    existingProductBeforeApprove.setServing(productBeforeApprove.getServing());
                }
                if (productBeforeApprove.getServingUom() != null) {
                    existingProductBeforeApprove.setServingUom(productBeforeApprove.getServingUom());
                }
                if (productBeforeApprove.getSodium() != null) {
                    existingProductBeforeApprove.setSodium(productBeforeApprove.getSodium());
                }
                if (productBeforeApprove.getSodiumUom() != null) {
                    existingProductBeforeApprove.setSodiumUom(productBeforeApprove.getSodiumUom());
                }
                if (productBeforeApprove.getStorageTypeId() != null) {
                    existingProductBeforeApprove.setStorageTypeId(productBeforeApprove.getStorageTypeId());
                }
                if (productBeforeApprove.getSubCategoryId() != null) {
                    existingProductBeforeApprove.setSubCategoryId(productBeforeApprove.getSubCategoryId());
                }
                if (productBeforeApprove.getSugar() != null) {
                    existingProductBeforeApprove.setSugar(productBeforeApprove.getSugar());
                }
                if (productBeforeApprove.getSugarUom() != null) {
                    existingProductBeforeApprove.setSugarUom(productBeforeApprove.getSugarUom());
                }
                if (productBeforeApprove.getTotalFat() != null) {
                    existingProductBeforeApprove.setTotalFat(productBeforeApprove.getTotalFat());
                }
                if (productBeforeApprove.getTransFat() != null) {
                    existingProductBeforeApprove.setTransFat(productBeforeApprove.getTransFat());
                }
                if (productBeforeApprove.getuPC() != null) {
                    existingProductBeforeApprove.setuPC(productBeforeApprove.getuPC());
                }
                if (productBeforeApprove.getUpdatedBy() != null) {
                    existingProductBeforeApprove.setUpdatedBy(productBeforeApprove.getUpdatedBy());
                }
                if (productBeforeApprove.getUpdatedOn() != null) {
                    existingProductBeforeApprove.setUpdatedOn(productBeforeApprove.getUpdatedOn());
                }
                if (productBeforeApprove.getVendor() != null) {
                    existingProductBeforeApprove.setVendor(productBeforeApprove.getVendor());
                }

                return existingProductBeforeApprove;
            })
            .map(productBeforeApproveRepository::save)
            .map(savedProductBeforeApprove -> {
                productBeforeApproveSearchRepository.index(savedProductBeforeApprove);
                return savedProductBeforeApprove;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBeforeApprove.getId().toString())
        );
    }

    /**
     * {@code GET  /product-before-approves} : get all the productBeforeApproves.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productBeforeApproves in body.
     */
    @GetMapping("")
    public List<ProductBeforeApprove> getAllProductBeforeApproves() {
        LOG.debug("REST request to get all ProductBeforeApproves");
        return productBeforeApproveRepository.findAll();
    }

    /**
     * {@code GET  /product-before-approves/:id} : get the "id" productBeforeApprove.
     *
     * @param id the id of the productBeforeApprove to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productBeforeApprove, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductBeforeApprove> getProductBeforeApprove(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductBeforeApprove : {}", id);
        Optional<ProductBeforeApprove> productBeforeApprove = productBeforeApproveRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productBeforeApprove);
    }

    /**
     * {@code DELETE  /product-before-approves/:id} : delete the "id" productBeforeApprove.
     *
     * @param id the id of the productBeforeApprove to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBeforeApprove(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductBeforeApprove : {}", id);
        productBeforeApproveRepository.deleteById(id);
        productBeforeApproveSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-before-approves/_search?query=:query} : search for the productBeforeApprove corresponding
     * to the query.
     *
     * @param query the query of the productBeforeApprove search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductBeforeApprove> searchProductBeforeApproves(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductBeforeApproves for query {}", query);
        try {
            return StreamSupport.stream(productBeforeApproveSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
