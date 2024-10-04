package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductsToUpdate.
 */
@Entity
@Table(name = "products_to_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productstoupdate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductsToUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "max_gln_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maxGLNCode;

    @Column(name = "max_manufacturer_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxManufacturerID;

    @Column(name = "product_id")
    private Long productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductsToUpdate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaxGLNCode() {
        return this.maxGLNCode;
    }

    public ProductsToUpdate maxGLNCode(String maxGLNCode) {
        this.setMaxGLNCode(maxGLNCode);
        return this;
    }

    public void setMaxGLNCode(String maxGLNCode) {
        this.maxGLNCode = maxGLNCode;
    }

    public Integer getMaxManufacturerID() {
        return this.maxManufacturerID;
    }

    public ProductsToUpdate maxManufacturerID(Integer maxManufacturerID) {
        this.setMaxManufacturerID(maxManufacturerID);
        return this;
    }

    public void setMaxManufacturerID(Integer maxManufacturerID) {
        this.maxManufacturerID = maxManufacturerID;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductsToUpdate productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductsToUpdate)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductsToUpdate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsToUpdate{" +
            "id=" + getId() +
            ", maxGLNCode='" + getMaxGLNCode() + "'" +
            ", maxManufacturerID=" + getMaxManufacturerID() +
            ", productId=" + getProductId() +
            "}";
    }
}
