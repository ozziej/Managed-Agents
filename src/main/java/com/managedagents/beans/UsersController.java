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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
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
public class UsersController implements Serializable
{

    @Inject
    private UsersBean usersBean;

    @Inject
    private LoginBean loginBean;

    @Inject
    PasswordManager passwordManager;
    
    private Users currentUser;
    private Users selectedUser;
    private LazyDataModel<Users> users;

    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void init()
    {
        currentUser = loginBean.getCurrentUser();
        getUserList();
    }

    public void getUserList()
    {
        users = new LazyDataModel<Users>()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Users> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
            {
                List<Users> userList = usersBean.findAllUsers(first, pageSize, sortField, sortOrder, filters);
                Long rowCount = usersBean.countAllUsers(sortField, sortOrder, filters);
                users.setRowCount(rowCount.intValue());
                return userList;
            }

            @Override
            public Users getRowData(String rowKey)
            {
                for (Users user : users)
                {
                    if (user.getUserId().equals(Integer.parseInt(rowKey)))
                    {
                        return user;
                    }
                }
                return null;
            }
        };
    }

    public void updateUser()
    {
        String shortMessage;
        String fullMessage;
        String emailAddress;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;

        if (selectedUser != null)
        {
            emailAddress = StringUtils.trim(selectedUser.getEmailAddress());
            selectedUser.setEmailAddress(emailAddress);

            if (selectedUser.getUserId().equals(0))
            {
                if (usersBean.findUserByEmailAddress(emailAddress) != null)
                {
                    shortMessage = "Error.";
                    fullMessage = "An account with this email address already exists.";
                    severity = FacesMessage.SEVERITY_ERROR;
                    context.validationFailed();
                }
                else
                {
                    String randomPassword = generateRandomPassword();
                    selectedUser = usersBean.addNewUser(selectedUser);

                    shortMessage = "New User";
                    fullMessage = "A new user, " + emailAddress + " has been created.";
                    severity = FacesMessage.SEVERITY_INFO;
                }
            }
            else
            {
                selectedUser = usersBean.editUser(selectedUser);
                if (selectedUser.equals(currentUser))
                {
                    currentUser = selectedUser;
                    loginBean.setCurrentUser(currentUser);
                }
                shortMessage = "Details have been updated.";
                fullMessage = "The details for " + emailAddress + " were updated.";
                severity = FacesMessage.SEVERITY_INFO;
            }
        }
        else
        {
            severity = FacesMessage.SEVERITY_ERROR;
            shortMessage = "No User Selected";
            fullMessage = "You need to select a user before saving.";
            context.validationFailed();
        }
        
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }
    

    public void cancelChanges()
    {

    }

    public void createNewUser()
    {
        selectedUser = new Users();
    }

    public void onRowSelect(SelectEvent event)
    {
        selectedUser = (Users) event.getObject();
    }

    public void onFilterEvent(FilterEvent event)
    {
        selectedUser = null;
    }

    public void onPaginationEvent(PageEvent event)
    {
        selectedUser = null;
    }

    public Users getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser)
    {
        this.currentUser = currentUser;
    }

    public Users getSelectedUser()
    {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    public LazyDataModel<Users> getUsers()
    {
        return users;
    }

    public void setUsers(LazyDataModel<Users> users)
    {
        this.users = users;
    }

    private String generateRandomPassword()
    {
        String randomPassword = null;
        passwordManager.createRandomPassword();
        try
        {
            passwordManager.encryptPassword();
            randomPassword = passwordManager.getPassword();
            selectedUser.setUserPass(passwordManager.getEncryptedPassword());
            selectedUser.setUserSalt(passwordManager.getSalt());
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return randomPassword;
    }
}
