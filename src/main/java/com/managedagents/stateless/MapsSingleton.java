/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import com.google.maps.GeoApiContext;

/**
 *
 * @author james
 */
public final class MapsSingleton {

    private static MapsSingleton INSTANCE = null;
    private final GeoApiContext context;

    private MapsSingleton() {
        context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCNh3BBlVol_qCg0udD60Hl0ksRMv9Gb7s")
                .build();
    }

    public static MapsSingleton getInstance() {
        synchronized (MapsSingleton.class) {
            if (INSTANCE == null) {
                INSTANCE = new MapsSingleton();
            }
        }
        return INSTANCE;
    }

    public GeoApiContext getContext() {
        return context;
    }

}
