/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.rest;

import com.managedagents.entities.Companies;
import com.managedagents.entities.Users;
import com.managedagents.stateless.CompaniesBean;
import com.managedagents.stateless.UsersBean;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
@Path("companies")
public class CompaniesREST {

    @Inject
    private CompaniesBean companiesBean;

    @Inject
    private UsersBean usersBean;
    
    @Consumes({
        MediaType.APPLICATION_JSON
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    @POST
    @Path("/userCompanies")
    public List<Companies> getUserCompanies(Users user) {
        List<Companies> companyList = new ArrayList<>();
        if (user != null){            
            companyList.addAll(usersBean.findUserCompanies(user)
                    .stream()
                    .map(m -> m.getCompany())
                    .collect(Collectors.toList()));
        }
        return companyList;
    }
}
