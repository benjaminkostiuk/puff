package com.unityTest.testrunner.utils;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Utils {

    // TODO Change mapping if necessary
    public static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }

    public static AccessToken getAuthToken(Principal principal) {
        // Get the author id from the auth token
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        return keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
    }

    public static boolean isAdminUser(AccessToken accessToken) {
        return accessToken.getRealmAccess().getRoles().contains("ROLE_ADMIN");
    }
}
