package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductDistributorAllocation.
 */
@Entity
@Table(name = "product_distributor_allocation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productdistributorallocation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDistributorAllocation implements Serializable {

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

    @Column(name = "distributor_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer distributorId;

    @Column(name = "is_allocated")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAllocated;

    @Column(name = "product_distributor_allocation_id")
    private UUID productDistributorAllocationId;

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

    public ProductDistributorAllocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public ProductDistributorAllocation createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public ProductDistributorAllocation createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getDistributorId() {
        return this.distributorId;
    }

    public ProductDistributorAllocation distributorId(Integer distributorId) {
        this.setDistributorId(distributorId);
        return this;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Boolean getIsAllocated() {
        return this.isAllocated;
    }

    public ProductDistributorAllocation isAllocated(Boolean isAllocated) {
        this.setIsAllocated(isAllocated);
        return this;
    }

    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public UUID getProductDistributorAllocationId() {
        return this.productDistributorAllocationId;
    }

    public ProductDistributorAllocation productDistributorAllocationId(UUID productDistributorAllocationId) {
        this.setProductDistributorAllocationId(productDistributorAllocationId);
        return this;
    }

    public void setProductDistributorAllocationId(UUID productDistributorAllocationId) {
        this.productDistributorAllocationId = productDistributorAllocationId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductDistributorAllocation productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductDistributorAllocation updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public ProductDistributorAllocation updatedOn(LocalDate updatedOn) {
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
        if (!(o instanceof ProductDistributorAllocation)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductDistributorAllocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDistributorAllocation{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", distributorId=" + getDistributorId() +
            ", isAllocated='" + getIsAllocated() + "'" +
            ", productDistributorAllocationId='" + getProductDistributorAllocationId() + "'" +
            ", productId=" + getProductId() +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
