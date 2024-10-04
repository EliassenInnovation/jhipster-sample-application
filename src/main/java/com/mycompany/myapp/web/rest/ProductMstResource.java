package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductMst;
import com.mycompany.myapp.repository.ProductMstRepository;
import com.mycompany.myapp.repository.search.ProductMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductMst}.
 */
@RestController
@RequestMapping("/api/product-msts")
@Transactional
public class ProductMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMstResource.class);

    private static final String ENTITY_NAME = "productMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductMstRepository productMstRepository;

    private final ProductMstSearchRepository productMstSearchRepository;

    public ProductMstResource(ProductMstRepository productMstRepository, ProductMstSearchRepository productMstSearchRepository) {
        this.productMstRepository = productMstRepository;
        this.productMstSearchRepository = productMstSearchRepository;
    }

    /**
     * {@code POST  /product-msts} : Create a new productMst.
     *
     * @param productMst the productMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productMst, or with status {@code 400 (Bad Request)} if the productMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductMst> createProductMst(@RequestBody ProductMst productMst) throws URISyntaxException {
        LOG.debug("REST request to save ProductMst : {}", productMst);
        if (productMst.getId() != null) {
            throw new BadRequestAlertException("A new productMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productMst = productMstRepository.save(productMst);
        productMstSearchRepository.index(productMst);
        return ResponseEntity.created(new URI("/api/product-msts/" + productMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productMst.getId().toString()))
            .body(productMst);
    }

    /**
     * {@code PUT  /product-msts/:id} : Updates an existing productMst.
     *
     * @param id the id of the productMst to save.
     * @param productMst the productMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMst,
     * or with status {@code 400 (Bad Request)} if the productMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductMst> updateProductMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductMst productMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductMst : {}, {}", id, productMst);
        if (productMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productMst = productMstRepository.save(productMst);
        productMstSearchRepository.index(productMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMst.getId().toString()))
            .body(productMst);
    }

    /**
     * {@code PATCH  /product-msts/:id} : Partial updates given fields of an existing productMst, field will ignore if it is null
     *
     * @param id the id of the productMst to save.
     * @param productMst the productMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMst,
     * or with status {@code 400 (Bad Request)} if the productMst is not valid,
     * or with status {@code 404 (Not Found)} if the productMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the productMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductMst> partialUpdateProductMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductMst productMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductMst partially : {}, {}", id, productMst);
        if (productMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductMst> result = productMstRepository
            .findById(productMst.getId())
            .map(existingProductMst -> {
                if (productMst.getAddedSugar() != null) {
                    existingProductMst.setAddedSugar(productMst.getAddedSugar());
                }
                if (productMst.getAddedSugarUom() != null) {
                    existingProductMst.setAddedSugarUom(productMst.getAddedSugarUom());
                }
                if (productMst.getAllergenKeywords() != null) {
                    existingProductMst.setAllergenKeywords(productMst.getAllergenKeywords());
                }
                if (productMst.getBrandName() != null) {
                    existingProductMst.setBrandName(productMst.getBrandName());
                }
                if (productMst.getCalories() != null) {
                    existingProductMst.setCalories(productMst.getCalories());
                }
                if (productMst.getCaloriesUom() != null) {
                    existingProductMst.setCaloriesUom(productMst.getCaloriesUom());
                }
                if (productMst.getCarbohydrates() != null) {
                    existingProductMst.setCarbohydrates(productMst.getCarbohydrates());
                }
                if (productMst.getCarbohydratesUom() != null) {
                    existingProductMst.setCarbohydratesUom(productMst.getCarbohydratesUom());
                }
                if (productMst.getCategoryId() != null) {
                    existingProductMst.setCategoryId(productMst.getCategoryId());
                }
                if (productMst.getCholesterol() != null) {
                    existingProductMst.setCholesterol(productMst.getCholesterol());
                }
                if (productMst.getCholesterolUOM() != null) {
                    existingProductMst.setCholesterolUOM(productMst.getCholesterolUOM());
                }
                if (productMst.getCreatedBy() != null) {
                    existingProductMst.setCreatedBy(productMst.getCreatedBy());
                }
                if (productMst.getCreatedOn() != null) {
                    existingProductMst.setCreatedOn(productMst.getCreatedOn());
                }
                if (productMst.getDescription() != null) {
                    existingProductMst.setDescription(productMst.getDescription());
                }
                if (productMst.getDietaryFiber() != null) {
                    existingProductMst.setDietaryFiber(productMst.getDietaryFiber());
                }
                if (productMst.getDietaryFiberUom() != null) {
                    existingProductMst.setDietaryFiberUom(productMst.getDietaryFiberUom());
                }
                if (productMst.getgTIN() != null) {
                    existingProductMst.setgTIN(productMst.getgTIN());
                }
                if (productMst.getIngredients() != null) {
                    existingProductMst.setIngredients(productMst.getIngredients());
                }
                if (productMst.getiOCCategoryId() != null) {
                    existingProductMst.setiOCCategoryId(productMst.getiOCCategoryId());
                }
                if (productMst.getIsActive() != null) {
                    existingProductMst.setIsActive(productMst.getIsActive());
                }
                if (productMst.getIsMerge() != null) {
                    existingProductMst.setIsMerge(productMst.getIsMerge());
                }
                if (productMst.getIsOneWorldSyncProduct() != null) {
                    existingProductMst.setIsOneWorldSyncProduct(productMst.getIsOneWorldSyncProduct());
                }
                if (productMst.getManufacturerId() != null) {
                    existingProductMst.setManufacturerId(productMst.getManufacturerId());
                }
                if (productMst.getManufacturerProductCode() != null) {
                    existingProductMst.setManufacturerProductCode(productMst.getManufacturerProductCode());
                }
                if (productMst.getManufacturerText1Ws() != null) {
                    existingProductMst.setManufacturerText1Ws(productMst.getManufacturerText1Ws());
                }
                if (productMst.getMergeDate() != null) {
                    existingProductMst.setMergeDate(productMst.getMergeDate());
                }
                if (productMst.getProductId() != null) {
                    existingProductMst.setProductId(productMst.getProductId());
                }
                if (productMst.getProductLabelPdfUrl() != null) {
                    existingProductMst.setProductLabelPdfUrl(productMst.getProductLabelPdfUrl());
                }
                if (productMst.getProductName() != null) {
                    existingProductMst.setProductName(productMst.getProductName());
                }
                if (productMst.getProtein() != null) {
                    existingProductMst.setProtein(productMst.getProtein());
                }
                if (productMst.getProteinUom() != null) {
                    existingProductMst.setProteinUom(productMst.getProteinUom());
                }
                if (productMst.getSaturatedFat() != null) {
                    existingProductMst.setSaturatedFat(productMst.getSaturatedFat());
                }
                if (productMst.getServing() != null) {
                    existingProductMst.setServing(productMst.getServing());
                }
                if (productMst.getServingUom() != null) {
                    existingProductMst.setServingUom(productMst.getServingUom());
                }
                if (productMst.getSodium() != null) {
                    existingProductMst.setSodium(productMst.getSodium());
                }
                if (productMst.getSodiumUom() != null) {
                    existingProductMst.setSodiumUom(productMst.getSodiumUom());
                }
                if (productMst.getStorageTypeId() != null) {
                    existingProductMst.setStorageTypeId(productMst.getStorageTypeId());
                }
                if (productMst.getSubCategoryId() != null) {
                    existingProductMst.setSubCategoryId(productMst.getSubCategoryId());
                }
                if (productMst.getSugar() != null) {
                    existingProductMst.setSugar(productMst.getSugar());
                }
                if (productMst.getSugarUom() != null) {
                    existingProductMst.setSugarUom(productMst.getSugarUom());
                }
                if (productMst.getTotalFat() != null) {
                    existingProductMst.setTotalFat(productMst.getTotalFat());
                }
                if (productMst.getTransFat() != null) {
                    existingProductMst.setTransFat(productMst.getTransFat());
                }
                if (productMst.getuPC() != null) {
                    existingProductMst.setuPC(productMst.getuPC());
                }
                if (productMst.getuPCGTIN() != null) {
                    existingProductMst.setuPCGTIN(productMst.getuPCGTIN());
                }
                if (productMst.getUpdatedBy() != null) {
                    existingProductMst.setUpdatedBy(productMst.getUpdatedBy());
                }
                if (productMst.getUpdatedOn() != null) {
                    existingProductMst.setUpdatedOn(productMst.getUpdatedOn());
                }
                if (productMst.getVendor() != null) {
                    existingProductMst.setVendor(productMst.getVendor());
                }

                return existingProductMst;
            })
            .map(productMstRepository::save)
            .map(savedProductMst -> {
                productMstSearchRepository.index(savedProductMst);
                return savedProductMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMst.getId().toString())
        );
    }

    /**
     * {@code GET  /product-msts} : get all the productMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productMsts in body.
     */
    @GetMapping("")
    public List<ProductMst> getAllProductMsts() {
        LOG.debug("REST request to get all ProductMsts");
        return productMstRepository.findAll();
    }

    /**
     * {@code GET  /product-msts/:id} : get the "id" productMst.
     *
     * @param id the id of the productMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductMst> getProductMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductMst : {}", id);
        Optional<ProductMst> productMst = productMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productMst);
    }

    /**
     * {@code DELETE  /product-msts/:id} : delete the "id" productMst.
     *
     * @param id the id of the productMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductMst : {}", id);
        productMstRepository.deleteById(id);
        productMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /product-msts/_search?query=:query} : search for the productMst corresponding
     * to the query.
     *
     * @param query the query of the productMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ProductMst> searchProductMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search ProductMsts for query {}", query);
        try {
            return StreamSupport.stream(productMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
