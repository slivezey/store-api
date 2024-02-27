/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Verifies the functions of the <code>UserDetailsImpl</code> class.
 */
public class TestUserDetailsImpl {

    @Test
    public void testEquals() throws Exception {
        UserDetailsImpl user1 = new UserDetailsImpl( 1L, "testuser", "test@example.com", "password", null );
        UserDetailsImpl user1dup = new UserDetailsImpl( 1L, "testuser", "test@example.com", "password", null );
        UserDetailsImpl user2 = new UserDetailsImpl( 2L, "testuser2", "test2@example.com", "password", null );

        assertTrue( user1.equals( user1 ) );
        assertTrue( user1.equals( user1dup ) );
        assertFalse( user1.equals( user2 ) );
        assertFalse( user1.equals( "testuser" ) );
        assertFalse( user1.equals( null ) );
    }

}
