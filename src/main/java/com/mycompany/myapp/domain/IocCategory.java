package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IocCategory.
 */
@Entity
@Table(name = "ioc_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ioccategory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IocCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ioc_category_color")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iocCategoryColor;

    @Column(name = "ioc_category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer iocCategoryId;

    @Column(name = "ioc_category_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iocCategoryName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IocCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIocCategoryColor() {
        return this.iocCategoryColor;
    }

    public IocCategory iocCategoryColor(String iocCategoryColor) {
        this.setIocCategoryColor(iocCategoryColor);
        return this;
    }

    public void setIocCategoryColor(String iocCategoryColor) {
        this.iocCategoryColor = iocCategoryColor;
    }

    public Integer getIocCategoryId() {
        return this.iocCategoryId;
    }

    public IocCategory iocCategoryId(Integer iocCategoryId) {
        this.setIocCategoryId(iocCategoryId);
        return this;
    }

    public void setIocCategoryId(Integer iocCategoryId) {
        this.iocCategoryId = iocCategoryId;
    }

    public String getIocCategoryName() {
        return this.iocCategoryName;
    }

    public IocCategory iocCategoryName(String iocCategoryName) {
        this.setIocCategoryName(iocCategoryName);
        return this;
    }

    public void setIocCategoryName(String iocCategoryName) {
        this.iocCategoryName = iocCategoryName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IocCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((IocCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IocCategory{" +
            "id=" + getId() +
            ", iocCategoryColor='" + getIocCategoryColor() + "'" +
            ", iocCategoryId=" + getIocCategoryId() +
            ", iocCategoryName='" + getIocCategoryName() + "'" +
            "}";
    }
}
