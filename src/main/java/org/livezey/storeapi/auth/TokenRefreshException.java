/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a refresh token has expired.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 4142212778407194377L;

    /**
     * Constructor that specified the token that expired and a message for the user.
     * 
     * @param token the token that was expired
     * @param message user message for the exception
     */
    public TokenRefreshException(String token, String message) {
        super( String.format( "Failed for [%s]: %s", token, message ) );
    }

}
