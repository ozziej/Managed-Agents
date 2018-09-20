/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    @NamedQuery(name = "Users.findByUserPass", query = "SELECT u FROM Users u WHERE u.userPass = :userPass"),
    @NamedQuery(name = "Users.findByUserSalt", query = "SELECT u FROM Users u WHERE u.userSalt = :userSalt"),
    @NamedQuery(name = "Users.findByTitle", query = "SELECT u FROM Users u WHERE u.title = :title"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "Users.findBySurname", query = "SELECT u FROM Users u WHERE u.surname = :surname"),
    @NamedQuery(name = "Users.findByOtherName", query = "SELECT u FROM Users u WHERE u.otherName = :otherName"),
    @NamedQuery(name = "Users.findByGender", query = "SELECT u FROM Users u WHERE u.gender = :gender"),
    @NamedQuery(name = "Users.findByEmailAddress", query = "SELECT u FROM Users u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = "Users.findByCountry", query = "SELECT u FROM Users u WHERE u.country = :country"),
    @NamedQuery(name = "Users.findByCity", query = "SELECT u FROM Users u WHERE u.city = :city"),
    @NamedQuery(name = "Users.findBySuburb", query = "SELECT u FROM Users u WHERE u.suburb = :suburb"),
    @NamedQuery(name = "Users.findByPhoneNumber", query = "SELECT u FROM Users u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Users.findByCellNumber", query = "SELECT u FROM Users u WHERE u.cellNumber = :cellNumber"),
    @NamedQuery(name = "Users.findByDateOfBirth", query = "SELECT u FROM Users u WHERE u.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "Users.findByFirstRegistered", query = "SELECT u FROM Users u WHERE u.firstRegistered = :firstRegistered"),
    @NamedQuery(name = "Users.findByUserStatus", query = "SELECT u FROM Users u WHERE u.userStatus = :userStatus")
})
public class Users implements Serializable
{

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
    @Size(min = 1, max = 64)
    @Column(name = "country")
    private String country;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "suburb")
    private String suburb;
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
    @Column(name = "other_details")
    private String otherDetails;
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
    @OneToMany(mappedBy = "editingUser")
    private List<Orders> ordersList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<CompanyUsers> companyUsersList;
    @OneToMany(mappedBy = "userId")
    private List<OrderItems> orderItemsList;

    public Users()
    {
        this.userId = 0;
        this.title = "Mr.";
        this.firstName = "First Name";
        this.surname = "Surname";
        this.otherName = "";
        this.gender = "MALE";
        this.emailAddress = "someone@somewhere.com";
        this.country = "South Africa";
        this.city = "Johannesburg";
        this.suburb = "";
        this.otherDetails = "";
        this.userStatus = "NEW";
        this.phoneNumber = "+27";
        this.cellNumber = "+27";
        this.firstRegistered = Calendar.getInstance().getTime();
        this.dateOfBirth = Calendar.getInstance().getTime();
    }

    public Users(Integer userId)
    {
        this.userId = userId;
    }

    public Users(Integer userId, String userPass, String userSalt, String title, String firstName, String surname, String otherName, String gender, String emailAddress, String country, String city, String suburb, String phoneNumber, String cellNumber, String otherDetails, Date dateOfBirth, Date firstRegistered, String userStatus)
    {
        this.userId = userId;
        this.userPass = userPass;
        this.userSalt = userSalt;
        this.title = title;
        this.firstName = firstName;
        this.surname = surname;
        this.otherName = otherName;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.country = country;
        this.city = city;
        this.suburb = suburb;
        this.phoneNumber = phoneNumber;
        this.cellNumber = cellNumber;
        this.otherDetails = otherDetails;
        this.dateOfBirth = dateOfBirth;
        this.firstRegistered = firstRegistered;
        this.userStatus = userStatus;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUserPass()
    {
        return userPass;
    }

    public void setUserPass(String userPass)
    {
        this.userPass = userPass;
    }

    public String getUserSalt()
    {
        return userSalt;
    }

    public void setUserSalt(String userSalt)
    {
        this.userSalt = userSalt;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getOtherName()
    {
        return otherName;
    }

    public void setOtherName(String otherName)
    {
        this.otherName = otherName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getSuburb()
    {
        return suburb;
    }

    public void setSuburb(String suburb)
    {
        this.suburb = suburb;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getCellNumber()
    {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber)
    {
        this.cellNumber = cellNumber;
    }

    public String getOtherDetails()
    {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails)
    {
        this.otherDetails = otherDetails;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getFirstRegistered()
    {
        return firstRegistered;
    }

    public void setFirstRegistered(Date firstRegistered)
    {
        this.firstRegistered = firstRegistered;
    }

    public String getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(String userStatus)
    {
        this.userStatus = userStatus;
    }

    @XmlTransient
    public List<Orders> getOrdersList()
    {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList)
    {
        this.ordersList = ordersList;
    }

    @XmlTransient
    public List<CompanyUsers> getCompanyUsersList()
    {
        return companyUsersList;
    }

    public void setCompanyUsersList(List<CompanyUsers> companyUsersList)
    {
        this.companyUsersList = companyUsersList;
    }

    @XmlTransient
    public List<OrderItems> getOrderItemsList()
    {
        return orderItemsList;
    }

    public void setOrderItemsList(List<OrderItems> orderItemsList)
    {
        this.orderItemsList = orderItemsList;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users))
        {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.managedagents.entities.Users[ userId=" + userId + " ]";
    }
    
}
