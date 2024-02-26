/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi;

import org.junit.jupiter.api.BeforeEach;
import org.livezey.storeapi.util.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Initializes the Spring Boot container with an in-memory database for use during unit testing.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractStoreApiTest {

    private static boolean userAndRolesInitialized = false;

    @LocalServerPort
    private int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    /**
     * Initializes the in-memory DB before each test by ensuring the roles and a test user have been created. It also
     * deletes all data that is not part of the basic system setup.
     * 
     * @throws Exception thrown if an error occurrs during the DB setup
     */
    @BeforeEach
    public void initializeDatabase() throws Exception {
        try (Connection dbConnection = DriverManager.getConnection( "jdbc:h2:mem:store", "sa", "sa" )) {
            Statement stmt = dbConnection.createStatement();

            if (!userAndRolesInitialized) {
                stmt.executeUpdate( "INSERT INTO roles(type) VALUES('ROLE_USER')" );
                stmt.executeUpdate(
                    "INSERT INTO users(username, email, password) VALUES('testuser', 'test@example.com', 'password')" );
                stmt.executeUpdate( "INSERT INTO user_roles VALUES(1, 1)" );
                stmt.executeUpdate( "INSERT INTO roles(type) VALUES('ROLE_ADMIN')" );
            }
            stmt.executeUpdate( "DELETE FROM refreshtoken" );
            stmt.executeUpdate( "DELETE FROM user_roles" );
            stmt.executeUpdate( "DELETE FROM users WHERE username <> 'testuser'" );

        } finally {
            userAndRolesInitialized = true;
        }
    }

    /**
     * Returns a valid URL for calling the test server.
     * 
     * @param path the path of the URL
     * @return String
     */
    public String buildUrl(String path) {
        StringBuilder url = new StringBuilder( "http://localhost:" ).append( port ).append( '/' );

        if (path != null) {
            url.append( path );
        }
        return url.toString();
    }

    static {
        try {
            System.setProperty( "spring.datasource.url", "jdbc:h2:mem:store" );
            System.setProperty( "spring.datasource.username", "sa" );
            System.setProperty( "spring.datasource.password", "sa" );
            System.setProperty( "spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.H2Dialect" );
            Environment.setEnv( "CORS_DISABLE", "true" );

        } catch (Exception e) {
            throw new ExceptionInInitializerError( e );
        }
    }

}
