package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.USDAUpdateMst;
import com.mycompany.myapp.repository.USDAUpdateMstRepository;
import com.mycompany.myapp.repository.search.USDAUpdateMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.USDAUpdateMst}.
 */
@RestController
@RequestMapping("/api/usda-update-msts")
@Transactional
public class USDAUpdateMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(USDAUpdateMstResource.class);

    private static final String ENTITY_NAME = "uSDAUpdateMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final USDAUpdateMstRepository uSDAUpdateMstRepository;

    private final USDAUpdateMstSearchRepository uSDAUpdateMstSearchRepository;

    public USDAUpdateMstResource(
        USDAUpdateMstRepository uSDAUpdateMstRepository,
        USDAUpdateMstSearchRepository uSDAUpdateMstSearchRepository
    ) {
        this.uSDAUpdateMstRepository = uSDAUpdateMstRepository;
        this.uSDAUpdateMstSearchRepository = uSDAUpdateMstSearchRepository;
    }

    /**
     * {@code POST  /usda-update-msts} : Create a new uSDAUpdateMst.
     *
     * @param uSDAUpdateMst the uSDAUpdateMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uSDAUpdateMst, or with status {@code 400 (Bad Request)} if the uSDAUpdateMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<USDAUpdateMst> createUSDAUpdateMst(@RequestBody USDAUpdateMst uSDAUpdateMst) throws URISyntaxException {
        LOG.debug("REST request to save USDAUpdateMst : {}", uSDAUpdateMst);
        if (uSDAUpdateMst.getId() != null) {
            throw new BadRequestAlertException("A new uSDAUpdateMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uSDAUpdateMst = uSDAUpdateMstRepository.save(uSDAUpdateMst);
        uSDAUpdateMstSearchRepository.index(uSDAUpdateMst);
        return ResponseEntity.created(new URI("/api/usda-update-msts/" + uSDAUpdateMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uSDAUpdateMst.getId().toString()))
            .body(uSDAUpdateMst);
    }

    /**
     * {@code PUT  /usda-update-msts/:id} : Updates an existing uSDAUpdateMst.
     *
     * @param id the id of the uSDAUpdateMst to save.
     * @param uSDAUpdateMst the uSDAUpdateMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uSDAUpdateMst,
     * or with status {@code 400 (Bad Request)} if the uSDAUpdateMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uSDAUpdateMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<USDAUpdateMst> updateUSDAUpdateMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody USDAUpdateMst uSDAUpdateMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update USDAUpdateMst : {}, {}", id, uSDAUpdateMst);
        if (uSDAUpdateMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uSDAUpdateMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uSDAUpdateMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        uSDAUpdateMst = uSDAUpdateMstRepository.save(uSDAUpdateMst);
        uSDAUpdateMstSearchRepository.index(uSDAUpdateMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uSDAUpdateMst.getId().toString()))
            .body(uSDAUpdateMst);
    }

    /**
     * {@code PATCH  /usda-update-msts/:id} : Partial updates given fields of an existing uSDAUpdateMst, field will ignore if it is null
     *
     * @param id the id of the uSDAUpdateMst to save.
     * @param uSDAUpdateMst the uSDAUpdateMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uSDAUpdateMst,
     * or with status {@code 400 (Bad Request)} if the uSDAUpdateMst is not valid,
     * or with status {@code 404 (Not Found)} if the uSDAUpdateMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the uSDAUpdateMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<USDAUpdateMst> partialUpdateUSDAUpdateMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody USDAUpdateMst uSDAUpdateMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update USDAUpdateMst partially : {}, {}", id, uSDAUpdateMst);
        if (uSDAUpdateMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uSDAUpdateMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uSDAUpdateMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<USDAUpdateMst> result = uSDAUpdateMstRepository
            .findById(uSDAUpdateMst.getId())
            .map(existingUSDAUpdateMst -> {
                if (uSDAUpdateMst.getAddedSugarsgperServing() != null) {
                    existingUSDAUpdateMst.setAddedSugarsgperServing(uSDAUpdateMst.getAddedSugarsgperServing());
                }
                if (uSDAUpdateMst.getAllAllergens() != null) {
                    existingUSDAUpdateMst.setAllAllergens(uSDAUpdateMst.getAllAllergens());
                }
                if (uSDAUpdateMst.getBrandName() != null) {
                    existingUSDAUpdateMst.setBrandName(uSDAUpdateMst.getBrandName());
                }
                if (uSDAUpdateMst.getCalciumCamgperServing() != null) {
                    existingUSDAUpdateMst.setCalciumCamgperServing(uSDAUpdateMst.getCalciumCamgperServing());
                }
                if (uSDAUpdateMst.getCalorieskcalperServing() != null) {
                    existingUSDAUpdateMst.setCalorieskcalperServing(uSDAUpdateMst.getCalorieskcalperServing());
                }
                if (uSDAUpdateMst.getCholesterolmgperServing() != null) {
                    existingUSDAUpdateMst.setCholesterolmgperServing(uSDAUpdateMst.getCholesterolmgperServing());
                }
                if (uSDAUpdateMst.getcNCredited() != null) {
                    existingUSDAUpdateMst.setcNCredited(uSDAUpdateMst.getcNCredited());
                }
                if (uSDAUpdateMst.getcNCrediting() != null) {
                    existingUSDAUpdateMst.setcNCrediting(uSDAUpdateMst.getcNCrediting());
                }
                if (uSDAUpdateMst.getcNExpirationDate() != null) {
                    existingUSDAUpdateMst.setcNExpirationDate(uSDAUpdateMst.getcNExpirationDate());
                }
                if (uSDAUpdateMst.getcNLabelDocument() != null) {
                    existingUSDAUpdateMst.setcNLabelDocument(uSDAUpdateMst.getcNLabelDocument());
                }
                if (uSDAUpdateMst.getcNLabelStatement() != null) {
                    existingUSDAUpdateMst.setcNLabelStatement(uSDAUpdateMst.getcNLabelStatement());
                }
                if (uSDAUpdateMst.getcNProductIdentification() != null) {
                    existingUSDAUpdateMst.setcNProductIdentification(uSDAUpdateMst.getcNProductIdentification());
                }
                if (uSDAUpdateMst.getcNQualified() != null) {
                    existingUSDAUpdateMst.setcNQualified(uSDAUpdateMst.getcNQualified());
                }
                if (uSDAUpdateMst.getcNQualifierCode() != null) {
                    existingUSDAUpdateMst.setcNQualifierCode(uSDAUpdateMst.getcNQualifierCode());
                }
                if (uSDAUpdateMst.getDietaryFibergperServing() != null) {
                    existingUSDAUpdateMst.setDietaryFibergperServing(uSDAUpdateMst.getDietaryFibergperServing());
                }
                if (uSDAUpdateMst.getDownloaded() != null) {
                    existingUSDAUpdateMst.setDownloaded(uSDAUpdateMst.getDownloaded());
                }
                if (uSDAUpdateMst.getEggs() != null) {
                    existingUSDAUpdateMst.setEggs(uSDAUpdateMst.getEggs());
                }
                if (uSDAUpdateMst.getFish() != null) {
                    existingUSDAUpdateMst.setFish(uSDAUpdateMst.getFish());
                }
                if (uSDAUpdateMst.getFoodCategory() != null) {
                    existingUSDAUpdateMst.setFoodCategory(uSDAUpdateMst.getFoodCategory());
                }
                if (uSDAUpdateMst.getFunctionalname() != null) {
                    existingUSDAUpdateMst.setFunctionalname(uSDAUpdateMst.getFunctionalname());
                }
                if (uSDAUpdateMst.getGtin() != null) {
                    existingUSDAUpdateMst.setGtin(uSDAUpdateMst.getGtin());
                }
                if (uSDAUpdateMst.getHalal() != null) {
                    existingUSDAUpdateMst.setHalal(uSDAUpdateMst.getHalal());
                }
                if (uSDAUpdateMst.getHierarchicalPlacement() != null) {
                    existingUSDAUpdateMst.setHierarchicalPlacement(uSDAUpdateMst.getHierarchicalPlacement());
                }
                if (uSDAUpdateMst.getInformationProvider() != null) {
                    existingUSDAUpdateMst.setInformationProvider(uSDAUpdateMst.getInformationProvider());
                }
                if (uSDAUpdateMst.getIngredientsenglish() != null) {
                    existingUSDAUpdateMst.setIngredientsenglish(uSDAUpdateMst.getIngredientsenglish());
                }
                if (uSDAUpdateMst.getIronFemgperServing() != null) {
                    existingUSDAUpdateMst.setIronFemgperServing(uSDAUpdateMst.getIronFemgperServing());
                }
                if (uSDAUpdateMst.getKosher() != null) {
                    existingUSDAUpdateMst.setKosher(uSDAUpdateMst.getKosher());
                }
                if (uSDAUpdateMst.getLastupdated() != null) {
                    existingUSDAUpdateMst.setLastupdated(uSDAUpdateMst.getLastupdated());
                }
                if (uSDAUpdateMst.getLongdescription() != null) {
                    existingUSDAUpdateMst.setLongdescription(uSDAUpdateMst.getLongdescription());
                }
                if (uSDAUpdateMst.getMilk() != null) {
                    existingUSDAUpdateMst.setMilk(uSDAUpdateMst.getMilk());
                }
                if (uSDAUpdateMst.getNutrientFormatTypeCodeReferenceCode() != null) {
                    existingUSDAUpdateMst.setNutrientFormatTypeCodeReferenceCode(uSDAUpdateMst.getNutrientFormatTypeCodeReferenceCode());
                }
                if (uSDAUpdateMst.getNutrientQuantityBasisTypeCode() != null) {
                    existingUSDAUpdateMst.setNutrientQuantityBasisTypeCode(uSDAUpdateMst.getNutrientQuantityBasisTypeCode());
                }
                if (uSDAUpdateMst.getNutrientQuantityBasisUnitofMeasure() != null) {
                    existingUSDAUpdateMst.setNutrientQuantityBasisUnitofMeasure(uSDAUpdateMst.getNutrientQuantityBasisUnitofMeasure());
                }
                if (uSDAUpdateMst.getNutrientQuantityBasisValue() != null) {
                    existingUSDAUpdateMst.setNutrientQuantityBasisValue(uSDAUpdateMst.getNutrientQuantityBasisValue());
                }
                if (uSDAUpdateMst.getNutrientsperservingcalculatedfrombymeasurereportedamount() != null) {
                    existingUSDAUpdateMst.setNutrientsperservingcalculatedfrombymeasurereportedamount(
                        uSDAUpdateMst.getNutrientsperservingcalculatedfrombymeasurereportedamount()
                    );
                }
                if (uSDAUpdateMst.getPeanuts() != null) {
                    existingUSDAUpdateMst.setPeanuts(uSDAUpdateMst.getPeanuts());
                }
                if (uSDAUpdateMst.getpFSCreditableIngredientTypeCode() != null) {
                    existingUSDAUpdateMst.setpFSCreditableIngredientTypeCode(uSDAUpdateMst.getpFSCreditableIngredientTypeCode());
                }
                if (uSDAUpdateMst.getpFSDocument() != null) {
                    existingUSDAUpdateMst.setpFSDocument(uSDAUpdateMst.getpFSDocument());
                }
                if (uSDAUpdateMst.getpFSTotalCreditableIngredientAmount() != null) {
                    existingUSDAUpdateMst.setpFSTotalCreditableIngredientAmount(uSDAUpdateMst.getpFSTotalCreditableIngredientAmount());
                }
                if (uSDAUpdateMst.getpFSTotalPortionWeight() != null) {
                    existingUSDAUpdateMst.setpFSTotalPortionWeight(uSDAUpdateMst.getpFSTotalPortionWeight());
                }
                if (uSDAUpdateMst.getPotassiumKmgperServing() != null) {
                    existingUSDAUpdateMst.setPotassiumKmgperServing(uSDAUpdateMst.getPotassiumKmgperServing());
                }
                if (uSDAUpdateMst.getPreparationStateCode() != null) {
                    existingUSDAUpdateMst.setPreparationStateCode(uSDAUpdateMst.getPreparationStateCode());
                }
                if (uSDAUpdateMst.getProductFormulationStatement() != null) {
                    existingUSDAUpdateMst.setProductFormulationStatement(uSDAUpdateMst.getProductFormulationStatement());
                }
                if (uSDAUpdateMst.getProteingperServing() != null) {
                    existingUSDAUpdateMst.setProteingperServing(uSDAUpdateMst.getProteingperServing());
                }
                if (uSDAUpdateMst.getSaturatedFatgperServing() != null) {
                    existingUSDAUpdateMst.setSaturatedFatgperServing(uSDAUpdateMst.getSaturatedFatgperServing());
                }
                if (uSDAUpdateMst.getServingDescription() != null) {
                    existingUSDAUpdateMst.setServingDescription(uSDAUpdateMst.getServingDescription());
                }
                if (uSDAUpdateMst.getServingSize() != null) {
                    existingUSDAUpdateMst.setServingSize(uSDAUpdateMst.getServingSize());
                }
                if (uSDAUpdateMst.getServingUnitofMeasure() != null) {
                    existingUSDAUpdateMst.setServingUnitofMeasure(uSDAUpdateMst.getServingUnitofMeasure());
                }
                if (uSDAUpdateMst.getServingsPerCase() != null) {
                    existingUSDAUpdateMst.setServingsPerCase(uSDAUpdateMst.getServingsPerCase());
                }
                if (uSDAUpdateMst.getSesame() != null) {
                    existingUSDAUpdateMst.setSesame(uSDAUpdateMst.getSesame());
                }
                if (uSDAUpdateMst.getShellfish() != null) {
                    existingUSDAUpdateMst.setShellfish(uSDAUpdateMst.getShellfish());
                }
                if (uSDAUpdateMst.getShortdescription() != null) {
                    existingUSDAUpdateMst.setShortdescription(uSDAUpdateMst.getShortdescription());
                }
                if (uSDAUpdateMst.getSodiummgperServing() != null) {
                    existingUSDAUpdateMst.setSodiummgperServing(uSDAUpdateMst.getSodiummgperServing());
                }
                if (uSDAUpdateMst.getSoybeans() != null) {
                    existingUSDAUpdateMst.setSoybeans(uSDAUpdateMst.getSoybeans());
                }
                if (uSDAUpdateMst.getSugarsgperServing() != null) {
                    existingUSDAUpdateMst.setSugarsgperServing(uSDAUpdateMst.getSugarsgperServing());
                }
                if (uSDAUpdateMst.getTotalCarbohydrategperServing() != null) {
                    existingUSDAUpdateMst.setTotalCarbohydrategperServing(uSDAUpdateMst.getTotalCarbohydrategperServing());
                }
                if (uSDAUpdateMst.getTotalFatgperServing() != null) {
                    existingUSDAUpdateMst.setTotalFatgperServing(uSDAUpdateMst.getTotalFatgperServing());
                }
                if (uSDAUpdateMst.getTradeChannels() != null) {
                    existingUSDAUpdateMst.setTradeChannels(uSDAUpdateMst.getTradeChannels());
                }
                if (uSDAUpdateMst.getTransFatgperServing() != null) {
                    existingUSDAUpdateMst.setTransFatgperServing(uSDAUpdateMst.getTransFatgperServing());
                }
                if (uSDAUpdateMst.getTreeNuts() != null) {
                    existingUSDAUpdateMst.setTreeNuts(uSDAUpdateMst.getTreeNuts());
                }
                if (uSDAUpdateMst.getuSDAFoodsMaterialCode() != null) {
                    existingUSDAUpdateMst.setuSDAFoodsMaterialCode(uSDAUpdateMst.getuSDAFoodsMaterialCode());
                }
                if (uSDAUpdateMst.getuSDAFoodsProductsDescription() != null) {
                    existingUSDAUpdateMst.setuSDAFoodsProductsDescription(uSDAUpdateMst.getuSDAFoodsProductsDescription());
                }
                if (uSDAUpdateMst.getVendorName() != null) {
                    existingUSDAUpdateMst.setVendorName(uSDAUpdateMst.getVendorName());
                }
                if (uSDAUpdateMst.getVendorID() != null) {
                    existingUSDAUpdateMst.setVendorID(uSDAUpdateMst.getVendorID());
                }
                if (uSDAUpdateMst.getVitaminDmcgperServing() != null) {
                    existingUSDAUpdateMst.setVitaminDmcgperServing(uSDAUpdateMst.getVitaminDmcgperServing());
                }
                if (uSDAUpdateMst.getWheat() != null) {
                    existingUSDAUpdateMst.setWheat(uSDAUpdateMst.getWheat());
                }

                return existingUSDAUpdateMst;
            })
            .map(uSDAUpdateMstRepository::save)
            .map(savedUSDAUpdateMst -> {
                uSDAUpdateMstSearchRepository.index(savedUSDAUpdateMst);
                return savedUSDAUpdateMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uSDAUpdateMst.getId().toString())
        );
    }

    /**
     * {@code GET  /usda-update-msts} : get all the uSDAUpdateMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uSDAUpdateMsts in body.
     */
    @GetMapping("")
    public List<USDAUpdateMst> getAllUSDAUpdateMsts() {
        LOG.debug("REST request to get all USDAUpdateMsts");
        return uSDAUpdateMstRepository.findAll();
    }

    /**
     * {@code GET  /usda-update-msts/:id} : get the "id" uSDAUpdateMst.
     *
     * @param id the id of the uSDAUpdateMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uSDAUpdateMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<USDAUpdateMst> getUSDAUpdateMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get USDAUpdateMst : {}", id);
        Optional<USDAUpdateMst> uSDAUpdateMst = uSDAUpdateMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uSDAUpdateMst);
    }

    /**
     * {@code DELETE  /usda-update-msts/:id} : delete the "id" uSDAUpdateMst.
     *
     * @param id the id of the uSDAUpdateMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUSDAUpdateMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete USDAUpdateMst : {}", id);
        uSDAUpdateMstRepository.deleteById(id);
        uSDAUpdateMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /usda-update-msts/_search?query=:query} : search for the uSDAUpdateMst corresponding
     * to the query.
     *
     * @param query the query of the uSDAUpdateMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<USDAUpdateMst> searchUSDAUpdateMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search USDAUpdateMsts for query {}", query);
        try {
            return StreamSupport.stream(uSDAUpdateMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
