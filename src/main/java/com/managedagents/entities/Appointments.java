/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import com.managedagents.constants.AppointmentStatus;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author james
 */
@Entity
@Table(name = "appointments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointments.findAll", query = "SELECT a FROM Appointments a"),
    @NamedQuery(name = "Appointments.findByAppointmentId", query = "SELECT a FROM Appointments a WHERE a.appointmentId = :appointmentId"),
    @NamedQuery(name = "Appointments.findByUser", query = "SELECT a FROM Appointments a WHERE a.user = :user"),
    @NamedQuery(name = "Appointments.findAllByUserDateBetween", query = "SELECT a FROM Appointments a WHERE (a.startTime BETWEEN :startTime and :endTime)"
            + " and (a.endTime BETWEEN :startTime and :endTime) and (a.user = :user or :user IS NULL)"),
    @NamedQuery(name = "Appointments.findByUserDateBetween", query = "SELECT a FROM Appointments a WHERE (a.startTime BETWEEN :startTime and :endTime)"
            + " and (a.endTime BETWEEN :startTime and :endTime) and (a.user = :user)"),
    @NamedQuery(name = "Appointments.findByAppointmentStatus", query = "SELECT a FROM Appointments a WHERE a.appointmentStatus = :appointmentStatus")})
public class Appointments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "appointment_id")
    private Integer appointmentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "appointment_notes")
    private String appointmentNotes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "appointment_status")
    private String appointmentStatus;
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @ManyToOne
    private Companies company;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users user;

    public Appointments() {
    }

    public Appointments(Users user, Companies company) {
        Date today = Calendar.getInstance().getTime();
        this.appointmentId = 0;
        this.company = company;
        this.user = user;
        this.startTime = today;
        this.endTime = today;
        this.appointmentNotes = "None";
        this.appointmentStatus = AppointmentStatus.CREATED.name();
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public String getAppointmentNotes() {
        return appointmentNotes;
    }

    public void setAppointmentNotes(String appointmentNotes) {
        this.appointmentNotes = appointmentNotes;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Companies getCompany() {
        return company;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentId != null ? appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Appointments)) {
            return false;
        }
        Appointments other = (Appointments) object;
        return !((this.appointmentId == null && other.appointmentId != null) 
                || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId)));
    }

    @Override
    public String toString() {
        return appointmentId.toString();
    }
    
}
