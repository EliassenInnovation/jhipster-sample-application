package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductChangeHistory.
 */
@Entity
@Table(name = "product_change_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productchangehistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "history_id")
    private UUID historyId;

    @Column(name = "ioc_category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer iocCategoryId;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "selection_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String selectionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductChangeHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ProductChangeHistory createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public ProductChangeHistory dateCreated(LocalDate dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UUID getHistoryId() {
        return this.historyId;
    }

    public ProductChangeHistory historyId(UUID historyId) {
        this.setHistoryId(historyId);
        return this;
    }

    public void setHistoryId(UUID historyId) {
        this.historyId = historyId;
    }

    public Integer getIocCategoryId() {
        return this.iocCategoryId;
    }

    public ProductChangeHistory iocCategoryId(Integer iocCategoryId) {
        this.setIocCategoryId(iocCategoryId);
        return this;
    }

    public void setIocCategoryId(Integer iocCategoryId) {
        this.iocCategoryId = iocCategoryId;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProductChangeHistory isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductChangeHistory productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public ProductChangeHistory schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public String getSelectionType() {
        return this.selectionType;
    }

    public ProductChangeHistory selectionType(String selectionType) {
        this.setSelectionType(selectionType);
        return this;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductChangeHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductChangeHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductChangeHistory{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", historyId='" + getHistoryId() + "'" +
            ", iocCategoryId=" + getIocCategoryId() +
            ", isActive='" + getIsActive() + "'" +
            ", productId=" + getProductId() +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", selectionType='" + getSelectionType() + "'" +
            "}";
    }
}
