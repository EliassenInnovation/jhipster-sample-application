package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommunityPost.
 */
@Entity
@Table(name = "community_post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "communitypost")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunityPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "community_post_id")
    private UUID communityPostId;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "date")
    private LocalDate date;

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "last_updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer lastUpdatedBy;

    @Column(name = "last_updated_on")
    private LocalDate lastUpdatedOn;

    @Column(name = "post_type_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer postTypeId;

    @Column(name = "privacy_type_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer privacyTypeId;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommunityPost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCommunityPostId() {
        return this.communityPostId;
    }

    public CommunityPost communityPostId(UUID communityPostId) {
        this.setCommunityPostId(communityPostId);
        return this;
    }

    public void setCommunityPostId(UUID communityPostId) {
        this.communityPostId = communityPostId;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public CommunityPost createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public CommunityPost createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CommunityPost date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public CommunityPost description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public CommunityPost isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public CommunityPost lastUpdatedBy(Integer lastUpdatedBy) {
        this.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public CommunityPost lastUpdatedOn(LocalDate lastUpdatedOn) {
        this.setLastUpdatedOn(lastUpdatedOn);
        return this;
    }

    public void setLastUpdatedOn(LocalDate lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Integer getPostTypeId() {
        return this.postTypeId;
    }

    public CommunityPost postTypeId(Integer postTypeId) {
        this.setPostTypeId(postTypeId);
        return this;
    }

    public void setPostTypeId(Integer postTypeId) {
        this.postTypeId = postTypeId;
    }

    public Integer getPrivacyTypeId() {
        return this.privacyTypeId;
    }

    public CommunityPost privacyTypeId(Integer privacyTypeId) {
        this.setPrivacyTypeId(privacyTypeId);
        return this;
    }

    public void setPrivacyTypeId(Integer privacyTypeId) {
        this.privacyTypeId = privacyTypeId;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public CommunityPost schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public CommunityPost userId(Integer userId) {
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
        if (!(o instanceof CommunityPost)) {
            return false;
        }
        return getId() != null && getId().equals(((CommunityPost) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityPost{" +
            "id=" + getId() +
            ", communityPostId='" + getCommunityPostId() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastUpdatedBy=" + getLastUpdatedBy() +
            ", lastUpdatedOn='" + getLastUpdatedOn() + "'" +
            ", postTypeId=" + getPostTypeId() +
            ", privacyTypeId=" + getPrivacyTypeId() +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", userId=" + getUserId() +
            "}";
    }
}
