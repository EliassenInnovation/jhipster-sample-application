package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupportCategory.
 */
@Entity
@Table(name = "support_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "supportcategory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupportCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "support_category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer supportCategoryId;

    @Column(name = "support_category_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String supportCategoryName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupportCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSupportCategoryId() {
        return this.supportCategoryId;
    }

    public SupportCategory supportCategoryId(Integer supportCategoryId) {
        this.setSupportCategoryId(supportCategoryId);
        return this;
    }

    public void setSupportCategoryId(Integer supportCategoryId) {
        this.supportCategoryId = supportCategoryId;
    }

    public String getSupportCategoryName() {
        return this.supportCategoryName;
    }

    public SupportCategory supportCategoryName(String supportCategoryName) {
        this.setSupportCategoryName(supportCategoryName);
        return this;
    }

    public void setSupportCategoryName(String supportCategoryName) {
        this.supportCategoryName = supportCategoryName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupportCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((SupportCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupportCategory{" +
            "id=" + getId() +
            ", supportCategoryId=" + getSupportCategoryId() +
            ", supportCategoryName='" + getSupportCategoryName() + "'" +
            "}";
    }
}
