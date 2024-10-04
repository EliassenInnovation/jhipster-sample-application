package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductH7New.
 */
@Entity
@Table(name = "product_h_7_new")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "producth7new")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductH7New implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "gtin_upc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gtinUpc;

    @Column(name = "h_7_keyword_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer h7KeywordId;

    @Column(name = "i_oc_group")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iOCGroup;

    @Column(name = "product_h_7_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer productH7Id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductH7New id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGtinUpc() {
        return this.gtinUpc;
    }

    public ProductH7New gtinUpc(String gtinUpc) {
        this.setGtinUpc(gtinUpc);
        return this;
    }

    public void setGtinUpc(String gtinUpc) {
        this.gtinUpc = gtinUpc;
    }

    public Integer geth7KeywordId() {
        return this.h7KeywordId;
    }

    public ProductH7New h7KeywordId(Integer h7KeywordId) {
        this.seth7KeywordId(h7KeywordId);
        return this;
    }

    public void seth7KeywordId(Integer h7KeywordId) {
        this.h7KeywordId = h7KeywordId;
    }

    public String getiOCGroup() {
        return this.iOCGroup;
    }

    public ProductH7New iOCGroup(String iOCGroup) {
        this.setiOCGroup(iOCGroup);
        return this;
    }

    public void setiOCGroup(String iOCGroup) {
        this.iOCGroup = iOCGroup;
    }

    public Integer getProductH7Id() {
        return this.productH7Id;
    }

    public ProductH7New productH7Id(Integer productH7Id) {
        this.setProductH7Id(productH7Id);
        return this;
    }

    public void setProductH7Id(Integer productH7Id) {
        this.productH7Id = productH7Id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ProductH7New productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public ProductH7New productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductH7New)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductH7New) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductH7New{" +
            "id=" + getId() +
            ", gtinUpc='" + getGtinUpc() + "'" +
            ", h7KeywordId=" + geth7KeywordId() +
            ", iOCGroup='" + getiOCGroup() + "'" +
            ", productH7Id=" + getProductH7Id() +
            ", productId=" + getProductId() +
            ", productName='" + getProductName() + "'" +
            "}";
    }
}
