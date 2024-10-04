package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductBeforeApprove.
 */
@Entity
@Table(name = "product_before_approve")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productbeforeapprove")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductBeforeApprove implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "added_sugar")
    private Double addedSugar;

    @Column(name = "added_sugar_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String addedSugarUom;

    @Lob
    @Column(name = "allergen_keywords")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allergenKeywords;

    @Column(name = "brand_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String brandName;

    @Column(name = "calories")
    private Double calories;

    @Column(name = "calories_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String caloriesUom;

    @Column(name = "carbohydrates")
    private Double carbohydrates;

    @Column(name = "carbohydrates_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String carbohydratesUom;

    @Column(name = "category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer categoryId;

    @Column(name = "cholesterol")
    private Double cholesterol;

    @Column(name = "cholesterol_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cholesterolUOM;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "dietary_fiber")
    private Double dietaryFiber;

    @Column(name = "dietary_fiber_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String dietaryFiberUom;

    @Lob
    @Column(name = "distributor_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String distributorId;

    @Lob
    @Column(name = "g_tin")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gTIN;

    @Lob
    @Column(name = "ingredients")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ingredients;

    @Column(name = "ioc_category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer iocCategoryId;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "is_merge")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isMerge;

    @Column(name = "manufacturer_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer manufacturerId;

    @Column(name = "manufacturer_product_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String manufacturerProductCode;

    @Column(name = "merge_date")
    private LocalDate mergeDate;

    @Column(name = "product_id")
    private Long productId;

    @Lob
    @Column(name = "product_label_pdf_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productLabelPdfUrl;

    @Column(name = "product_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productName;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "protein_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String proteinUom;

    @Column(name = "saturated_fat")
    private Double saturatedFat;

    @Column(name = "serving")
    private Double serving;

    @Column(name = "serving_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String servingUom;

    @Column(name = "sodium")
    private Double sodium;

    @Column(name = "sodium_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sodiumUom;

    @Column(name = "storage_type_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer storageTypeId;

    @Column(name = "sub_category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer subCategoryId;

    @Column(name = "sugar")
    private Double sugar;

    @Column(name = "sugar_uom")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sugarUom;

    @Column(name = "total_fat")
    private Double totalFat;

    @Column(name = "trans_fat")
    private Double transFat;

    @Lob
    @Column(name = "u_pc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uPC;

    @Column(name = "updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    @Column(name = "vendor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vendor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductBeforeApprove id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAddedSugar() {
        return this.addedSugar;
    }

    public ProductBeforeApprove addedSugar(Double addedSugar) {
        this.setAddedSugar(addedSugar);
        return this;
    }

    public void setAddedSugar(Double addedSugar) {
        this.addedSugar = addedSugar;
    }

    public String getAddedSugarUom() {
        return this.addedSugarUom;
    }

    public ProductBeforeApprove addedSugarUom(String addedSugarUom) {
        this.setAddedSugarUom(addedSugarUom);
        return this;
    }

    public void setAddedSugarUom(String addedSugarUom) {
        this.addedSugarUom = addedSugarUom;
    }

    public String getAllergenKeywords() {
        return this.allergenKeywords;
    }

    public ProductBeforeApprove allergenKeywords(String allergenKeywords) {
        this.setAllergenKeywords(allergenKeywords);
        return this;
    }

    public void setAllergenKeywords(String allergenKeywords) {
        this.allergenKeywords = allergenKeywords;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public ProductBeforeApprove brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getCalories() {
        return this.calories;
    }

    public ProductBeforeApprove calories(Double calories) {
        this.setCalories(calories);
        return this;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public String getCaloriesUom() {
        return this.caloriesUom;
    }

    public ProductBeforeApprove caloriesUom(String caloriesUom) {
        this.setCaloriesUom(caloriesUom);
        return this;
    }

    public void setCaloriesUom(String caloriesUom) {
        this.caloriesUom = caloriesUom;
    }

    public Double getCarbohydrates() {
        return this.carbohydrates;
    }

    public ProductBeforeApprove carbohydrates(Double carbohydrates) {
        this.setCarbohydrates(carbohydrates);
        return this;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getCarbohydratesUom() {
        return this.carbohydratesUom;
    }

    public ProductBeforeApprove carbohydratesUom(String carbohydratesUom) {
        this.setCarbohydratesUom(carbohydratesUom);
        return this;
    }

    public void setCarbohydratesUom(String carbohydratesUom) {
        this.carbohydratesUom = carbohydratesUom;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public ProductBeforeApprove categoryId(Integer categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getCholesterol() {
        return this.cholesterol;
    }

    public ProductBeforeApprove cholesterol(Double cholesterol) {
        this.setCholesterol(cholesterol);
        return this;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getCholesterolUOM() {
        return this.cholesterolUOM;
    }

    public ProductBeforeApprove cholesterolUOM(String cholesterolUOM) {
        this.setCholesterolUOM(cholesterolUOM);
        return this;
    }

    public void setCholesterolUOM(String cholesterolUOM) {
        this.cholesterolUOM = cholesterolUOM;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ProductBeforeApprove createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public ProductBeforeApprove createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductBeforeApprove description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDietaryFiber() {
        return this.dietaryFiber;
    }

    public ProductBeforeApprove dietaryFiber(Double dietaryFiber) {
        this.setDietaryFiber(dietaryFiber);
        return this;
    }

    public void setDietaryFiber(Double dietaryFiber) {
        this.dietaryFiber = dietaryFiber;
    }

    public String getDietaryFiberUom() {
        return this.dietaryFiberUom;
    }

    public ProductBeforeApprove dietaryFiberUom(String dietaryFiberUom) {
        this.setDietaryFiberUom(dietaryFiberUom);
        return this;
    }

    public void setDietaryFiberUom(String dietaryFiberUom) {
        this.dietaryFiberUom = dietaryFiberUom;
    }

    public String getDistributorId() {
        return this.distributorId;
    }

    public ProductBeforeApprove distributorId(String distributorId) {
        this.setDistributorId(distributorId);
        return this;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getgTIN() {
        return this.gTIN;
    }

    public ProductBeforeApprove gTIN(String gTIN) {
        this.setgTIN(gTIN);
        return this;
    }

    public void setgTIN(String gTIN) {
        this.gTIN = gTIN;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public ProductBeforeApprove ingredients(String ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getIocCategoryId() {
        return this.iocCategoryId;
    }

    public ProductBeforeApprove iocCategoryId(Integer iocCategoryId) {
        this.setIocCategoryId(iocCategoryId);
        return this;
    }

    public void setIocCategoryId(Integer iocCategoryId) {
        this.iocCategoryId = iocCategoryId;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProductBeforeApprove isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsMerge() {
        return this.isMerge;
    }

    public ProductBeforeApprove isMerge(Boolean isMerge) {
        this.setIsMerge(isMerge);
        return this;
    }

    public void setIsMerge(Boolean isMerge) {
        this.isMerge = isMerge;
    }

    public Integer getManufacturerId() {
        return this.manufacturerId;
    }

    public ProductBeforeApprove manufacturerId(Integer manufacturerId) {
        this.setManufacturerId(manufacturerId);
        return this;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerProductCode() {
        return this.manufacturerProductCode;
    }

    public ProductBeforeApprove manufacturerProductCode(String manufacturerProductCode) {
        this.setManufacturerProductCode(manufacturerProductCode);
        return this;
    }

    public void setManufacturerProductCode(String manufacturerProductCode) {
        this.manufacturerProductCode = manufacturerProductCode;
    }

    public LocalDate getMergeDate() {
        return this.mergeDate;
    }

    public ProductBeforeApprove mergeDate(LocalDate mergeDate) {
        this.setMergeDate(mergeDate);
        return this;
    }

    public void setMergeDate(LocalDate mergeDate) {
        this.mergeDate = mergeDate;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductBeforeApprove productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductLabelPdfUrl() {
        return this.productLabelPdfUrl;
    }

    public ProductBeforeApprove productLabelPdfUrl(String productLabelPdfUrl) {
        this.setProductLabelPdfUrl(productLabelPdfUrl);
        return this;
    }

    public void setProductLabelPdfUrl(String productLabelPdfUrl) {
        this.productLabelPdfUrl = productLabelPdfUrl;
    }

    public String getProductName() {
        return this.productName;
    }

    public ProductBeforeApprove productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProtein() {
        return this.protein;
    }

    public ProductBeforeApprove protein(Double protein) {
        this.setProtein(protein);
        return this;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public String getProteinUom() {
        return this.proteinUom;
    }

    public ProductBeforeApprove proteinUom(String proteinUom) {
        this.setProteinUom(proteinUom);
        return this;
    }

    public void setProteinUom(String proteinUom) {
        this.proteinUom = proteinUom;
    }

    public Double getSaturatedFat() {
        return this.saturatedFat;
    }

    public ProductBeforeApprove saturatedFat(Double saturatedFat) {
        this.setSaturatedFat(saturatedFat);
        return this;
    }

    public void setSaturatedFat(Double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public Double getServing() {
        return this.serving;
    }

    public ProductBeforeApprove serving(Double serving) {
        this.setServing(serving);
        return this;
    }

    public void setServing(Double serving) {
        this.serving = serving;
    }

    public String getServingUom() {
        return this.servingUom;
    }

    public ProductBeforeApprove servingUom(String servingUom) {
        this.setServingUom(servingUom);
        return this;
    }

    public void setServingUom(String servingUom) {
        this.servingUom = servingUom;
    }

    public Double getSodium() {
        return this.sodium;
    }

    public ProductBeforeApprove sodium(Double sodium) {
        this.setSodium(sodium);
        return this;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public String getSodiumUom() {
        return this.sodiumUom;
    }

    public ProductBeforeApprove sodiumUom(String sodiumUom) {
        this.setSodiumUom(sodiumUom);
        return this;
    }

    public void setSodiumUom(String sodiumUom) {
        this.sodiumUom = sodiumUom;
    }

    public Integer getStorageTypeId() {
        return this.storageTypeId;
    }

    public ProductBeforeApprove storageTypeId(Integer storageTypeId) {
        this.setStorageTypeId(storageTypeId);
        return this;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public Integer getSubCategoryId() {
        return this.subCategoryId;
    }

    public ProductBeforeApprove subCategoryId(Integer subCategoryId) {
        this.setSubCategoryId(subCategoryId);
        return this;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Double getSugar() {
        return this.sugar;
    }

    public ProductBeforeApprove sugar(Double sugar) {
        this.setSugar(sugar);
        return this;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public String getSugarUom() {
        return this.sugarUom;
    }

    public ProductBeforeApprove sugarUom(String sugarUom) {
        this.setSugarUom(sugarUom);
        return this;
    }

    public void setSugarUom(String sugarUom) {
        this.sugarUom = sugarUom;
    }

    public Double getTotalFat() {
        return this.totalFat;
    }

    public ProductBeforeApprove totalFat(Double totalFat) {
        this.setTotalFat(totalFat);
        return this;
    }

    public void setTotalFat(Double totalFat) {
        this.totalFat = totalFat;
    }

    public Double getTransFat() {
        return this.transFat;
    }

    public ProductBeforeApprove transFat(Double transFat) {
        this.setTransFat(transFat);
        return this;
    }

    public void setTransFat(Double transFat) {
        this.transFat = transFat;
    }

    public String getuPC() {
        return this.uPC;
    }

    public ProductBeforeApprove uPC(String uPC) {
        this.setuPC(uPC);
        return this;
    }

    public void setuPC(String uPC) {
        this.uPC = uPC;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductBeforeApprove updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public ProductBeforeApprove updatedOn(LocalDate updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getVendor() {
        return this.vendor;
    }

    public ProductBeforeApprove vendor(String vendor) {
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
        if (!(o instanceof ProductBeforeApprove)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductBeforeApprove) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductBeforeApprove{" +
            "id=" + getId() +
            ", addedSugar=" + getAddedSugar() +
            ", addedSugarUom='" + getAddedSugarUom() + "'" +
            ", allergenKeywords='" + getAllergenKeywords() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", calories=" + getCalories() +
            ", caloriesUom='" + getCaloriesUom() + "'" +
            ", carbohydrates=" + getCarbohydrates() +
            ", carbohydratesUom='" + getCarbohydratesUom() + "'" +
            ", categoryId=" + getCategoryId() +
            ", cholesterol=" + getCholesterol() +
            ", cholesterolUOM='" + getCholesterolUOM() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", description='" + getDescription() + "'" +
            ", dietaryFiber=" + getDietaryFiber() +
            ", dietaryFiberUom='" + getDietaryFiberUom() + "'" +
            ", distributorId='" + getDistributorId() + "'" +
            ", gTIN='" + getgTIN() + "'" +
            ", ingredients='" + getIngredients() + "'" +
            ", iocCategoryId=" + getIocCategoryId() +
            ", isActive='" + getIsActive() + "'" +
            ", isMerge='" + getIsMerge() + "'" +
            ", manufacturerId=" + getManufacturerId() +
            ", manufacturerProductCode='" + getManufacturerProductCode() + "'" +
            ", mergeDate='" + getMergeDate() + "'" +
            ", productId=" + getProductId() +
            ", productLabelPdfUrl='" + getProductLabelPdfUrl() + "'" +
            ", productName='" + getProductName() + "'" +
            ", protein=" + getProtein() +
            ", proteinUom='" + getProteinUom() + "'" +
            ", saturatedFat=" + getSaturatedFat() +
            ", serving=" + getServing() +
            ", servingUom='" + getServingUom() + "'" +
            ", sodium=" + getSodium() +
            ", sodiumUom='" + getSodiumUom() + "'" +
            ", storageTypeId=" + getStorageTypeId() +
            ", subCategoryId=" + getSubCategoryId() +
            ", sugar=" + getSugar() +
            ", sugarUom='" + getSugarUom() + "'" +
            ", totalFat=" + getTotalFat() +
            ", transFat=" + getTransFat() +
            ", uPC='" + getuPC() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", vendor='" + getVendor() + "'" +
            "}";
    }
}
