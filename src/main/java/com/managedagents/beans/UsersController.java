/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.constants.DefaultMessages;
import com.managedagents.constants.UsersStatus;
import com.managedagents.entities.Companies;
import com.managedagents.entities.CompanyUsers;
import com.managedagents.entities.Users;
import com.managedagents.stateless.PasswordManager;
import com.managedagents.stateless.UsersBean;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
public class UsersController implements Serializable {

    @Inject
    private UsersBean usersBean;

    @Inject
    private LoginBean loginBean;

    @Inject
    PasswordManager passwordManager;

    private Users currentUser;
    private Users selectedUser;
    private LazyDataModel<Users> users;
    private List<Users> usersList;

    private List<CompanyUsers> companyUsersList;

    private CompanyUsers selectedCompanyUser;

    private Companies selectedCompany;

    private String userPassword;

    private String updateElements;

    private String buttonUpdate;

    private final List<String> userStatusList = new ArrayList<>();
    
    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void init() {
        currentUser = loginBean.getCurrentUser();
        findUserStatusTypes();
        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().contains("otherusers")) {
            selectedUser = null;
            updateElements = ":userListForm:userDataTable,:usersEditForm :growl";
            buttonUpdate = ":userListForm:editSelectedUser, :userListForm:editUserPassword, :userListForm:editUserCompanies";
            getUserList();
        }
        else {
            selectedUser = currentUser;
            buttonUpdate = "";
            updateElements = ":myDetailsForm,:usersEditForm :growl";
        }
    }

    public void getUserList() {
        users = new LazyDataModel<Users>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<Users> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Users> userList = usersBean.findAllUsers(first, pageSize, sortField, sortOrder, filters);
                Long rowCount = usersBean.countAllUsers(sortField, sortOrder, filters);
                users.setRowCount(rowCount.intValue());
                return userList;
            }

            @Override
            public Users getRowData(String rowKey) {
                for (Users user : users.getWrappedData()) {
                    if (user.getUserId().equals(Integer.parseInt(rowKey))) {
                        return user;
                    }
                }
                return null;
            }
        };
    }

    public void updateUser() {
        String shortMessage;
        String fullMessage;
        String emailAddress;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;

        if (selectedUser != null) {

            if (companyUsersList == null) {
                findUserCompanies();
            }

            selectedUser.setCompanyUsersList(companyUsersList);
            emailAddress = StringUtils.trim(selectedUser.getEmailAddress());
            selectedUser.setEmailAddress(emailAddress);

            if (selectedUser.getUserId().equals(0)) {
                if (usersBean.findUserByEmailAddress(emailAddress) != null) {
                    shortMessage = DefaultMessages.DEFAULT_ERROR;
                    fullMessage = "An account with this email address already exists.";
                    severity = FacesMessage.SEVERITY_ERROR;
                    context.validationFailed();
                }
                else {
                    String randomPassword = generateRandomPassword();
                    selectedUser = usersBean.addNewUser(selectedUser);

                    shortMessage = "New User";
                    fullMessage = "A new user, " + emailAddress + " has been created. Their password is : " + randomPassword;
                    severity = FacesMessage.SEVERITY_INFO;
                }
            }
            else {
                selectedUser = usersBean.editUser(selectedUser);
                if (selectedUser.equals(currentUser)) {
                    currentUser = selectedUser;
                    loginBean.setCurrentUser(currentUser);
                }
                shortMessage = "Details have been updated.";
                fullMessage = "The details for " + emailAddress + " were updated.";
                severity = FacesMessage.SEVERITY_INFO;
            }
        }
        else {
            severity = FacesMessage.SEVERITY_ERROR;
            shortMessage = "No User Selected";
            fullMessage = "You need to select a user before saving.";
            context.validationFailed();
        }

        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void updatePassword() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();

        passwordManager.setPassword(userPassword);
        try {
            passwordManager.encryptPassword();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        selectedUser.setUserPass(passwordManager.getEncryptedPassword());
        selectedUser.setUserSalt(passwordManager.getSalt());

        selectedUser = usersBean.editUser(selectedUser);
        shortMessage = "Password has been changed.";
        fullMessage = "The password for " + selectedUser.getEmailAddress() + " has been changed.";
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, shortMessage, fullMessage));
    }

    public void findUserCompanies() {
        companyUsersList = new ArrayList<>();
        companyUsersList.addAll(usersBean.findUserCompanies(selectedUser));
    }

    public void removeUserCompany() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;

        if (selectedCompanyUser != null) {
            severity = FacesMessage.SEVERITY_INFO;
            shortMessage = "Removed";
            fullMessage = selectedCompanyUser.getCompany().getCompanyName() + " was removed";
            companyUsersList.remove(selectedCompanyUser);
            selectedCompanyUser = null;
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing Selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void addCompany() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;
        if (selectedCompany != null) {
            if (companyUsersList.stream().anyMatch(company
                    -> company.getCompany().equals(selectedCompany))) {
                shortMessage = DefaultMessages.DEFAULT_ERROR;
                fullMessage = "Company is already in the list.";
                severity = FacesMessage.SEVERITY_ERROR;
            }
            else {
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Added.";
                fullMessage = selectedCompany.getCompanyName() + " was added.";
                companyUsersList.add(new CompanyUsers(selectedCompany, selectedUser, (short) 0));
            }
        }
        else {
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "Nothing Selected.";
            severity = FacesMessage.SEVERITY_ERROR;
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void findAllUsers() {
        usersList = usersBean.findAllUsers();
    }

    public void cancelChanges() {
        selectedUser = usersBean.findUserByUserId(selectedUser.getUserId());
    }

    public void createNewUser() {
        selectedUser = new Users();
    }

    public void onRowSelect(SelectEvent event) {
        selectedUser = (Users) event.getObject();
    }

    public void onFilterEvent(FilterEvent event) {
        selectedUser = null;
    }

    public void onPaginationEvent(PageEvent event) {
        selectedUser = null;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public LazyDataModel<Users> getUsers() {
        return users;
    }

    public void setUsers(LazyDataModel<Users> users) {
        this.users = users;
    }

    private String generateRandomPassword() {
        String randomPassword = null;
        passwordManager.createRandomPassword();
        try {
            passwordManager.encryptPassword();
            randomPassword = passwordManager.getPassword();
            selectedUser.setUserPass(passwordManager.getEncryptedPassword());
            selectedUser.setUserSalt(passwordManager.getSalt());
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return randomPassword;
    }

    private void findUserStatusTypes() {
        for (UsersStatus s : UsersStatus.values()) {
            userStatusList.add(s.toString());
        }
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public String getUpdateElements() {
        return updateElements;
    }

    public String getButtonUpdate() {
        return buttonUpdate;
    }

    public void setSelectedToCurrentUser() {
        selectedUser = currentUser;
    }

    public List<CompanyUsers> getCompanyUsersList() {
        return companyUsersList;
    }

    public void setCompanyUsersList(List<CompanyUsers> companyUsersList) {
        this.companyUsersList = companyUsersList;
    }

    public CompanyUsers getSelectedCompanyUser() {
        return selectedCompanyUser;
    }

    public void setSelectedCompanyUser(CompanyUsers selectedCompanyUser) {
        this.selectedCompanyUser = selectedCompanyUser;
    }

    public Companies getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Companies selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public List<String> getUserStatusTypes() {
        return userStatusList;
    }
}
