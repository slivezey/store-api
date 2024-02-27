/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.livezey.storeapi.jpa.RefreshTokenRepository;
import org.livezey.storeapi.jpa.UserRepository;
import org.livezey.storeapi.model.RefreshToken;
import org.livezey.storeapi.model.User;

import java.time.Instant;
import java.util.Optional;

/**
 * Verifies the functions of the <code>RefreshTokenService</code>.
 */
public class TestRefreshTokenService {

    @Test
    public void createToken() throws Exception {
        RefreshTokenService service = new RefreshTokenService();
        RefreshTokenRepository mockTokenRepository = mock( RefreshTokenRepository.class );
        UserRepository mockUserRepository = mock( UserRepository.class );

        service.tokenRepository = mockTokenRepository;
        service.userRepository = mockUserRepository;
        service.tokenDuration = 0L;
        when( mockUserRepository.findByUsername( "testuser" ) ).thenAnswer( i -> createMockUser( i.getArgument( 0 ) ) );
        when( mockTokenRepository.findByUser( isNull() ) ).thenAnswer( i -> Optional.ofNullable( null ) );
        when( mockTokenRepository.save( any() ) ).thenAnswer( i -> i.getArgument( 0 ) );

        RefreshToken token = service.createRefreshToken( "testuser" );

        assertNotNull( token );
        assertNotNull( token.getUser() );
        assertEquals( "testuser", token.getUser().getUsername() );
        assertNotNull( token.getToken() );
        assertNotNull( token.getExpiryDate() );
    }

    @Test
    public void validateToken_valid() throws Exception {
        RefreshTokenService service = new RefreshTokenService();
        RefreshTokenRepository mockTokenRepository = mock( RefreshTokenRepository.class );
        RefreshToken token = new RefreshToken();

        token.setExpiryDate( Instant.now().plusSeconds( 10 ) );

        service.tokenRepository = mockTokenRepository;
        doNothing().when( mockTokenRepository ).delete( any() );

        assertEquals( token, service.verifyExpiration( token ) );
    }

    @Test
    public void validateToken_expired() throws Exception {
        RefreshTokenService service = new RefreshTokenService();
        RefreshTokenRepository mockTokenRepository = mock( RefreshTokenRepository.class );
        RefreshToken token = new RefreshToken();

        token.setExpiryDate( Instant.now().minusSeconds( 10 ) );

        service.tokenRepository = mockTokenRepository;
        doNothing().when( mockTokenRepository ).delete( any() );

        Assertions.assertThrows( TokenRefreshException.class, () -> service.verifyExpiration( token ) );
    }

    @Test
    public void deleteToken_invalidUsername() throws Exception {
        RefreshTokenService service = new RefreshTokenService();
        UserRepository mockUserRepository = mock( UserRepository.class );

        service.userRepository = mockUserRepository;
        when( mockUserRepository.findByUsername( "testuser" ) ).thenReturn( Optional.ofNullable( null ) );

        assertEquals( -1, service.deleteByUsername( "testuser" ) );
    }

    private Optional<User> createMockUser(String username) {
        User user = new User();

        user.setUsername( username );
        return Optional.of( user );
    }

}
