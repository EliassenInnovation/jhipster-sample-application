package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupportTicketMst.
 */
@Entity
@Table(name = "support_ticket_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "supportticketmst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupportTicketMst implements Serializable {

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

    @Column(name = "email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "is_with_out_login")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isWithOutLogin;

    @Column(name = "last_updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer lastUpdatedBy;

    @Column(name = "last_updated_on")
    private LocalDate lastUpdatedOn;

    @Column(name = "priority")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String priority;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String status;

    @Column(name = "subject")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subject;

    @Column(name = "support_category_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer supportCategoryId;

    @Column(name = "ticket_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ticketId;

    @Column(name = "ticket_reference_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ticketReferenceNumber;

    @Column(name = "user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer userId;

    @Column(name = "user_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String userName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SupportTicketMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public SupportTicketMst createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public SupportTicketMst createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getEmail() {
        return this.email;
    }

    public SupportTicketMst email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SupportTicketMst isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsWithOutLogin() {
        return this.isWithOutLogin;
    }

    public SupportTicketMst isWithOutLogin(Boolean isWithOutLogin) {
        this.setIsWithOutLogin(isWithOutLogin);
        return this;
    }

    public void setIsWithOutLogin(Boolean isWithOutLogin) {
        this.isWithOutLogin = isWithOutLogin;
    }

    public Integer getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public SupportTicketMst lastUpdatedBy(Integer lastUpdatedBy) {
        this.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public SupportTicketMst lastUpdatedOn(LocalDate lastUpdatedOn) {
        this.setLastUpdatedOn(lastUpdatedOn);
        return this;
    }

    public void setLastUpdatedOn(LocalDate lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getPriority() {
        return this.priority;
    }

    public SupportTicketMst priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public SupportTicketMst schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public String getStatus() {
        return this.status;
    }

    public SupportTicketMst status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return this.subject;
    }

    public SupportTicketMst subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getSupportCategoryId() {
        return this.supportCategoryId;
    }

    public SupportTicketMst supportCategoryId(Integer supportCategoryId) {
        this.setSupportCategoryId(supportCategoryId);
        return this;
    }

    public void setSupportCategoryId(Integer supportCategoryId) {
        this.supportCategoryId = supportCategoryId;
    }

    public Integer getTicketId() {
        return this.ticketId;
    }

    public SupportTicketMst ticketId(Integer ticketId) {
        this.setTicketId(ticketId);
        return this;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getTicketReferenceNumber() {
        return this.ticketReferenceNumber;
    }

    public SupportTicketMst ticketReferenceNumber(Integer ticketReferenceNumber) {
        this.setTicketReferenceNumber(ticketReferenceNumber);
        return this;
    }

    public void setTicketReferenceNumber(Integer ticketReferenceNumber) {
        this.ticketReferenceNumber = ticketReferenceNumber;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public SupportTicketMst userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public SupportTicketMst userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupportTicketMst)) {
            return false;
        }
        return getId() != null && getId().equals(((SupportTicketMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupportTicketMst{" +
            "id=" + getId() +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", email='" + getEmail() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isWithOutLogin='" + getIsWithOutLogin() + "'" +
            ", lastUpdatedBy=" + getLastUpdatedBy() +
            ", lastUpdatedOn='" + getLastUpdatedOn() + "'" +
            ", priority='" + getPriority() + "'" +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", status='" + getStatus() + "'" +
            ", subject='" + getSubject() + "'" +
            ", supportCategoryId=" + getSupportCategoryId() +
            ", ticketId=" + getTicketId() +
            ", ticketReferenceNumber=" + getTicketReferenceNumber() +
            ", userId=" + getUserId() +
            ", userName='" + getUserName() + "'" +
            "}";
    }
}
