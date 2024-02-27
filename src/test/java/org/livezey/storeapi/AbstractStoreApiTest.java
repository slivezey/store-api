/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi;

import org.junit.jupiter.api.BeforeEach;
import org.livezey.storeapi.openapi.model.SignInRequest;
import org.livezey.storeapi.openapi.model.SignInResponse;
import org.livezey.storeapi.util.Environment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    protected RestTemplate restTemplate;

    /**
     * Default constructor.
     */
    public AbstractStoreApiTest() {
        RestTemplateBuilder builder = new RestTemplateBuilder();

        restTemplate = new RestTemplate();
        builder.errorHandler( new ResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            public void handleError(ClientHttpResponse response) throws IOException {}
        } );
        builder.configure( restTemplate );

    }

    /**
     * Initializes the in-memory DB before each test by ensuring the roles and a test user have been created. It also
     * deletes all data that is not part of the basic system setup. The password for the test user is 'password'.
     * 
     * @throws Exception thrown if an error occurrs during the DB setup
     */
    @BeforeEach
    public void initializeDatabase() throws Exception {
        try (Connection dbConnection = DriverManager.getConnection( "jdbc:h2:mem:store", "sa", "sa" )) {
            Statement stmt = dbConnection.createStatement();

            if (!userAndRolesInitialized) {
                stmt.executeUpdate( "INSERT INTO roles(type) VALUES('ROLE_USER')" );
                stmt.executeUpdate( "INSERT INTO roles(type) VALUES('ROLE_ADMIN')" );
                stmt.executeUpdate( "INSERT INTO users(username, email, password) "
                    + "VALUES('testuser', 'test@example.com', '$2a$12$aUZGNfKw71UwPIpBi90hY.p1qQIjMYVquKw35X643HeHyVDH0aY66')" );
                stmt.executeUpdate( "INSERT INTO user_roles VALUES(1, 1)" );
            }
            stmt.executeUpdate( "DELETE FROM refreshtoken" );
            stmt.executeUpdate( "DELETE FROM user_roles WHERE user_id <> 1" );
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

    /**
     * Performs a sign-in to the test service using the credentials provided.
     * 
     * @param username the username for the sign-in
     * @param password the password for the sign-in
     * @return ResponseEntity&lt;SignInResponse&gt;
     * @throws Exception thrown if an error occurrs during the sign-in process
     */
    protected ResponseEntity<SignInResponse> doSignIn(String username, String password) throws Exception {
        SignInRequest request = new SignInRequest();

        request.setUsername( username );
        request.setPassword( password );

        return restTemplate.postForEntity( buildUrl( "/auth/sign-in" ), new HttpEntity<>( request ),
            SignInResponse.class );
    }

    static {
        try {
            System.setProperty( "spring.datasource.url", "jdbc:h2:mem:store" );
            System.setProperty( "spring.datasource.username", "sa" );
            System.setProperty( "spring.datasource.password", "sa" );
            System.setProperty( "spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.H2Dialect" );
            Environment.getDefault().setEnv( "CORS_DISABLE", "true" );

        } catch (Exception e) {
            throw new ExceptionInInitializerError( e );
        }
    }

}
