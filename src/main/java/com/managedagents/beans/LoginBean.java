/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.entities.Users;
import com.managedagents.stateless.PasswordManager;
import com.managedagents.stateless.UsersBean;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author james
 */
@Named
@SessionScoped
public class LoginBean implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private Users currentUser;
    private String emailAddress;
    private String password;

    @Inject
    private UsersBean usersBean;
    
    @Inject
    private PasswordManager passwordManager;
    
    @PostConstruct
    public void init()
    {
        
    }
    
    public String loginUser()
    {
        String returnPage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        currentUser = usersBean.findUserByEmailAddress(emailAddress);
        if (currentUser == null)
        {
            context.addMessage(null, new FacesMessage(severity, "Error!", "Invalid Username or Password"));
            returnPage = "login.xhtml";
        }
        else
        {
            passwordManager.setPassword(password);
            passwordManager.setSalt(currentUser.getUserSalt());
            try
            {
                passwordManager.verifyPassword();
            }
            catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
            {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!passwordManager.getEncryptedPassword().equals(currentUser.getUserPass()))
            {
                currentUser = null;
                context.addMessage(null, new FacesMessage(severity, "Error!", "Invalid Username or Password"));
                returnPage = "login.xhtml";
            }
            else
            {
                returnPage = "/secured/mydetails.xhtml?faces-redirect=true";
            }
        }
        return returnPage;
    }
    
    public String logoutUser(){
        currentUser = null;
        String returnPage = "/index.xhtml?faces-redirect=true";//dont do a redirect, or it wont keep the message below
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("loginPanel:messages", new FacesMessage(FacesMessage.SEVERITY_INFO,"Logged out","You have successfully logged out"));
        return returnPage;
    }

    public Users getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser)
    {
        this.currentUser = currentUser;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    
}
