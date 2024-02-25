/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.model;

/**
 * Enumeration of the possible role types within the system
 */
public enum RoleType {

    ROLE_USER("user"), ROLE_ADMIN("admin");

    private String title;

    /**
     * Constructor that specifies the title of the role.
     * 
     * @param title the title of the role
     */
    private RoleType(String title) {
        this.title = title;
    }

    /**
     * Returns the title of the role.
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the <code>RoleType</code> value with the specified title or null if no such title is defined.
     * 
     * @param title the title of the role to return
     * @return RoleType
     */
    public static RoleType findByTitle(String title) {
        RoleType rt = null;

        for (RoleType _rt : values()) {
            if (_rt.title.equals( title )) {
                rt = _rt;
                break;
            }
        }
        return rt;
    }

}
