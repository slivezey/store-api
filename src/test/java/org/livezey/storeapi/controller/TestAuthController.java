/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.livezey.storeapi.AbstractStoreApiTest;
import org.livezey.storeapi.openapi.model.Message;
import org.livezey.storeapi.openapi.model.SignInResponse;
import org.livezey.storeapi.openapi.model.SignUpRequest;
import org.livezey.storeapi.openapi.model.TokenRefreshRequest;
import org.livezey.storeapi.openapi.model.TokenRefreshResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Verifies the functions of the <code>AuthController</code> class.
 */
class TestAuthController extends AbstractStoreApiTest {

    @Test
    public void signUp() throws Exception {
        SignUpRequest request = new SignUpRequest();

        request.setUsername( "someuser" );
        request.setEmail( "someuser@example.com" );
        request.setPassword( "password" );

        ResponseEntity<Message> response = restTemplate.postForEntity( buildUrl( "/auth/sign-up" ),
            new HttpEntity<SignUpRequest>( request ), Message.class );
        assertEquals( 200, response.getStatusCode().value() );
        assertNotNull( response.getBody() );
    }

    @Test
    public void signUp_duplicateUsername() throws Exception {
        SignUpRequest request = new SignUpRequest();

        request.setUsername( "testuser" );
        request.setEmail( "someuser@example.com" );
        request.setPassword( "password" );

        HttpClientErrorException ex = assertThrows( HttpClientErrorException.class, () -> restTemplate
            .postForEntity( buildUrl( "/auth/sign-up" ), new HttpEntity<>( request ), Message.class ) );
        assertEquals( 400, ex.getStatusCode().value() );
    }

    @Test
    public void signUp_duplicateEmail() throws Exception {
        SignUpRequest request = new SignUpRequest();

        request.setUsername( "someuser" );
        request.setEmail( "test@example.com" );
        request.setPassword( "password" );

        HttpClientErrorException ex = assertThrows( HttpClientErrorException.class, () -> restTemplate
            .postForEntity( buildUrl( "/auth/sign-up" ), new HttpEntity<>( request ), Message.class ) );
        assertEquals( 400, ex.getStatusCode().value() );
    }

    @Test
    public void signIn() throws Exception {
        ResponseEntity<SignInResponse> response = doSignIn( "testuser", "password" );

        assertEquals( 200, response.getStatusCode().value() );
        assertNotNull( response.getBody() );
    }

    @Test
    public void signIn_incorrectPassword() throws Exception {
        HttpClientErrorException ex =
            assertThrows( HttpClientErrorException.class, () -> doSignIn( "testuser", "badpassword" ) );

        assertEquals( 401, ex.getStatusCode().value() );
    }

    @Test
    public void signOut() throws Exception {
        ResponseEntity<SignInResponse> signInResponse = doSignIn( "testuser", "password" );
        HttpHeaders headers = new HttpHeaders();

        assertEquals( 200, signInResponse.getStatusCode().value() );

        headers.setBearerAuth( signInResponse.getBody().getAccessToken() );
        restTemplate.postForEntity( buildUrl( "/auth/sign-out" ), new HttpEntity<>( null, headers ), null );
    }

    @Test
    public void signOut_invalidToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth( "--invalidToken--" );
        restTemplate.postForEntity( buildUrl( "/auth/sign-out" ), new HttpEntity<>( null, headers ), null );
    }

    @Test
    public void tokenRefresh() throws Exception {
        ResponseEntity<SignInResponse> signInResponse = doSignIn( "testuser", "password" );
        TokenRefreshRequest request = new TokenRefreshRequest();

        assertEquals( 200, signInResponse.getStatusCode().value() );
        request.setRefreshToken( signInResponse.getBody().getRefreshToken() );

        ResponseEntity<TokenRefreshResponse> response = restTemplate.postForEntity( buildUrl( "/auth/token-refresh" ),
            new HttpEntity<>( request ), TokenRefreshResponse.class );

        assertEquals( 200, response.getStatusCode().value() );
        assertNotNull( response.getBody() );
        assertNotNull( response.getBody().getAccessToken() );
    }

    @Test
    public void tokenRefresh_invalidToken() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest();

        request.setRefreshToken( "--invalid-refresh-token--" );

        HttpClientErrorException ex = assertThrows( HttpClientErrorException.class,
            () -> restTemplate.postForEntity( buildUrl( "/auth/token-refresh" ), new HttpEntity<>( request ),
                TokenRefreshResponse.class ) );

        assertEquals( 401, ex.getStatusCode().value() );
    }

}
