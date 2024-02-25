/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Persistent entity representing a user of the system.
 */
@Entity
@Table(name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Full constructor.
     * 
     * @param username the identifying name of the user
     * @param email the email address for the user
     * @param password the password used to authenticate the user
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the persistent ID (primary key) for the user.
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Assigns the persistent ID (primary key) for the user.
     *
     * @param id the primary key value to assign
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the identifying name of the user.
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Assigns the identifying name of the user.
     *
     * @param username the user name to assign
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the email address for the user.
     *
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Assigns the email address for the user.
     *
     * @param email the email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Assigns the password used to authenticate the user.
     *
     * @param password the password string to assign
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the roles that have been assigned to the user.
     *
     * @return Set&lt;Role&gt;
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Assigns the roles that have been assigned to the user.
     *
     * @param roles the field value to assign
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
