package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReplacedProducts.
 */
@Entity
@Table(name = "replaced_products")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "replacedproducts")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReplacedProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "replaced_by_user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer replacedByUserId;

    @Column(name = "replaced_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer replacedId;

    @Column(name = "replaced_product_id")
    private Long replacedProductId;

    @Column(name = "replacement_date")
    private LocalDate replacementDate;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReplacedProducts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ReplacedProducts isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ReplacedProducts productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getReplacedByUserId() {
        return this.replacedByUserId;
    }

    public ReplacedProducts replacedByUserId(Integer replacedByUserId) {
        this.setReplacedByUserId(replacedByUserId);
        return this;
    }

    public void setReplacedByUserId(Integer replacedByUserId) {
        this.replacedByUserId = replacedByUserId;
    }

    public Integer getReplacedId() {
        return this.replacedId;
    }

    public ReplacedProducts replacedId(Integer replacedId) {
        this.setReplacedId(replacedId);
        return this;
    }

    public void setReplacedId(Integer replacedId) {
        this.replacedId = replacedId;
    }

    public Long getReplacedProductId() {
        return this.replacedProductId;
    }

    public ReplacedProducts replacedProductId(Long replacedProductId) {
        this.setReplacedProductId(replacedProductId);
        return this;
    }

    public void setReplacedProductId(Long replacedProductId) {
        this.replacedProductId = replacedProductId;
    }

    public LocalDate getReplacementDate() {
        return this.replacementDate;
    }

    public ReplacedProducts replacementDate(LocalDate replacementDate) {
        this.setReplacementDate(replacementDate);
        return this;
    }

    public void setReplacementDate(LocalDate replacementDate) {
        this.replacementDate = replacementDate;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public ReplacedProducts schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReplacedProducts)) {
            return false;
        }
        return getId() != null && getId().equals(((ReplacedProducts) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReplacedProducts{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", productId=" + getProductId() +
            ", replacedByUserId=" + getReplacedByUserId() +
            ", replacedId=" + getReplacedId() +
            ", replacedProductId=" + getReplacedProductId() +
            ", replacementDate='" + getReplacementDate() + "'" +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            "}";
    }
}
