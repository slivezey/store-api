/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.livezey.storeapi.jpa.RefreshTokenRepository;
import org.livezey.storeapi.jpa.UserRepository;
import org.livezey.storeapi.model.RefreshToken;
import org.livezey.storeapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles the creation and validation of refresh tokens.
 */
@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long tokenDuration;

    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the specified refresh token from persistent storage.
     * 
     * @param token the refresh token string
     * @return Optional&lt;RefreshToken&gt;
     */
    public Optional<RefreshToken> findByToken(String token) {
        return tokenRepository.findByToken( token );
    }

    /**
     * Creates a new refresh token that will be associated with the specified user.
     * 
     * @param username the identity name of the user for which to create the refresh token
     * @return RefreshToken
     */
    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername( username ).get();
        Optional<RefreshToken> existingToken = tokenRepository.findByUser( user );
        RefreshToken refreshToken = existingToken.orElse( new RefreshToken() );

        if (refreshToken == null) {
            refreshToken = new RefreshToken();
        }
        refreshToken.setUser( user );
        refreshToken.setExpiryDate( Instant.now().plusMillis( tokenDuration ) );
        refreshToken.setToken( UUID.randomUUID().toString() );
        refreshToken = tokenRepository.save( refreshToken );
        return refreshToken;
    }

    /**
     * Verifies that the given refresh token has not yet expired.
     * 
     * @param token the refresh token to verify
     * @return RefreshToken
     * @throws TokenRefreshException thrown if the token has already expired
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo( Instant.now() ) < 0) {
            tokenRepository.delete( token );
            throw new TokenRefreshException( token.getToken(), "Refresh token expired.  Please sign in again." );
        }
        return token;
    }

    /**
     * Deletes all refresh tokens associated with the specified user.
     * 
     * @param username the identity name of the user for which to delete all refresh tokens
     * @return int
     */
    @Transactional
    public int deleteByUsername(String username) {
        Optional<User> user = userRepository.findByUsername( username );
        int count = -1;

        if (user.isPresent()) {
            count = tokenRepository.deleteByUser( user.get() );
        }
        return count;
    }

}
