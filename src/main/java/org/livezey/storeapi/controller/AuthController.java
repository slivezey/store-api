/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.controller;

import org.livezey.storeapi.auth.JWTUtils;
import org.livezey.storeapi.auth.RefreshTokenService;
import org.livezey.storeapi.auth.TokenRefreshException;
import org.livezey.storeapi.auth.UserDetailsImpl;
import org.livezey.storeapi.jpa.RoleRepository;
import org.livezey.storeapi.jpa.UserRepository;
import org.livezey.storeapi.model.RefreshToken;
import org.livezey.storeapi.model.Role;
import org.livezey.storeapi.model.RoleType;
import org.livezey.storeapi.model.User;
import org.livezey.storeapi.openapi.api.AuthApi;
import org.livezey.storeapi.openapi.model.Message;
import org.livezey.storeapi.openapi.model.SignInRequest;
import org.livezey.storeapi.openapi.model.SignInResponse;
import org.livezey.storeapi.openapi.model.SignUpRequest;
import org.livezey.storeapi.openapi.model.TokenRefreshRequest;
import org.livezey.storeapi.openapi.model.TokenRefreshResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

/**
 * Controller that implements the REST functions related to user sign-up and sign-in.
 */
@RestController
public class AuthController implements AuthApi {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    /**
     * @see org.livezey.storeapi.api.AuthApi#signUp(org.livezey.storeapi.model.SignUpRequest)
     */
    @Override
    public ResponseEntity<Message> signUp(@Valid SignUpRequest signUpRequest) {
        Message response = new Message();

        if (userRepository.existsByUsername( signUpRequest.getUsername() )) {
            response.setMessage( "Username is already taken." );
            return ResponseEntity.badRequest().body( response );
        }

        if (userRepository.existsByEmail( signUpRequest.getEmail() )) {
            response.setMessage( "Email is already in use." );
            return ResponseEntity.badRequest().body( response );
        }

        User user = new User( signUpRequest.getUsername(), signUpRequest.getEmail(),
            encoder.encode( signUpRequest.getPassword() ) );
        Role userRole = roleRepository.findByType( RoleType.ROLE_USER ).get();
        Set<Role> roles = new HashSet<>();

        roles.add( userRole );
        user.setRoles( roles );
        userRepository.save( user );

        response.setMessage( "User registered successfully." );
        return ResponseEntity.ok( response );
    }

    /**
     * @see org.livezey.storeapi.api.AuthApi#signIn(org.livezey.storeapi.model.SignInRequest)
     */
    @Override
    public ResponseEntity<SignInResponse> signIn(@Valid SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken( signInRequest.getUsername(), signInRequest.getPassword() ) );
        SecurityContextHolder.getContext().setAuthentication( authentication );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken( userDetails );
        List<String> roles =
            userDetails.getAuthorities().stream().map( item -> item.getAuthority() ).collect( Collectors.toList() );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken( userDetails.getUsername() );
        SignInResponse response = new SignInResponse();

        response.setAccessToken( jwt );
        response.setType( "Bearer" );
        response.setRefreshToken( refreshToken.getToken() );
        response.setUsername( userDetails.getUsername() );
        response.setEmail( userDetails.getEmail() );
        response.setRoles( roles );
        return ResponseEntity.ok( response );
    }

    /**
     * @see org.livezey.storeapi.api.AuthApi#tokenRefresh(org.livezey.storeapi.model.TokenRefreshRequest)
     */
    @Override
    public ResponseEntity<TokenRefreshResponse> tokenRefresh(@Valid TokenRefreshRequest tokenRefreshRequest) {
        String refreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenService.findByToken( refreshToken ).map( refreshTokenService::verifyExpiration )
            .map( RefreshToken::getUser ).map( user -> {
                String token = jwtUtils.generateTokenFromUsername( user.getUsername() );
                TokenRefreshResponse response = new TokenRefreshResponse();

                response.setAccessToken( token );
                response.setType( "Bearer" );
                response.setRefreshToken( refreshToken );
                return ResponseEntity.ok( response );
            } ).orElseThrow( () -> new TokenRefreshException( refreshToken, "Invalid refresh token." ) );
    }

    /**
     * @see org.livezey.storeapi.api.AuthApi#logout()
     */
    @Override
    public ResponseEntity<Message> logout() {
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message response = new Message();
        UserDetailsImpl user = (userObj instanceof UserDetailsImpl) ? (UserDetailsImpl) userObj : null;

        if (user != null) {
            refreshTokenService.deleteByUsername( user.getUsername() );
        }
        response.setMessage( "Logout successful." );
        return ResponseEntity.ok( response );
    }

}
