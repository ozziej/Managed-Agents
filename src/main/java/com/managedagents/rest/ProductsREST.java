/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.rest;

import com.managedagents.entities.Products;
import com.managedagents.stateless.ProductsBean;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author james
 */
@Stateless
@Path("products")
public class ProductsREST
{
    @Inject
    private ProductsBean productsBean;
    
    @GET
    @POST
    @Path("/productsList")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Products> getProductsList()
    {
        return productsBean.findAllProducts();
    }
    
    @GET
    @POST
    @Path("/validProductsList")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Products> getAllValidProductsList()
    {
        return productsBean.findAllValidProducts();
    }
    
}
