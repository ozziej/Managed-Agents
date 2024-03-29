/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.filters;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author james
 */
public class ExceptionHandlerFilter extends ExceptionHandlerFactory
{

    private final ExceptionHandlerFactory parent;

    public ExceptionHandlerFilter(ExceptionHandlerFactory parent)
    {
        this.parent = parent;
    }
    
    @Override
    public ExceptionHandler getExceptionHandler()
    {
        ExceptionHandler result = parent.getExceptionHandler();
        result = new CustomExceptionHandler(result);
        return result;
    }
    
}
