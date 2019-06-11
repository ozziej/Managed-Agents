/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.managedagents.entities.Appointments;
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
public class AppointmentsBean {

    private static final String APPOINTMENT_ID = "appointmentId";
    
    @PersistenceContext(unitName = "ManagedAgentsPU")
    private EntityManager em;
    
    public List<Appointments> findAllAppointments(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointments> cq = cb.createQuery(Appointments.class);

        Root<Appointments> appointment = cq.from(Appointments.class);
        CriteriaQuery<Appointments> select = cq.select(appointment);
        if (sortField == null) {
            sortField = APPOINTMENT_ID;
        }
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(appointment.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(appointment.get(sortField)));
        }
        List<Predicate> predicateList = new ArrayList<>();
        filters.entrySet().forEach((filter) -> {
            predicateList.add(cb.like(appointment.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
        });
        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);
        cq.where(predicateArray);
        TypedQuery<Appointments> typedQuery = em.createQuery(select);
        if (first >= 0) {
            typedQuery.setFirstResult(first);
        }
        if (pageSize >= 0) {
            typedQuery.setMaxResults(pageSize);
        }

        return typedQuery.getResultList();
    }

    public Long countAllAppointments(String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Appointments> company = cq.from(Appointments.class);
        if (sortField == null) {
            sortField = APPOINTMENT_ID;
        }
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            cq.orderBy(cb.asc(company.get(sortField)));
        }
        else if (sortOrder.equals(SortOrder.DESCENDING)) {
            cq.orderBy(cb.desc(company.get(sortField)));
        }
        List<Predicate> predicateList = new ArrayList<>();
        filters.entrySet().forEach((filter) -> {
            predicateList.add(cb.like(company.get(filter.getKey()), "%" + filter.getValue().toString() + "%"));
        });
        Predicate[] predicateArray = predicateList.stream().toArray(Predicate[]::new);
        cq.where(predicateArray);
        cq.select(cb.count(company));
        return em.createQuery(cq).getSingleResult();
    }
    
    public List<Appointments> findAllAppointments(){
        TypedQuery<Appointments> query = em.createNamedQuery("Appointments.findAll", Appointments.class);
        return query.getResultList();
    }
    
    public List<Appointments> findUserAppointments(Users user){
        TypedQuery<Appointments> query = em.createNamedQuery("Appointments.findByUser", Appointments.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
    
    public Appointments addNewAppointment(Appointments appointment){
        em.persist(appointment);
        em.flush();
        return appointment;
    }
    
    public Appointments editAppointment(Appointments appointment){
        appointment = em.merge(appointment);
        em.flush();
        return appointment;
    }
}
