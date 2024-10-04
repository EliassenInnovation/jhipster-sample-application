package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationValue.
 */
@Entity
@Table(name = "application_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "applicationvalue")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "application_value_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer applicationValueId;

    @Column(name = "jhi_key")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String key;

    @Column(name = "value_date")
    private ZonedDateTime valueDate;

    @Column(name = "value_int")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer valueInt;

    @Column(name = "value_long")
    private Long valueLong;

    @Column(name = "value_string")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String valueString;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApplicationValueId() {
        return this.applicationValueId;
    }

    public ApplicationValue applicationValueId(Integer applicationValueId) {
        this.setApplicationValueId(applicationValueId);
        return this;
    }

    public void setApplicationValueId(Integer applicationValueId) {
        this.applicationValueId = applicationValueId;
    }

    public String getKey() {
        return this.key;
    }

    public ApplicationValue key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ZonedDateTime getValueDate() {
        return this.valueDate;
    }

    public ApplicationValue valueDate(ZonedDateTime valueDate) {
        this.setValueDate(valueDate);
        return this;
    }

    public void setValueDate(ZonedDateTime valueDate) {
        this.valueDate = valueDate;
    }

    public Integer getValueInt() {
        return this.valueInt;
    }

    public ApplicationValue valueInt(Integer valueInt) {
        this.setValueInt(valueInt);
        return this;
    }

    public void setValueInt(Integer valueInt) {
        this.valueInt = valueInt;
    }

    public Long getValueLong() {
        return this.valueLong;
    }

    public ApplicationValue valueLong(Long valueLong) {
        this.setValueLong(valueLong);
        return this;
    }

    public void setValueLong(Long valueLong) {
        this.valueLong = valueLong;
    }

    public String getValueString() {
        return this.valueString;
    }

    public ApplicationValue valueString(String valueString) {
        this.setValueString(valueString);
        return this;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationValue)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationValue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationValue{" +
            "id=" + getId() +
            ", applicationValueId=" + getApplicationValueId() +
            ", key='" + getKey() + "'" +
            ", valueDate='" + getValueDate() + "'" +
            ", valueInt=" + getValueInt() +
            ", valueLong=" + getValueLong() +
            ", valueString='" + getValueString() + "'" +
            "}";
    }
}
