package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonthMst.
 */
@Entity
@Table(name = "month_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "monthmst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonthMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "month_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer monthID;

    @Column(name = "month_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String monthName;

    @Column(name = "year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String year;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonthMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MonthMst isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMonthID() {
        return this.monthID;
    }

    public MonthMst monthID(Integer monthID) {
        this.setMonthID(monthID);
        return this;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
    }

    public String getMonthName() {
        return this.monthName;
    }

    public MonthMst monthName(String monthName) {
        this.setMonthName(monthName);
        return this;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getYear() {
        return this.year;
    }

    public MonthMst year(String year) {
        this.setYear(year);
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthMst)) {
            return false;
        }
        return getId() != null && getId().equals(((MonthMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonthMst{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", monthID=" + getMonthID() +
            ", monthName='" + getMonthName() + "'" +
            ", year='" + getYear() + "'" +
            "}";
    }
}
