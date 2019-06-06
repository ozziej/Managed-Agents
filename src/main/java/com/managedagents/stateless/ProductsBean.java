/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.managedagents.entities.Products;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author james
 */
@Stateless
public class ProductsBean
{
    @PersistenceContext(unitName = "ManagedAgentsPU")
    private EntityManager em;

    public List<Products> findAllProducts()
    {
        TypedQuery<Products> query = em.createNamedQuery("Products.findAll", Products.class);
        return query.getResultList();
    }
    
    public List<Products> findAllValidProducts()
    {
        TypedQuery<Products> query = em.createNamedQuery("Products.findAll", Products.class);
        return query.getResultList();
    }
    
    public Products findByProductId(Integer productId)
    {
        TypedQuery<Products> query = em.createNamedQuery("Products.findByProductId", Products.class);
        query.setParameter("productId", productId);
        if (query.getResultList().isEmpty())
        {
            return null;
        }
        else
        {
            return query.getResultList().get(0);
        }
    }
    
    public Products addNewProduct(Products products)
    {
        em.persist(products);
        em.flush();
        return products;
    }
    
    public Products editProduct(Products products)
    {
        products = em.merge(products);
        em.flush();
        return products;
    }
}
