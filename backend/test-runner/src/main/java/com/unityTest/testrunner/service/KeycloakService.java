package com.unityTest.testrunner.service;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for authenticating with the keycloak api through a service client
 */
@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String authUrl;

    @Value("${keycloak.realm}")
    private String realmName;

    @Value("${service-account.client-id}")
    private String clientId;

    @Value("${service-account.client-secret}")
    private String clientSecret;

    private Keycloak instance;

    /**
     * Initialize an api auth call to the keycloak server using
     * the service client credentials
     */
    private void initializeConnection() {
        this.instance = KeycloakBuilder.builder()
                .serverUrl(this.authUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(this.realmName)
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .resteasyClient(
                        new ResteasyClientBuilder().connectionPoolSize(5).build()
                ).build();
    }

    /**
     * Get an authenticated Keycloak object to make api calls with
     * @return Authenticated Keycloak obj
     */
    public Keycloak getConnection() {
        if(this.instance != null) return this.instance;
        this.initializeConnection();
        return this.instance;
    }

    public String getRealmName() {
        return this.realmName;
    }
}
