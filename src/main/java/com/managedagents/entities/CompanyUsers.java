/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author james
 */
@Entity
@Table(name = "company_users")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "CompanyUsers.findAll", query = "SELECT c FROM CompanyUsers c"),
            @NamedQuery(name = "CompanyUsers.findByUser", query = "SELECT c FROM CompanyUsers c LEFT JOIN FETCH c.company WHERE c.user = :user"),
            @NamedQuery(name = "CompanyUsers.findOtherByUser", query = "SELECT c FROM CompanyUsers c LEFT JOIN FETCH c.user WHERE c.company IN "
                    + "(SELECT DISTINCT cu.company from CompanyUsers cu WHERE cu.user = :user)"),
            @NamedQuery(name = "CompanyUsers.findByCompany", query = "SELECT c FROM CompanyUsers c LEFT JOIN FETCH c.user WHERE c.company = :company"),
        })
public class CompanyUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "company_user_id")
    private Integer companyUserId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "change_settings")
    private short changeSettings;
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @ManyToOne(optional = false)
    private Companies company;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Users user;

    public CompanyUsers() {
        this(new Companies(), new Users(), (short) 0);
    }

    public CompanyUsers(Companies company, Users user, short changeSettings) {
        this.companyUserId = 0;
        this.company = company;
        this.user = user;
        this.changeSettings = changeSettings;
    }

    public Integer getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(Integer companyUserId) {
        this.companyUserId = companyUserId;
    }

    public short getChangeSettings() {
        return changeSettings;
    }

    public void setChangeSettings(short changeSettings) {
        this.changeSettings = changeSettings;
    }
    
    public boolean getChangeSettingsBoolean(){
        return changeSettings == 1;
    }
    
    public void setChangeSettingsBoolean(boolean changeSettingsBoolean){
        this.changeSettings = changeSettingsBoolean?(short)1:(short)0;
    }

    public Companies getCompany() {
        return company;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyUserId != null ? companyUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CompanyUsers)) {
            return false;
        }
        CompanyUsers other = (CompanyUsers) object;
        return !((this.companyUserId == null && other.companyUserId != null) || (this.companyUserId != null && !this.companyUserId.equals(other.companyUserId)));
    }

    @Override
    public String toString() {
        return "com.managedagents.entities.CompanyUsers[ companyUserId=" + companyUserId + " ]";
    }

}
