/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.managedagents.constants.CompanyStatus;
import com.managedagents.constants.DefaultMessages;
import com.managedagents.entities.Companies;
import com.managedagents.entities.CompanyUsers;
import com.managedagents.entities.Users;
import com.managedagents.stateless.CompaniesBean;
import com.managedagents.stateless.MapsSingleton;
import java.io.IOException;
import java.io.Serializable;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author james
 */
@Named
@ViewScoped
public class CompaniesController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CompaniesBean companiesBean;

    @Inject
    private LoginBean loginBean;

    private Companies selectedCompany;

    private Users currentUser;

    private LazyDataModel<Companies> companies;

    private List<CompanyUsers> companyUsersList;

    private List<Companies> companyList;

    private Users selectedUser;

    private MapModel companyMapModel;

    private Marker marker;

    @PostConstruct
    public void init() {
        currentUser = loginBean.getCurrentUser();
        getCompaniesList();
    }

    public void getCompaniesList() {
        companies = new LazyDataModel<Companies>() {
            @Override
            public List<Companies> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Companies> companyList = companiesBean.findAllCompanies(first, pageSize, sortField, sortOrder, filters);
                Long rowCount = companiesBean.countAllCompanies(sortField, sortOrder, filters);
                companies.setRowCount(rowCount.intValue());
                return companyList;
            }

            @Override
            public Companies getRowData(String rowKey) {
                for (Companies company : companies.getWrappedData()) {
                    if (company.getCompanyId().equals(Integer.parseInt(rowKey))) {
                        return company;
                    }
                }
                return null;
            }

        };
    }

    public void addUser() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;
        if (selectedUser != null) {
            if (companyUsersList == null) {
                companyUsersList = new ArrayList<>();
            }
            CompanyUsers companyUser = new CompanyUsers(selectedCompany, selectedUser, (short) 0);
            if (companyUsersList.stream().anyMatch(user
                    -> user.getUser().getUserId().equals(selectedUser.getUserId()))) {
                severity = FacesMessage.SEVERITY_ERROR;
                shortMessage = "Failed";
                fullMessage = "User is already in the list.";
            }
            else {
                companyUsersList.add(companyUser);
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Added";
                fullMessage = "Added user " + selectedUser.getEmailAddress();
            }
        }
        else {
            severity = FacesMessage.SEVERITY_ERROR;
            shortMessage = "Failed";
            fullMessage = "Cannot add user.";
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void removeUser() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;

        if (selectedUser != null && companyUsersList != null && !companyUsersList.isEmpty()) {
            CompanyUsers user = companyUsersList.stream().filter(u -> u.getUser().equals(selectedUser)).findFirst().get();
            companyUsersList.remove(user);
            severity = FacesMessage.SEVERITY_INFO;
            shortMessage = "Removed";
            fullMessage = "User " + selectedUser.getEmailAddress() + " was removed";
            selectedUser = null;
        }
        else {
            severity = FacesMessage.SEVERITY_ERROR;
            shortMessage = "Failed";
            fullMessage = "Cannot remove from an empty list.";
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void findCompanyUsers() {
        companyUsersList = new ArrayList<>();
        if (selectedCompany != null) {
            companyUsersList.addAll(selectedCompany.getCompanyUsersList());
        }
    }

    public void findAllCompanies() {
        companyList = new ArrayList<>();
        companyList.addAll(companiesBean.findAllCompanies());
    }

    public void updateCompany() {
        String shortMessage;
        String fullMessage;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage.Severity severity;
        if (selectedCompany != null) {

            selectedCompany.setCompanyUsersList(companyUsersList);

            if (selectedCompany.getCompanyId().equals(0)) {
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "New Company";
                fullMessage = "A new company," + selectedCompany.getCompanyName() + ", has been created";
                companiesBean.addNewCompany(selectedCompany);
            }
            else {
                severity = FacesMessage.SEVERITY_INFO;
                shortMessage = "Updated.";
                fullMessage = "Company " + selectedCompany.getCompanyName() + " has been updated.";
                companiesBean.editCompany(selectedCompany);
            }
        }
        else {
            severity = FacesMessage.SEVERITY_ERROR;
            shortMessage = DefaultMessages.DEFAULT_ERROR;
            fullMessage = "No company selected.";
        }
        context.addMessage(null, new FacesMessage(severity, shortMessage, fullMessage));
    }

    public void cancelChanges() {
        selectedCompany = companiesBean.findCompaniesById(selectedCompany.getCompanyId());
        updateMapMarker();
        findCompanyUsers();
    }

    public void createNewCompany() {
        selectedCompany = new Companies();
        updateMapMarker();
    }

    public void onRowSelect(SelectEvent event) {
        selectedCompany = (Companies) event.getObject();
        updateMapMarker();
    }

    public void onFilterEvent(FilterEvent event) {
        cancelChanges();
    }

    public void onPaginationEvent(PageEvent event) {
        cancelChanges();
    }

    public Companies getSelectedCompany() {
        return selectedCompany;
    }

    public void setSelectedCompany(Companies selectedCompany) {
        this.selectedCompany = selectedCompany;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public LazyDataModel<Companies> getCompanies() {
        return companies;
    }

    public void setCompanies(LazyDataModel<Companies> companies) {
        this.companies = companies;
    }

    public List<CompanyUsers> getCompanyUsersList() {
        return companyUsersList;
    }

    public void setCompanyUsersList(List<CompanyUsers> companyUsersList) {
        this.companyUsersList = companyUsersList;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Companies> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Companies> companyList) {
        this.companyList = companyList;
    }

    public List<String> getCompanyStatusNames() {
        List<String> statusList = new ArrayList<>();
        for (CompanyStatus s : CompanyStatus.values()) {
            statusList.add(s.toString());
        }
        return statusList;
    }

    public boolean isCompanyAdminUser() {
        if (companyUsersList != null) {

            boolean value = companyUsersList.stream()
                    .filter(user -> user.getUser().equals(currentUser))
                    .anyMatch(user -> user.getChangeSettingsBoolean());
            return value;
        }
        else {
            return false;
        }
    }

    public MapModel getCompanyMapModel() {
        return companyMapModel;
    }

    public void setCompanyMapModel(MapModel companyMapModel) {
        this.companyMapModel = companyMapModel;
    }

    private void updateMapMarker() {
        if (selectedCompany != null) {
            LatLng latLng = new LatLng(selectedCompany.getLocationLatitude().doubleValue(), selectedCompany.getLocationLongitude().doubleValue());
            companyMapModel = new DefaultMapModel();
            marker = new Marker(latLng, selectedCompany.getCompanyName(), selectedCompany);
            marker.setDraggable(true);
            companyMapModel.addOverlay(marker);
        }
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        try {
            marker = event.getMarker();
            String address = "None";
            com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(marker.getLatlng().getLat(), marker.getLatlng().getLng());

            GeocodingApiRequest request = GeocodingApi.reverseGeocode(MapsSingleton.getInstance().getContext(), latLng);
            GeocodingResult[] result = request.await();
            if (result != null && result.length > 0) {
                address = result[0].formattedAddress;
                selectedCompany.setPhysicalAddress(address);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", address));
        }
        catch (ApiException | InterruptedException | IOException ex) {
            Logger.getLogger(CompaniesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

}
