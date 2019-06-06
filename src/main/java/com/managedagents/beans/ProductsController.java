/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.beans;

import com.managedagents.entities.Products;
import com.managedagents.stateless.ProductsBean;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author james
 */
@Named
@ViewScoped
public class ProductsController implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Inject ProductsBean productsBean;
    
    private List<Products> productList;
    
    @PostConstruct
    public void init()
    {
        //empty as we dont need to init anything yet
    }
    
    public void findAllProducts()
    {
        productList = productsBean.findAllProducts();
    }

    public List<Products> getProductList()
    {
        return productList;
    }

    public void setProductList(List<Products> productList)
    {
        this.productList = productList;
    }
    
}
