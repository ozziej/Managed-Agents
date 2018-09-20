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
    @NamedQuery(name = "CompanyUsers.findByCompanyUserId", query = "SELECT c FROM CompanyUsers c WHERE c.companyUserId = :companyUserId"),
    @NamedQuery(name = "CompanyUsers.findByChangeSettings", query = "SELECT c FROM CompanyUsers c WHERE c.changeSettings = :changeSettings")
})
public class CompanyUsers implements Serializable
{

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
    @JsonIgnore
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @ManyToOne(optional = false)
    private Companies companyId;
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Users user;

    public CompanyUsers()
    {
    }

    public CompanyUsers(Integer companyUserId)
    {
        this.companyUserId = companyUserId;
    }

    public CompanyUsers(Integer companyUserId, short changeSettings)
    {
        this.companyUserId = companyUserId;
        this.changeSettings = changeSettings;
    }

    public Integer getCompanyUserId()
    {
        return companyUserId;
    }

    public void setCompanyUserId(Integer companyUserId)
    {
        this.companyUserId = companyUserId;
    }

    public short getChangeSettings()
    {
        return changeSettings;
    }

    public void setChangeSettings(short changeSettings)
    {
        this.changeSettings = changeSettings;
    }

    public Companies getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(Companies companyId)
    {
        this.companyId = companyId;
    }

    public Users getUser()
    {
        return user;
    }

    public void setUser(Users user)
    {
        this.user = user;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (companyUserId != null ? companyUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyUsers))
        {
            return false;
        }
        CompanyUsers other = (CompanyUsers) object;
        if ((this.companyUserId == null && other.companyUserId != null) || (this.companyUserId != null && !this.companyUserId.equals(other.companyUserId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.managedagents.entities.CompanyUsers[ companyUserId=" + companyUserId + " ]";
    }
    
}
