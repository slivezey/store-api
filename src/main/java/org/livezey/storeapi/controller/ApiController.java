/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.controller;

import org.livezey.storeapi.api.ApiApi;
import org.livezey.storeapi.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Primary controller for the application's REST API.
 */
@RestController
public class ApiController implements ApiApi {

    /**
     * @see org.livezey.storeapi.api.ApiApi#echo(org.livezey.storeapi.model.Message)
     */
    @Override
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Message> echo(@Valid Message message) {
        Message response = new Message();

        response.setMessage( message.getMessage() );
        return ResponseEntity.ok( response );
    }

}
