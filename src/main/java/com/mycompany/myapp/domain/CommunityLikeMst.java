package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommunityLikeMst.
 */
@Entity
@Table(name = "community_like_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "communitylikemst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunityLikeMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "community_like_id")
    private UUID communityLikeId;

    @Column(name = "community_post_id")
    private UUID communityPostId;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "is_liked")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isLiked;

    @Column(name = "liked_by_user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer likedByUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommunityLikeMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCommunityLikeId() {
        return this.communityLikeId;
    }

    public CommunityLikeMst communityLikeId(UUID communityLikeId) {
        this.setCommunityLikeId(communityLikeId);
        return this;
    }

    public void setCommunityLikeId(UUID communityLikeId) {
        this.communityLikeId = communityLikeId;
    }

    public UUID getCommunityPostId() {
        return this.communityPostId;
    }

    public CommunityLikeMst communityPostId(UUID communityPostId) {
        this.setCommunityPostId(communityPostId);
        return this;
    }

    public void setCommunityPostId(UUID communityPostId) {
        this.communityPostId = communityPostId;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public CommunityLikeMst createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public CommunityLikeMst isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsLiked() {
        return this.isLiked;
    }

    public CommunityLikeMst isLiked(Boolean isLiked) {
        this.setIsLiked(isLiked);
        return this;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Integer getLikedByUserId() {
        return this.likedByUserId;
    }

    public CommunityLikeMst likedByUserId(Integer likedByUserId) {
        this.setLikedByUserId(likedByUserId);
        return this;
    }

    public void setLikedByUserId(Integer likedByUserId) {
        this.likedByUserId = likedByUserId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunityLikeMst)) {
            return false;
        }
        return getId() != null && getId().equals(((CommunityLikeMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityLikeMst{" +
            "id=" + getId() +
            ", communityLikeId='" + getCommunityLikeId() + "'" +
            ", communityPostId='" + getCommunityPostId() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isLiked='" + getIsLiked() + "'" +
            ", likedByUserId=" + getLikedByUserId() +
            "}";
    }
}
