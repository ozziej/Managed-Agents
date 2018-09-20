/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.managedagents.entities.Orders;
import com.managedagents.entities.Users;
import com.managedagents.stateless.OrdersBean;
import com.managedagents.stateless.UsersBean;
import java.io.IOException;
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
@Path("orders")
public class OrdersREST
{

    @Inject
    private OrdersBean ordersBean;

    @Inject
    private UsersBean usersBean;

    @POST
    @Path("/userOrders")
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
    public List<Orders> getUserOrders(String jsonString)
    {
        Integer userId;
        List<Orders> orderList = null;
        Users user = null;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        
        try
        {
            map = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>()
            {
            });
        }
        catch (IOException ex)
        {
            Logger.getLogger(UsersREST.class.getName()).log(Level.SEVERE, null, ex);
        }

        userId = (Integer) map.getOrDefault("userId", 0);
        if (userId > 0)
        {
            user = usersBean.findUserByUserId(userId);
        }
        
        if (user != null)
        {
            orderList = ordersBean.findUserOrders(user);
        }
        return orderList;
    }
}
