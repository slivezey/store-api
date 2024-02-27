/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Verifies the functions of the <code>Role</code> class.
 */
public class TestRole {

    @Test
    public void accessors() throws Exception {
        Integer testId = Integer.valueOf( 0 );
        RoleType testType = RoleType.ROLE_USER;
        Role theRole = new Role();

        theRole.setId( testId );
        theRole.setType( testType );

        assertEquals( testId, theRole.getId() );
        assertEquals( testType, theRole.getType() );
    }

}
