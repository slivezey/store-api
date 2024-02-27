/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Verifies the functions of the <code>User</code> class.
 */
public class TestUser {

    @Test
    public void accessors() throws Exception {
        Long testId = Long.valueOf( 0l );
        String testUsername = "testuser";
        String testEmail = "test@example.com";
        String testPassword = "password";
        Set<Role> testRoles = new HashSet<>();
        Role testRole = new Role();
        User theUser = new User();

        theUser.setId( testId );
        theUser.setUsername( testUsername );
        theUser.setEmail( testEmail );
        theUser.setPassword( testPassword );
        theUser.setRoles( testRoles );
        testRoles.add( testRole );

        assertEquals( testId, theUser.getId() );
        assertEquals( testUsername, theUser.getUsername() );
        assertEquals( testEmail, theUser.getEmail() );
        assertEquals( testPassword, theUser.getPassword() );
        assertEquals( testRoles, theUser.getRoles() );
        assertEquals( 1, theUser.getRoles().size() );
        assertEquals( testRole, theUser.getRoles().toArray()[0] );
    }

}
