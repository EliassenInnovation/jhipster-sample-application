package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BlockReportPost.
 */
@Entity
@Table(name = "block_report_post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "blockreportpost")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlockReportPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "block_categories")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String blockCategories;

    @Lob
    @Column(name = "blocking_reason")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String blockingReason;

    @Column(name = "post_block_report_id")
    private UUID postBlockReportId;

    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "post_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String postType;

    @Column(name = "requested_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer requestedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BlockReportPost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlockCategories() {
        return this.blockCategories;
    }

    public BlockReportPost blockCategories(String blockCategories) {
        this.setBlockCategories(blockCategories);
        return this;
    }

    public void setBlockCategories(String blockCategories) {
        this.blockCategories = blockCategories;
    }

    public String getBlockingReason() {
        return this.blockingReason;
    }

    public BlockReportPost blockingReason(String blockingReason) {
        this.setBlockingReason(blockingReason);
        return this;
    }

    public void setBlockingReason(String blockingReason) {
        this.blockingReason = blockingReason;
    }

    public UUID getPostBlockReportId() {
        return this.postBlockReportId;
    }

    public BlockReportPost postBlockReportId(UUID postBlockReportId) {
        this.setPostBlockReportId(postBlockReportId);
        return this;
    }

    public void setPostBlockReportId(UUID postBlockReportId) {
        this.postBlockReportId = postBlockReportId;
    }

    public UUID getPostId() {
        return this.postId;
    }

    public BlockReportPost postId(UUID postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return this.postType;
    }

    public BlockReportPost postType(String postType) {
        this.setPostType(postType);
        return this;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Integer getRequestedBy() {
        return this.requestedBy;
    }

    public BlockReportPost requestedBy(Integer requestedBy) {
        this.setRequestedBy(requestedBy);
        return this;
    }

    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockReportPost)) {
            return false;
        }
        return getId() != null && getId().equals(((BlockReportPost) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockReportPost{" +
            "id=" + getId() +
            ", blockCategories='" + getBlockCategories() + "'" +
            ", blockingReason='" + getBlockingReason() + "'" +
            ", postBlockReportId='" + getPostBlockReportId() + "'" +
            ", postId='" + getPostId() + "'" +
            ", postType='" + getPostType() + "'" +
            ", requestedBy=" + getRequestedBy() +
            "}";
    }
}
