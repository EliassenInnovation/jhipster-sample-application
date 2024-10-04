package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductManufacturerAllocation.
 */
@Entity
@Table(name = "product_manufacturer_allocation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productmanufacturerallocation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductManufacturerAllocation implements Serializable {

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

    @Column(name = "is_allocated")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAllocated;

    @Column(name = "manufacture_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer manufactureId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_manufacturer_allocation_id")
    private UUID productManufacturerAllocationId;

    @Column(name = "updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductManufacturerAllocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ProductManufacturerAllocation createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public ProductManufacturerAllocation createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsAllocated() {
        return this.isAllocated;
    }

    public ProductManufacturerAllocation isAllocated(Boolean isAllocated) {
        this.setIsAllocated(isAllocated);
        return this;
    }

    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Integer getManufactureId() {
        return this.manufactureId;
    }

    public ProductManufacturerAllocation manufactureId(Integer manufactureId) {
        this.setManufactureId(manufactureId);
        return this;
    }

    public void setManufactureId(Integer manufactureId) {
        this.manufactureId = manufactureId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductManufacturerAllocation productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public UUID getProductManufacturerAllocationId() {
        return this.productManufacturerAllocationId;
    }

    public ProductManufacturerAllocation productManufacturerAllocationId(UUID productManufacturerAllocationId) {
        this.setProductManufacturerAllocationId(productManufacturerAllocationId);
        return this;
    }

    public void setProductManufacturerAllocationId(UUID productManufacturerAllocationId) {
        this.productManufacturerAllocationId = productManufacturerAllocationId;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductManufacturerAllocation updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public ProductManufacturerAllocation updatedOn(LocalDate updatedOn) {
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
        if (!(o instanceof ProductManufacturerAllocation)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductManufacturerAllocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductManufacturerAllocation{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", isAllocated='" + getIsAllocated() + "'" +
            ", manufactureId=" + getManufactureId() +
            ", productId=" + getProductId() +
            ", productManufacturerAllocationId='" + getProductManufacturerAllocationId() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
