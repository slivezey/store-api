/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Utility methods that handle the details of creating, signing, and validating JWT tokens.
 */
@Component
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger( JWTUtils.class );

    @Value("${jwt.secret-key}")
    protected String jwtSecret;

    @Value("${jwt.expiration}")
    protected int jwtExpiration;

    /**
     * Generates and signs a JWT and returns the resulting ecoded string.
     * 
     * @param user the user to which the JWT will be associated
     * @return String
     */
    public String generateJwtToken(UserDetailsImpl user) {
        return generateTokenFromUsername( user.getUsername() );
    }

    /**
     * Generates and signs a JWT and returns the resulting ecoded string.
     * 
     * @param username the identity name of the user to which the JWT will be associated
     * @return String
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject( username ).setIssuedAt( new Date() )
            .setExpiration( new Date( (new Date()).getTime() + jwtExpiration ) )
            .signWith( SignatureAlgorithm.HS512, jwtSecret ).compact();
    }

    /**
     * Returns the identity name of the user who is associated with the given token.
     * 
     * @param token the JWT for which to return the associated user name
     * @return String
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey( jwtSecret ).parseClaimsJws( token ).getBody().getSubject();
    }

    /**
     * Returns true if the given JWT is valid.
     * 
     * @param authToken the token to validate
     * @return boolean
     */
    public boolean validateJwtToken(String token) {
        boolean isValid = false;
        try {
            Jwts.parser().setSigningKey( jwtSecret ).parseClaimsJws( token );
            isValid = true;

        } catch (SignatureException e) {
            logger.error( "Invalid JWT Signature: {}", e.getMessage() );
        } catch (MalformedJwtException e) {
            logger.error( "Invalid JWT Token: {}", e.getMessage() );
        } catch (ExpiredJwtException e) {
            logger.error( "Expired JWT Token: {}", e.getMessage() );
        } catch (UnsupportedJwtException e) {
            logger.error( "Unsupported JWT Token: {}", e.getMessage() );
        } catch (IllegalArgumentException e) {
            logger.error( "Empty JWT Claims: {}", e.getMessage() );
        }
        return isValid;
    }

}
