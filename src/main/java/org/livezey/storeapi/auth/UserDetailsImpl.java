/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.livezey.storeapi.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the Spring <code>UserDetails</code> interface.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -2174461813219752466L;

    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Full constructor.
     * 
     * @param id the persistent ID (primary key) of the user
     * @param username the identity name of the user
     * @param email the email address of the user
     * @param password the password string used to authenticate the user
     * @param authorities the authorities that have been granted to the user
     */
    public UserDetailsImpl(Long id, String username, String email, String password,
        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Constructs a details instance from the <code>User</code> entity provided.
     * 
     * @param user the user entity from which to initialize this details instance
     * @return UserDetailsImpl
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map( role -> new SimpleGrantedAuthority( role.getType().name() ) ).collect( Collectors.toList() );

        return new UserDetailsImpl( user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
            authorities );
    }

    /**
     * Returns the persistent ID (primary key) of the user.
     * 
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the email address of the user.
     * 
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return ((UserDetailsImpl) o).getId() == this.id;
    }

}
