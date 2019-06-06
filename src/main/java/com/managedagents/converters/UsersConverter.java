/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.converters;

import com.managedagents.stateless.UsersBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author james
 */
@RequestScoped
@Named
public class UsersConverter implements Converter {

    @Inject
    private UsersBean usersBean;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return usersBean.findUserByUserId(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        else {
            return value.toString();
        }
    }

}
