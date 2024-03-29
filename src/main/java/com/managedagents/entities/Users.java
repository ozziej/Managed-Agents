/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import com.managedagents.constants.UsersStatus;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "users")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
            @NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
            @NamedQuery(name = "Users.findBySurname", query = "SELECT u FROM Users u WHERE u.surname = :surname"),
            @NamedQuery(name = "Users.findByGender", query = "SELECT u FROM Users u WHERE u.gender = :gender"),
            @NamedQuery(name = "Users.findByEmailAddress", query = "SELECT u FROM Users u WHERE u.emailAddress = :emailAddress"),
            @NamedQuery(name = "Users.findByPhoneNumber", query = "SELECT u FROM Users u WHERE u.phoneNumber = :phoneNumber"),
            @NamedQuery(name = "Users.findByCellNumber", query = "SELECT u FROM Users u WHERE u.cellNumber = :cellNumber"),
            @NamedQuery(name = "Users.findOtherByUser", query = "SELECT DISTINCT c.user FROM CompanyUsers c LEFT JOIN FETCH c.user WHERE c.company IN "
                    + "(SELECT cu.company from CompanyUsers cu WHERE cu.user = :user)"),
            @NamedQuery(name = "Users.findByUserStatus", query = "SELECT u FROM Users u WHERE u.userStatus = :userStatus")
        })
public class Users implements Serializable {

    @OneToMany(mappedBy = "user")
    private Collection<Appointments> appointmentsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 28)
    @Column(name = "user_pass")
    private String userPass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "user_salt")
    private String userSalt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "other_name")
    private String otherName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "gender")
    private String gender;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "email_address")
    private String emailAddress;
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
    @Column(name = "postal_address")
    private String postalAddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Basic(optional = false)
    @NotNull
    @Column(name = "first_registered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstRegistered;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "user_status")
    private String userStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Collection<CompanyUsers> companyUsersList;

    public Users() {
        this.userId = 0;
        this.userPass = "";
        this.userSalt = "";
        this.title = "Mr.";
        this.firstName = "First Name";
        this.surname = "Surname";
        this.otherName = "";
        this.gender = "MALE";
        this.emailAddress = "someone@somewhere.com";
        this.postalAddress = "South Africa";
        this.userStatus = "NEW";
        this.phoneNumber = "+27";
        this.cellNumber = "+27";
        this.firstRegistered = Calendar.getInstance().getTime();
        this.dateOfBirth = Calendar.getInstance().getTime();
    }

    public Users(Integer userId, String userPass, String userSalt, String title, String firstName, String surname, String otherName, String gender, String emailAddress, String phoneNumber, String cellNumber, String postalAddress, Date dateOfBirth, Date firstRegistered, String userStatus) {
        this.userId = userId;
        this.userPass = userPass;
        this.userSalt = userSalt;
        this.title = title;
        this.firstName = firstName;
        this.surname = surname;
        this.otherName = otherName;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.cellNumber = cellNumber;
        this.postalAddress = postalAddress;
        this.dateOfBirth = dateOfBirth;
        this.firstRegistered = firstRegistered;
        this.userStatus = userStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getFirstRegistered() {
        return firstRegistered;
    }

    public void setFirstRegistered(Date firstRegistered) {
        this.firstRegistered = firstRegistered;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    
    @XmlTransient
    public boolean isAdminUser(){
        return this.userStatus.equals(UsersStatus.ADMIN.toString());
    }
    
    @XmlTransient
    public boolean isSalesUser(){
        return this.userStatus.equals(UsersStatus.USER.toString());
    }

    @XmlTransient
    public Collection<CompanyUsers> getCompanyUsersList() {
        return companyUsersList;
    }

    public void setCompanyUsersList(Collection<CompanyUsers> companyUsersList) {
        this.companyUsersList = companyUsersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        return !((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId)));
    }

    @Override
    public String toString() {
        return userId.toString();
    }

    @XmlTransient
    public Collection<Appointments> getAppointmentsCollection() {
        return appointmentsCollection;
    }

    public void setAppointmentsCollection(Collection<Appointments> appointmentsCollection) {
        this.appointmentsCollection = appointmentsCollection;
    }

}
