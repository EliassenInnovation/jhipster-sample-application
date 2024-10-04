package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SuggestedProducts.
 */
@Entity
@Table(name = "suggested_products")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "suggestedproducts")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuggestedProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "is_approve")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isApprove;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "suggested_by_district")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer suggestedByDistrict;

    @Column(name = "suggested_by_user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer suggestedByUserId;

    @Column(name = "suggested_product_id")
    private Long suggestedProductId;

    @Column(name = "suggestion_date")
    private LocalDate suggestionDate;

    @Column(name = "suggestion_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer suggestionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuggestedProducts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SuggestedProducts isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsApprove() {
        return this.isApprove;
    }

    public SuggestedProducts isApprove(Boolean isApprove) {
        this.setIsApprove(isApprove);
        return this;
    }

    public void setIsApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }

    public Long getProductId() {
        return this.productId;
    }

    public SuggestedProducts productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getSuggestedByDistrict() {
        return this.suggestedByDistrict;
    }

    public SuggestedProducts suggestedByDistrict(Integer suggestedByDistrict) {
        this.setSuggestedByDistrict(suggestedByDistrict);
        return this;
    }

    public void setSuggestedByDistrict(Integer suggestedByDistrict) {
        this.suggestedByDistrict = suggestedByDistrict;
    }

    public Integer getSuggestedByUserId() {
        return this.suggestedByUserId;
    }

    public SuggestedProducts suggestedByUserId(Integer suggestedByUserId) {
        this.setSuggestedByUserId(suggestedByUserId);
        return this;
    }

    public void setSuggestedByUserId(Integer suggestedByUserId) {
        this.suggestedByUserId = suggestedByUserId;
    }

    public Long getSuggestedProductId() {
        return this.suggestedProductId;
    }

    public SuggestedProducts suggestedProductId(Long suggestedProductId) {
        this.setSuggestedProductId(suggestedProductId);
        return this;
    }

    public void setSuggestedProductId(Long suggestedProductId) {
        this.suggestedProductId = suggestedProductId;
    }

    public LocalDate getSuggestionDate() {
        return this.suggestionDate;
    }

    public SuggestedProducts suggestionDate(LocalDate suggestionDate) {
        this.setSuggestionDate(suggestionDate);
        return this;
    }

    public void setSuggestionDate(LocalDate suggestionDate) {
        this.suggestionDate = suggestionDate;
    }

    public Integer getSuggestionId() {
        return this.suggestionId;
    }

    public SuggestedProducts suggestionId(Integer suggestionId) {
        this.setSuggestionId(suggestionId);
        return this;
    }

    public void setSuggestionId(Integer suggestionId) {
        this.suggestionId = suggestionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuggestedProducts)) {
            return false;
        }
        return getId() != null && getId().equals(((SuggestedProducts) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuggestedProducts{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", isApprove='" + getIsApprove() + "'" +
            ", productId=" + getProductId() +
            ", suggestedByDistrict=" + getSuggestedByDistrict() +
            ", suggestedByUserId=" + getSuggestedByUserId() +
            ", suggestedProductId=" + getSuggestedProductId() +
            ", suggestionDate='" + getSuggestionDate() + "'" +
            ", suggestionId=" + getSuggestionId() +
            "}";
    }
}
