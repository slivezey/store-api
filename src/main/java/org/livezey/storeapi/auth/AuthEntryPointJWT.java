/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Implementation of the <code>AuthenticationEntryPoint</code> that sends an Unauthorized error response when a user's
 * JWT is missing or invalid.
 */
@Component
public class AuthEntryPointJWT implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger( AuthEntryPointJWT.class );

    /**
     * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        Map<String,Object> body = new HashMap<>();

        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        response.setContentType( MediaType.APPLICATION_JSON_VALUE );

        body.put( "status", HttpServletResponse.SC_UNAUTHORIZED );
        body.put( "error", "Unauthorized" );
        body.put( "message", authException.getMessage() );
        body.put( "path", request.getServletPath() );

        new ObjectMapper().writeValue( response.getOutputStream(), body );
        logger.error( "Unauthorized Error: {}", authException.getMessage() );
    }

}
