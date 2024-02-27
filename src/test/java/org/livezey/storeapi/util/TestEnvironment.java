/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Verifies the functions of the <code>Environment</code> class.
 */
public class TestEnvironment {

    @Test
    public void trueEnvironmentVariable() throws Exception {
        String systemPath = System.getenv( "PATH" );
        Environment testEnv = new Environment();

        assertEquals( systemPath, testEnv.getEnv( "PATH" ) );
    }

    @Test
    public void overriddenEnvironmentVariable() throws Exception {
        String realSystemPath = System.getenv( "PATH" );
        String systemPathOverride = "PATH-OVERRIDE-VALUE";
        Environment testEnv = new Environment();

        testEnv.setEnv( "PATH", systemPathOverride );
        assertNotEquals( realSystemPath, testEnv.getEnv( "PATH" ) );
        assertEquals( systemPathOverride, testEnv.getEnv( "PATH" ) );
    }

}
