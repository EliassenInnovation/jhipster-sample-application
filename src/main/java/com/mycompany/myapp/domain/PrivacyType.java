package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrivacyType.
 */
@Entity
@Table(name = "privacy_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "privacytype")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrivacyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "privacy_type_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer privacyTypeId;

    @Column(name = "privacy_type_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String privacyTypeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrivacyType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrivacyTypeId() {
        return this.privacyTypeId;
    }

    public PrivacyType privacyTypeId(Integer privacyTypeId) {
        this.setPrivacyTypeId(privacyTypeId);
        return this;
    }

    public void setPrivacyTypeId(Integer privacyTypeId) {
        this.privacyTypeId = privacyTypeId;
    }

    public String getPrivacyTypeName() {
        return this.privacyTypeName;
    }

    public PrivacyType privacyTypeName(String privacyTypeName) {
        this.setPrivacyTypeName(privacyTypeName);
        return this;
    }

    public void setPrivacyTypeName(String privacyTypeName) {
        this.privacyTypeName = privacyTypeName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrivacyType)) {
            return false;
        }
        return getId() != null && getId().equals(((PrivacyType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrivacyType{" +
            "id=" + getId() +
            ", privacyTypeId=" + getPrivacyTypeId() +
            ", privacyTypeName='" + getPrivacyTypeName() + "'" +
            "}";
    }
}
