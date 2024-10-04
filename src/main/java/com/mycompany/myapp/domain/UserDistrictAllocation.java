package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserDistrictAllocation.
 */
@Entity
@Table(name = "user_district_allocation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userdistrictallocation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDistrictAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "is_allocated")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAllocated;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    @Column(name = "user_district_allocation_id")
    private UUID userDistrictAllocationId;

    @Column(name = "user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserDistrictAllocation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public UserDistrictAllocation createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public UserDistrictAllocation createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsAllocated() {
        return this.isAllocated;
    }

    public UserDistrictAllocation isAllocated(Boolean isAllocated) {
        this.setIsAllocated(isAllocated);
        return this;
    }

    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public UserDistrictAllocation schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public UserDistrictAllocation updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public UserDistrictAllocation updatedOn(LocalDate updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UUID getUserDistrictAllocationId() {
        return this.userDistrictAllocationId;
    }

    public UserDistrictAllocation userDistrictAllocationId(UUID userDistrictAllocationId) {
        this.setUserDistrictAllocationId(userDistrictAllocationId);
        return this;
    }

    public void setUserDistrictAllocationId(UUID userDistrictAllocationId) {
        this.userDistrictAllocationId = userDistrictAllocationId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public UserDistrictAllocation userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDistrictAllocation)) {
            return false;
        }
        return getId() != null && getId().equals(((UserDistrictAllocation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDistrictAllocation{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", isAllocated='" + getIsAllocated() + "'" +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", userDistrictAllocationId='" + getUserDistrictAllocationId() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
