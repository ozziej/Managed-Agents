/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.constants.AppointmentStatus;
import com.managedagents.constants.DefaultMessages;
import com.managedagents.entities.Appointments;
import com.managedagents.entities.CompanyUsers;
import com.managedagents.entities.Users;
import com.managedagents.stateless.AppointmentsBean;
import com.managedagents.stateless.UsersBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author james
 */
@Named
@ViewScoped

public class AppointmentsController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsersBean usersBean;

    @Inject
    private LoginBean loginBean;

    @Inject
    private AppointmentsBean appointmentsBean;

    private Users currentUser;

    private Appointments selectedAppointment;

    private LazyDataModel<Appointments> appointmentsData;

    private List<CompanyUsers> companyUsersList;

    @PostConstruct
    public void init() {
        currentUser = loginBean.getCurrentUser();
        getAllAppointments();
    }

    public void getAllAppointments() {
        appointmentsData = new LazyDataModel<Appointments>() {
            @Override
            public List<Appointments> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Appointments> appointmentsList = appointmentsBean.findAllAppointments(first, pageSize, sortField, sortOrder, filters);
                Long rowCount = appointmentsBean.countAllAppointments(sortField, sortOrder, filters);
                appointmentsData.setRowCount(rowCount.intValue());
                return appointmentsList;
            }

            @Override
            public Appointments getRowData(String rowKey) {
                for (Appointments appointments : appointmentsData.getWrappedData()) {
                    if (appointments.getAppointmentId().equals(Integer.parseInt(rowKey))) {
                        return appointments;
                    }
                }
                return null;
            }

        };
    }

    public void updateAppointment() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;

        if (selectedAppointment != null) {
            
            if (selectedAppointment.getCompany() != null) {
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
                shortMessage = DefaultMessages.DEFAULT_ERROR;
                fullMessage = "Please select a company";
                severity = FacesMessage.SEVERITY_ERROR;
                context.validationFailed();
            }
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing Selected";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void findUserCompanies() {
        companyUsersList = new ArrayList<>();
        companyUsersList.addAll(usersBean.findUserCompanies(currentUser));
    }

    public void onRowSelect(SelectEvent event) {
        selectedAppointment = (Appointments) event.getObject();
    }

    public void onFilterEvent(FilterEvent event) {
        selectedAppointment = null;
    }

    public void onPaginationEvent(PageEvent event) {
        selectedAppointment = null;
    }

    public void newAppointment() {
        selectedAppointment = new Appointments(currentUser, null);
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

    public LazyDataModel<Appointments> getAppointmentsData() {
        return appointmentsData;
    }

    public void setAppointmentsData(LazyDataModel<Appointments> appointmentsData) {
        this.appointmentsData = appointmentsData;
    }

    public List<CompanyUsers> getCompanyUsersList() {
        return companyUsersList;
    }

    public void setCompanyUsersList(List<CompanyUsers> companyUsersList) {
        this.companyUsersList = companyUsersList;
    }

    public List<String> getAppointmentStatusNames(){
        List<String> statusList = new ArrayList<>();
        for (AppointmentStatus s : AppointmentStatus.values()){
            statusList.add(s.toString());
        }
        return statusList;
    }
}
