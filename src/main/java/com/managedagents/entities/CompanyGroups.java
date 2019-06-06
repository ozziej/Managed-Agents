/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author james
 */
@Entity
@Table(name = "company_groups")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "CompanyGroups.findAll", query = "SELECT c FROM CompanyGroups c"),
            @NamedQuery(name = "CompanyGroups.findByCompanyGroupId", query = "SELECT c FROM CompanyGroups c WHERE c.companyGroupId = :companyGroupId")
        })

public class CompanyGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "company_group_id")
    private Integer companyGroupId;
    @JsonIgnore
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @ManyToOne(optional = false)
    private Companies company;
    @JsonIgnore
    @JoinColumn(name = "parent_company_id", referencedColumnName = "company_id")
    @ManyToOne(optional = false)
    private Companies parentCompanyId;

    public CompanyGroups() {
        this.companyGroupId = 0;
        this.company = new Companies();
        this.parentCompanyId = new Companies();
    }

    public Integer getCompanyGroupId() {
        return companyGroupId;
    }

    public void setCompanyGroupId(Integer companyGroupId) {
        this.companyGroupId = companyGroupId;
    }

    public Companies getCompany() {
        return company;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public Companies getParentCompanyId() {
        return parentCompanyId;
    }

    public void setParentCompanyId(Companies parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyGroupId != null ? companyGroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CompanyGroups)) {
            return false;
        }
        CompanyGroups other = (CompanyGroups) object;
        return !((this.companyGroupId == null && other.companyGroupId != null) || (this.companyGroupId != null && !this.companyGroupId.equals(other.companyGroupId)));
    }

    @Override
    public String toString() {
        return "com.managedagents.entities.CompanyGroups[ companyGroupId=" + companyGroupId + " ]";
    }

}
