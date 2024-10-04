package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.OneWorldSyncProduct;
import com.mycompany.myapp.repository.OneWorldSyncProductRepository;
import com.mycompany.myapp.repository.search.OneWorldSyncProductSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.OneWorldSyncProduct}.
 */
@RestController
@RequestMapping("/api/one-world-sync-products")
@Transactional
public class OneWorldSyncProductResource {

    private static final Logger LOG = LoggerFactory.getLogger(OneWorldSyncProductResource.class);

    private static final String ENTITY_NAME = "oneWorldSyncProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OneWorldSyncProductRepository oneWorldSyncProductRepository;

    private final OneWorldSyncProductSearchRepository oneWorldSyncProductSearchRepository;

    public OneWorldSyncProductResource(
        OneWorldSyncProductRepository oneWorldSyncProductRepository,
        OneWorldSyncProductSearchRepository oneWorldSyncProductSearchRepository
    ) {
        this.oneWorldSyncProductRepository = oneWorldSyncProductRepository;
        this.oneWorldSyncProductSearchRepository = oneWorldSyncProductSearchRepository;
    }

    /**
     * {@code POST  /one-world-sync-products} : Create a new oneWorldSyncProduct.
     *
     * @param oneWorldSyncProduct the oneWorldSyncProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oneWorldSyncProduct, or with status {@code 400 (Bad Request)} if the oneWorldSyncProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OneWorldSyncProduct> createOneWorldSyncProduct(@RequestBody OneWorldSyncProduct oneWorldSyncProduct)
        throws URISyntaxException {
        LOG.debug("REST request to save OneWorldSyncProduct : {}", oneWorldSyncProduct);
        if (oneWorldSyncProduct.getId() != null) {
            throw new BadRequestAlertException("A new oneWorldSyncProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        oneWorldSyncProduct = oneWorldSyncProductRepository.save(oneWorldSyncProduct);
        oneWorldSyncProductSearchRepository.index(oneWorldSyncProduct);
        return ResponseEntity.created(new URI("/api/one-world-sync-products/" + oneWorldSyncProduct.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, oneWorldSyncProduct.getId().toString()))
            .body(oneWorldSyncProduct);
    }

    /**
     * {@code PUT  /one-world-sync-products/:id} : Updates an existing oneWorldSyncProduct.
     *
     * @param id the id of the oneWorldSyncProduct to save.
     * @param oneWorldSyncProduct the oneWorldSyncProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oneWorldSyncProduct,
     * or with status {@code 400 (Bad Request)} if the oneWorldSyncProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oneWorldSyncProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OneWorldSyncProduct> updateOneWorldSyncProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OneWorldSyncProduct oneWorldSyncProduct
    ) throws URISyntaxException {
        LOG.debug("REST request to update OneWorldSyncProduct : {}, {}", id, oneWorldSyncProduct);
        if (oneWorldSyncProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oneWorldSyncProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oneWorldSyncProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        oneWorldSyncProduct = oneWorldSyncProductRepository.save(oneWorldSyncProduct);
        oneWorldSyncProductSearchRepository.index(oneWorldSyncProduct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oneWorldSyncProduct.getId().toString()))
            .body(oneWorldSyncProduct);
    }

    /**
     * {@code PATCH  /one-world-sync-products/:id} : Partial updates given fields of an existing oneWorldSyncProduct, field will ignore if it is null
     *
     * @param id the id of the oneWorldSyncProduct to save.
     * @param oneWorldSyncProduct the oneWorldSyncProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oneWorldSyncProduct,
     * or with status {@code 400 (Bad Request)} if the oneWorldSyncProduct is not valid,
     * or with status {@code 404 (Not Found)} if the oneWorldSyncProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the oneWorldSyncProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OneWorldSyncProduct> partialUpdateOneWorldSyncProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OneWorldSyncProduct oneWorldSyncProduct
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OneWorldSyncProduct partially : {}, {}", id, oneWorldSyncProduct);
        if (oneWorldSyncProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oneWorldSyncProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oneWorldSyncProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OneWorldSyncProduct> result = oneWorldSyncProductRepository
            .findById(oneWorldSyncProduct.getId())
            .map(existingOneWorldSyncProduct -> {
                if (oneWorldSyncProduct.getAddedSugars() != null) {
                    existingOneWorldSyncProduct.setAddedSugars(oneWorldSyncProduct.getAddedSugars());
                }
                if (oneWorldSyncProduct.getAddedSugarUom() != null) {
                    existingOneWorldSyncProduct.setAddedSugarUom(oneWorldSyncProduct.getAddedSugarUom());
                }
                if (oneWorldSyncProduct.getAllergenKeyword() != null) {
                    existingOneWorldSyncProduct.setAllergenKeyword(oneWorldSyncProduct.getAllergenKeyword());
                }
                if (oneWorldSyncProduct.getAllergens() != null) {
                    existingOneWorldSyncProduct.setAllergens(oneWorldSyncProduct.getAllergens());
                }
                if (oneWorldSyncProduct.getBrandName() != null) {
                    existingOneWorldSyncProduct.setBrandName(oneWorldSyncProduct.getBrandName());
                }
                if (oneWorldSyncProduct.getCalories() != null) {
                    existingOneWorldSyncProduct.setCalories(oneWorldSyncProduct.getCalories());
                }
                if (oneWorldSyncProduct.getCaloriesUom() != null) {
                    existingOneWorldSyncProduct.setCaloriesUom(oneWorldSyncProduct.getCaloriesUom());
                }
                if (oneWorldSyncProduct.getCarbohydrates() != null) {
                    existingOneWorldSyncProduct.setCarbohydrates(oneWorldSyncProduct.getCarbohydrates());
                }
                if (oneWorldSyncProduct.getCarbohydratesUom() != null) {
                    existingOneWorldSyncProduct.setCarbohydratesUom(oneWorldSyncProduct.getCarbohydratesUom());
                }
                if (oneWorldSyncProduct.getCategoryName() != null) {
                    existingOneWorldSyncProduct.setCategoryName(oneWorldSyncProduct.getCategoryName());
                }
                if (oneWorldSyncProduct.getCholesterol() != null) {
                    existingOneWorldSyncProduct.setCholesterol(oneWorldSyncProduct.getCholesterol());
                }
                if (oneWorldSyncProduct.getCholesterolUOM() != null) {
                    existingOneWorldSyncProduct.setCholesterolUOM(oneWorldSyncProduct.getCholesterolUOM());
                }
                if (oneWorldSyncProduct.getCreatedOn() != null) {
                    existingOneWorldSyncProduct.setCreatedOn(oneWorldSyncProduct.getCreatedOn());
                }
                if (oneWorldSyncProduct.getDataForm() != null) {
                    existingOneWorldSyncProduct.setDataForm(oneWorldSyncProduct.getDataForm());
                }
                if (oneWorldSyncProduct.getDietaryFiber() != null) {
                    existingOneWorldSyncProduct.setDietaryFiber(oneWorldSyncProduct.getDietaryFiber());
                }
                if (oneWorldSyncProduct.getDietaryFiberUom() != null) {
                    existingOneWorldSyncProduct.setDietaryFiberUom(oneWorldSyncProduct.getDietaryFiberUom());
                }
                if (oneWorldSyncProduct.getDistributor() != null) {
                    existingOneWorldSyncProduct.setDistributor(oneWorldSyncProduct.getDistributor());
                }
                if (oneWorldSyncProduct.getDoNotConsiderProduct() != null) {
                    existingOneWorldSyncProduct.setDoNotConsiderProduct(oneWorldSyncProduct.getDoNotConsiderProduct());
                }
                if (oneWorldSyncProduct.getExtendedModel() != null) {
                    existingOneWorldSyncProduct.setExtendedModel(oneWorldSyncProduct.getExtendedModel());
                }
                if (oneWorldSyncProduct.getgLNNumber() != null) {
                    existingOneWorldSyncProduct.setgLNNumber(oneWorldSyncProduct.getgLNNumber());
                }
                if (oneWorldSyncProduct.getgTIN() != null) {
                    existingOneWorldSyncProduct.setgTIN(oneWorldSyncProduct.getgTIN());
                }
                if (oneWorldSyncProduct.geth7() != null) {
                    existingOneWorldSyncProduct.seth7(oneWorldSyncProduct.geth7());
                }
                if (oneWorldSyncProduct.getImage() != null) {
                    existingOneWorldSyncProduct.setImage(oneWorldSyncProduct.getImage());
                }
                if (oneWorldSyncProduct.getIngredients() != null) {
                    existingOneWorldSyncProduct.setIngredients(oneWorldSyncProduct.getIngredients());
                }
                if (oneWorldSyncProduct.getIsActive() != null) {
                    existingOneWorldSyncProduct.setIsActive(oneWorldSyncProduct.getIsActive());
                }
                if (oneWorldSyncProduct.getIsApprove() != null) {
                    existingOneWorldSyncProduct.setIsApprove(oneWorldSyncProduct.getIsApprove());
                }
                if (oneWorldSyncProduct.getIsMerge() != null) {
                    existingOneWorldSyncProduct.setIsMerge(oneWorldSyncProduct.getIsMerge());
                }
                if (oneWorldSyncProduct.getIsProductSync() != null) {
                    existingOneWorldSyncProduct.setIsProductSync(oneWorldSyncProduct.getIsProductSync());
                }
                if (oneWorldSyncProduct.getManufacturer() != null) {
                    existingOneWorldSyncProduct.setManufacturer(oneWorldSyncProduct.getManufacturer());
                }
                if (oneWorldSyncProduct.getManufacturerId() != null) {
                    existingOneWorldSyncProduct.setManufacturerId(oneWorldSyncProduct.getManufacturerId());
                }
                if (oneWorldSyncProduct.getManufacturerText1Ws() != null) {
                    existingOneWorldSyncProduct.setManufacturerText1Ws(oneWorldSyncProduct.getManufacturerText1Ws());
                }
                if (oneWorldSyncProduct.getModifiedOn() != null) {
                    existingOneWorldSyncProduct.setModifiedOn(oneWorldSyncProduct.getModifiedOn());
                }
                if (oneWorldSyncProduct.getProductDescription() != null) {
                    existingOneWorldSyncProduct.setProductDescription(oneWorldSyncProduct.getProductDescription());
                }
                if (oneWorldSyncProduct.getProductId() != null) {
                    existingOneWorldSyncProduct.setProductId(oneWorldSyncProduct.getProductId());
                }
                if (oneWorldSyncProduct.getProductName() != null) {
                    existingOneWorldSyncProduct.setProductName(oneWorldSyncProduct.getProductName());
                }
                if (oneWorldSyncProduct.getProtein() != null) {
                    existingOneWorldSyncProduct.setProtein(oneWorldSyncProduct.getProtein());
                }
                if (oneWorldSyncProduct.getProteinUom() != null) {
                    existingOneWorldSyncProduct.setProteinUom(oneWorldSyncProduct.getProteinUom());
                }
                if (oneWorldSyncProduct.getSaturatedFat() != null) {
                    existingOneWorldSyncProduct.setSaturatedFat(oneWorldSyncProduct.getSaturatedFat());
                }
                if (oneWorldSyncProduct.getServing() != null) {
                    existingOneWorldSyncProduct.setServing(oneWorldSyncProduct.getServing());
                }
                if (oneWorldSyncProduct.getServingUom() != null) {
                    existingOneWorldSyncProduct.setServingUom(oneWorldSyncProduct.getServingUom());
                }
                if (oneWorldSyncProduct.getSodium() != null) {
                    existingOneWorldSyncProduct.setSodium(oneWorldSyncProduct.getSodium());
                }
                if (oneWorldSyncProduct.getSodiumUom() != null) {
                    existingOneWorldSyncProduct.setSodiumUom(oneWorldSyncProduct.getSodiumUom());
                }
                if (oneWorldSyncProduct.getStorageTypeId() != null) {
                    existingOneWorldSyncProduct.setStorageTypeId(oneWorldSyncProduct.getStorageTypeId());
                }
                if (oneWorldSyncProduct.getStorageTypeName() != null) {
                    existingOneWorldSyncProduct.setStorageTypeName(oneWorldSyncProduct.getStorageTypeName());
                }
                if (oneWorldSyncProduct.getSubCategory1Name() != null) {
                    existingOneWorldSyncProduct.setSubCategory1Name(oneWorldSyncProduct.getSubCategory1Name());
                }
                if (oneWorldSyncProduct.getSubCategory2Name() != null) {
                    existingOneWorldSyncProduct.setSubCategory2Name(oneWorldSyncProduct.getSubCategory2Name());
                }
                if (oneWorldSyncProduct.getSugar() != null) {
                    existingOneWorldSyncProduct.setSugar(oneWorldSyncProduct.getSugar());
                }
                if (oneWorldSyncProduct.getSugarUom() != null) {
                    existingOneWorldSyncProduct.setSugarUom(oneWorldSyncProduct.getSugarUom());
                }
                if (oneWorldSyncProduct.getSyncEffective() != null) {
                    existingOneWorldSyncProduct.setSyncEffective(oneWorldSyncProduct.getSyncEffective());
                }
                if (oneWorldSyncProduct.getSyncHeaderLastChange() != null) {
                    existingOneWorldSyncProduct.setSyncHeaderLastChange(oneWorldSyncProduct.getSyncHeaderLastChange());
                }
                if (oneWorldSyncProduct.getSyncItemReferenceId() != null) {
                    existingOneWorldSyncProduct.setSyncItemReferenceId(oneWorldSyncProduct.getSyncItemReferenceId());
                }
                if (oneWorldSyncProduct.getSyncLastChange() != null) {
                    existingOneWorldSyncProduct.setSyncLastChange(oneWorldSyncProduct.getSyncLastChange());
                }
                if (oneWorldSyncProduct.getSyncPublication() != null) {
                    existingOneWorldSyncProduct.setSyncPublication(oneWorldSyncProduct.getSyncPublication());
                }
                if (oneWorldSyncProduct.getTotalFat() != null) {
                    existingOneWorldSyncProduct.setTotalFat(oneWorldSyncProduct.getTotalFat());
                }
                if (oneWorldSyncProduct.getTransFat() != null) {
                    existingOneWorldSyncProduct.setTransFat(oneWorldSyncProduct.getTransFat());
                }
                if (oneWorldSyncProduct.getuPC() != null) {
                    existingOneWorldSyncProduct.setuPC(oneWorldSyncProduct.getuPC());
                }
                if (oneWorldSyncProduct.getVendor() != null) {
                    existingOneWorldSyncProduct.setVendor(oneWorldSyncProduct.getVendor());
                }

                return existingOneWorldSyncProduct;
            })
            .map(oneWorldSyncProductRepository::save)
            .map(savedOneWorldSyncProduct -> {
                oneWorldSyncProductSearchRepository.index(savedOneWorldSyncProduct);
                return savedOneWorldSyncProduct;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oneWorldSyncProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /one-world-sync-products} : get all the oneWorldSyncProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oneWorldSyncProducts in body.
     */
    @GetMapping("")
    public List<OneWorldSyncProduct> getAllOneWorldSyncProducts() {
        LOG.debug("REST request to get all OneWorldSyncProducts");
        return oneWorldSyncProductRepository.findAll();
    }

    /**
     * {@code GET  /one-world-sync-products/:id} : get the "id" oneWorldSyncProduct.
     *
     * @param id the id of the oneWorldSyncProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oneWorldSyncProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OneWorldSyncProduct> getOneWorldSyncProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OneWorldSyncProduct : {}", id);
        Optional<OneWorldSyncProduct> oneWorldSyncProduct = oneWorldSyncProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(oneWorldSyncProduct);
    }

    /**
     * {@code DELETE  /one-world-sync-products/:id} : delete the "id" oneWorldSyncProduct.
     *
     * @param id the id of the oneWorldSyncProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOneWorldSyncProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OneWorldSyncProduct : {}", id);
        oneWorldSyncProductRepository.deleteById(id);
        oneWorldSyncProductSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /one-world-sync-products/_search?query=:query} : search for the oneWorldSyncProduct corresponding
     * to the query.
     *
     * @param query the query of the oneWorldSyncProduct search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<OneWorldSyncProduct> searchOneWorldSyncProducts(@RequestParam("query") String query) {
        LOG.debug("REST request to search OneWorldSyncProducts for query {}", query);
        try {
            return StreamSupport.stream(oneWorldSyncProductSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
