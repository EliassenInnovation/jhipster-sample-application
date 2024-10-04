package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AllergenMst.
 */
@Entity
@Table(name = "allergen_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "allergenmst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllergenMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "allergen_group")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allergenGroup;

    @Column(name = "allergen_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer allergenId;

    @Column(name = "allergen_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String allergenName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AllergenMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAllergenGroup() {
        return this.allergenGroup;
    }

    public AllergenMst allergenGroup(String allergenGroup) {
        this.setAllergenGroup(allergenGroup);
        return this;
    }

    public void setAllergenGroup(String allergenGroup) {
        this.allergenGroup = allergenGroup;
    }

    public Integer getAllergenId() {
        return this.allergenId;
    }

    public AllergenMst allergenId(Integer allergenId) {
        this.setAllergenId(allergenId);
        return this;
    }

    public void setAllergenId(Integer allergenId) {
        this.allergenId = allergenId;
    }

    public String getAllergenName() {
        return this.allergenName;
    }

    public AllergenMst allergenName(String allergenName) {
        this.setAllergenName(allergenName);
        return this;
    }

    public void setAllergenName(String allergenName) {
        this.allergenName = allergenName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllergenMst)) {
            return false;
        }
        return getId() != null && getId().equals(((AllergenMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllergenMst{" +
            "id=" + getId() +
            ", allergenGroup='" + getAllergenGroup() + "'" +
            ", allergenId=" + getAllergenId() +
            ", allergenName='" + getAllergenName() + "'" +
            "}";
    }
}
