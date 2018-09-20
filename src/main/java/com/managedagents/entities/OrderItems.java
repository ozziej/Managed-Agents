/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author james
 */
@Entity
@Table(name = "order_items")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "OrderItems.findAll", query = "SELECT o FROM OrderItems o"),
    @NamedQuery(name = "OrderItems.findByItemId", query = "SELECT o FROM OrderItems o WHERE o.itemId = :itemId"),
    @NamedQuery(name = "OrderItems.findByItemDiscount", query = "SELECT o FROM OrderItems o WHERE o.itemDiscount = :itemDiscount"),
    @NamedQuery(name = "OrderItems.findByItemPrice", query = "SELECT o FROM OrderItems o WHERE o.itemPrice = :itemPrice"),
    @NamedQuery(name = "OrderItems.findByItemQuantity", query = "SELECT o FROM OrderItems o WHERE o.itemQuantity = :itemQuantity")
})
public class OrderItems implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "item_id")
    private Integer itemId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "item_description")
    private String itemDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_discount")
    private double itemDiscount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_price")
    private double itemPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_quantity")
    private int itemQuantity;
    @JsonBackReference
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @ManyToOne(optional = false)
    private Orders order;
    @JsonIgnore
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(optional = false)
    private Products product;
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users userId;

    public OrderItems()
    {
    }

    public OrderItems(Integer itemId)
    {
        this.itemId = itemId;
    }

    public OrderItems(Integer itemId, String itemDescription, double itemDiscount, double itemPrice, int itemQuantity)
    {
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.itemDiscount = itemDiscount;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public Integer getItemId()
    {
        return itemId;
    }

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    public String getItemDescription()
    {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    public double getItemDiscount()
    {
        return itemDiscount;
    }

    public void setItemDiscount(double itemDiscount)
    {
        this.itemDiscount = itemDiscount;
    }

    public double getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice)
    {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity()
    {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity)
    {
        this.itemQuantity = itemQuantity;
    }

    public Orders getOrder()
    {
        return order;
    }

    public void setOrder(Orders order)
    {
        this.order = order;
    }

    public Products getProduct()
    {
        return product;
    }

    public void setProduct(Products product)
    {
        this.product = product;
    }

    public Users getUserId()
    {
        return userId;
    }

    public void setUserId(Users userId)
    {
        this.userId = userId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderItems))
        {
            return false;
        }
        OrderItems other = (OrderItems) object;
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "com.managedagents.entities.OrderItems[ itemId=" + itemId + " ]";
    }
    
}
