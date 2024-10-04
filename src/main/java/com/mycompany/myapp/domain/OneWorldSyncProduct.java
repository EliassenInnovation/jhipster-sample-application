package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OneWorldSyncProduct.
 */
@Entity
@Table(name = "one_world_sync_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "oneworldsyncproduct")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OneWorldSyncProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "added_sugars")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String addedSugars;

    @Column(name = "added_sugar_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String addedSugarUom;

    @Lob
    @Column(name = "allergen_keyword")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allergenKeyword;

    @Column(name = "allergens")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer allergens;

    @Column(name = "brand_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String brandName;

    @Column(name = "calories")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String calories;

    @Column(name = "calories_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String caloriesUom;

    @Column(name = "carbohydrates")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String carbohydrates;

    @Column(name = "carbohydrates_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String carbohydratesUom;

    @Column(name = "category_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String categoryName;

    @Column(name = "cholesterol")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cholesterol;

    @Column(name = "cholesterol_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cholesterolUOM;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "data_form")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dataForm;

    @Column(name = "dietary_fiber")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dietaryFiber;

    @Column(name = "dietary_fiber_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dietaryFiberUom;

    @Column(name = "distributor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String distributor;

    @Column(name = "do_not_consider_product")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean doNotConsiderProduct;

    @Lob
    @Column(name = "extended_model")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String extendedModel;

    @Column(name = "g_ln_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gLNNumber;

    @Column(name = "g_tin")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gTIN;

    @Column(name = "h_7")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer h7;

    @Lob
    @Column(name = "image")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String image;

    @Lob
    @Column(name = "ingredients")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ingredients;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "is_approve")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isApprove;

    @Column(name = "is_merge")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isMerge;

    @Column(name = "is_product_sync")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isProductSync;

    @Lob
    @Column(name = "manufacturer")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String manufacturer;

    @Column(name = "manufacturer_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer manufacturerId;

    @Column(name = "manufacturer_text_1_ws")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String manufacturerText1Ws;

    @Column(name = "modified_on")
    private LocalDate modifiedOn;

    @Lob
    @Column(name = "product_description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productDescription;

    @Column(name = "product_id")
    private Long productId;

    @Lob
    @Column(name = "product_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productName;

    @Column(name = "protein")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String protein;

    @Column(name = "protein_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String proteinUom;

    @Column(name = "saturated_fat")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String saturatedFat;

    @Column(name = "serving")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String serving;

    @Column(name = "serving_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String servingUom;

    @Column(name = "sodium")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sodium;

    @Column(name = "sodium_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sodiumUom;

    @Column(name = "storage_type_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer storageTypeId;

    @Lob
    @Column(name = "storage_type_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String storageTypeName;

    @Column(name = "sub_category_1_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subCategory1Name;

    @Column(name = "sub_category_2_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subCategory2Name;

    @Column(name = "sugar")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sugar;

    @Column(name = "sugar_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sugarUom;

    @Column(name = "sync_effective")
    private LocalDate syncEffective;

    @Column(name = "sync_header_last_change")
    private LocalDate syncHeaderLastChange;

    @Column(name = "sync_item_reference_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String syncItemReferenceId;

    @Column(name = "sync_last_change")
    private LocalDate syncLastChange;

    @Column(name = "sync_publication")
    private LocalDate syncPublication;

    @Column(name = "total_fat")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String totalFat;

    @Column(name = "trans_fat")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String transFat;

    @Column(name = "u_pc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uPC;

    @Column(name = "vendor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vendor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OneWorldSyncProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddedSugars() {
        return this.addedSugars;
    }

    public OneWorldSyncProduct addedSugars(String addedSugars) {
        this.setAddedSugars(addedSugars);
        return this;
    }

    public void setAddedSugars(String addedSugars) {
        this.addedSugars = addedSugars;
    }

    public String getAddedSugarUom() {
        return this.addedSugarUom;
    }

    public OneWorldSyncProduct addedSugarUom(String addedSugarUom) {
        this.setAddedSugarUom(addedSugarUom);
        return this;
    }

    public void setAddedSugarUom(String addedSugarUom) {
        this.addedSugarUom = addedSugarUom;
    }

    public String getAllergenKeyword() {
        return this.allergenKeyword;
    }

    public OneWorldSyncProduct allergenKeyword(String allergenKeyword) {
        this.setAllergenKeyword(allergenKeyword);
        return this;
    }

    public void setAllergenKeyword(String allergenKeyword) {
        this.allergenKeyword = allergenKeyword;
    }

    public Integer getAllergens() {
        return this.allergens;
    }

    public OneWorldSyncProduct allergens(Integer allergens) {
        this.setAllergens(allergens);
        return this;
    }

    public void setAllergens(Integer allergens) {
        this.allergens = allergens;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public OneWorldSyncProduct brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCalories() {
        return this.calories;
    }

    public OneWorldSyncProduct calories(String calories) {
        this.setCalories(calories);
        return this;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCaloriesUom() {
        return this.caloriesUom;
    }

    public OneWorldSyncProduct caloriesUom(String caloriesUom) {
        this.setCaloriesUom(caloriesUom);
        return this;
    }

    public void setCaloriesUom(String caloriesUom) {
        this.caloriesUom = caloriesUom;
    }

    public String getCarbohydrates() {
        return this.carbohydrates;
    }

    public OneWorldSyncProduct carbohydrates(String carbohydrates) {
        this.setCarbohydrates(carbohydrates);
        return this;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getCarbohydratesUom() {
        return this.carbohydratesUom;
    }

    public OneWorldSyncProduct carbohydratesUom(String carbohydratesUom) {
        this.setCarbohydratesUom(carbohydratesUom);
        return this;
    }

    public void setCarbohydratesUom(String carbohydratesUom) {
        this.carbohydratesUom = carbohydratesUom;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public OneWorldSyncProduct categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCholesterol() {
        return this.cholesterol;
    }

    public OneWorldSyncProduct cholesterol(String cholesterol) {
        this.setCholesterol(cholesterol);
        return this;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getCholesterolUOM() {
        return this.cholesterolUOM;
    }

    public OneWorldSyncProduct cholesterolUOM(String cholesterolUOM) {
        this.setCholesterolUOM(cholesterolUOM);
        return this;
    }

    public void setCholesterolUOM(String cholesterolUOM) {
        this.cholesterolUOM = cholesterolUOM;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public OneWorldSyncProduct createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getDataForm() {
        return this.dataForm;
    }

    public OneWorldSyncProduct dataForm(String dataForm) {
        this.setDataForm(dataForm);
        return this;
    }

    public void setDataForm(String dataForm) {
        this.dataForm = dataForm;
    }

    public String getDietaryFiber() {
        return this.dietaryFiber;
    }

    public OneWorldSyncProduct dietaryFiber(String dietaryFiber) {
        this.setDietaryFiber(dietaryFiber);
        return this;
    }

    public void setDietaryFiber(String dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public String getDietaryFiberUom() {
        return this.dietaryFiberUom;
    }

    public OneWorldSyncProduct dietaryFiberUom(String dietaryFiberUom) {
        this.setDietaryFiberUom(dietaryFiberUom);
        return this;
    }

    public void setDietaryFiberUom(String dietaryFiberUom) {
        this.dietaryFiberUom = dietaryFiberUom;
    }

    public String getDistributor() {
        return this.distributor;
    }

    public OneWorldSyncProduct distributor(String distributor) {
        this.setDistributor(distributor);
        return this;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public Boolean getDoNotConsiderProduct() {
        return this.doNotConsiderProduct;
    }

    public OneWorldSyncProduct doNotConsiderProduct(Boolean doNotConsiderProduct) {
        this.setDoNotConsiderProduct(doNotConsiderProduct);
        return this;
    }

    public void setDoNotConsiderProduct(Boolean doNotConsiderProduct) {
        this.doNotConsiderProduct = doNotConsiderProduct;
    }

    public String getExtendedModel() {
        return this.extendedModel;
    }

    public OneWorldSyncProduct extendedModel(String extendedModel) {
        this.setExtendedModel(extendedModel);
        return this;
    }

    public void setExtendedModel(String extendedModel) {
        this.extendedModel = extendedModel;
    }

    public String getgLNNumber() {
        return this.gLNNumber;
    }

    public OneWorldSyncProduct gLNNumber(String gLNNumber) {
        this.setgLNNumber(gLNNumber);
        return this;
    }

    public void setgLNNumber(String gLNNumber) {
        this.gLNNumber = gLNNumber;
    }

    public String getgTIN() {
        return this.gTIN;
    }

    public OneWorldSyncProduct gTIN(String gTIN) {
        this.setgTIN(gTIN);
        return this;
    }

    public void setgTIN(String gTIN) {
        this.gTIN = gTIN;
    }

    public Integer geth7() {
        return this.h7;
    }

    public OneWorldSyncProduct h7(Integer h7) {
        this.seth7(h7);
        return this;
    }

    public void seth7(Integer h7) {
        this.h7 = h7;
    }

    public String getImage() {
        return this.image;
    }

    public OneWorldSyncProduct image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public OneWorldSyncProduct ingredients(String ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public OneWorldSyncProduct isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsApprove() {
        return this.isApprove;
    }

    public OneWorldSyncProduct isApprove(Boolean isApprove) {
        this.setIsApprove(isApprove);
        return this;
    }

    public void setIsApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }

    public Boolean getIsMerge() {
        return this.isMerge;
    }

    public OneWorldSyncProduct isMerge(Boolean isMerge) {
        this.setIsMerge(isMerge);
        return this;
    }

    public void setIsMerge(Boolean isMerge) {
        this.isMerge = isMerge;
    }

    public Boolean getIsProductSync() {
        return this.isProductSync;
    }

    public OneWorldSyncProduct isProductSync(Boolean isProductSync) {
        this.setIsProductSync(isProductSync);
        return this;
    }

    public void setIsProductSync(Boolean isProductSync) {
        this.isProductSync = isProductSync;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public OneWorldSyncProduct manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getManufacturerId() {
        return this.manufacturerId;
    }

    public OneWorldSyncProduct manufacturerId(Integer manufacturerId) {
        this.setManufacturerId(manufacturerId);
        return this;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerText1Ws() {
        return this.manufacturerText1Ws;
    }

    public OneWorldSyncProduct manufacturerText1Ws(String manufacturerText1Ws) {
        this.setManufacturerText1Ws(manufacturerText1Ws);
        return this;
    }

    public void setManufacturerText1Ws(String manufacturerText1Ws) {
        this.manufacturerText1Ws = manufacturerText1Ws;
    }

    public LocalDate getModifiedOn() {
        return this.modifiedOn;
    }

    public OneWorldSyncProduct modifiedOn(LocalDate modifiedOn) {
        this.setModifiedOn(modifiedOn);
        return this;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public OneWorldSyncProduct productDescription(String productDescription) {
        this.setProductDescription(productDescription);
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Long getProductId() {
        return this.productId;
    }

    public OneWorldSyncProduct productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public OneWorldSyncProduct productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProtein() {
        return this.protein;
    }

    public OneWorldSyncProduct protein(String protein) {
        this.setProtein(protein);
        return this;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getProteinUom() {
        return this.proteinUom;
    }

    public OneWorldSyncProduct proteinUom(String proteinUom) {
        this.setProteinUom(proteinUom);
        return this;
    }

    public void setProteinUom(String proteinUom) {
        this.proteinUom = proteinUom;
    }

    public String getSaturatedFat() {
        return this.saturatedFat;
    }

    public OneWorldSyncProduct saturatedFat(String saturatedFat) {
        this.setSaturatedFat(saturatedFat);
        return this;
    }

    public void setSaturatedFat(String saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public String getServing() {
        return this.serving;
    }

    public OneWorldSyncProduct serving(String serving) {
        this.setServing(serving);
        return this;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getServingUom() {
        return this.servingUom;
    }

    public OneWorldSyncProduct servingUom(String servingUom) {
        this.setServingUom(servingUom);
        return this;
    }

    public void setServingUom(String servingUom) {
        this.servingUom = servingUom;
    }

    public String getSodium() {
        return this.sodium;
    }

    public OneWorldSyncProduct sodium(String sodium) {
        this.setSodium(sodium);
        return this;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getSodiumUom() {
        return this.sodiumUom;
    }

    public OneWorldSyncProduct sodiumUom(String sodiumUom) {
        this.setSodiumUom(sodiumUom);
        return this;
    }

    public void setSodiumUom(String sodiumUom) {
        this.sodiumUom = sodiumUom;
    }

    public Integer getStorageTypeId() {
        return this.storageTypeId;
    }

    public OneWorldSyncProduct storageTypeId(Integer storageTypeId) {
        this.setStorageTypeId(storageTypeId);
        return this;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public String getStorageTypeName() {
        return this.storageTypeName;
    }

    public OneWorldSyncProduct storageTypeName(String storageTypeName) {
        this.setStorageTypeName(storageTypeName);
        return this;
    }

    public void setStorageTypeName(String storageTypeName) {
        this.storageTypeName = storageTypeName;
    }

    public String getSubCategory1Name() {
        return this.subCategory1Name;
    }

    public OneWorldSyncProduct subCategory1Name(String subCategory1Name) {
        this.setSubCategory1Name(subCategory1Name);
        return this;
    }

    public void setSubCategory1Name(String subCategory1Name) {
        this.subCategory1Name = subCategory1Name;
    }

    public String getSubCategory2Name() {
        return this.subCategory2Name;
    }

    public OneWorldSyncProduct subCategory2Name(String subCategory2Name) {
        this.setSubCategory2Name(subCategory2Name);
        return this;
    }

    public void setSubCategory2Name(String subCategory2Name) {
        this.subCategory2Name = subCategory2Name;
    }

    public String getSugar() {
        return this.sugar;
    }

    public OneWorldSyncProduct sugar(String sugar) {
        this.setSugar(sugar);
        return this;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getSugarUom() {
        return this.sugarUom;
    }

    public OneWorldSyncProduct sugarUom(String sugarUom) {
        this.setSugarUom(sugarUom);
        return this;
    }

    public void setSugarUom(String sugarUom) {
        this.sugarUom = sugarUom;
    }

    public LocalDate getSyncEffective() {
        return this.syncEffective;
    }

    public OneWorldSyncProduct syncEffective(LocalDate syncEffective) {
        this.setSyncEffective(syncEffective);
        return this;
    }

    public void setSyncEffective(LocalDate syncEffective) {
        this.syncEffective = syncEffective;
    }

    public LocalDate getSyncHeaderLastChange() {
        return this.syncHeaderLastChange;
    }

    public OneWorldSyncProduct syncHeaderLastChange(LocalDate syncHeaderLastChange) {
        this.setSyncHeaderLastChange(syncHeaderLastChange);
        return this;
    }

    public void setSyncHeaderLastChange(LocalDate syncHeaderLastChange) {
        this.syncHeaderLastChange = syncHeaderLastChange;
    }

    public String getSyncItemReferenceId() {
        return this.syncItemReferenceId;
    }

    public OneWorldSyncProduct syncItemReferenceId(String syncItemReferenceId) {
        this.setSyncItemReferenceId(syncItemReferenceId);
        return this;
    }

    public void setSyncItemReferenceId(String syncItemReferenceId) {
        this.syncItemReferenceId = syncItemReferenceId;
    }

    public LocalDate getSyncLastChange() {
        return this.syncLastChange;
    }

    public OneWorldSyncProduct syncLastChange(LocalDate syncLastChange) {
        this.setSyncLastChange(syncLastChange);
        return this;
    }

    public void setSyncLastChange(LocalDate syncLastChange) {
        this.syncLastChange = syncLastChange;
    }

    public LocalDate getSyncPublication() {
        return this.syncPublication;
    }

    public OneWorldSyncProduct syncPublication(LocalDate syncPublication) {
        this.setSyncPublication(syncPublication);
        return this;
    }

    public void setSyncPublication(LocalDate syncPublication) {
        this.syncPublication = syncPublication;
    }

    public String getTotalFat() {
        return this.totalFat;
    }

    public OneWorldSyncProduct totalFat(String totalFat) {
        this.setTotalFat(totalFat);
        return this;
    }

    public void setTotalFat(String totalFat) {
        this.totalFat = totalFat;
    }

    public String getTransFat() {
        return this.transFat;
    }

    public OneWorldSyncProduct transFat(String transFat) {
        this.setTransFat(transFat);
        return this;
    }

    public void setTransFat(String transFat) {
        this.transFat = transFat;
    }

    public String getuPC() {
        return this.uPC;
    }

    public OneWorldSyncProduct uPC(String uPC) {
        this.setuPC(uPC);
        return this;
    }

    public void setuPC(String uPC) {
        this.uPC = uPC;
    }

    public String getVendor() {
        return this.vendor;
    }

    public OneWorldSyncProduct vendor(String vendor) {
        this.setVendor(vendor);
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OneWorldSyncProduct)) {
            return false;
        }
        return getId() != null && getId().equals(((OneWorldSyncProduct) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OneWorldSyncProduct{" +
            "id=" + getId() +
            ", addedSugars='" + getAddedSugars() + "'" +
            ", addedSugarUom='" + getAddedSugarUom() + "'" +
            ", allergenKeyword='" + getAllergenKeyword() + "'" +
            ", allergens=" + getAllergens() +
            ", brandName='" + getBrandName() + "'" +
            ", calories='" + getCalories() + "'" +
            ", caloriesUom='" + getCaloriesUom() + "'" +
            ", carbohydrates='" + getCarbohydrates() + "'" +
            ", carbohydratesUom='" + getCarbohydratesUom() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            ", cholesterol='" + getCholesterol() + "'" +
            ", cholesterolUOM='" + getCholesterolUOM() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", dataForm='" + getDataForm() + "'" +
            ", dietaryFiber='" + getDietaryFiber() + "'" +
            ", dietaryFiberUom='" + getDietaryFiberUom() + "'" +
            ", distributor='" + getDistributor() + "'" +
            ", doNotConsiderProduct='" + getDoNotConsiderProduct() + "'" +
            ", extendedModel='" + getExtendedModel() + "'" +
            ", gLNNumber='" + getgLNNumber() + "'" +
            ", gTIN='" + getgTIN() + "'" +
            ", h7=" + geth7() +
            ", image='" + getImage() + "'" +
            ", ingredients='" + getIngredients() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isApprove='" + getIsApprove() + "'" +
            ", isMerge='" + getIsMerge() + "'" +
            ", isProductSync='" + getIsProductSync() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", manufacturerId=" + getManufacturerId() +
            ", manufacturerText1Ws='" + getManufacturerText1Ws() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            ", productDescription='" + getProductDescription() + "'" +
            ", productId=" + getProductId() +
            ", productName='" + getProductName() + "'" +
            ", protein='" + getProtein() + "'" +
            ", proteinUom='" + getProteinUom() + "'" +
            ", saturatedFat='" + getSaturatedFat() + "'" +
            ", serving='" + getServing() + "'" +
            ", servingUom='" + getServingUom() + "'" +
            ", sodium='" + getSodium() + "'" +
            ", sodiumUom='" + getSodiumUom() + "'" +
            ", storageTypeId=" + getStorageTypeId() +
            ", storageTypeName='" + getStorageTypeName() + "'" +
            ", subCategory1Name='" + getSubCategory1Name() + "'" +
            ", subCategory2Name='" + getSubCategory2Name() + "'" +
            ", sugar='" + getSugar() + "'" +
            ", sugarUom='" + getSugarUom() + "'" +
            ", syncEffective='" + getSyncEffective() + "'" +
            ", syncHeaderLastChange='" + getSyncHeaderLastChange() + "'" +
            ", syncItemReferenceId='" + getSyncItemReferenceId() + "'" +
            ", syncLastChange='" + getSyncLastChange() + "'" +
            ", syncPublication='" + getSyncPublication() + "'" +
            ", totalFat='" + getTotalFat() + "'" +
            ", transFat='" + getTransFat() + "'" +
            ", uPC='" + getuPC() + "'" +
            ", vendor='" + getVendor() + "'" +
            "}";
    }
}
