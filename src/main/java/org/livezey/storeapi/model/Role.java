/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Persistent entity representing a system role.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType type;

    /**
     * Default constructor.
     */
    public Role() {}

    /**
     * Full constructor.
     * 
     * @param type the type of the role
     */
    public Role(RoleType type) {
        this.type = type;
    }

    /**
     * Returns the persistent ID (primary key) for the role.
     *
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    /**
     * Assigns the persistent ID (primary key) for the role.
     *
     * @param id the primary key value to assign
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the type of the role.
     *
     * @return RoleType
     */
    public RoleType getType() {
        return type;
    }

    /**
     * Assigns the type of the role.
     *
     * @param type the role type to assign
     */
    public void setType(RoleType type) {
        this.type = type;
    }

}
