/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

import java.util.List;

public class CompanyUsersRequest {
    private List<CompanyUsers> companyUsers;
    private Companies companies;

    public CompanyUsersRequest() {
    }

    public CompanyUsersRequest(List<CompanyUsers> companyUsers, Companies companies) {
        this.companyUsers = companyUsers;
        this.companies = companies;
    }

    public List<CompanyUsers> getCompanyUsers() {
        return companyUsers;
    }

    public void setCompanyUsers(List<CompanyUsers> companyUsers) {
        this.companyUsers = companyUsers;
    }

    public Companies getCompanies() {
        return companies;
    }

    public void setCompanies(Companies companies) {
        this.companies = companies;
    }
}