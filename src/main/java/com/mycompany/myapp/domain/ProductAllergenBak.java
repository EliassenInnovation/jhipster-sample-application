package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductAllergenBak.
 */
@Entity
@Table(name = "product_allergen_bak")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productallergenbak")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductAllergenBak implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "allergen_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer allergenId;

    @Column(name = "allergen_group")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allergenGroup;

    @Column(name = "allergen_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allergenName;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "g_tin")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gTIN;

    @Column(name = "g_tinupc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gTINUPC;

    @Column(name = "product_allergen_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer productAllergenId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "u_pc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uPC;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductAllergenBak id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAllergenId() {
        return this.allergenId;
    }

    public ProductAllergenBak allergenId(Integer allergenId) {
        this.setAllergenId(allergenId);
        return this;
    }

    public void setAllergenId(Integer allergenId) {
        this.allergenId = allergenId;
    }

    public String getAllergenGroup() {
        return this.allergenGroup;
    }

    public ProductAllergenBak allergenGroup(String allergenGroup) {
        this.setAllergenGroup(allergenGroup);
        return this;
    }

    public void setAllergenGroup(String allergenGroup) {
        this.allergenGroup = allergenGroup;
    }

    public String getAllergenName() {
        return this.allergenName;
    }

    public ProductAllergenBak allergenName(String allergenName) {
        this.setAllergenName(allergenName);
        return this;
    }

    public void setAllergenName(String allergenName) {
        this.allergenName = allergenName;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductAllergenBak description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getgTIN() {
        return this.gTIN;
    }

    public ProductAllergenBak gTIN(String gTIN) {
        this.setgTIN(gTIN);
        return this;
    }

    public void setgTIN(String gTIN) {
        this.gTIN = gTIN;
    }

    public String getgTINUPC() {
        return this.gTINUPC;
    }

    public ProductAllergenBak gTINUPC(String gTINUPC) {
        this.setgTINUPC(gTINUPC);
        return this;
    }

    public void setgTINUPC(String gTINUPC) {
        this.gTINUPC = gTINUPC;
    }

    public Integer getProductAllergenId() {
        return this.productAllergenId;
    }

    public ProductAllergenBak productAllergenId(Integer productAllergenId) {
        this.setProductAllergenId(productAllergenId);
        return this;
    }

    public void setProductAllergenId(Integer productAllergenId) {
        this.productAllergenId = productAllergenId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductAllergenBak productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getuPC() {
        return this.uPC;
    }

    public ProductAllergenBak uPC(String uPC) {
        this.setuPC(uPC);
        return this;
    }

    public void setuPC(String uPC) {
        this.uPC = uPC;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductAllergenBak)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductAllergenBak) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductAllergenBak{" +
            "id=" + getId() +
            ", allergenId=" + getAllergenId() +
            ", allergenGroup='" + getAllergenGroup() + "'" +
            ", allergenName='" + getAllergenName() + "'" +
            ", description='" + getDescription() + "'" +
            ", gTIN='" + getgTIN() + "'" +
            ", gTINUPC='" + getgTINUPC() + "'" +
            ", productAllergenId=" + getProductAllergenId() +
            ", productId=" + getProductId() +
            ", uPC='" + getuPC() + "'" +
            "}";
    }
}
