/**
 * Copyright (C) 2024 Stephen Livezey. All rights reserved.
 */

package org.livezey.storeapi.auth;

import org.livezey.storeapi.util.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Handles the configuration of JWT web security for the application.
 */
@Configuration
public class WebSecurityConfig {

    private static boolean CORS_DISABLE;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJWT unauthorizedHandler;

    /**
     * Returns the filter that will extract and validate JWTs from each inbound HTTP request.
     * 
     * @return AuthTokenFilter
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Returns a configured instance of the authentication provider DAO.
     * 
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService( userDetailsService );
        authProvider.setPasswordEncoder( passwordEncoder() );
        return authProvider;
    }

    /**
     * Returns the authentication manager for the given configuration.
     * 
     * @param authConfig the authentication configuration instance
     * @return AuthenticationManager
     * @throws Exception thrown of the authentication manager is not configured
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Returns a password encoder instance.
     * 
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                if (CORS_DISABLE) {
                    registry.addMapping( "/**" ).allowedMethods( "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH" );
                }
            }
        };
    }

    /**
     * Configures the HTTP filter chain for the application.
     * 
     * @param http the HTTP security instance to be configured
     * @return SecurityFilterChain
     * @throws Exception thrown if an error occurs during the configuration process
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors( Customizer.withDefaults() ).csrf( AbstractHttpConfigurer::disable )
            .exceptionHandling( configurer -> configurer.authenticationEntryPoint( unauthorizedHandler ) )
            .sessionManagement( configurer -> configurer.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
            .authorizeHttpRequests( auth -> auth.requestMatchers( "/auth/**", "/api/anon/**" ).permitAll() )
            .authorizeHttpRequests( auth -> auth.requestMatchers( "/api/user/**" ).hasAnyRole( "USER" ) )
            .authorizeHttpRequests( auth -> auth.requestMatchers( "/api/admin/**" ).hasAnyRole( "ADMIN" ) );
        http.authenticationProvider( authenticationProvider() );
        http.addFilterBefore( authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    /**
     * Static initializer that determines whether CORS should be disabled.
     */
    static {
        String corsDisable = Environment.getDefault().getEnv( "CORS_DISABLE" );
        CORS_DISABLE = (corsDisable == null) ? false : corsDisable.toLowerCase().equals( "true" );
    }

}
