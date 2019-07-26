/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author james
 */
@Entity
@Table(name = "companies")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Companies.findAll", query = "SELECT c FROM Companies c"),
            @NamedQuery(name = "Companies.findByCompanyId", query = "SELECT c FROM Companies c WHERE c.companyId = :companyId"),
            @NamedQuery(name = "Companies.findByCompanyName", query = "SELECT c FROM Companies c WHERE c.companyName = :companyName"),
            @NamedQuery(name = "Companies.findByPhoneNumber", query = "SELECT c FROM Companies c WHERE c.phoneNumber = :phoneNumber"),
            @NamedQuery(name = "Companies.findByCellNumber", query = "SELECT c FROM Companies c WHERE c.cellNumber = :cellNumber"),
            @NamedQuery(name = "Companies.findByVatNumber", query = "SELECT c FROM Companies c WHERE c.vatNumber = :vatNumber"),
            @NamedQuery(name = "Companies.findByModificationDate", query = "SELECT c FROM Companies c WHERE c.modificationDate = :modificationDate"),
            @NamedQuery(name = "Companies.findByLocationLatitude", query = "SELECT c FROM Companies c WHERE c.locationLatitude = :locationLatitude"),
            @NamedQuery(name = "Companies.findByLocationLongitude", query = "SELECT c FROM Companies c WHERE c.locationLongitude = :locationLongitude"),
            @NamedQuery(name = "Companies.findByUser", query = "SELECT c FROM Companies c LEFT OUTER JOIN c.companyUsersList cu "
                    + "ON cu.company = c WHERE cu.user = :user"),
            @NamedQuery(name = "Companies.findByCompanyStatus", query = "SELECT c FROM Companies c WHERE c.companyStatus = :companyStatus")

        })
public class Companies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "company_id")
    private Integer companyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "company_name")
    private String companyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 24)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 24)
    @Column(name = "cell_number")
    private String cellNumber;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "website_address")
    private String websiteAddress;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "physical_address")
    private String physicalAddress;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "postal_address")
    private String postalAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "vat_number")
    private String vatNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "location_latitude")
    private BigDecimal locationLatitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "location_longitude")
    private BigDecimal locationLongitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "company_status")
    private String companyStatus;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "image_uri")
    private String imageUri;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CompanyUsers> companyUsersList;

    public Companies() {
        this(0, "None", "011", "082", "None", "None", "None", "None", Calendar.getInstance().getTime(),
                new BigDecimal("-26.20532898"), new BigDecimal("28.04381490"), "OPEN", "NONE");
    }

    public Companies(Integer companyId, String companyName, String phoneNumber, String cellNumber, String websiteAddress, String physicalAddress, String postalAddress, String vatNumber, Date modificationDate, BigDecimal locationLatitude, BigDecimal locationLongitude, String companyStatus, String imageUri) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.cellNumber = cellNumber;
        this.websiteAddress = websiteAddress;
        this.physicalAddress = physicalAddress;
        this.postalAddress = postalAddress;
        this.vatNumber = vatNumber;
        this.modificationDate = modificationDate;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.companyStatus = companyStatus;
        this.imageUri = imageUri;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getWebsiteAddress() {
        return websiteAddress;
    }

    public void setWebsiteAddress(String websiteAddress) {
        this.websiteAddress = websiteAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public BigDecimal getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(BigDecimal locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public BigDecimal getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(BigDecimal locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    
    @XmlTransient
    public boolean isImageEmpty(){
        return this.imageUri.equals("NONE");
    }

    @XmlTransient
    public List<CompanyUsers> getCompanyUsersList() {
        return companyUsersList;
    }

    public void setCompanyUsersList(List<CompanyUsers> companyUsersList) {
        this.companyUsersList = companyUsersList;
    }
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyId != null ? companyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Companies)) {
            return false;
        }
        Companies other = (Companies) object;
        return !((this.companyId == null && other.companyId != null) || (this.companyId != null && !this.companyId.equals(other.companyId)));
    }

    @Override
    public String toString() {
        return companyId.toString();
    }

}
