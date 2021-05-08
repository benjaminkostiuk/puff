package com.unityTest.testrunner.utils;

import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.VoteAction;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import java.security.Principal;
import java.util.Collections;
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

    // Convert a lang string to a PLanguage
    public static PLanguage parsePLanguage(String lang) {
        // Convert lang to PLanguage
        PLanguage pLanguage = null;
        if(lang != null) {
            try { pLanguage = PLanguage.valueOf(lang); }
            catch (IllegalArgumentException e) { throw new IllegalArgumentException("Not one of accepted values for language"); }
        }
        return pLanguage;
    }

    // Convert a vote action string into a VoteAction obj
    public static VoteAction parseVoteAction(String action) {
        // Convert action to VoteAction
        VoteAction voteAction;
        try {
            voteAction = VoteAction.valueOf(action);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not one of accepted values for vote action");
        }
        return voteAction;
    }

    public static AccessToken getAuthToken(Principal principal) {
        // Get the author id from the auth token
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        return keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
    }

    public static boolean isAdminUser(AccessToken accessToken) {
        return accessToken.getRealmAccess().getRoles().contains("admin");
    }

    public static boolean isAuthorOrAdmin(AccessToken accessToken, String authorId) {
        return authorId.equals(accessToken.getSubject()) || isAdminUser(accessToken);
    }

    public static String buildPath(String... dirs) {
        return String.join("/", dirs);
    }

    // Indents a function body by *n* indents, can be replaced if project upgrades to java 12
    public static String indent(String body, int n) {
        String tab = String.join("", Collections.nCopies(n, "\t"));
        return tab + body.replace("\n", "\n"+tab);
    }
}
