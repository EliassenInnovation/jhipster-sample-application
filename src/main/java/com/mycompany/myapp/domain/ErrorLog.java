package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ErrorLog.
 */
@Entity
@Table(name = "error_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "errorlog")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "error_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer errorId;

    @Lob
    @Column(name = "error_message")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorMessage;

    @Lob
    @Column(name = "error_path")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String errorPath;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ErrorLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public ErrorLog createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getErrorId() {
        return this.errorId;
    }

    public ErrorLog errorId(Integer errorId) {
        this.setErrorId(errorId);
        return this;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ErrorLog errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorPath() {
        return this.errorPath;
    }

    public ErrorLog errorPath(String errorPath) {
        this.setErrorPath(errorPath);
        return this;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorLog)) {
            return false;
        }
        return getId() != null && getId().equals(((ErrorLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ErrorLog{" +
            "id=" + getId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", errorId=" + getErrorId() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", errorPath='" + getErrorPath() + "'" +
            "}";
    }
}
