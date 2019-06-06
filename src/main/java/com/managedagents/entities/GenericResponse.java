/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.entities;

/**
 *
 * @author james
 */
public class GenericResponse {
    private final ResponseCode responseCode;
    private final String responseMessage;

    public enum ResponseCode{
        ERROR,
        SUCCESSFUL,
        NO_RESPONSE
    }

    public GenericResponse(ResponseCode responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
