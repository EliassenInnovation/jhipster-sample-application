package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SchoolDistrict.
 */
@Entity
@Table(name = "school_district")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schooldistrict")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SchoolDistrict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "city")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String city;

    @Column(name = "contract_company")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contractCompany;

    @Column(name = "country")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String country;

    @Column(name = "created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createdBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Lob
    @Column(name = "district_logo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String districtLogo;

    @Column(name = "district_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String districtName;

    @Column(name = "email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Column(name = "food_service_options")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String foodServiceOptions;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "last_updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer lastUpdatedBy;

    @Column(name = "last_updated_on")
    private LocalDate lastUpdatedOn;

    @Column(name = "phone_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phoneNumber;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "site_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String siteCode;

    @Column(name = "state")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer state;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SchoolDistrict id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return this.city;
    }

    public SchoolDistrict city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContractCompany() {
        return this.contractCompany;
    }

    public SchoolDistrict contractCompany(String contractCompany) {
        this.setContractCompany(contractCompany);
        return this;
    }

    public void setContractCompany(String contractCompany) {
        this.contractCompany = contractCompany;
    }

    public String getCountry() {
        return this.country;
    }

    public SchoolDistrict country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public SchoolDistrict createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public SchoolDistrict createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getDistrictLogo() {
        return this.districtLogo;
    }

    public SchoolDistrict districtLogo(String districtLogo) {
        this.setDistrictLogo(districtLogo);
        return this;
    }

    public void setDistrictLogo(String districtLogo) {
        this.districtLogo = districtLogo;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public SchoolDistrict districtName(String districtName) {
        this.setDistrictName(districtName);
        return this;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getEmail() {
        return this.email;
    }

    public SchoolDistrict email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoodServiceOptions() {
        return this.foodServiceOptions;
    }

    public SchoolDistrict foodServiceOptions(String foodServiceOptions) {
        this.setFoodServiceOptions(foodServiceOptions);
        return this;
    }

    public void setFoodServiceOptions(String foodServiceOptions) {
        this.foodServiceOptions = foodServiceOptions;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SchoolDistrict isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public SchoolDistrict lastUpdatedBy(Integer lastUpdatedBy) {
        this.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDate getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public SchoolDistrict lastUpdatedOn(LocalDate lastUpdatedOn) {
        this.setLastUpdatedOn(lastUpdatedOn);
        return this;
    }

    public void setLastUpdatedOn(LocalDate lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public SchoolDistrict phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public SchoolDistrict schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public String getSiteCode() {
        return this.siteCode;
    }

    public SchoolDistrict siteCode(String siteCode) {
        this.setSiteCode(siteCode);
        return this;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Integer getState() {
        return this.state;
    }

    public SchoolDistrict state(Integer state) {
        this.setState(state);
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolDistrict)) {
            return false;
        }
        return getId() != null && getId().equals(((SchoolDistrict) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchoolDistrict{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", contractCompany='" + getContractCompany() + "'" +
            ", country='" + getCountry() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", districtLogo='" + getDistrictLogo() + "'" +
            ", districtName='" + getDistrictName() + "'" +
            ", email='" + getEmail() + "'" +
            ", foodServiceOptions='" + getFoodServiceOptions() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastUpdatedBy=" + getLastUpdatedBy() +
            ", lastUpdatedOn='" + getLastUpdatedOn() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", siteCode='" + getSiteCode() + "'" +
            ", state=" + getState() +
            "}";
    }
}
