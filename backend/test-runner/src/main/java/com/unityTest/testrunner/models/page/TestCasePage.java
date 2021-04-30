package com.unityTest.testrunner.models.page;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.models.response.Author;
import com.unityTest.testrunner.models.response.TestCase;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;

@Slf4j
@ApiModel(value = "TestCasePage", description = "Page request for test cases")
public class TestCasePage extends BasePage<TestCase> {
    public TestCasePage(Page<Case> page) {
        super(page.map(Case::toTestCase));
    }

    /**
     * TestCasePage constructor
     * @param page Page of cases
     * @param keycloak Keycloak api connection
     */
    public TestCasePage(Page<Case> page, Keycloak keycloak, String realmName) {
        super(page.map(caze -> {
            // Get user representation with id from keycloak connection
            Author author = null;
            try {
                UserRepresentation user = keycloak.realm(realmName).users().get(caze.getAuthorId()).toRepresentation();
                // Create author object with the user representation
                author = new Author(user.getFirstName(), user.getLastName());
            } catch (javax.ws.rs.NotFoundException e) {
                log.error(String.format("Failed to find keycloak user with id %s. Error %s", caze.getAuthorId(), e.getLocalizedMessage()));
            }
            return caze.toTestCase(author);
        }));
    }

    /**
     * TestCasePage constructor
     * @param page Page of cases
     * @param author Author of all cases
     */
    public TestCasePage(Page<Case> page, Author author) {
        super(page.map(caze -> caze.toTestCase(author)));
    }
}
