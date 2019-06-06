/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.managedagents.entities.Appointments;
import com.managedagents.entities.Users;
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
public class AppointmentsBean {

    @PersistenceContext(unitName = "ManagedAgentsPU")
    private EntityManager em;
    
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
