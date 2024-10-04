package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A USDAUpdateMst.
 */
@Entity
@Table(name = "usda_update_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "usdaupdatemst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class USDAUpdateMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "added_sugarsgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String addedSugarsgperServing;

    @Column(name = "all_allergens")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allAllergens;

    @Column(name = "brand_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String brandName;

    @Column(name = "calcium_camgper_serving")
    private Double calciumCamgperServing;

    @Column(name = "calorieskcalper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String calorieskcalperServing;

    @Column(name = "cholesterolmgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cholesterolmgperServing;

    @Column(name = "c_n_credited")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNCredited;

    @Column(name = "c_n_crediting")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNCrediting;

    @Column(name = "c_n_expiration_date")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNExpirationDate;

    @Column(name = "c_n_label_document")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNLabelDocument;

    @Lob
    @Column(name = "c_n_label_statement")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNLabelStatement;

    @Column(name = "c_n_product_identification")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNProductIdentification;

    @Column(name = "c_n_qualified")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNQualified;

    @Column(name = "c_n_qualifier_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cNQualifierCode;

    @Column(name = "dietary_fibergper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dietaryFibergperServing;

    @Column(name = "downloaded")
    private LocalDate downloaded;

    @Column(name = "eggs")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String eggs;

    @Column(name = "fish")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fish;

    @Column(name = "food_category")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String foodCategory;

    @Column(name = "functionalname")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String functionalname;

    @Column(name = "gtin")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gtin;

    @Column(name = "halal")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String halal;

    @Column(name = "hierarchical_placement")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String hierarchicalPlacement;

    @Column(name = "information_provider")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String informationProvider;

    @Lob
    @Column(name = "ingredientsenglish")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ingredientsenglish;

    @Column(name = "iron_femgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ironFemgperServing;

    @Column(name = "kosher")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String kosher;

    @Column(name = "lastupdated")
    private LocalDate lastupdated;

    @Column(name = "longdescription")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String longdescription;

    @Column(name = "milk")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String milk;

    @Column(name = "nutrient_format_type_code_reference_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nutrientFormatTypeCodeReferenceCode;

    @Column(name = "nutrient_quantity_basis_type_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nutrientQuantityBasisTypeCode;

    @Column(name = "nutrient_quantity_basis_unitof_measure")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nutrientQuantityBasisUnitofMeasure;

    @Column(name = "nutrient_quantity_basis_value")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nutrientQuantityBasisValue;

    @Column(name = "nutrientsperservingcalculatedfrombymeasurereportedamount")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean nutrientsperservingcalculatedfrombymeasurereportedamount;

    @Column(name = "peanuts")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String peanuts;

    @Column(name = "p_fs_creditable_ingredient_type_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pFSCreditableIngredientTypeCode;

    @Column(name = "p_fs_document")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pFSDocument;

    @Column(name = "p_fs_total_creditable_ingredient_amount")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pFSTotalCreditableIngredientAmount;

    @Column(name = "p_fs_total_portion_weight")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pFSTotalPortionWeight;

    @Column(name = "potassium_kmgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String potassiumKmgperServing;

    @Column(name = "preparation_state_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String preparationStateCode;

    @Column(name = "product_formulation_statement")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productFormulationStatement;

    @Column(name = "proteingper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String proteingperServing;

    @Column(name = "saturated_fatgper_serving")
    private Double saturatedFatgperServing;

    @Column(name = "serving_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String servingDescription;

    @Column(name = "serving_size")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String servingSize;

    @Column(name = "serving_unitof_measure")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String servingUnitofMeasure;

    @Column(name = "servings_per_case")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String servingsPerCase;

    @Column(name = "sesame")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sesame;

    @Column(name = "shellfish")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String shellfish;

    @Column(name = "shortdescription")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String shortdescription;

    @Column(name = "sodiummgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sodiummgperServing;

    @Column(name = "soybeans")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String soybeans;

    @Column(name = "sugarsgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sugarsgperServing;

    @Column(name = "total_carbohydrategper_serving")
    private Double totalCarbohydrategperServing;

    @Column(name = "total_fatgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String totalFatgperServing;

    @Column(name = "trade_channels")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tradeChannels;

    @Column(name = "trans_fatgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String transFatgperServing;

    @Column(name = "tree_nuts")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String treeNuts;

    @Column(name = "u_sda_foods_material_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uSDAFoodsMaterialCode;

    @Column(name = "u_sda_foods_products_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uSDAFoodsProductsDescription;

    @Column(name = "vendor_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vendorName;

    @Column(name = "vendor_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vendorID;

    @Column(name = "vitamin_dmcgper_serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vitaminDmcgperServing;

    @Column(name = "wheat")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String wheat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public USDAUpdateMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddedSugarsgperServing() {
        return this.addedSugarsgperServing;
    }

    public USDAUpdateMst addedSugarsgperServing(String addedSugarsgperServing) {
        this.setAddedSugarsgperServing(addedSugarsgperServing);
        return this;
    }

    public void setAddedSugarsgperServing(String addedSugarsgperServing) {
        this.addedSugarsgperServing = addedSugarsgperServing;
    }

    public String getAllAllergens() {
        return this.allAllergens;
    }

    public USDAUpdateMst allAllergens(String allAllergens) {
        this.setAllAllergens(allAllergens);
        return this;
    }

    public void setAllAllergens(String allAllergens) {
        this.allAllergens = allAllergens;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public USDAUpdateMst brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getCalciumCamgperServing() {
        return this.calciumCamgperServing;
    }

    public USDAUpdateMst calciumCamgperServing(Double calciumCamgperServing) {
        this.setCalciumCamgperServing(calciumCamgperServing);
        return this;
    }

    public void setCalciumCamgperServing(Double calciumCamgperServing) {
        this.calciumCamgperServing = calciumCamgperServing;
    }

    public String getCalorieskcalperServing() {
        return this.calorieskcalperServing;
    }

    public USDAUpdateMst calorieskcalperServing(String calorieskcalperServing) {
        this.setCalorieskcalperServing(calorieskcalperServing);
        return this;
    }

    public void setCalorieskcalperServing(String calorieskcalperServing) {
        this.calorieskcalperServing = calorieskcalperServing;
    }

    public String getCholesterolmgperServing() {
        return this.cholesterolmgperServing;
    }

    public USDAUpdateMst cholesterolmgperServing(String cholesterolmgperServing) {
        this.setCholesterolmgperServing(cholesterolmgperServing);
        return this;
    }

    public void setCholesterolmgperServing(String cholesterolmgperServing) {
        this.cholesterolmgperServing = cholesterolmgperServing;
    }

    public String getcNCredited() {
        return this.cNCredited;
    }

    public USDAUpdateMst cNCredited(String cNCredited) {
        this.setcNCredited(cNCredited);
        return this;
    }

    public void setcNCredited(String cNCredited) {
        this.cNCredited = cNCredited;
    }

    public String getcNCrediting() {
        return this.cNCrediting;
    }

    public USDAUpdateMst cNCrediting(String cNCrediting) {
        this.setcNCrediting(cNCrediting);
        return this;
    }

    public void setcNCrediting(String cNCrediting) {
        this.cNCrediting = cNCrediting;
    }

    public String getcNExpirationDate() {
        return this.cNExpirationDate;
    }

    public USDAUpdateMst cNExpirationDate(String cNExpirationDate) {
        this.setcNExpirationDate(cNExpirationDate);
        return this;
    }

    public void setcNExpirationDate(String cNExpirationDate) {
        this.cNExpirationDate = cNExpirationDate;
    }

    public String getcNLabelDocument() {
        return this.cNLabelDocument;
    }

    public USDAUpdateMst cNLabelDocument(String cNLabelDocument) {
        this.setcNLabelDocument(cNLabelDocument);
        return this;
    }

    public void setcNLabelDocument(String cNLabelDocument) {
        this.cNLabelDocument = cNLabelDocument;
    }

    public String getcNLabelStatement() {
        return this.cNLabelStatement;
    }

    public USDAUpdateMst cNLabelStatement(String cNLabelStatement) {
        this.setcNLabelStatement(cNLabelStatement);
        return this;
    }

    public void setcNLabelStatement(String cNLabelStatement) {
        this.cNLabelStatement = cNLabelStatement;
    }

    public String getcNProductIdentification() {
        return this.cNProductIdentification;
    }

    public USDAUpdateMst cNProductIdentification(String cNProductIdentification) {
        this.setcNProductIdentification(cNProductIdentification);
        return this;
    }

    public void setcNProductIdentification(String cNProductIdentification) {
        this.cNProductIdentification = cNProductIdentification;
    }

    public String getcNQualified() {
        return this.cNQualified;
    }

    public USDAUpdateMst cNQualified(String cNQualified) {
        this.setcNQualified(cNQualified);
        return this;
    }

    public void setcNQualified(String cNQualified) {
        this.cNQualified = cNQualified;
    }

    public String getcNQualifierCode() {
        return this.cNQualifierCode;
    }

    public USDAUpdateMst cNQualifierCode(String cNQualifierCode) {
        this.setcNQualifierCode(cNQualifierCode);
        return this;
    }

    public void setcNQualifierCode(String cNQualifierCode) {
        this.cNQualifierCode = cNQualifierCode;
    }

    public String getDietaryFibergperServing() {
        return this.dietaryFibergperServing;
    }

    public USDAUpdateMst dietaryFibergperServing(String dietaryFibergperServing) {
        this.setDietaryFibergperServing(dietaryFibergperServing);
        return this;
    }

    public void setDietaryFibergperServing(String dietaryFibergperServing) {
        this.dietaryFibergperServing = dietaryFibergperServing;
    }

    public LocalDate getDownloaded() {
        return this.downloaded;
    }

    public USDAUpdateMst downloaded(LocalDate downloaded) {
        this.setDownloaded(downloaded);
        return this;
    }

    public void setDownloaded(LocalDate downloaded) {
        this.downloaded = downloaded;
    }

    public String getEggs() {
        return this.eggs;
    }

    public USDAUpdateMst eggs(String eggs) {
        this.setEggs(eggs);
        return this;
    }

    public void setEggs(String eggs) {
        this.eggs = eggs;
    }

    public String getFish() {
        return this.fish;
    }

    public USDAUpdateMst fish(String fish) {
        this.setFish(fish);
        return this;
    }

    public void setFish(String fish) {
        this.fish = fish;
    }

    public String getFoodCategory() {
        return this.foodCategory;
    }

    public USDAUpdateMst foodCategory(String foodCategory) {
        this.setFoodCategory(foodCategory);
        return this;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFunctionalname() {
        return this.functionalname;
    }

    public USDAUpdateMst functionalname(String functionalname) {
        this.setFunctionalname(functionalname);
        return this;
    }

    public void setFunctionalname(String functionalname) {
        this.functionalname = functionalname;
    }

    public String getGtin() {
        return this.gtin;
    }

    public USDAUpdateMst gtin(String gtin) {
        this.setGtin(gtin);
        return this;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getHalal() {
        return this.halal;
    }

    public USDAUpdateMst halal(String halal) {
        this.setHalal(halal);
        return this;
    }

    public void setHalal(String halal) {
        this.halal = halal;
    }

    public String getHierarchicalPlacement() {
        return this.hierarchicalPlacement;
    }

    public USDAUpdateMst hierarchicalPlacement(String hierarchicalPlacement) {
        this.setHierarchicalPlacement(hierarchicalPlacement);
        return this;
    }

    public void setHierarchicalPlacement(String hierarchicalPlacement) {
        this.hierarchicalPlacement = hierarchicalPlacement;
    }

    public String getInformationProvider() {
        return this.informationProvider;
    }

    public USDAUpdateMst informationProvider(String informationProvider) {
        this.setInformationProvider(informationProvider);
        return this;
    }

    public void setInformationProvider(String informationProvider) {
        this.informationProvider = informationProvider;
    }

    public String getIngredientsenglish() {
        return this.ingredientsenglish;
    }

    public USDAUpdateMst ingredientsenglish(String ingredientsenglish) {
        this.setIngredientsenglish(ingredientsenglish);
        return this;
    }

    public void setIngredientsenglish(String ingredientsenglish) {
        this.ingredientsenglish = ingredientsenglish;
    }

    public String getIronFemgperServing() {
        return this.ironFemgperServing;
    }

    public USDAUpdateMst ironFemgperServing(String ironFemgperServing) {
        this.setIronFemgperServing(ironFemgperServing);
        return this;
    }

    public void setIronFemgperServing(String ironFemgperServing) {
        this.ironFemgperServing = ironFemgperServing;
    }

    public String getKosher() {
        return this.kosher;
    }

    public USDAUpdateMst kosher(String kosher) {
        this.setKosher(kosher);
        return this;
    }

    public void setKosher(String kosher) {
        this.kosher = kosher;
    }

    public LocalDate getLastupdated() {
        return this.lastupdated;
    }

    public USDAUpdateMst lastupdated(LocalDate lastupdated) {
        this.setLastupdated(lastupdated);
        return this;
    }

    public void setLastupdated(LocalDate lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getLongdescription() {
        return this.longdescription;
    }

    public USDAUpdateMst longdescription(String longdescription) {
        this.setLongdescription(longdescription);
        return this;
    }

    public void setLongdescription(String longdescription) {
        this.longdescription = longdescription;
    }

    public String getMilk() {
        return this.milk;
    }

    public USDAUpdateMst milk(String milk) {
        this.setMilk(milk);
        return this;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public String getNutrientFormatTypeCodeReferenceCode() {
        return this.nutrientFormatTypeCodeReferenceCode;
    }

    public USDAUpdateMst nutrientFormatTypeCodeReferenceCode(String nutrientFormatTypeCodeReferenceCode) {
        this.setNutrientFormatTypeCodeReferenceCode(nutrientFormatTypeCodeReferenceCode);
        return this;
    }

    public void setNutrientFormatTypeCodeReferenceCode(String nutrientFormatTypeCodeReferenceCode) {
        this.nutrientFormatTypeCodeReferenceCode = nutrientFormatTypeCodeReferenceCode;
    }

    public String getNutrientQuantityBasisTypeCode() {
        return this.nutrientQuantityBasisTypeCode;
    }

    public USDAUpdateMst nutrientQuantityBasisTypeCode(String nutrientQuantityBasisTypeCode) {
        this.setNutrientQuantityBasisTypeCode(nutrientQuantityBasisTypeCode);
        return this;
    }

    public void setNutrientQuantityBasisTypeCode(String nutrientQuantityBasisTypeCode) {
        this.nutrientQuantityBasisTypeCode = nutrientQuantityBasisTypeCode;
    }

    public String getNutrientQuantityBasisUnitofMeasure() {
        return this.nutrientQuantityBasisUnitofMeasure;
    }

    public USDAUpdateMst nutrientQuantityBasisUnitofMeasure(String nutrientQuantityBasisUnitofMeasure) {
        this.setNutrientQuantityBasisUnitofMeasure(nutrientQuantityBasisUnitofMeasure);
        return this;
    }

    public void setNutrientQuantityBasisUnitofMeasure(String nutrientQuantityBasisUnitofMeasure) {
        this.nutrientQuantityBasisUnitofMeasure = nutrientQuantityBasisUnitofMeasure;
    }

    public String getNutrientQuantityBasisValue() {
        return this.nutrientQuantityBasisValue;
    }

    public USDAUpdateMst nutrientQuantityBasisValue(String nutrientQuantityBasisValue) {
        this.setNutrientQuantityBasisValue(nutrientQuantityBasisValue);
        return this;
    }

    public void setNutrientQuantityBasisValue(String nutrientQuantityBasisValue) {
        this.nutrientQuantityBasisValue = nutrientQuantityBasisValue;
    }

    public Boolean getNutrientsperservingcalculatedfrombymeasurereportedamount() {
        return this.nutrientsperservingcalculatedfrombymeasurereportedamount;
    }

    public USDAUpdateMst nutrientsperservingcalculatedfrombymeasurereportedamount(
        Boolean nutrientsperservingcalculatedfrombymeasurereportedamount
    ) {
        this.setNutrientsperservingcalculatedfrombymeasurereportedamount(nutrientsperservingcalculatedfrombymeasurereportedamount);
        return this;
    }

    public void setNutrientsperservingcalculatedfrombymeasurereportedamount(
        Boolean nutrientsperservingcalculatedfrombymeasurereportedamount
    ) {
        this.nutrientsperservingcalculatedfrombymeasurereportedamount = nutrientsperservingcalculatedfrombymeasurereportedamount;
    }

    public String getPeanuts() {
        return this.peanuts;
    }

    public USDAUpdateMst peanuts(String peanuts) {
        this.setPeanuts(peanuts);
        return this;
    }

    public void setPeanuts(String peanuts) {
        this.peanuts = peanuts;
    }

    public String getpFSCreditableIngredientTypeCode() {
        return this.pFSCreditableIngredientTypeCode;
    }

    public USDAUpdateMst pFSCreditableIngredientTypeCode(String pFSCreditableIngredientTypeCode) {
        this.setpFSCreditableIngredientTypeCode(pFSCreditableIngredientTypeCode);
        return this;
    }

    public void setpFSCreditableIngredientTypeCode(String pFSCreditableIngredientTypeCode) {
        this.pFSCreditableIngredientTypeCode = pFSCreditableIngredientTypeCode;
    }

    public String getpFSDocument() {
        return this.pFSDocument;
    }

    public USDAUpdateMst pFSDocument(String pFSDocument) {
        this.setpFSDocument(pFSDocument);
        return this;
    }

    public void setpFSDocument(String pFSDocument) {
        this.pFSDocument = pFSDocument;
    }

    public String getpFSTotalCreditableIngredientAmount() {
        return this.pFSTotalCreditableIngredientAmount;
    }

    public USDAUpdateMst pFSTotalCreditableIngredientAmount(String pFSTotalCreditableIngredientAmount) {
        this.setpFSTotalCreditableIngredientAmount(pFSTotalCreditableIngredientAmount);
        return this;
    }

    public void setpFSTotalCreditableIngredientAmount(String pFSTotalCreditableIngredientAmount) {
        this.pFSTotalCreditableIngredientAmount = pFSTotalCreditableIngredientAmount;
    }

    public String getpFSTotalPortionWeight() {
        return this.pFSTotalPortionWeight;
    }

    public USDAUpdateMst pFSTotalPortionWeight(String pFSTotalPortionWeight) {
        this.setpFSTotalPortionWeight(pFSTotalPortionWeight);
        return this;
    }

    public void setpFSTotalPortionWeight(String pFSTotalPortionWeight) {
        this.pFSTotalPortionWeight = pFSTotalPortionWeight;
    }

    public String getPotassiumKmgperServing() {
        return this.potassiumKmgperServing;
    }

    public USDAUpdateMst potassiumKmgperServing(String potassiumKmgperServing) {
        this.setPotassiumKmgperServing(potassiumKmgperServing);
        return this;
    }

    public void setPotassiumKmgperServing(String potassiumKmgperServing) {
        this.potassiumKmgperServing = potassiumKmgperServing;
    }

    public String getPreparationStateCode() {
        return this.preparationStateCode;
    }

    public USDAUpdateMst preparationStateCode(String preparationStateCode) {
        this.setPreparationStateCode(preparationStateCode);
        return this;
    }

    public void setPreparationStateCode(String preparationStateCode) {
        this.preparationStateCode = preparationStateCode;
    }

    public String getProductFormulationStatement() {
        return this.productFormulationStatement;
    }

    public USDAUpdateMst productFormulationStatement(String productFormulationStatement) {
        this.setProductFormulationStatement(productFormulationStatement);
        return this;
    }

    public void setProductFormulationStatement(String productFormulationStatement) {
        this.productFormulationStatement = productFormulationStatement;
    }

    public String getProteingperServing() {
        return this.proteingperServing;
    }

    public USDAUpdateMst proteingperServing(String proteingperServing) {
        this.setProteingperServing(proteingperServing);
        return this;
    }

    public void setProteingperServing(String proteingperServing) {
        this.proteingperServing = proteingperServing;
    }

    public Double getSaturatedFatgperServing() {
        return this.saturatedFatgperServing;
    }

    public USDAUpdateMst saturatedFatgperServing(Double saturatedFatgperServing) {
        this.setSaturatedFatgperServing(saturatedFatgperServing);
        return this;
    }

    public void setSaturatedFatgperServing(Double saturatedFatgperServing) {
        this.saturatedFatgperServing = saturatedFatgperServing;
    }

    public String getServingDescription() {
        return this.servingDescription;
    }

    public USDAUpdateMst servingDescription(String servingDescription) {
        this.setServingDescription(servingDescription);
        return this;
    }

    public void setServingDescription(String servingDescription) {
        this.servingDescription = servingDescription;
    }

    public String getServingSize() {
        return this.servingSize;
    }

    public USDAUpdateMst servingSize(String servingSize) {
        this.setServingSize(servingSize);
        return this;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public String getServingUnitofMeasure() {
        return this.servingUnitofMeasure;
    }

    public USDAUpdateMst servingUnitofMeasure(String servingUnitofMeasure) {
        this.setServingUnitofMeasure(servingUnitofMeasure);
        return this;
    }

    public void setServingUnitofMeasure(String servingUnitofMeasure) {
        this.servingUnitofMeasure = servingUnitofMeasure;
    }

    public String getServingsPerCase() {
        return this.servingsPerCase;
    }

    public USDAUpdateMst servingsPerCase(String servingsPerCase) {
        this.setServingsPerCase(servingsPerCase);
        return this;
    }

    public void setServingsPerCase(String servingsPerCase) {
        this.servingsPerCase = servingsPerCase;
    }

    public String getSesame() {
        return this.sesame;
    }

    public USDAUpdateMst sesame(String sesame) {
        this.setSesame(sesame);
        return this;
    }

    public void setSesame(String sesame) {
        this.sesame = sesame;
    }

    public String getShellfish() {
        return this.shellfish;
    }

    public USDAUpdateMst shellfish(String shellfish) {
        this.setShellfish(shellfish);
        return this;
    }

    public void setShellfish(String shellfish) {
        this.shellfish = shellfish;
    }

    public String getShortdescription() {
        return this.shortdescription;
    }

    public USDAUpdateMst shortdescription(String shortdescription) {
        this.setShortdescription(shortdescription);
        return this;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public String getSodiummgperServing() {
        return this.sodiummgperServing;
    }

    public USDAUpdateMst sodiummgperServing(String sodiummgperServing) {
        this.setSodiummgperServing(sodiummgperServing);
        return this;
    }

    public void setSodiummgperServing(String sodiummgperServing) {
        this.sodiummgperServing = sodiummgperServing;
    }

    public String getSoybeans() {
        return this.soybeans;
    }

    public USDAUpdateMst soybeans(String soybeans) {
        this.setSoybeans(soybeans);
        return this;
    }

    public void setSoybeans(String soybeans) {
        this.soybeans = soybeans;
    }

    public String getSugarsgperServing() {
        return this.sugarsgperServing;
    }

    public USDAUpdateMst sugarsgperServing(String sugarsgperServing) {
        this.setSugarsgperServing(sugarsgperServing);
        return this;
    }

    public void setSugarsgperServing(String sugarsgperServing) {
        this.sugarsgperServing = sugarsgperServing;
    }

    public Double getTotalCarbohydrategperServing() {
        return this.totalCarbohydrategperServing;
    }

    public USDAUpdateMst totalCarbohydrategperServing(Double totalCarbohydrategperServing) {
        this.setTotalCarbohydrategperServing(totalCarbohydrategperServing);
        return this;
    }

    public void setTotalCarbohydrategperServing(Double totalCarbohydrategperServing) {
        this.totalCarbohydrategperServing = totalCarbohydrategperServing;
    }

    public String getTotalFatgperServing() {
        return this.totalFatgperServing;
    }

    public USDAUpdateMst totalFatgperServing(String totalFatgperServing) {
        this.setTotalFatgperServing(totalFatgperServing);
        return this;
    }

    public void setTotalFatgperServing(String totalFatgperServing) {
        this.totalFatgperServing = totalFatgperServing;
    }

    public String getTradeChannels() {
        return this.tradeChannels;
    }

    public USDAUpdateMst tradeChannels(String tradeChannels) {
        this.setTradeChannels(tradeChannels);
        return this;
    }

    public void setTradeChannels(String tradeChannels) {
        this.tradeChannels = tradeChannels;
    }

    public String getTransFatgperServing() {
        return this.transFatgperServing;
    }

    public USDAUpdateMst transFatgperServing(String transFatgperServing) {
        this.setTransFatgperServing(transFatgperServing);
        return this;
    }

    public void setTransFatgperServing(String transFatgperServing) {
        this.transFatgperServing = transFatgperServing;
    }

    public String getTreeNuts() {
        return this.treeNuts;
    }

    public USDAUpdateMst treeNuts(String treeNuts) {
        this.setTreeNuts(treeNuts);
        return this;
    }

    public void setTreeNuts(String treeNuts) {
        this.treeNuts = treeNuts;
    }

    public String getuSDAFoodsMaterialCode() {
        return this.uSDAFoodsMaterialCode;
    }

    public USDAUpdateMst uSDAFoodsMaterialCode(String uSDAFoodsMaterialCode) {
        this.setuSDAFoodsMaterialCode(uSDAFoodsMaterialCode);
        return this;
    }

    public void setuSDAFoodsMaterialCode(String uSDAFoodsMaterialCode) {
        this.uSDAFoodsMaterialCode = uSDAFoodsMaterialCode;
    }

    public String getuSDAFoodsProductsDescription() {
        return this.uSDAFoodsProductsDescription;
    }

    public USDAUpdateMst uSDAFoodsProductsDescription(String uSDAFoodsProductsDescription) {
        this.setuSDAFoodsProductsDescription(uSDAFoodsProductsDescription);
        return this;
    }

    public void setuSDAFoodsProductsDescription(String uSDAFoodsProductsDescription) {
        this.uSDAFoodsProductsDescription = uSDAFoodsProductsDescription;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public USDAUpdateMst vendorName(String vendorName) {
        this.setVendorName(vendorName);
        return this;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorID() {
        return this.vendorID;
    }

    public USDAUpdateMst vendorID(String vendorID) {
        this.setVendorID(vendorID);
        return this;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getVitaminDmcgperServing() {
        return this.vitaminDmcgperServing;
    }

    public USDAUpdateMst vitaminDmcgperServing(String vitaminDmcgperServing) {
        this.setVitaminDmcgperServing(vitaminDmcgperServing);
        return this;
    }

    public void setVitaminDmcgperServing(String vitaminDmcgperServing) {
        this.vitaminDmcgperServing = vitaminDmcgperServing;
    }

    public String getWheat() {
        return this.wheat;
    }

    public USDAUpdateMst wheat(String wheat) {
        this.setWheat(wheat);
        return this;
    }

    public void setWheat(String wheat) {
        this.wheat = wheat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof USDAUpdateMst)) {
            return false;
        }
        return getId() != null && getId().equals(((USDAUpdateMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "USDAUpdateMst{" +
            "id=" + getId() +
            ", addedSugarsgperServing='" + getAddedSugarsgperServing() + "'" +
            ", allAllergens='" + getAllAllergens() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", calciumCamgperServing=" + getCalciumCamgperServing() +
            ", calorieskcalperServing='" + getCalorieskcalperServing() + "'" +
            ", cholesterolmgperServing='" + getCholesterolmgperServing() + "'" +
            ", cNCredited='" + getcNCredited() + "'" +
            ", cNCrediting='" + getcNCrediting() + "'" +
            ", cNExpirationDate='" + getcNExpirationDate() + "'" +
            ", cNLabelDocument='" + getcNLabelDocument() + "'" +
            ", cNLabelStatement='" + getcNLabelStatement() + "'" +
            ", cNProductIdentification='" + getcNProductIdentification() + "'" +
            ", cNQualified='" + getcNQualified() + "'" +
            ", cNQualifierCode='" + getcNQualifierCode() + "'" +
            ", dietaryFibergperServing='" + getDietaryFibergperServing() + "'" +
            ", downloaded='" + getDownloaded() + "'" +
            ", eggs='" + getEggs() + "'" +
            ", fish='" + getFish() + "'" +
            ", foodCategory='" + getFoodCategory() + "'" +
            ", functionalname='" + getFunctionalname() + "'" +
            ", gtin='" + getGtin() + "'" +
            ", halal='" + getHalal() + "'" +
            ", hierarchicalPlacement='" + getHierarchicalPlacement() + "'" +
            ", informationProvider='" + getInformationProvider() + "'" +
            ", ingredientsenglish='" + getIngredientsenglish() + "'" +
            ", ironFemgperServing='" + getIronFemgperServing() + "'" +
            ", kosher='" + getKosher() + "'" +
            ", lastupdated='" + getLastupdated() + "'" +
            ", longdescription='" + getLongdescription() + "'" +
            ", milk='" + getMilk() + "'" +
            ", nutrientFormatTypeCodeReferenceCode='" + getNutrientFormatTypeCodeReferenceCode() + "'" +
            ", nutrientQuantityBasisTypeCode='" + getNutrientQuantityBasisTypeCode() + "'" +
            ", nutrientQuantityBasisUnitofMeasure='" + getNutrientQuantityBasisUnitofMeasure() + "'" +
            ", nutrientQuantityBasisValue='" + getNutrientQuantityBasisValue() + "'" +
            ", nutrientsperservingcalculatedfrombymeasurereportedamount='" + getNutrientsperservingcalculatedfrombymeasurereportedamount() + "'" +
            ", peanuts='" + getPeanuts() + "'" +
            ", pFSCreditableIngredientTypeCode='" + getpFSCreditableIngredientTypeCode() + "'" +
            ", pFSDocument='" + getpFSDocument() + "'" +
            ", pFSTotalCreditableIngredientAmount='" + getpFSTotalCreditableIngredientAmount() + "'" +
            ", pFSTotalPortionWeight='" + getpFSTotalPortionWeight() + "'" +
            ", potassiumKmgperServing='" + getPotassiumKmgperServing() + "'" +
            ", preparationStateCode='" + getPreparationStateCode() + "'" +
            ", productFormulationStatement='" + getProductFormulationStatement() + "'" +
            ", proteingperServing='" + getProteingperServing() + "'" +
            ", saturatedFatgperServing=" + getSaturatedFatgperServing() +
            ", servingDescription='" + getServingDescription() + "'" +
            ", servingSize='" + getServingSize() + "'" +
            ", servingUnitofMeasure='" + getServingUnitofMeasure() + "'" +
            ", servingsPerCase='" + getServingsPerCase() + "'" +
            ", sesame='" + getSesame() + "'" +
            ", shellfish='" + getShellfish() + "'" +
            ", shortdescription='" + getShortdescription() + "'" +
            ", sodiummgperServing='" + getSodiummgperServing() + "'" +
            ", soybeans='" + getSoybeans() + "'" +
            ", sugarsgperServing='" + getSugarsgperServing() + "'" +
            ", totalCarbohydrategperServing=" + getTotalCarbohydrategperServing() +
            ", totalFatgperServing='" + getTotalFatgperServing() + "'" +
            ", tradeChannels='" + getTradeChannels() + "'" +
            ", transFatgperServing='" + getTransFatgperServing() + "'" +
            ", treeNuts='" + getTreeNuts() + "'" +
            ", uSDAFoodsMaterialCode='" + getuSDAFoodsMaterialCode() + "'" +
            ", uSDAFoodsProductsDescription='" + getuSDAFoodsProductsDescription() + "'" +
            ", vendorName='" + getVendorName() + "'" +
            ", vendorID='" + getVendorID() + "'" +
            ", vitaminDmcgperServing='" + getVitaminDmcgperServing() + "'" +
            ", wheat='" + getWheat() + "'" +
            "}";
    }
}
