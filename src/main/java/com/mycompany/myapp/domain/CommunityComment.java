package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommunityComment.
 */
@Entity
@Table(name = "community_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "communitycomment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunityComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "comment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String comment;

    @Column(name = "comment_by_user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer commentByUserId;

    @Column(name = "community_comment_id")
    private UUID communityCommentId;

    @Column(name = "community_post_id")
    private UUID communityPostId;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "last_updated_on")
    private LocalDate lastUpdatedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommunityComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return this.comment;
    }

    public CommunityComment comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCommentByUserId() {
        return this.commentByUserId;
    }

    public CommunityComment commentByUserId(Integer commentByUserId) {
        this.setCommentByUserId(commentByUserId);
        return this;
    }

    public void setCommentByUserId(Integer commentByUserId) {
        this.commentByUserId = commentByUserId;
    }

    public UUID getCommunityCommentId() {
        return this.communityCommentId;
    }

    public CommunityComment communityCommentId(UUID communityCommentId) {
        this.setCommunityCommentId(communityCommentId);
        return this;
    }

    public void setCommunityCommentId(UUID communityCommentId) {
        this.communityCommentId = communityCommentId;
    }

    public UUID getCommunityPostId() {
        return this.communityPostId;
    }

    public CommunityComment communityPostId(UUID communityPostId) {
        this.setCommunityPostId(communityPostId);
        return this;
    }

    public void setCommunityPostId(UUID communityPostId) {
        this.communityPostId = communityPostId;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public CommunityComment createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public CommunityComment isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public CommunityComment lastUpdatedOn(LocalDate lastUpdatedOn) {
        this.setLastUpdatedOn(lastUpdatedOn);
        return this;
    }

    public void setLastUpdatedOn(LocalDate lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunityComment)) {
            return false;
        }
        return getId() != null && getId().equals(((CommunityComment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityComment{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", commentByUserId=" + getCommentByUserId() +
            ", communityCommentId='" + getCommunityCommentId() + "'" +
            ", communityPostId='" + getCommunityPostId() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastUpdatedOn='" + getLastUpdatedOn() + "'" +
            "}";
    }
}
