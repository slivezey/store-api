/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides access to environment variables, but also allows local overrides for testing purposes.
 */
public class Environment {

    private static Map<String,String> overrides = new HashMap<>();

    /**
     * Returns the value of an environment variable.
     * 
     * @param key the name of the environment variable
     * @return String
     */
    public static String getEnv(String key) {
        return overrides.computeIfAbsent( key, k -> System.getenv( k ) );
    }

    /**
     * Assigns the value of a locally-accessible environment variable.
     * 
     * @param key the name of the environment variable
     * @param value the value to assign
     */
    public static void setEnv(String key, String value) {
        overrides.put( key, value );
    }

}
