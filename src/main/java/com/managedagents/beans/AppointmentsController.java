/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.entities.Appointments;
import com.managedagents.entities.Companies;
import com.managedagents.entities.Users;
import com.managedagents.stateless.AppointmentsBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author james
 */
@Named
@ViewScoped

public class AppointmentsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginBean loginBean;

    @Inject
    private AppointmentsBean appointmentsBean;

    private Users currentUser;

    private Appointments selectedAppointment;

    private Companies selectedCompany;

    @PostConstruct
    public void init() {
        currentUser = loginBean.getCurrentUser();
    }

    public void getAllAppointments() {

    }

    public void updateAppointment() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;

        if (selectedAppointment != null) {
            if (selectedAppointment.getAppointmentId().equals(0)) {
                appointmentsBean.addNewAppointment(selectedAppointment);
                shortMessage = "New Appointment";
                fullMessage = "Appointment has been scheduled.";
            }
            else {
                shortMessage = "Updated Appointment";
                fullMessage = "Appointment has been modified.";
                appointmentsBean.editAppointment(selectedAppointment);
            }
        }
        else {
            shortMessage = "Error";
            fullMessage = "Nothing Selected";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void newAppointment() {
        selectedAppointment = new Appointments(currentUser, selectedCompany);
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public Appointments getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Appointments selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public Companies getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Companies selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

}
