/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.managedagents.entities.Companies;
import com.managedagents.entities.CompanyUsers;
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
public class UsersBean {

    private static final String USER_ID = "userId";

    @PersistenceContext(unitName = "ManagedAgentsPU")
    private EntityManager em;

    public List<Users> findAllUsers() {
        TypedQuery<Users> query = em.createNamedQuery("Users.findAll", Users.class);
        return query.getResultList();
    }

    public List<CompanyUsers> findCompanyUsers(Companies company) {
        TypedQuery<CompanyUsers> query = em.createNamedQuery("CompanyUsers.findByCompany", CompanyUsers.class);
        query.setParameter("company", company);
        return query.getResultList();
    }
    
    public List<CompanyUsers> findUserCompanies(Users user) {
        TypedQuery<CompanyUsers> query = em.createNamedQuery("CompanyUsers.findByUser", CompanyUsers.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
    

    public Users findUserByUserId(Integer userId) {
        TypedQuery<Users> query = em.createNamedQuery("Users.findByUserId", Users.class);
        query.setParameter(USER_ID, userId);
        List<Users> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        else {
            return result.get(0);
        }
    }

    public Users findUserByEmailAddress(String emailAddress) {
        TypedQuery<Users> query = em.createNamedQuery("Users.findByEmailAddress", Users.class);
        query.setParameter("emailAddress", emailAddress);
        List<Users> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        else {
            return result.get(0);
        }
    }

    public Long countAllUsers(String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Users> user = cq.from(Users.class);

        if (sortField == null) {
            sortField = USER_ID;
        }

        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(user.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(user.get(sortField)));
        }

        List<Predicate> predicateList = new ArrayList<>();

        filters.entrySet().forEach((filter) -> {
            if (filter.getKey().equals("userStatus") || filter.getKey().equals(USER_ID)) {
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

    public List<Users> findAllUsers(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root<Users> user = cq.from(Users.class);
        CriteriaQuery<Users> select = cq.select(user);

        if (sortField == null) {
            sortField = USER_ID;
        }

        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(user.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(user.get(sortField)));
        }

        List<Predicate> predicateList = new ArrayList<>();

        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (filter.getKey().equals("userStatus") || filter.getKey().equals(USER_ID)) {
                predicateList.add(cb.equal(user.get(filter.getKey()), filter.getValue().toString()));
            }
            else {
                predicateList.add(cb.like(user.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
            }
        }

        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);

        cq.where(predicateArray);

        TypedQuery<Users> typedQuery = em.createQuery(select);
        if (first >= 0) {
            typedQuery.setFirstResult(first);
        }
        if (pageSize >= 0) {
            typedQuery.setMaxResults(pageSize);
        }

        return typedQuery.getResultList();
    }

    public Users addNewUser(Users user) {
        em.persist(user);
        em.flush();
        return user;
    }

    public Users editUser(Users user) {
        user = em.merge(user);
        em.flush();
        return user;
    }
}
