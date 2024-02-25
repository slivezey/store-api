/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.jpa;

import org.livezey.storeapi.model.Role;
import org.livezey.storeapi.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for the <code>Role</code> entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * Retrieves the persistent role entity associated with the type provided.
     * 
     * @param type the type of the role entity to retrieve
     * @return Optional&lt;Role&gt;
     */
    Optional<Role> findByType(RoleType type);

}
