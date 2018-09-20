/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "orders")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
            @NamedQuery(name = "Orders.findByUser", query = "SELECT o FROM Orders o WHERE o.user = :user"),
            @NamedQuery(name = "Orders.findByOrderId", query = "SELECT o FROM Orders o WHERE o.orderId = :orderId"),
            @NamedQuery(name = "Orders.findByOrderDate", query = "SELECT o FROM Orders o WHERE o.orderDate = :orderDate"),
            @NamedQuery(name = "Orders.findByModificationDate", query = "SELECT o FROM Orders o WHERE o.modificationDate = :modificationDate"),
            @NamedQuery(name = "Orders.findByStatusType", query = "SELECT o FROM Orders o WHERE o.statusType = :statusType")
        })
public class Orders implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "order_id")
    private Integer orderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "order_comments")
    private String orderComments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "status_type")
    private String statusType;
    @JsonIgnore
    @JoinColumn(name = "editing_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users editingUser;
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Users user;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItems> orderItemsList;

    public Orders()
    {
    }

    public Orders(Users editingUser)
    {
        Date today = Calendar.getInstance().getTime();
        this.orderId = 0;
        this.orderDate = today;
        this.orderComments = "New";
        this.modificationDate = today;
        this.statusType = "NEW";
        this.editingUser = editingUser;
        this.user = editingUser;
    }

    public Integer getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Integer orderId)
    {
        this.orderId = orderId;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public String getOrderComments()
    {
        return orderComments;
    }

    public void setOrderComments(String orderComments)
    {
        this.orderComments = orderComments;
    }

    public Date getModificationDate()
    {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    public String getStatusType()
    {
        return statusType;
    }

    public void setStatusType(String statusType)
    {
        this.statusType = statusType;
    }

    public Users getEditingUser()
    {
        return editingUser;
    }

    public void setEditingUser(Users editingUser)
    {
        this.editingUser = editingUser;
    }

    public Users getUser()
    {
        return user;
    }

    public void setUser(Users user)
    {
        this.user = user;
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
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders))
        {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.managedagents.entities.Orders[ orderId=" + orderId + " ]";
    }

}
