/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.rest;

import com.managedagents.entities.Companies;
import com.managedagents.entities.CompanyUsers;
import com.managedagents.entities.CompanyUsersRequest;
import com.managedagents.entities.GenericResponse;
import com.managedagents.entities.Users;
import com.managedagents.stateless.CompaniesBean;
import com.managedagents.stateless.UsersBean;
import java.util.ArrayList;
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
        if (user != null) {
            return companiesBean.findCompaniesByUser(user);
        }
        else {
            return null;
        }
    }

    @Consumes({
        MediaType.APPLICATION_JSON
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    @POST
    @Path("/companyUsers")
    public List<CompanyUsers> getCompanyUsers(Companies companies) {
        return usersBean.findCompanyUsers(companies);
    }

    @Consumes({
        MediaType.APPLICATION_JSON
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    @POST
    @Path("/updateCompany")
    public GenericResponse updateCompany(CompanyUsersRequest usersRequest) {
        String result;
        GenericResponse.ResponseCode resultCode;
        GenericResponse response;
        Companies companies = usersRequest.getCompanies();
        List<CompanyUsers> companyUsers = usersRequest.getCompanyUsers();

        if (companies.getCompanyId().equals(0)) {
            companies = companiesBean.addNewCompany(companies);
            List<CompanyUsers> updatedList = new ArrayList<>();
            for (CompanyUsers cu : companyUsers) {
                updatedList.add(new CompanyUsers(companies, cu.getUser(), cu.getChangeSettings()));
            }
            companies.setCompanyUsersList(updatedList);
            companiesBean.editCompany(companies);
            result = "Added new company " + companies.getCompanyName();
            resultCode = GenericResponse.ResponseCode.SUCCESSFUL;
        }
        else {
            companies.setCompanyUsersList(companyUsers);
            companiesBean.editCompany(companies);
            result = "Company " + companies.getCompanyName() + " edited";
            resultCode = GenericResponse.ResponseCode.SUCCESSFUL;
        }
        response = new GenericResponse(resultCode, result);
        return response;
    }
}
