package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SetMappings.
 */
@Entity
@Table(name = "set_mappings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "setmappings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SetMappings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer iD;

    @Column(name = "one_world_value")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String oneWorldValue;

    @Column(name = "product_value")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String productValue;

    @Column(name = "set_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String setName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SetMappings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiD() {
        return this.iD;
    }

    public SetMappings iD(Integer iD) {
        this.setiD(iD);
        return this;
    }

    public void setiD(Integer iD) {
        this.iD = iD;
    }

    public String getOneWorldValue() {
        return this.oneWorldValue;
    }

    public SetMappings oneWorldValue(String oneWorldValue) {
        this.setOneWorldValue(oneWorldValue);
        return this;
    }

    public void setOneWorldValue(String oneWorldValue) {
        this.oneWorldValue = oneWorldValue;
    }

    public String getProductValue() {
        return this.productValue;
    }

    public SetMappings productValue(String productValue) {
        this.setProductValue(productValue);
        return this;
    }

    public void setProductValue(String productValue) {
        this.productValue = productValue;
    }

    public String getSetName() {
        return this.setName;
    }

    public SetMappings setName(String setName) {
        this.setSetName(setName);
        return this;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SetMappings)) {
            return false;
        }
        return getId() != null && getId().equals(((SetMappings) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SetMappings{" +
            "id=" + getId() +
            ", iD=" + getiD() +
            ", oneWorldValue='" + getOneWorldValue() + "'" +
            ", productValue='" + getProductValue() + "'" +
            ", setName='" + getSetName() + "'" +
            "}";
    }
}
