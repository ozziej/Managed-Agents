/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.managedagents.entities.CompanyUsers;
import com.managedagents.entities.GenericResponse;
import com.managedagents.entities.Users;
import com.managedagents.stateless.PasswordManager;
import com.managedagents.stateless.UsersBean;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Path("users")
public class UsersREST {

    @Inject
    private UsersBean usersBean;

    @Inject
    private PasswordManager passwordManager;

    @POST
    @Path("/login")
    @Consumes(
            {
                MediaType.APPLICATION_JSON
            }
    )
    @Produces(
            {
                MediaType.APPLICATION_JSON
            }
    )
    public Users login(String jsonString) {
        String emailAddress;
        String userPass;
        String userSalt;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        }
        catch (IOException ex) {
            Logger.getLogger(UsersREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        emailAddress = (String) map.getOrDefault("emailAddress", "");
        userPass = (String) map.getOrDefault("password", "");

        Users user = usersBean.findUserByEmailAddress(emailAddress);
        if (user != null) {
            userSalt = user.getUserSalt();
            passwordManager.setPassword(userPass);
            passwordManager.setSalt(userSalt);

            try {
                passwordManager.verifyPassword();

                if (!passwordManager.getEncryptedPassword().equals(user.getUserPass())) {
                    user = null;
                }
            }
            catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                Logger.getLogger(UsersREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }

    @POST
    @Path("/update")
    @Consumes({
        MediaType.APPLICATION_JSON
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    public GenericResponse updateUser(Users user) {
        String result;
        GenericResponse.ResponseCode resultCode;

        GenericResponse response;
        List<CompanyUsers> companyUsers = usersBean.findUserCompanies(user);
        user.setCompanyUsersList(companyUsers);

        if (!user.getUserId().equals(0)) {
            usersBean.editUser(user);
            result = "Success";
            resultCode = GenericResponse.ResponseCode.SUCCESSFUL;
        }
        else{
            result = "Failed";
            resultCode = GenericResponse.ResponseCode.ERROR;
        }
        response = new GenericResponse(resultCode, result);
        return response;
    }
}
