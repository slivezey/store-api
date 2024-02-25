/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.jpa;

import org.livezey.storeapi.model.RefreshToken;
import org.livezey.storeapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for the <code>RefreshToken</code> entity.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    /**
     * Retrieves a <code>RefreshToken</code> record using the token value.
     * 
     * @param token the value of the token to retrieve
     * @return Optional&lt;RefreshToken&gt;
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Retrieves a <code>RefreshToken</code> for the given user (if one exists).
     * 
     * @param user the user for which to retrieve the associated token
     * @return Optional&lt;RefreshToken&gt;
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Deletes all existing refresh tokens for the given user.
     * 
     * @param user the user for which to delete the associated refresh tokens
     * @return int
     */
    @Modifying
    int deleteByUser(User user);

}
