package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonthlyNumbers.
 */
@Entity
@Table(name = "monthly_numbers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "monthlynumbers")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonthlyNumbers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "actual_month_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer actualMonthId;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "enrollment")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer enrollment;

    @Column(name = "free_and_reduced_percent")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer freeAndReducedPercent;

    @Column(name = "i_d")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer iD;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "meals_served")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer mealsServed;

    @Column(name = "modified_on")
    private LocalDate modifiedOn;

    @Column(name = "month_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer monthId;

    @Column(name = "number_of_districts")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numberOfDistricts;

    @Column(name = "number_of_sites")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numberOfSites;

    @Column(name = "reg_date")
    private LocalDate regDate;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String year;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonthlyNumbers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActualMonthId() {
        return this.actualMonthId;
    }

    public MonthlyNumbers actualMonthId(Integer actualMonthId) {
        this.setActualMonthId(actualMonthId);
        return this;
    }

    public void setActualMonthId(Integer actualMonthId) {
        this.actualMonthId = actualMonthId;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public MonthlyNumbers createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getEnrollment() {
        return this.enrollment;
    }

    public MonthlyNumbers enrollment(Integer enrollment) {
        this.setEnrollment(enrollment);
        return this;
    }

    public void setEnrollment(Integer enrollment) {
        this.enrollment = enrollment;
    }

    public Integer getFreeAndReducedPercent() {
        return this.freeAndReducedPercent;
    }

    public MonthlyNumbers freeAndReducedPercent(Integer freeAndReducedPercent) {
        this.setFreeAndReducedPercent(freeAndReducedPercent);
        return this;
    }

    public void setFreeAndReducedPercent(Integer freeAndReducedPercent) {
        this.freeAndReducedPercent = freeAndReducedPercent;
    }

    public Integer getiD() {
        return this.iD;
    }

    public MonthlyNumbers iD(Integer iD) {
        this.setiD(iD);
        return this;
    }

    public void setiD(Integer iD) {
        this.iD = iD;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MonthlyNumbers isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMealsServed() {
        return this.mealsServed;
    }

    public MonthlyNumbers mealsServed(Integer mealsServed) {
        this.setMealsServed(mealsServed);
        return this;
    }

    public void setMealsServed(Integer mealsServed) {
        this.mealsServed = mealsServed;
    }

    public LocalDate getModifiedOn() {
        return this.modifiedOn;
    }

    public MonthlyNumbers modifiedOn(LocalDate modifiedOn) {
        this.setModifiedOn(modifiedOn);
        return this;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getMonthId() {
        return this.monthId;
    }

    public MonthlyNumbers monthId(Integer monthId) {
        this.setMonthId(monthId);
        return this;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    public Integer getNumberOfDistricts() {
        return this.numberOfDistricts;
    }

    public MonthlyNumbers numberOfDistricts(Integer numberOfDistricts) {
        this.setNumberOfDistricts(numberOfDistricts);
        return this;
    }

    public void setNumberOfDistricts(Integer numberOfDistricts) {
        this.numberOfDistricts = numberOfDistricts;
    }

    public Integer getNumberOfSites() {
        return this.numberOfSites;
    }

    public MonthlyNumbers numberOfSites(Integer numberOfSites) {
        this.setNumberOfSites(numberOfSites);
        return this;
    }

    public void setNumberOfSites(Integer numberOfSites) {
        this.numberOfSites = numberOfSites;
    }

    public LocalDate getRegDate() {
        return this.regDate;
    }

    public MonthlyNumbers regDate(LocalDate regDate) {
        this.setRegDate(regDate);
        return this;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public MonthlyNumbers schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public String getYear() {
        return this.year;
    }

    public MonthlyNumbers year(String year) {
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
        if (!(o instanceof MonthlyNumbers)) {
            return false;
        }
        return getId() != null && getId().equals(((MonthlyNumbers) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonthlyNumbers{" +
            "id=" + getId() +
            ", actualMonthId=" + getActualMonthId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", enrollment=" + getEnrollment() +
            ", freeAndReducedPercent=" + getFreeAndReducedPercent() +
            ", iD=" + getiD() +
            ", isActive='" + getIsActive() + "'" +
            ", mealsServed=" + getMealsServed() +
            ", modifiedOn='" + getModifiedOn() + "'" +
            ", monthId=" + getMonthId() +
            ", numberOfDistricts=" + getNumberOfDistricts() +
            ", numberOfSites=" + getNumberOfSites() +
            ", regDate='" + getRegDate() + "'" +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", year='" + getYear() + "'" +
            "}";
    }
}
