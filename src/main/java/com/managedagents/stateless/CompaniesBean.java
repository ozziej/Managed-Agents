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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;

/**
 *
 * @author james
 */
@Stateless
public class CompaniesBean {

    private static final String COMPANY_ID = "companyId";

    @PersistenceContext(unitName = "ManagedAgentsPU")
    private EntityManager em;

    public List<Companies> findAllCompanies() {
        TypedQuery<Companies> query = em.createNamedQuery("Companies.findAll", Companies.class);
        return query.getResultList();
    }
    
    public List<Companies> findCompaniesByUser(Users user) {
        TypedQuery<Companies> query = em.createNamedQuery("Companies.findByUser", Companies.class);
        query.setParameter("user", user);
        return query.getResultList();
    }    

    public Companies findCompaniesById(Integer companyId) {
        TypedQuery<Companies> query = em.createNamedQuery("Companies.findByCompanyId", Companies.class);
        query.setParameter(COMPANY_ID, companyId);
        List<Companies> result = query.getResultList();
        if (result.isEmpty()){
            return null;
        }
        else{
            return result.get(0);
        }
    }

    public List<Companies> findAllCompanies(Users user, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Companies> cq = cb.createQuery(Companies.class);

        Root<Companies> company = cq.from(Companies.class);
        Join<Companies, CompanyUsers> companyUsers = company.join("companyUsersList");
        Join<CompanyUsers, Users> users = companyUsers.join("user");
        
        CriteriaQuery<Companies> select = cq.select(company);
        if (sortField == null) {
            sortField = COMPANY_ID;
        }
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(company.get(sortField)));
        }   
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(company.get(sortField)));
        }
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(users, user));
        
        filters.entrySet().forEach((filter) -> {
            predicateList.add(cb.like(company.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
        });
        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);
        cq.where(predicateArray);
        TypedQuery<Companies> typedQuery = em.createQuery(select);
        if (first >= 0) {
            typedQuery.setFirstResult(first);
        }
        if (pageSize >= 0) {
            typedQuery.setMaxResults(pageSize);
        }

        return typedQuery.getResultList();
    }

    public Long countAllCompanies(Users user, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Companies> company = cq.from(Companies.class);
        Join<Companies, CompanyUsers> companyUsers = company.join("companyUsersList");
        Join<CompanyUsers, Users> users = companyUsers.join("user");
        
        if (sortField == null) {
            sortField = COMPANY_ID;
        }
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(company.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(company.get(sortField)));
        }
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(users, user));
        filters.entrySet().forEach((filter) -> {
            predicateList.add(cb.like(company.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
        });
        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);
        cq.where(predicateArray);
        cq.select(cb.count(company));
        return em.createQuery(cq).getSingleResult();
    }
    
    public Companies addNewCompany(Companies company) {
        em.persist(company);
        em.flush();
        return company;
    }

    public Companies editCompany(Companies company) {
        company = em.merge(company);
        em.flush();
        return company;
    }
}
