/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * Verifies the functions of the <code>RefreshToken</code> class.
 */
public class TestRefreshToken {

    @Test
    public void accessors() throws Exception {
        Long testId = Long.valueOf( 100l );
        User testUser = new User();
        String testToken = "testToken";
        Instant testExpiryDate = Instant.now();
        RefreshToken theToken = new RefreshToken();

        theToken.setId( testId );
        theToken.setUser( testUser );
        theToken.setToken( testToken );
        theToken.setExpiryDate( testExpiryDate );

        assertEquals( testId, theToken.getId() );
        assertEquals( testUser, theToken.getUser() );
        assertEquals( testToken, theToken.getToken() );
        assertEquals( testExpiryDate, theToken.getExpiryDate() );
    }

}
