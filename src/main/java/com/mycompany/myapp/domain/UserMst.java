package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserMst.
 */
@Entity
@Table(name = "user_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "usermst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "address_line_1")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String addressLine1;

    @Column(name = "address_line_2")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String addressLine2;

    @Column(name = "city")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String city;

    @Column(name = "country")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String country;

    @Lob
    @Column(name = "cover_image")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String coverImage;

    @Column(name = "create_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer createBy;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @Column(name = "first_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String firstName;

    @Column(name = "is_active")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isActive;

    @Column(name = "last_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastName;

    @Column(name = "manufacturer_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer manufacturerId;

    @Column(name = "mobile")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mobile;

    @Column(name = "object_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String objectId;

    @Column(name = "password")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String password;

    @Lob
    @Column(name = "profile_image")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String profileImage;

    @Column(name = "role_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer roleId;

    @Column(name = "school_district_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer schoolDistrictId;

    @Column(name = "state")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer state;

    @Column(name = "updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer updatedBy;

    @Column(name = "updated_on")
    private LocalDate updatedOn;

    @Column(name = "user_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer userId;

    @Column(name = "zip_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String zipCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public UserMst addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public UserMst addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public UserMst city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public UserMst country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public UserMst coverImage(String coverImage) {
        this.setCoverImage(coverImage);
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getCreateBy() {
        return this.createBy;
    }

    public UserMst createBy(Integer createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public UserMst createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getEmail() {
        return this.email;
    }

    public UserMst email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserMst firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UserMst isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserMst lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getManufacturerId() {
        return this.manufacturerId;
    }

    public UserMst manufacturerId(Integer manufacturerId) {
        this.setManufacturerId(manufacturerId);
        return this;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getMobile() {
        return this.mobile;
    }

    public UserMst mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public UserMst objectId(String objectId) {
        this.setObjectId(objectId);
        return this;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPassword() {
        return this.password;
    }

    public UserMst password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public UserMst profileImage(String profileImage) {
        this.setProfileImage(profileImage);
        return this;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public UserMst roleId(Integer roleId) {
        this.setRoleId(roleId);
        return this;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getSchoolDistrictId() {
        return this.schoolDistrictId;
    }

    public UserMst schoolDistrictId(Integer schoolDistrictId) {
        this.setSchoolDistrictId(schoolDistrictId);
        return this;
    }

    public void setSchoolDistrictId(Integer schoolDistrictId) {
        this.schoolDistrictId = schoolDistrictId;
    }

    public Integer getState() {
        return this.state;
    }

    public UserMst state(Integer state) {
        this.setState(state);
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public UserMst updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedOn() {
        return this.updatedOn;
    }

    public UserMst updatedOn(LocalDate updatedOn) {
        this.setUpdatedOn(updatedOn);
        return this;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public UserMst userId(Integer userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public UserMst zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMst)) {
            return false;
        }
        return getId() != null && getId().equals(((UserMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMst{" +
            "id=" + getId() +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", createBy=" + getCreateBy() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", manufacturerId=" + getManufacturerId() +
            ", mobile='" + getMobile() + "'" +
            ", objectId='" + getObjectId() + "'" +
            ", password='" + getPassword() + "'" +
            ", profileImage='" + getProfileImage() + "'" +
            ", roleId=" + getRoleId() +
            ", schoolDistrictId=" + getSchoolDistrictId() +
            ", state=" + getState() +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", userId=" + getUserId() +
            ", zipCode='" + getZipCode() + "'" +
            "}";
    }
}
