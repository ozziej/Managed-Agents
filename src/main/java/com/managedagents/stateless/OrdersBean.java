/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.managedagents.entities.Orders;
import com.managedagents.entities.Users;
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
public class OrdersBean
{
    public static final String ORDER_ID = "orderId";
    
    @PersistenceContext(unitName = "ManagedAgentsPU")
    private EntityManager em;
    
    public List<Orders> findAllOrders()
    {
        TypedQuery<Orders> query = em.createNamedQuery("Orders.findAll", Orders.class);
        return query.getResultList();
    }

    public List<Orders> findUserOrders(Users user)
    {
        TypedQuery<Orders> query = em.createNamedQuery("Orders.findByUser", Orders.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public Orders findOrderById(Integer orderId)
    {
        TypedQuery<Orders> query = em.createNamedQuery("Orders.findByOrderId", Orders.class);
        query.setParameter(ORDER_ID, orderId);
        if (query.getResultList().isEmpty())
        {
            return null;
        }
        else
        {
            return query.getResultList().get(0);
        }
    }
    
    public Long countAllOrders(String sortField, SortOrder sortOrder, Map<String, Object> filters)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Orders> order = cq.from(Orders.class);
        
        if (sortField == null)
        {
            sortField = ORDER_ID;
        }
        
        if (sortOrder.equals(SortOrder.ASCENDING))
        {
            cq.orderBy(cb.asc(order.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING))
        {
            cq.orderBy(cb.desc(order.get(sortField)));
        }
        List<Predicate> predicateList = new ArrayList<>();
        for (Map.Entry<String, Object> filter : filters.entrySet())
        {
            if (filter.getKey().equals("orderStatus") || filter.getKey().equals(ORDER_ID))
            {
                predicateList.add(cb.equal(order.get(filter.getKey()), filter.getValue().toString()));
            }
            else
            {
                predicateList.add(cb.like(order.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
            }
        }

        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);

        cq.where(predicateArray);
        cq.select(cb.count(order));
        return em.createQuery(cq).getSingleResult();        
    }
    
    public List<Orders> findAllOrders(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> order = cq.from(Orders.class);
        CriteriaQuery<Orders> select = cq.select(order);
        
        if (sortField == null)
        {
            sortField = ORDER_ID;
        }
        
        if (sortOrder.equals(SortOrder.ASCENDING))
        {
            cq.orderBy(cb.asc(order.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING))
        {
            cq.orderBy(cb.desc(order.get(sortField)));
        }
        List<Predicate> predicateList = new ArrayList<>();
        for (Map.Entry<String, Object> filter : filters.entrySet())
        {
            if (filter.getKey().equals("orderStatus") || filter.getKey().equals(ORDER_ID))
            {
                predicateList.add(cb.equal(order.get(filter.getKey()), filter.getValue().toString()));
            }
            else
            {
                predicateList.add(cb.like(order.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
            }
        }

        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);
        cq.where(predicateArray);

        TypedQuery<Orders> typedQuery = em.createQuery(select);
        if (first >= 0)
        {
            typedQuery.setFirstResult(first);
        }
        if (pageSize >= 0)
        {
            typedQuery.setMaxResults(pageSize);
        }
        return typedQuery.getResultList();
    }
    
    public Orders addNewOrder(Orders orders)
    {
        em.persist(orders);
        em.flush();
        return orders;
    }
    
    public Orders editOrder(Orders orders)
    {
        orders = em.merge(orders);
        em.flush();
        return orders;
    }
    
    public void deleteOrder(Orders orders){
        orders = em.find(Orders.class, orders.getOrderId());
        em.remove(orders);
        em.flush();
    }
}
