package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StorageTypes.
 */
@Entity
@Table(name = "storage_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "storagetypes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StorageTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "storage_type_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer storageTypeId;

    @Column(name = "storage_type_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String storageTypeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StorageTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStorageTypeId() {
        return this.storageTypeId;
    }

    public StorageTypes storageTypeId(Integer storageTypeId) {
        this.setStorageTypeId(storageTypeId);
        return this;
    }

    public void setStorageTypeId(Integer storageTypeId) {
        this.storageTypeId = storageTypeId;
    }

    public String getStorageTypeName() {
        return this.storageTypeName;
    }

    public StorageTypes storageTypeName(String storageTypeName) {
        this.setStorageTypeName(storageTypeName);
        return this;
    }

    public void setStorageTypeName(String storageTypeName) {
        this.storageTypeName = storageTypeName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageTypes)) {
            return false;
        }
        return getId() != null && getId().equals(((StorageTypes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageTypes{" +
            "id=" + getId() +
            ", storageTypeId=" + getStorageTypeId() +
            ", storageTypeName='" + getStorageTypeName() + "'" +
            "}";
    }
}
