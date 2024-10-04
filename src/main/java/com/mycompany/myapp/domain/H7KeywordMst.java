package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A H7KeywordMst.
 */
@Entity
@Table(name = "h_7_keyword_mst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "h7keywordmst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class H7KeywordMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "h_7_group")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String h7Group;

    @Column(name = "h_7_keyword")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String h7Keyword;

    @Column(name = "h_7_keyword_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer h7keywordId;

    @Column(name = "ioc_group")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iocGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public H7KeywordMst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String geth7Group() {
        return this.h7Group;
    }

    public H7KeywordMst h7Group(String h7Group) {
        this.seth7Group(h7Group);
        return this;
    }

    public void seth7Group(String h7Group) {
        this.h7Group = h7Group;
    }

    public String geth7Keyword() {
        return this.h7Keyword;
    }

    public H7KeywordMst h7Keyword(String h7Keyword) {
        this.seth7Keyword(h7Keyword);
        return this;
    }

    public void seth7Keyword(String h7Keyword) {
        this.h7Keyword = h7Keyword;
    }

    public Integer geth7keywordId() {
        return this.h7keywordId;
    }

    public H7KeywordMst h7keywordId(Integer h7keywordId) {
        this.seth7keywordId(h7keywordId);
        return this;
    }

    public void seth7keywordId(Integer h7keywordId) {
        this.h7keywordId = h7keywordId;
    }

    public String getIocGroup() {
        return this.iocGroup;
    }

    public H7KeywordMst iocGroup(String iocGroup) {
        this.setIocGroup(iocGroup);
        return this;
    }

    public void setIocGroup(String iocGroup) {
        this.iocGroup = iocGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof H7KeywordMst)) {
            return false;
        }
        return getId() != null && getId().equals(((H7KeywordMst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "H7KeywordMst{" +
            "id=" + getId() +
            ", h7Group='" + geth7Group() + "'" +
            ", h7Keyword='" + geth7Keyword() + "'" +
            ", h7keywordId=" + geth7keywordId() +
            ", iocGroup='" + getIocGroup() + "'" +
            "}";
    }
}
