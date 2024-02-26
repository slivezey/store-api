
package org.livezey.storeapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.livezey.storeapi.AbstractStoreApiTest;
import org.livezey.storeapi.model.Message;
import org.livezey.storeapi.model.SignUpRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

class TestAuthController extends AbstractStoreApiTest {

    @Test
    public void signUp() {
        SignUpRequest request = new SignUpRequest();

        request.setUsername( "someuser" );
        request.setEmail( "someuser@example.com" );
        request.setPassword( "password" );

        ResponseEntity<Message> response = restTemplate.postForEntity( buildUrl( "/auth/sign-up" ),
            new HttpEntity<SignUpRequest>( request ), Message.class );
        Assertions.assertEquals( 200, response.getStatusCode().value() );
    }

}
