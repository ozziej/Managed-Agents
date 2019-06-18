/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.filters;

import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author james
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper
{

    private final ExceptionHandler parent;

    public CustomExceptionHandler(ExceptionHandler parent) {
        this.parent = parent;
    }
    
    @Override
    public ExceptionHandler getWrapped()
    {
        return this.parent;
    }

    @Override
    public void handle() throws FacesException
    {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) 
        {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable t = context.getException();

            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();

            try 
            {
                if (t instanceof ViewExpiredException) 
                {
                    requestMap.put("javax.servlet.error.message", "Your session has expired, please login again.");
                    String errorPageLocation = "/error.xhtml";
                    fc.setViewRoot(fc.getApplication().getViewHandler().createView(fc, errorPageLocation));
                    fc.getPartialViewContext().setRenderAll(true);
                    fc.renderResponse();
                }
                else 
                {
                    requestMap.put("javax.servlet.error.message", t.getMessage());
                    nav.handleNavigation(fc, null, "/error.xhtml");
                }

                fc.renderResponse();
            } 
            finally 
            {
                i.remove();
            }
        }
        getWrapped().handle();
    }
    
    
}
