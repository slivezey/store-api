/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that is applied for every inbound HTTP request that parses the JWT from the bearer token header and uses it to
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger( AuthTokenFilter.class );

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(jakarta.servlet.http.HttpServletRequest,
     *      jakarta.servlet.http.HttpServletResponse, jakarta.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String jwt = parseJwt( request );

            if ((jwt != null) && jwtUtils.validateJwtToken( jwt )) {
                String username = jwtUtils.getUserNameFromJwtToken( jwt );

                UserDetails userDetails = userDetailsService.loadUserByUsername( username );
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );
                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

                SecurityContextHolder.getContext().setAuthentication( authentication );
            }

        } catch (Exception e) {
            logger.error( "Unable to process user authentication.", e );
        }
        filterChain.doFilter( request, response );
    }

    /**
     * Returns the encoded JWT from the Authorization header of the given request if the header is present and is
     * populated with a Bearer token value.
     * 
     * @param request the HTTP request from which to extract the JWT
     * @return String
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader( "Authorization" );
        String jwt = null;

        if (StringUtils.hasText( headerAuth ) && headerAuth.startsWith( "Bearer " )) {
            jwt = headerAuth.substring( 7, headerAuth.length() );
        }
        return jwt;
    }

}
