/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.converters;

import com.managedagents.entities.Products;
import com.managedagents.stateless.ProductsBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.convert.Converter;
import javax.inject.Inject;

/**
 *
 * @author james
 */
@RequestScoped
@Named
public class ProductConverter implements Converter
{
    @Inject
    private ProductsBean productsBean;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        Products product = productsBean.findByProductId(Integer.parseInt(value));
        return product;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null)
        {
            return "";
        }
        else
        {
            return value.toString();
        }
    }
    
}
