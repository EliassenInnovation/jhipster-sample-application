package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductGtinAllocation.
 */
@Entity
@Table(name = "product_gtin_allocation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productgtinallocation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductGtinAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "g_tin")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gTIN;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "product_gtin_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer productGtinId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductGtinAllocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ProductGtinAllocation createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public ProductGtinAllocation createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getgTIN() {
        return this.gTIN;
    }

    public ProductGtinAllocation gTIN(String gTIN) {
        this.setgTIN(gTIN);
        return this;
    }

    public void setgTIN(String gTIN) {
        this.gTIN = gTIN;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProductGtinAllocation isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getProductGtinId() {
        return this.productGtinId;
    }

    public ProductGtinAllocation productGtinId(Integer productGtinId) {
        this.setProductGtinId(productGtinId);
        return this;
    }

    public void setProductGtinId(Integer productGtinId) {
        this.productGtinId = productGtinId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductGtinAllocation productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductGtinAllocation updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public ProductGtinAllocation updatedOn(LocalDate updatedOn) {
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
        if (!(o instanceof ProductGtinAllocation)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductGtinAllocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductGtinAllocation{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", gTIN='" + getgTIN() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", productGtinId=" + getProductGtinId() +
            ", productId=" + getProductId() +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
