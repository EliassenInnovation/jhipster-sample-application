package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupportTicketTransaction.
 */
@Entity
@Table(name = "support_ticket_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "supporttickettransaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupportTicketTransaction implements Serializable {

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

    @Lob
    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "file_extension")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fileExtension;

    @Column(name = "file_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fileName;

    @Column(name = "file_path")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String filePath;

    @Column(name = "file_size")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer fileSize;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "is_sent_by_fig_support")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isSentByFigSupport;

    @Column(name = "last_updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer lastUpdatedBy;

    @Column(name = "last_updated_on")
    private LocalDate lastUpdatedOn;

    @Column(name = "ticket_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ticketId;

    @Column(name = "ticket_transaction_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ticketTransactionId;

    @Column(name = "user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupportTicketTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public SupportTicketTransaction createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public SupportTicketTransaction createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getDescription() {
        return this.description;
    }

    public SupportTicketTransaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public SupportTicketTransaction fileExtension(String fileExtension) {
        this.setFileExtension(fileExtension);
        return this;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileName() {
        return this.fileName;
    }

    public SupportTicketTransaction fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public SupportTicketTransaction filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public SupportTicketTransaction fileSize(Integer fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SupportTicketTransaction isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsSentByFigSupport() {
        return this.isSentByFigSupport;
    }

    public SupportTicketTransaction isSentByFigSupport(Boolean isSentByFigSupport) {
        this.setIsSentByFigSupport(isSentByFigSupport);
        return this;
    }

    public void setIsSentByFigSupport(Boolean isSentByFigSupport) {
        this.isSentByFigSupport = isSentByFigSupport;
    }

    public Integer getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public SupportTicketTransaction lastUpdatedBy(Integer lastUpdatedBy) {
        this.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public SupportTicketTransaction lastUpdatedOn(LocalDate lastUpdatedOn) {
        this.setLastUpdatedOn(lastUpdatedOn);
        return this;
    }

    public void setLastUpdatedOn(LocalDate lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Integer getTicketId() {
        return this.ticketId;
    }

    public SupportTicketTransaction ticketId(Integer ticketId) {
        this.setTicketId(ticketId);
        return this;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getTicketTransactionId() {
        return this.ticketTransactionId;
    }

    public SupportTicketTransaction ticketTransactionId(Integer ticketTransactionId) {
        this.setTicketTransactionId(ticketTransactionId);
        return this;
    }

    public void setTicketTransactionId(Integer ticketTransactionId) {
        this.ticketTransactionId = ticketTransactionId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public SupportTicketTransaction userId(Integer userId) {
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
        if (!(o instanceof SupportTicketTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((SupportTicketTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupportTicketTransaction{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileExtension='" + getFileExtension() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", isActive='" + getIsActive() + "'" +
            ", isSentByFigSupport='" + getIsSentByFigSupport() + "'" +
            ", lastUpdatedBy=" + getLastUpdatedBy() +
            ", lastUpdatedOn='" + getLastUpdatedOn() + "'" +
            ", ticketId=" + getTicketId() +
            ", ticketTransactionId=" + getTicketTransactionId() +
            ", userId=" + getUserId() +
            "}";
    }
}
