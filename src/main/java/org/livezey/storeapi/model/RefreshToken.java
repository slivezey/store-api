/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Persistent entity representing a JWT refresh token.
 */
@Entity(name = "refreshtoken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    /**
     * Returns the persistent identifier (primary key) of the refresh token.
     *
     * @return long
     */
    public long getId() {
        return id;
    }

    /**
     * Assigns the persistent identifier (primary key) of the refresh token.
     *
     * @param id the field value to assign
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the user entity associated with the refresh token.
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Assigns the user entity associated with the refresh token.
     *
     * @param user the user entity to assign
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the encoded string value of the JWT refresh token.
     *
     * @return String
     */
    public String getToken() {
        return token;
    }

    /**
     * Assigns the encoded string value of the JWT refresh token.
     *
     * @param token the encoded string value to assign
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the expiration date of the refresh token.
     *
     * @return Instant
     */
    public Instant getExpiryDate() {
        return expiryDate;
    }

    /**
     * Assigns the expiration date of the refresh token.
     *
     * @param expiryDate the expiration date to assign
     */
    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

}
