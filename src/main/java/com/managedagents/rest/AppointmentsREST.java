/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.rest;

import com.managedagents.entities.Appointments;
import com.managedagents.entities.AppointmentsRequest;
import com.managedagents.entities.GenericResponse;
import com.managedagents.entities.Users;
import com.managedagents.stateless.AppointmentsBean;
import com.managedagents.stateless.UsersBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author james
 */
@Stateless
@Path("appointments")
public class AppointmentsREST {

    @Inject
    private AppointmentsBean appointmentsBean;

    @Inject
    private UsersBean usersBean;

    @POST
    @Path("/appointmentsList")
    @Consumes({
        MediaType.APPLICATION_JSON
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    public List<Appointments> getUserAppointments(AppointmentsRequest request) {
        Users user = request.getUser();
        long startTime = request.getStartTime();
        long endTime = request.getEndTime();
        
        if (user != null) {
            Date startDateTime = new Date(startTime);
            Date endDateTime = new Date(endTime);
            return appointmentsBean.findUserAppointmentsByDate(user, startDateTime, endDateTime);
        }
        else {
            return new ArrayList<>();
        }
    }

    @POST
    @Consumes({
        MediaType.APPLICATION_JSON
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    @Path("/updateAppointment")
    public GenericResponse updateAppointment(Appointments appointments) {
        String result;
        GenericResponse.ResponseCode resultCode;

        if (appointments.getAppointmentId().equals(0)) {
            appointmentsBean.addNewAppointment(appointments);
            result = "New appointment with " + appointments.getCompany().getCompanyName() + " was added.";
            resultCode = GenericResponse.ResponseCode.SUCCESSFUL;
        }
        else {
            result = "Appointment with " + appointments.getCompany().getCompanyName() + " was updated.";
            appointmentsBean.editAppointment(appointments);
            resultCode = GenericResponse.ResponseCode.SUCCESSFUL;
        }

        return new GenericResponse(resultCode, result);
    }
}
