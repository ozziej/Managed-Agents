/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.filters;

import com.managedagents.beans.LoginBean;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author james
 */
@WebFilter(filterName = "AuthenticationFilter",
        urlPatterns = {"/secured/*"})
public class AuthenticationFilter implements Filter
{
    @Inject LoginBean loginBean;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
    {
        if (loginBean.getCurrentUser() == null)
        {
            String contextPath = ((HttpServletRequest)request).getContextPath();
            ((HttpServletResponse)response).sendRedirect(contextPath + "/index.xhtml?faces-redirect=true");            
        }
        else
        {
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {
        
    }
    
}
