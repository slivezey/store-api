/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.jpa;

import org.livezey.storeapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for the <code>User</code> entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Retrieves a user entity by its username value.
     * 
     * @param username the identity name of the user to retrieve
     * @return Optional&lt;User&gt;
     */
    Optional<User> findByUsername(String username);

    /**
     * Returns true if a user with the given name already exists.
     * 
     * @param username the name of the user to check for
     * @return Boolean
     */
    Boolean existsByUsername(String username);

    /**
     * Returns true if a user with the given email address already exists.
     * 
     * @param email the email of the user to check for
     * @return Boolean
     */
    Boolean existsByEmail(String email);

}
