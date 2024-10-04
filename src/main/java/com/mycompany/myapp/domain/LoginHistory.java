package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LoginHistory.
 */
@Entity
@Table(name = "login_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "loginhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoginHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "forgot_pin")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer forgotPin;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "login_date")
    private LocalDate loginDate;

    @Column(name = "login_log_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer loginLogId;

    @Column(name = "login_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String loginType;

    @Column(name = "log_out_date")
    private LocalDate logOutDate;

    @Column(name = "user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoginHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getForgotPin() {
        return this.forgotPin;
    }

    public LoginHistory forgotPin(Integer forgotPin) {
        this.setForgotPin(forgotPin);
        return this;
    }

    public void setForgotPin(Integer forgotPin) {
        this.forgotPin = forgotPin;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public LoginHistory isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getLoginDate() {
        return this.loginDate;
    }

    public LoginHistory loginDate(LocalDate loginDate) {
        this.setLoginDate(loginDate);
        return this;
    }

    public void setLoginDate(LocalDate loginDate) {
        this.loginDate = loginDate;
    }

    public Integer getLoginLogId() {
        return this.loginLogId;
    }

    public LoginHistory loginLogId(Integer loginLogId) {
        this.setLoginLogId(loginLogId);
        return this;
    }

    public void setLoginLogId(Integer loginLogId) {
        this.loginLogId = loginLogId;
    }

    public String getLoginType() {
        return this.loginType;
    }

    public LoginHistory loginType(String loginType) {
        this.setLoginType(loginType);
        return this;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public LocalDate getLogOutDate() {
        return this.logOutDate;
    }

    public LoginHistory logOutDate(LocalDate logOutDate) {
        this.setLogOutDate(logOutDate);
        return this;
    }

    public void setLogOutDate(LocalDate logOutDate) {
        this.logOutDate = logOutDate;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public LoginHistory userId(Integer userId) {
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
        if (!(o instanceof LoginHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((LoginHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginHistory{" +
            "id=" + getId() +
            ", forgotPin=" + getForgotPin() +
            ", isActive='" + getIsActive() + "'" +
            ", loginDate='" + getLoginDate() + "'" +
            ", loginLogId=" + getLoginLogId() +
            ", loginType='" + getLoginType() + "'" +
            ", logOutDate='" + getLogOutDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
