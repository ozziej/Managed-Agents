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
@Table(name = "products")
@XmlRootElement
@NamedQueries(
        {
            @NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p"),
            @NamedQuery(name = "Products.findByProductId", query = "SELECT p FROM Products p WHERE p.productId = :productId"),
            @NamedQuery(name = "Products.findByProductSingleType", query = "SELECT p FROM Products p WHERE p.productSingleType = :productSingleType"),
            @NamedQuery(name = "Products.findByProductMaximum", query = "SELECT p FROM Products p WHERE p.productMaximum = :productMaximum"),
            @NamedQuery(name = "Products.findByProductPrice", query = "SELECT p FROM Products p WHERE p.productPrice = :productPrice"),
            @NamedQuery(name = "Products.findByProductValidFrom", query = "SELECT p FROM Products p WHERE p.productValidFrom = :productValidFrom"),
            @NamedQuery(name = "Products.findByProductValidUntil", query = "SELECT p FROM Products p WHERE p.productValidUntil = :productValidUntil")
        })
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "product_id")
    private Integer productId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "product_description")
    private String productDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_single_type")
    private short productSingleType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_maximum")
    private int productMaximum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_price")
    private double productPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_valid_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date productValidFrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_valid_until")
    @Temporal(TemporalType.TIMESTAMP)
    private Date productValidUntil;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private List<ProductImages> productImagesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<OrderItems> orderItemsList;

    public Products() {
        this(0, "None", (short) 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
    }

    public Products(Integer productId, String productDescription, short productSingleType, int productMaximum, double productPrice, Date productValidFrom, Date productValidUntil) {
        this.productId = productId;
        this.productDescription = productDescription;
        this.productSingleType = productSingleType;
        this.productMaximum = productMaximum;
        this.productPrice = productPrice;
        this.productValidFrom = productValidFrom;
        this.productValidUntil = productValidUntil;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public short getProductSingleType() {
        return productSingleType;
    }

    public void setProductSingleType(short productSingleType) {
        this.productSingleType = productSingleType;
    }

    public int getProductMaximum() {
        return productMaximum;
    }

    public void setProductMaximum(int productMaximum) {
        this.productMaximum = productMaximum;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Date getProductValidFrom() {
        return productValidFrom;
    }

    public void setProductValidFrom(Date productValidFrom) {
        this.productValidFrom = productValidFrom;
    }

    public Date getProductValidUntil() {
        return productValidUntil;
    }

    public void setProductValidUntil(Date productValidUntil) {
        this.productValidUntil = productValidUntil;
    }

    @XmlTransient
    public List<ProductImages> getProductImagesList() {
        return productImagesList;
    }

    public void setProductImagesList(List<ProductImages> productImagesList) {
        this.productImagesList = productImagesList;
    }

    @XmlTransient
    public List<OrderItems> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<OrderItems> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Products)) {
            return false;
        }
        Products other = (Products) object;
        return !((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId)));
    }

    @Override
    public String toString() {
        return this.productId.toString();
    }

}
