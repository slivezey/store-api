/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.livezey.storeapi.model.User;

/**
 * Verifies the functions of the <code>JWTUtils</code> class.
 */
public class TestJWTUtils {

    private static JWTUtils jwtUtils;

    @BeforeAll
    public static void initJwtUtils() {
        jwtUtils = new JWTUtils();
        jwtUtils.jwtSecret =
            "pueYwq89EilHhRESQPty6c7+HZrWTacq3j9ANeEXKbv86y4H8hXNPHsjvygaEBSsV4APcJbYmD9AxBbNzicl4ap5KA3j5xa4fBNqAfHY6hi7tICvI3AQRilmGZHXU3PyjJRIWTeDZJ+5od5HurLxIZUfL0Xpn9BES3TtwXEiu7cdB4KVBvVnuibms/oPV/2OT5u11HXJ3uzYMY5miipiK/7oTUhtrSbONguFpw==";
        jwtUtils.jwtExpiration = 3600000;
    }

    @Test
    public void testGenerate() {
        UserDetailsImpl user = UserDetailsImpl.build( getTestUser() );
        String accessToken = jwtUtils.generateJwtToken( user );
        System.out.println( accessToken );

        assertTrue( jwtUtils.validateJwtToken( accessToken ) );
    }

    private User getTestUser() {
        return new User( "testuser", "test@example.com", "password" );
    }

}
