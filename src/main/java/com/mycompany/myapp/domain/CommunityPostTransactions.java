package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommunityPostTransactions.
 */
@Entity
@Table(name = "community_post_transactions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "communityposttransactions")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunityPostTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "attachment_url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String attachmentUrl;

    @Column(name = "community_post_id")
    private UUID communityPostId;

    @Column(name = "community_post_transaction_id")
    private UUID communityPostTransactionId;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "last_updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer lastUpdatedBy;

    @Column(name = "last_updated_on")
    private LocalDate lastUpdatedOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommunityPostTransactions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentUrl() {
        return this.attachmentUrl;
    }

    public CommunityPostTransactions attachmentUrl(String attachmentUrl) {
        this.setAttachmentUrl(attachmentUrl);
        return this;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public UUID getCommunityPostId() {
        return this.communityPostId;
    }

    public CommunityPostTransactions communityPostId(UUID communityPostId) {
        this.setCommunityPostId(communityPostId);
        return this;
    }

    public void setCommunityPostId(UUID communityPostId) {
        this.communityPostId = communityPostId;
    }

    public UUID getCommunityPostTransactionId() {
        return this.communityPostTransactionId;
    }

    public CommunityPostTransactions communityPostTransactionId(UUID communityPostTransactionId) {
        this.setCommunityPostTransactionId(communityPostTransactionId);
        return this;
    }

    public void setCommunityPostTransactionId(UUID communityPostTransactionId) {
        this.communityPostTransactionId = communityPostTransactionId;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public CommunityPostTransactions createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public CommunityPostTransactions createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public CommunityPostTransactions isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public CommunityPostTransactions lastUpdatedBy(Integer lastUpdatedBy) {
        this.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public CommunityPostTransactions lastUpdatedOn(LocalDate lastUpdatedOn) {
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
        if (!(o instanceof CommunityPostTransactions)) {
            return false;
        }
        return getId() != null && getId().equals(((CommunityPostTransactions) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityPostTransactions{" +
            "id=" + getId() +
            ", attachmentUrl='" + getAttachmentUrl() + "'" +
            ", communityPostId='" + getCommunityPostId() + "'" +
            ", communityPostTransactionId='" + getCommunityPostTransactionId() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastUpdatedBy=" + getLastUpdatedBy() +
            ", lastUpdatedOn='" + getLastUpdatedOn() + "'" +
            "}";
    }
}
