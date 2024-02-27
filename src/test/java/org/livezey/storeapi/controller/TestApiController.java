/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.livezey.storeapi.AbstractStoreApiTest;
import org.livezey.storeapi.openapi.model.Message;
import org.livezey.storeapi.openapi.model.SignInResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * Verifies the functions of the <code>ApiController</code> class.
 */
public class TestApiController extends AbstractStoreApiTest {

    @Test
    public void tokenRefresh() throws Exception {
        ResponseEntity<SignInResponse> signInResponse = doSignIn( "testuser", "password" );
        HttpHeaders headers = new HttpHeaders();
        Message request = new Message();

        assertEquals( 200, signInResponse.getStatusCode().value() );
        headers.setBearerAuth( signInResponse.getBody().getAccessToken() );
        request.setMessage( "ping" );

        ResponseEntity<Message> response = restTemplate.postForEntity( buildUrl( "/api/user/echo" ),
            new HttpEntity<>( request, headers ), Message.class );

        assertEquals( 200, response.getStatusCode().value() );
        assertNotNull( response.getBody() );
        assertEquals( "ping", response.getBody().getMessage() );
    }

}
