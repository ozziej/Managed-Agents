/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.managedagents.entities.ProductImages;
import com.managedagents.entities.Products;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;

/**
 *
 * @author james
 */
@Stateless
public class ProductsBean
{
    private static final String PRODUCT_ID = "productId";
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
    
    public List<ProductImages> findAllProductImages(Products product){
        TypedQuery<ProductImages> query = em.createNamedQuery("ProductImages.findByProduct", ProductImages.class);
        query.setParameter("product", product);
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
    
    public Long countAllProducts(String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Products> user = cq.from(Products.class);

        if (sortField == null) {
            sortField = PRODUCT_ID;
        }

        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(user.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(user.get(sortField)));
        }

        List<Predicate> predicateList = new ArrayList<>();

        filters.entrySet().forEach((filter) -> {
            if (filter.getKey().equals("userStatus") || filter.getKey().equals(PRODUCT_ID)) {
                predicateList.add(cb.equal(user.get(filter.getKey()), filter.getValue().toString()));
            }
            else {
                predicateList.add(cb.like(user.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
            }
        });

        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);

        cq.where(predicateArray);
        cq.select(cb.count(user));
        return em.createQuery(cq).getSingleResult();
    }

    public List<Products> findAllProducts(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Products> cq = cb.createQuery(Products.class);

        Root<Products> user = cq.from(Products.class);
        CriteriaQuery<Products> select = cq.select(user);

        if (sortField == null) {
            sortField = PRODUCT_ID;
        }

        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(user.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(user.get(sortField)));
        }

        List<Predicate> predicateList = new ArrayList<>();

        filters.entrySet().forEach((filter) -> {
            if (filter.getKey().equals("userStatus") || filter.getKey().equals(PRODUCT_ID)) {
                predicateList.add(cb.equal(user.get(filter.getKey()), filter.getValue().toString()));
            }
            else {
                predicateList.add(cb.like(user.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
            }
        });

        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);

        cq.where(predicateArray);

        TypedQuery<Products> typedQuery = em.createQuery(select);
        if (first >= 0) {
            typedQuery.setFirstResult(first);
        }
        if (pageSize >= 0) {
            typedQuery.setMaxResults(pageSize);
        }

        return typedQuery.getResultList();
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
