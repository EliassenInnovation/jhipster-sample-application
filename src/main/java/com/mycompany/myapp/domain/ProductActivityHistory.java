package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductActivityHistory.
 */
@Entity
@Table(name = "product_activity_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productactivityhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductActivityHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "activity_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer activityId;

    @Column(name = "activity_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String activityType;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "suggested_product_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer suggestedProductId;

    @Column(name = "updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductActivityHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return this.activityId;
    }

    public ProductActivityHistory activityId(Integer activityId) {
        this.setActivityId(activityId);
        return this;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return this.activityType;
    }

    public ProductActivityHistory activityType(String activityType) {
        this.setActivityType(activityType);
        return this;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ProductActivityHistory createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public ProductActivityHistory createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ProductActivityHistory date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProductActivityHistory isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductActivityHistory productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getSuggestedProductId() {
        return this.suggestedProductId;
    }

    public ProductActivityHistory suggestedProductId(Integer suggestedProductId) {
        this.setSuggestedProductId(suggestedProductId);
        return this;
    }

    public void setSuggestedProductId(Integer suggestedProductId) {
        this.suggestedProductId = suggestedProductId;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductActivityHistory updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public ProductActivityHistory updatedOn(LocalDate updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductActivityHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductActivityHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductActivityHistory{" +
            "id=" + getId() +
            ", activityId=" + getActivityId() +
            ", activityType='" + getActivityType() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", date='" + getDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", productId=" + getProductId() +
            ", suggestedProductId=" + getSuggestedProductId() +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
