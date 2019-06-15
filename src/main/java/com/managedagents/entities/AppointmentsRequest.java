/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

/**
 *
 * @author james
 */
public class AppointmentsRequest {

    private final Users user;
    private final long startTime;
    private final long endTime;

    public AppointmentsRequest(){
        this.user = new Users();
        this.startTime = 0l;
        this.endTime = 0l;
    }
    
    public AppointmentsRequest(Users user, long startTime, long endTime) {
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }    

    public Users getUser() {
        return user;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
